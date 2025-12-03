package ru.javapro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javapro.dto.PaymentRequest;
import ru.javapro.dto.ProductDto;
import ru.javapro.integration.ProductServiceClient;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final ProductServiceClient productIntegrationService;

    public ProductDto processPayment(PaymentRequest paymentRequest) {
        return productIntegrationService.processPayment(paymentRequest);
    }
}
