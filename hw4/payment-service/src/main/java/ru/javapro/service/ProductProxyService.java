package ru.javapro.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import ru.javapro.dto.ProductDto;
import ru.javapro.integration.ProductServiceClient;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductProxyService {
    private final ProductServiceClient productIntegrationService;

    public List<ProductDto> findAllProductsByUserId(Long userId) {
        return productIntegrationService.findAllProductsByUserId(userId);
    }
}
