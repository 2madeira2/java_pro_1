package ru.javapro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javapro.dto.ProductDto;
import ru.javapro.dto.ProductDecreaseBalanceRequest;
import ru.javapro.exception.InsufficientFundsException;
import ru.javapro.exception.ProductNotFoundException;
import ru.javapro.exception.UserNotFoundException;
import ru.javapro.mapper.ProductMapper;
import ru.javapro.model.Product;
import ru.javapro.repository.ProductRepository;
import ru.javapro.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;

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

    @Transactional
    public ProductDto decreaseProductBalance(Long productId, ProductDecreaseBalanceRequest request) {
        var product = productRepository.findByIdWithLock(productId);

        if (product == null) {
            throw new ProductNotFoundException("Продукт с id " + productId + " не найден!");
        }

        var balance = product.getBalance();

        if ((balance.compareTo(request.sum())) < 0) {
            throw new InsufficientFundsException("Недостаточный баланс для проведения операции на счету с id " + productId);
        }

        product.setBalance(balance.subtract(request.sum()));
        productRepository.save(product);

        return productMapper.toDto(product);

    }
}
