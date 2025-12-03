package ru.javapro.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javapro.dto.PaymentRequest;
import ru.javapro.dto.ProductDto;
import ru.javapro.service.PaymentService;
import ru.javapro.service.ProductProxyService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final ProductProxyService productProxyService;

    @GetMapping("/products/user/{userId}")
    public List<ProductDto> getProductsByUserId(@PathVariable Long userId) {
        return productProxyService.findAllProductsByUserId(userId);
    }

    @PostMapping("/process")
    public ProductDto processPayment(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.processPayment(paymentRequest);
    }
}
