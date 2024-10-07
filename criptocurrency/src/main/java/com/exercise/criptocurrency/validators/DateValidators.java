package com.exercise.criptocurrency.validators;

import com.exercise.criptocurrency.exception.InvalidDateRangeException;

import java.time.LocalDateTime;

public class DateValidators {

    public static void validarRangoDeFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio != null && fechaFin != null && fechaInicio.isAfter(fechaFin)) {
            throw new InvalidDateRangeException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
    }
}
