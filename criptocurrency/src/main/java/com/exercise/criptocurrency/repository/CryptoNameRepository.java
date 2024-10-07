package com.exercise.criptocurrency.repository;

import com.exercise.criptocurrency.entities.CryptoName;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CryptoNameRepository extends R2dbcRepository<CryptoName, Long> {

    Mono<CryptoName> findByName(String nombre);
}
