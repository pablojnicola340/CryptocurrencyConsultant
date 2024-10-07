package com.exercise.criptocurrency.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(CryptoController.class)
public class CryptoControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    /*@MockBean
    private CriptoService criptoService;

    @Test
    void testConsultarValor() {
        String nombre = "bitcoin";
        String service = "service1";
        CriptoResponse response = new CriptoResponse("bitcoin", 30000.0);

        when(criptoService.consultarValor(nombre, service)).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/cripto/consultar?nombre={nombre}&service={service}", nombre, service)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CriptoResponse.class)
                .isEqualTo(response);
    }*/
}
