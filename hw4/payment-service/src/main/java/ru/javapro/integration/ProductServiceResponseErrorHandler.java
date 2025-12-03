package ru.javapro.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import ru.javapro.exception.InsufficientFundsException;
import ru.javapro.exception.ProductNotFoundException;
import ru.javapro.exception.ProductServiceException;
import ru.javapro.exception.UserNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductServiceResponseErrorHandler implements ResponseErrorHandler {
    private final ObjectMapper objectMapper;

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        return statusCode.is4xxClientError() || statusCode.is5xxServerError();
    }

    @Override
    public void handleError(@NonNull ClientHttpResponse response) throws IOException {
        handleError(null, null, response);
    }

    @Override
    public void handleError(@Nullable URI url, @Nullable HttpMethod method, ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        String responseBody = readResponseBody(response);

        log.error("Ошибка от product-service: url={}, method={}, status={}, body={}",
                url, method, statusCode.value(), responseBody);

        String errorCode = extractField(responseBody, "errorCode");
        String message = extractField(responseBody, "message");

        if (message == null) {
            message = extractField(responseBody, "error");
        }

        if (errorCode != null) {
            switch (errorCode) {
                case "USER_NOT_FOUND" -> throw new UserNotFoundException(
                        message != null ? message : "Пользователь не найден");
                case "PRODUCT_NOT_FOUND" -> throw new ProductNotFoundException(
                        message != null ? message : "Продукт не найден");
                case "INSUFFICIENT_FUNDS" -> throw new InsufficientFundsException(
                        message != null ? message : "Недостаточно средств");
            }
        }

        if (statusCode == HttpStatus.NOT_FOUND) {

            if (url != null && url.getPath().contains("/user/")) {
                throw new UserNotFoundException(message != null ? message : "Пользователь не найден");
            } else {
                throw new ProductNotFoundException(message != null ? message : "Продукт не найден");
            }
        }

        if (statusCode == HttpStatus.BAD_REQUEST) {
            throw new InsufficientFundsException(message != null ? message : "Недостаточно средств");
        }

        if (statusCode.value() >= 500) {
            throw new ProductServiceException("Сервис продуктов временно недоступен", statusCode.value());
        }

        throw new ProductServiceException(
                message != null ? message : "Ошибка сервиса продуктов",
                statusCode.value()
        );
    }

    private String readResponseBody(ClientHttpResponse response) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            log.warn("Не удалось прочитать тело ответа: {}", e.getMessage());
            return "";
        }
    }

    private String extractField(String json, String fieldName) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode field = root.get(fieldName);
            return field != null && !field.isNull() ? field.asText() : null;
        } catch (Exception e) {
            log.debug("Не удалсь распарсить json: {}", e.getMessage());
            return null;
        }
    }
}
