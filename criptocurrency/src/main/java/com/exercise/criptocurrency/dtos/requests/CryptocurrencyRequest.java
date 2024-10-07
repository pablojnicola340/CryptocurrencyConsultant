package com.exercise.criptocurrency.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CryptocurrencyRequest {
    @NotEmpty(message = "El nombre no puede estar vacío.")
    @Size(max = 10, message = "El nombre no puede tener más de 10 caracteres.")
    private String name;
}
