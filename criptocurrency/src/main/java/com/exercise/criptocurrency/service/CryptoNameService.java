package com.exercise.criptocurrency.service;

import com.exercise.criptocurrency.dtos.responses.CryptoNameClientResponse;
import com.exercise.criptocurrency.entities.CryptoName;
import com.exercise.criptocurrency.repository.CryptoNameRepository;
import com.exercise.criptocurrency.restClient.CryptoPriceServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class CryptoNameService {

    @Autowired
    private CryptoPriceServiceClient cryptoPriceServiceClient;

    @Autowired
    private CryptoNameRepository cryptoNameRepository;

    @EventListener
    public void onApplicationStarted(ApplicationStartedEvent event) {
        populateCryptoNames().subscribe();
    }

    @Transactional
    public Mono<Void> populateCryptoNames() {
        Instant inicio = Instant.now();
        List<CryptoNameClientResponse> cryptoNames = cryptoPriceServiceClient.getCryptoNames().getBody();
        return Flux.fromIterable(cryptoNames)
                .map(cryptoName -> CryptoName.builder()
                        .name(cryptoName.getId())
                        .build())
                .flatMap(cryptoNameRepository::save)
                .onErrorResume(ex -> {
                    if (ex instanceof DuplicateKeyException) {
                        return Mono.empty();
                    } else {
                        return Mono.error(ex);
                    }
                })
                .then()
                .doOnTerminate(() -> {
                    Instant fin = Instant.now();
                    Duration duracion = Duration.between(inicio, fin);
                    System.out.println("Se actualizó la tabla de criptomonedas del servicio en " + duracion.toSeconds() + " segundos.");
                })
                .doOnCancel(() -> {
                    Instant fin = Instant.now();
                    Duration duracion = Duration.between(inicio, fin);
                    System.out.println("Se actualizó la tabla de criptomonedas del servicio en " + duracion.toSeconds() + " segundos.");
                });

    }
}
