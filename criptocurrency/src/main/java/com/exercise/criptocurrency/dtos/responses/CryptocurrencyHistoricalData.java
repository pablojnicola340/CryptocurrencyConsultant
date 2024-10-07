package com.exercise.criptocurrency.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CryptocurrencyHistoricalData {

    @JsonProperty("Datos de paginacion")
    CryptoPaginationData criptoPaginationData;

    @JsonProperty("Resultados de cotizaciones")
    List<CryptocurrencyHistoricalPage> cryptocurrencyHistoricalPages;
}
