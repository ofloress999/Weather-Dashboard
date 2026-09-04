package br.com.weather.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrentWeatherDTO {
    @JsonProperty("temperature")
    private BigDecimal temperatura;

    @JsonProperty("windspeed")
    private Double velocidadeDoVento;
    
    @JsonProperty("weathercode")
    private String weathercode;
}
