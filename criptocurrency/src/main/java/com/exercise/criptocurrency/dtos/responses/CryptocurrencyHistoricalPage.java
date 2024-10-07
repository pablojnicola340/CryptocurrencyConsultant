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
public class CryptocurrencyHistoricalPage {

    @JsonProperty("Numero de pagina")
    int pageNumber;

    @JsonProperty("Resultados")
    List<CryptocurrencyPriceResponse> cryptocurrencyPriceResponseList;
}
