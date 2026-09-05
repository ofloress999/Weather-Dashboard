package br.com.weather.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentDTO {

    @JsonProperty("temperature_2m")
    private BigDecimal temperatura;

    @JsonProperty("wind_speed_10m")
    private Double velocidadeDoVento;

    @JsonProperty("weather_code")
    private Integer weathercode;

    @JsonProperty("relative_humidity_2m")
    private Integer umidade;
}