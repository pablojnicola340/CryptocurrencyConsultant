package com.exercise.criptocurrency.exception;

public class InvalidPriceRangeException extends RuntimeException{
    public InvalidPriceRangeException(String message) {
        super(message);
    }

    public InvalidPriceRangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
