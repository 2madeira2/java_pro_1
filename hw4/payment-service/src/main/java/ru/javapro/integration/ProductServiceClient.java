package ru.javapro.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.javapro.dto.PaymentForIntegrationDto;
import ru.javapro.dto.PaymentRequest;
import ru.javapro.dto.ProductDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceClient {

    @Value("${product-service.url}")
    private String productServiceUrl;
    private final RestTemplate restTemplate;

    public ProductDto processPayment(PaymentRequest paymentRequest) {
        String url = productServiceUrl + "/api/v1/products/" + paymentRequest.productId() + "/decrease";
        log.info("Отправка запроса на списание: productId={}, sum={}",
                paymentRequest.productId(), paymentRequest.sum());

            ProductDto result = restTemplate.postForObject(
                    url,
                    new PaymentForIntegrationDto(paymentRequest.sum()),
                    ProductDto.class
            );
            log.info("Платёж успешно обработан для продукта: {}", paymentRequest.productId());
            return result;
    }

    public List<ProductDto> findAllProductsByUserId(Long userId) {
        String url = productServiceUrl + "/api/v1/products/user/" + userId;
        log.info("Запрос продуктов для пользователя: {}", userId);

        ProductDto[] products = restTemplate.getForObject(url, ProductDto[].class);

        log.info("Получено {} продуктов для пользователя {}",
                products != null ? products.length : 0, userId);

        return products != null ? Arrays.asList(products) : Collections.emptyList();
    }
}
