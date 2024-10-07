package com.exercise.criptocurrency.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "cryptocurrency_prices")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CryptocurrencyPrice {

    @Id
    private Long id;

    @Column("crypto_id")
    private Long cryptoId;

    @Column
    private BigDecimal price;

    @Column("price_date")
    private LocalDateTime priceDate;
}
