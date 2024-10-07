package com.exercise.criptocurrency.exception;

public class CryptoCurrencyNotFoundException extends RuntimeException {
    public CryptoCurrencyNotFoundException(String message) {
        super(message);
    }

    public CryptoCurrencyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
