package ru.javapro.exception;

import lombok.Getter;

@Getter
public class ProductServiceException extends RuntimeException {
    private final int statusCode;

    public ProductServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}