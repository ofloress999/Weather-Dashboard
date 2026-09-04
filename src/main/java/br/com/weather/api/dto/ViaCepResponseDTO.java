package br.com.weather.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ViaCepResponseDTO {
    private String cep;
    @JsonProperty("localidade")
    private String cidade;
    private String uf;
}
