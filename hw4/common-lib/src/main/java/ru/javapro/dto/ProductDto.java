package ru.javapro.dto;

import java.math.BigDecimal;

public record ProductDto(
        Long id,
        String accountNumber,
        BigDecimal balance,
        ProductType productType,
        Long userId
) {}
