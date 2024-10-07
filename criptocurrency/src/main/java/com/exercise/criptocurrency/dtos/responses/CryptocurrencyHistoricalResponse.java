package com.exercise.criptocurrency.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CryptocurrencyHistoricalResponse {

    @JsonProperty("Summary")
     private CryptocurrencyHistoricalFilter filtro;
    @JsonProperty("Resultados")
     private CryptocurrencyHistoricalData data;
}
