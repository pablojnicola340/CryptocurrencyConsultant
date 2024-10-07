package com.exercise.criptocurrency.repository;

import com.exercise.criptocurrency.entities.CryptocurrencyPrice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface CryptoHistoricalRepository extends R2dbcRepository<CryptocurrencyPrice, Long> {
    Flux<CryptocurrencyPrice> findTop1ByCryptoIdOrderByPriceDateDesc(Long id);
    Flux<CryptocurrencyPrice> findByCryptoIdAndPriceDateBetweenAndPriceBetween(Long id, LocalDateTime fechaInicio, LocalDateTime fechaFin, BigDecimal minPrice, BigDecimal maxPrice);
    Mono<CryptocurrencyPrice> findFirstByOrderByPriceDateAsc();
    Mono<CryptocurrencyPrice> findFirstByOrderByPriceDateDesc();
    Mono<CryptocurrencyPrice> findFirstByOrderByPriceAsc();
    Mono<CryptocurrencyPrice> findFirstByOrderByPriceDesc();
}
