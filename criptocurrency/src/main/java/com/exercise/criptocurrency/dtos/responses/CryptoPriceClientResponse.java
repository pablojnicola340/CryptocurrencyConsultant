package com.exercise.criptocurrency.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CryptoPriceClientResponse {

    private String name;
    @JsonProperty("market_data")
    private MarketData marketData;
}
