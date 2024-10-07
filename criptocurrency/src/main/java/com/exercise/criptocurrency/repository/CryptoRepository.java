package com.exercise.criptocurrency.repository;

import com.exercise.criptocurrency.entities.Cryptocurrency;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CryptoRepository extends ReactiveCrudRepository<Cryptocurrency, Long> {
    Flux<Cryptocurrency> findByName(String nombre);
}
