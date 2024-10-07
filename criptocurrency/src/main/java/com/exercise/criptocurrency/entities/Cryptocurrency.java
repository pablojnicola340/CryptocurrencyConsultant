package com.exercise.criptocurrency.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "cryptocurrencies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cryptocurrency {

    @Id
    private Long id;

    @Column()
    private String name;

    @Column("created_at")
    private LocalDateTime createdAt;

    public static CryptocurrencyBuilder builder() {
        return new CryptocurrencyBuilder().createdAt(LocalDateTime.now());
    }
}
