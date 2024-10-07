package com.exercise.criptocurrency.restClient;

import com.exercise.criptocurrency.dtos.responses.CryptoNameClientResponse;
import com.exercise.criptocurrency.dtos.responses.CryptoPriceClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "coinGeckoClient", url = "${api.coingecko.url}")
public interface CryptoPriceServiceClient {
    @GetMapping("/coins/{id}")
    ResponseEntity<CryptoPriceClientResponse> getCryptoPrice(@PathVariable("id") String id);

    @GetMapping("/coins/list")
    ResponseEntity<List<CryptoNameClientResponse>> getCryptoNames();
}
