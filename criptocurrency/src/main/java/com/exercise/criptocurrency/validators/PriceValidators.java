package com.exercise.criptocurrency.validators;

import com.exercise.criptocurrency.exception.InvalidPriceRangeException;

import java.math.BigDecimal;

public class PriceValidators {

    public static void validarRangoDePrecios(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new InvalidPriceRangeException("El valor mínimo no puede ser mayor que el máximo.");
        }
    }
}
