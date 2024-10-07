package com.exercise.criptocurrency.service;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CryptoServiceTest {
    /*@MockBean
    private CriptoRepository criptoRepository;

    @MockBean
    private CriptoHistoricoRepository criptoHistoricoRepository;

    @MockBean
    private CriptoServiceClient1 criptoServiceClient1;

    @MockBean
    private CriptoServiceClient2 criptoServiceClient2;

    @Autowired
    private CriptoService criptoService;

    @Test
    void testAgregarCripto() {
        Cripto cripto = new Cripto("bitcoin");
        when(criptoRepository.save(any(Cripto.class))).thenReturn(Mono.just(cripto));

        CriptoRequest request = new CriptoRequest("bitcoin");
        Mono<Cripto> result = criptoService.agregarCripto(request);

        StepVerifier.create(result)
                .expectNext(cripto)
                .verifyComplete();
    }

    @Test
    void testConsultarValor() {
        String nombre = "bitcoin";
        CriptoResponse response = new CriptoResponse(nombre, 30000.0);

        when(criptoServiceClient1.consultarPrecio(nombre)).thenReturn(Mono.just(response));

        Mono<CriptoResponse> result = criptoService.consultarValor(nombre, "service1");

        StepVerifier.create(result)
                .expectNext(response)
                .verifyComplete();
    }*/
}
