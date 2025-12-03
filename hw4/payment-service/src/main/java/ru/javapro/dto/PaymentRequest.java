package ru.javapro.dto;

import java.math.BigDecimal;

public record PaymentRequest(Long productId, BigDecimal sum) {
}
