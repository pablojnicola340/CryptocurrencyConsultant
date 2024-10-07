package com.exercise.criptocurrency.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.exercise.criptocurrency.dtos.responses.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidPriceRangeException.class)
    public ResponseEntity<ErrorResponse> handlePriceRangeException(InvalidPriceRangeException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<ErrorResponse> handleDateRangeException(InvalidDateRangeException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse("Ocurrió un error interno: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CryptoCurrencyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCriptoCurrencyNotFoundException(CryptoCurrencyNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
