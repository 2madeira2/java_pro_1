package ru.javapro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javapro.exception.ProductNotFoundException;
import ru.javapro.exception.UserNotFoundException;
import ru.javapro.model.Product;
import ru.javapro.repository.ProductRepository;
import ru.javapro.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<Product> findAllProductsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Не найден пользователь с данным id: " + userId);
        }

        return productRepository.findAllByUserId(userId);
    }

    public Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Не найден продукт по данному id: " + productId));
    }
}
