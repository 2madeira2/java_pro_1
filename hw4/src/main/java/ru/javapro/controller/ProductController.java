package ru.javapro.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javapro.dto.ProductDto;
import ru.javapro.mapper.ProductMapper;
import ru.javapro.service.ProductService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable Long productId) {
        return productMapper.toDto(productService.findProductById(productId));
    }

    @GetMapping("/user/{userId}")
    public List<ProductDto> getProductsByUserId(@PathVariable Long userId) {
        return productMapper.toDtoList(productService.findAllProductsByUserId(userId));
    }
}
