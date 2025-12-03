package ru.javapro.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.javapro.dto.PaymentForIntegrationDto;
import ru.javapro.dto.PaymentRequest;
import ru.javapro.dto.ProductDto;
import ru.javapro.exception.InsufficientFundsException;
import ru.javapro.exception.ProductNotFoundException;
import ru.javapro.exception.ProductServiceException;
import ru.javapro.exception.UserNotFoundException;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceClient {

    @Value("${product-service.url}")
    private String productServiceUrl;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ProductDto processPayment(PaymentRequest paymentRequest) {
        String url = productServiceUrl + "/api/v1/products/" + paymentRequest.productId() + "/decrease";
        log.info("Отправка запроса на списание: productId={}, sum={}",
                paymentRequest.productId(), paymentRequest.sum());

        try {
            ProductDto result = restTemplate.postForObject(
                    url,
                    new PaymentForIntegrationDto(paymentRequest.sum()),
                    ProductDto.class
            );
            log.info("Платёж успешно обработан для продукта: {}", paymentRequest.productId());
            return result;

        } catch (HttpClientErrorException.NotFound e) {
            String message = extractErrorMessage(e.getResponseBodyAsString());
            throw new ProductNotFoundException(message != null ? message : "Продукт с id " + paymentRequest.productId() + " не найден");

        } catch (HttpClientErrorException.BadRequest e) {
            String message = extractErrorMessage(e.getResponseBodyAsString());
            throw new InsufficientFundsException(message != null ? message : "Ошибка при оплате");
        } catch (HttpClientErrorException e) {
            throw new ProductServiceException(
                    "Ошибка сервиса продуктов: " + e.getResponseBodyAsString(),
                    e.getStatusCode().value()
            );
        } catch (RestClientException e) {
            throw new ProductServiceException("Сервис продуктов недоступен", 503);
        }
    }

    public List<ProductDto> findAllProductsByUserId(Long userId) {
        String url = productServiceUrl + "/api/v1/products/user/" + userId;
        log.info("Запрос продуктов для пользователя: {}", userId);

        try {
            ResponseEntity<List<ProductDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            List<ProductDto> products = response.getBody();
            log.info("Получено {} продуктов для пользователя {}",
                    products != null ? products.size() : 0, userId);

            return products != null ? products : Collections.emptyList();

        } catch (HttpClientErrorException.NotFound e) {
            String message = extractErrorMessage(e.getResponseBodyAsString());
            throw new UserNotFoundException(message != null ? message : "Пользователь с id " + userId + " не найден");

        } catch (HttpClientErrorException e) {
            throw new ProductServiceException(
                    "Ошибка сервиса продуктов: " + e.getResponseBodyAsString(),
                    e.getStatusCode().value()
            );

        } catch (RestClientException e) {
            throw new ProductServiceException("Сервис продуктов недоступен", 503);
        }
    }


    private String extractErrorMessage(String responseBody) {
        try {
            if (responseBody == null || responseBody.isEmpty())
                return null;
            JsonNode node = objectMapper.readTree(responseBody);
            if (node.has("error")) {
                return node.get("error").asText();
            }
        } catch (Exception e) {
            log.warn("Не удалось распарсить тело ошибки: {}", responseBody);
        }
        return responseBody;
    }

}
