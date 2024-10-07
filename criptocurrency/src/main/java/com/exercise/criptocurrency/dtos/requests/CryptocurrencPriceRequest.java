package com.exercise.criptocurrency.dtos.requests;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CryptocurrencPriceRequest {
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
