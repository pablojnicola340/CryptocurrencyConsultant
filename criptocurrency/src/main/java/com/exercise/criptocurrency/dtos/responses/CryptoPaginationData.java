package com.exercise.criptocurrency.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CryptoPaginationData {

    @JsonProperty("Total de paginas")
    int totalPaginas;

    @JsonProperty("Total de resultados de la consulta")
    int totalResultados;

    @JsonProperty("Criterio de ordenacion aplicado")
    String criterio;
}
