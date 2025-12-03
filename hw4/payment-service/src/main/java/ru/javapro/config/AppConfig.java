package ru.javapro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.javapro.integration.ProductServiceResponseErrorHandler;

import java.time.Duration;

@Configuration
public class AppConfig {

    @Value("${product-service.timeout.connect:5000}")
    private long connectTimeout;

    @Value("${product-service.timeout.read:10000}")
    private long readTimeout;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, ProductServiceResponseErrorHandler errorHandler) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(connectTimeout))
                .setReadTimeout(Duration.ofSeconds(readTimeout))
                .errorHandler(errorHandler)
                .build();
    }
}
