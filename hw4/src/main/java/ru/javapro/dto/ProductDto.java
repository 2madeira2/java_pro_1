package ru.javapro.dto;

import ru.javapro.model.ProductType;

import java.math.BigDecimal;

public record ProductDto(
        Long id,
        String accountNumber,
        BigDecimal balance,
        ProductType productType,
        Long userId
) {}
