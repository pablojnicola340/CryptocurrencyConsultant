package com.exercise.criptocurrency.service;

import com.exercise.criptocurrency.dtos.requests.CryptocurrencyRequest;
import com.exercise.criptocurrency.dtos.requests.Sort;
import com.exercise.criptocurrency.dtos.responses.*;
import com.exercise.criptocurrency.entities.Cryptocurrency;
import com.exercise.criptocurrency.entities.CryptocurrencyPrice;
import com.exercise.criptocurrency.exception.CryptoCurrencyNotFoundException;
import com.exercise.criptocurrency.repository.CryptoHistoricalRepository;
import com.exercise.criptocurrency.repository.CryptoNameRepository;
import com.exercise.criptocurrency.repository.CryptoRepository;
import com.exercise.criptocurrency.restClient.CryptoPriceServiceClient;
import com.exercise.criptocurrency.validators.DateValidators;
import com.exercise.criptocurrency.validators.PriceValidators;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CryptoService {

    private final CryptoRepository cryptoRepository;
    private final CryptoNameRepository cryptoNameRepository;
    private final CryptoHistoricalRepository cryptoHistoricoRepository;
    private final CryptoPriceServiceClient cryptoServiceClient;

    @Autowired
    public CryptoService(CryptoRepository cryptoRepository, CryptoNameRepository cryptoNameRepository, CryptoHistoricalRepository criptoHistoricoRepository,
                         CryptoPriceServiceClient cryptoServiceClient) {
        this.cryptoRepository = cryptoRepository;
        this.cryptoNameRepository = cryptoNameRepository;
        this.cryptoHistoricoRepository = criptoHistoricoRepository;
        this.cryptoServiceClient = cryptoServiceClient;
    }

    public Mono<CryptocurrencyResponse> agregarCripto(CryptocurrencyRequest request) {

        return cryptoNameRepository.findByName(request.getName().toLowerCase())
                .flatMap(cryptoName -> {
                    if (cryptoName != null) {
                        Cryptocurrency cryptocurrency = Cryptocurrency.builder()
                                .name(request.getName().toLowerCase())
                                .build();

                        return cryptoRepository.save(cryptocurrency)
                                .map(savedCrypto -> CryptocurrencyResponse.builder()
                                        .id(savedCrypto.getId())
                                        .name(savedCrypto.getName())
                                        .createdAt(savedCrypto.getCreatedAt())
                                        .build());
                    } else {
                        return Mono.error(new CryptoCurrencyNotFoundException("Nombre de criptomoneda inexistente"));
                    }
                });
    }

    public Mono<CryptocurrencyPriceResponse> consultarValor(String nombre) {
        return cryptoRepository.findByName(nombre)
                .next()
                .flatMap(crypto -> Mono.fromCallable(() -> cryptoServiceClient.getCryptoPrice(nombre))
                        .flatMap(responseEntity -> {
                            System.out.println("Response Status: " + responseEntity.getStatusCode());
                            System.out.println("Response Body: " + responseEntity.getBody());

                            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                                CryptoPriceClientResponse response = responseEntity.getBody();

                                CryptocurrencyPrice cryptoPrice = CryptocurrencyPrice.builder()
                                        .cryptoId(crypto.getId())
                                        .price(BigDecimal.valueOf(response.getMarketData().getCurrentPrice().getArs()))
                                        .priceDate(LocalDateTime.now())
                                        .build();

                                return cryptoHistoricoRepository.save(cryptoPrice)
                                        .flatMap(savedPrice -> Mono.just(CryptocurrencyPriceResponse.builder()
                                                .name(response.getName())
                                                .price(savedPrice.getPrice())
                                                .timestamp(LocalDateTime.now())
                                                .build()));
                            } else {
                                return Mono.error(new RuntimeException("Error al obtener el precio de la criptomoneda: " + responseEntity.getStatusCode()));
                            }
                        })
                        .onErrorResume(e -> Mono.error(new RuntimeException("Error al obtener el precio de la criptomoneda", e)))
                )
                .switchIfEmpty(Mono.error(new CryptoCurrencyNotFoundException("No se encontró la criptomoneda con nombre: " + nombre)));
    }

    public Flux<CryptocurrencyResponse> listarCriptomonedas() {
        return cryptoRepository.findAll()
                .map(savedCrypto -> CryptocurrencyResponse.builder()
                        .id(savedCrypto.getId())
                        .name(savedCrypto.getName())
                        .createdAt(savedCrypto.getCreatedAt())
                        .build());
    }

    public Mono<CryptocurrencyHistoricalResponse> consultarHistorial(List<String> nombres, LocalDateTime fechaInicio, LocalDateTime fechaFin, BigDecimal maxPrice, BigDecimal minPrice, String sort, int size) {

        PriceValidators.validarRangoDePrecios(minPrice, maxPrice);
        DateValidators.validarRangoDeFechas(fechaInicio, fechaFin);

        Sort sortObj = convertirSort(sort);

        Mono<List<String>> nombresDefecto = Mono.justOrEmpty(nombres)
                .filter(list -> !list.isEmpty())
                .switchIfEmpty(
                        cryptoRepository.findAll()
                                .map(Cryptocurrency::getName)
                                .collectList()
                );

        Mono<LocalDateTime> fechaInicioDefecto = Mono.justOrEmpty(fechaInicio)
                .switchIfEmpty(
                        cryptoHistoricoRepository.findFirstByOrderByPriceDateAsc()
                                .map(CryptocurrencyPrice::getPriceDate)
                );

        Mono<LocalDateTime> fechaFinDefecto = Mono.justOrEmpty(fechaFin)
                .switchIfEmpty(
                        cryptoHistoricoRepository.findFirstByOrderByPriceDateDesc()
                                .map(CryptocurrencyPrice::getPriceDate)
                );

        Mono<BigDecimal> maxPriceDefecto = Mono.justOrEmpty(maxPrice)
                .filter(price -> price.compareTo(BigDecimal.ZERO) > 0)
                .switchIfEmpty(
                        cryptoHistoricoRepository.findFirstByOrderByPriceDesc()
                                .map(cryptoPrice -> cryptoPrice.getPrice())
                );

        Mono<BigDecimal> minPriceDefecto = Mono.justOrEmpty(minPrice)
                .filter(price -> price.compareTo(BigDecimal.ZERO) > 0)
                .switchIfEmpty(
                        cryptoHistoricoRepository.findFirstByOrderByPriceAsc()
                                .map(cryptoPrice -> cryptoPrice.getPrice())
                );

        return nombresDefecto.flatMapMany(Flux::fromIterable)
                .flatMap(nombre -> cryptoRepository.findByName(nombre)
                        .flatMap(crypto -> fechaInicioDefecto
                                .zipWith(fechaFinDefecto)
                                .flatMapMany(fechas -> minPriceDefecto
                                        .zipWith(maxPriceDefecto)
                                        .flatMapMany(precios -> cryptoHistoricoRepository.findByCryptoIdAndPriceDateBetweenAndPriceBetween(
                                                        crypto.getId(),
                                                        fechas.getT1(),
                                                        fechas.getT2(),
                                                        precios.getT1(),
                                                        precios.getT2())
                                                .map(historico -> CryptocurrencyPriceResponse.builder()
                                                        .name(crypto.getName())
                                                        .price(historico.getPrice())
                                                        .timestamp(historico.getPriceDate())
                                                        .build()))

                                )
                        )
                )
                .sort((o1, o2) -> {
                    if (sortObj.getField().equals("name")) {
                        return sortObj.getDirection() == Sort.Direction.ASC ? o1.getName().compareTo(o2.getName()) : o2.getName().compareTo(o1.getName());
                    } else if (sortObj.getField().equals("timestamp")) {
                        return sortObj.getDirection() == Sort.Direction.ASC ? o1.getTimestamp().compareTo(o2.getTimestamp()) : o2.getTimestamp().compareTo(o1.getTimestamp());
                    } else if (sortObj.getField().equals("price")) {
                        return sortObj.getDirection() == Sort.Direction.ASC ? o1.getPrice().compareTo(o2.getPrice()) : o2.getPrice().compareTo(o1.getPrice());
                    } else {
                        return 0;
                    }
                })
                .collectList()
                .flatMap(list -> {
                    long totalResults = list.size();
                    int totalPages = (int) Math.ceil((double) totalResults / size);

                    List<CryptocurrencyHistoricalPage> pages = new ArrayList<>();

                    for (int i = 0; i < totalPages; i++) {
                        int start = i * size;
                        int end = Math.min(start + size, (int) totalResults);
                        List<CryptocurrencyPriceResponse> pageResults = list.subList(start, end);
                        CryptocurrencyHistoricalPage page = CryptocurrencyHistoricalPage.builder()
                                .pageNumber(i + 1)
                                .cryptocurrencyPriceResponseList(pageResults)
                                .build();
                        pages.add(page);
                    }

                    return Mono.zip(nombresDefecto, fechaInicioDefecto, fechaFinDefecto, maxPriceDefecto, minPriceDefecto)
                            .map(tuple -> calcularFiltro(list, tuple.getT1(), tuple.getT2(), tuple.getT3(), tuple.getT4(), tuple.getT5()))
                            .map(filtro -> CryptocurrencyHistoricalResponse.builder()
                                    .filtro(filtro)
                                    .data(CryptocurrencyHistoricalData.builder()
                                            .criptoPaginationData(CryptoPaginationData
                                                    .builder()
                                                    .totalPaginas(totalPages)
                                                    .totalResultados((int) totalResults)
                                                    .criterio(sortObj.getField() + " - " + sortObj.getDirection())
                                                    .build())
                                            .cryptocurrencyHistoricalPages(pages)
                                            .build())
                                    .build());
                });
    }

    public Flux<CryptocurrencyPriceResponse> ultimaCotizacion(String nombre) {
        return cryptoRepository.findByName(nombre)
                .flatMap(crypto ->
                        cryptoHistoricoRepository.findTop1ByCryptoIdOrderByPriceDateDesc(crypto.getId())
                                .map(historico -> CryptocurrencyPriceResponse.builder()
                                        .name(crypto.getName())
                                        .price(historico.getPrice())
                                        .timestamp(historico.getPriceDate())
                                        .build())
                );
    }

    private CryptocurrencyHistoricalFilter calcularFiltro(List<CryptocurrencyPriceResponse> list, List<String> nombres, LocalDateTime fechaInicio, LocalDateTime fechaFin, BigDecimal maxPrice, BigDecimal minPrice) {
        //Revisar:
        //1- si el rango del summary tiene que calcularse a partir del rango del request, o deberian ser los rangos totales segun las criptos consultadas.
        //2- si la construccion del objeto a cotinuacion es necesaria, junto con los parametros que recibe el metodo:
        CryptocurrencyHistoricalFilter filter = CryptocurrencyHistoricalFilter.builder()
                .nombres(nombres)
                .maxPrice(maxPrice)
                .minPrice(minPrice)
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .build();

        if (CollectionUtils.isNotEmpty(list)) {
            LocalDateTime fechaInicioFilter = list.stream()
                    .map(CryptocurrencyPriceResponse::getTimestamp)
                    .min(Comparator.naturalOrder())
                    .orElse(fechaInicio != null ? fechaInicio : LocalDateTime.now().minusMonths(1));

            LocalDateTime fechaFinFilter = list.stream()
                    .map(CryptocurrencyPriceResponse::getTimestamp)
                    .max(Comparator.naturalOrder())
                    .orElse(fechaFin != null ? fechaFin : LocalDateTime.now());

            BigDecimal maxPriceFilter = list.stream()
                    .map(CryptocurrencyPriceResponse::getPrice)
                    .max(Comparator.naturalOrder())
                    .orElse(maxPrice);

            BigDecimal minPriceFilter = list.stream()
                    .map(CryptocurrencyPriceResponse::getPrice)
                    .min(Comparator.naturalOrder())
                    .orElse(minPrice);

            filter.setFechaInicio(fechaInicioFilter);
            filter.setFechaFin(fechaFinFilter);
            filter.setMaxPrice(maxPriceFilter);
            filter.setMinPrice(minPriceFilter);
        }
        return filter;
    }

    private Sort convertirSort(String sort) {
        String[] partes = sort.split(",");
        return new Sort(partes[0], Sort.Direction.valueOf(partes[1].toUpperCase()));

    }
}
