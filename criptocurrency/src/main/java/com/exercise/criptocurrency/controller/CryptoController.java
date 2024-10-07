package com.exercise.criptocurrency.controller;

import com.exercise.criptocurrency.dtos.requests.CryptocurrencyRequest;
import com.exercise.criptocurrency.dtos.responses.CryptocurrencyHistoricalResponse;
import com.exercise.criptocurrency.dtos.responses.CryptocurrencyPriceResponse;
import com.exercise.criptocurrency.dtos.responses.CryptocurrencyResponse;
import com.exercise.criptocurrency.exception.CryptoCurrencyNotFoundException;
import com.exercise.criptocurrency.service.CryptoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Validated
@Tag(name = "Cryptocurrency API", description = "API for managing cryptocurrencies and their quotations")
public class CryptoController {

    private static final String MONEDAS_PATH = "/monedas";
    private static final String COTIZACIONES_PATH = "/cotizaciones";

    private final CryptoService cryptoService;

    @Autowired
    public CryptoController(CryptoService criptoService) {
        this.cryptoService = criptoService;
    }

    @Operation(summary = "Add a new cryptocurrency", description = "Allows the addition of a new cryptocurrency with its respective details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cryptocurrency created successfully",
                    content = @Content(schema = @Schema(implementation = CryptocurrencyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or cryptocurrency not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(path = {MONEDAS_PATH + "/nueva"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<ResponseEntity<CryptocurrencyResponse>> agregarCripto(
            @Valid @RequestBody CryptocurrencyRequest request) {
        return cryptoService.agregarCripto(request)
                .map(cripto -> ResponseEntity.status(201).body(cripto))
                .onErrorResume(e -> {
                    if (e instanceof CryptoCurrencyNotFoundException) {
                        return Mono.just(ResponseEntity.badRequest().body(
                                CryptocurrencyResponse.builder().errors(Arrays.asList("Cryptocurrency not found")).build()));
                    } else {
                        return Mono.just(ResponseEntity.status(500).body(null));
                    }
                });
    }

    @Operation(summary = "Get a list of all cryptocurrencies", description = "Returns a list of all available cryptocurrencies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of cryptocurrencies returned successfully",
                    content = @Content(schema = @Schema(implementation = CryptocurrencyResponse.class)))
    })
    @GetMapping(path = {MONEDAS_PATH}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Flux<CryptocurrencyResponse> listadoCriptomonedas() {
        return cryptoService.listarCriptomonedas();
    }

    @Operation(summary = "Get the value of a cryptocurrency", description = "Returns the current value of a specified cryptocurrency by its name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cryptocurrency value returned successfully",
                    content = @Content(schema = @Schema(implementation = CryptocurrencyPriceResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cryptocurrency not found")
    })
    @PostMapping(path = {COTIZACIONES_PATH + "/{nombre}/valor"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<ResponseEntity<CryptocurrencyPriceResponse>> consultarValor(
            @Parameter(description = "Name of the cryptocurrency") @PathVariable("nombre") String nombre) {
        return cryptoService.consultarValor(nombre)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get historical price data", description = "Returns the historical price data for a list of cryptocurrencies within a date range.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historical data returned successfully",
                    content = @Content(schema = @Schema(implementation = CryptocurrencyHistoricalResponse.class)))
    })
    @GetMapping(path = {COTIZACIONES_PATH + "/historial"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<CryptocurrencyHistoricalResponse> consultarHistorial(
            @RequestParam(required = false) List<String> nombres,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime fechaFin,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return cryptoService.consultarHistorial(nombres, fechaInicio, fechaFin, maxPrice, minPrice, sort, size);
    }

    @Operation(summary = "Get the last quotation of a cryptocurrency", description = "Returns the last known quotation for a specified cryptocurrency by name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Last quotation returned successfully",
                    content = @Content(schema = @Schema(implementation = CryptocurrencyPriceResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cryptocurrency not found")
    })
    @GetMapping(path = {COTIZACIONES_PATH + "/{nombre}/ultima-cotizacion"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Flux<CryptocurrencyPriceResponse> ultimaCotizacion(
            @Parameter(description = "Name of the cryptocurrency") @PathVariable("nombre") String nombre) {
        return cryptoService.ultimaCotizacion(nombre);
    }
}