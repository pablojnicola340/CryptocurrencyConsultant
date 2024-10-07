package com.exercise.criptocurrency.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CryptocurrencyHistoricalFilter {

    @JsonProperty("Criptomonedas")
    private List<String> nombres;
    @JsonProperty("Valor maximo de cotizacion")
    private BigDecimal maxPrice;
    @JsonProperty("Valor minimo de cotizacion")
    private BigDecimal minPrice;
    @JsonProperty("Desde")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime fechaInicio;
    @JsonProperty("Hasta")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime fechaFin;
}
