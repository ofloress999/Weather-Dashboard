package br.com.weather.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMeteoResponseDTO {

    @JsonProperty("current")
    private CurrentDTO current;
}