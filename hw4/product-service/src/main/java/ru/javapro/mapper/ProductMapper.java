package ru.javapro.mapper;

import org.springframework.stereotype.Component;
import ru.javapro.dto.ProductDto;
import ru.javapro.model.Product;

import java.util.List;

@Component
public class ProductMapper {
    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        return new ProductDto(
                product.getId(),
                product.getAccountNumber(),
                product.getBalance(),
                product.getType(),
                product.getUser() != null ? product.getUser().getId() : null
        );
    }

    public List<ProductDto> toDtoList(List<Product> products) {
        return products.stream()
                .map(this::toDto)
                .toList();
    }
}
