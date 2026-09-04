package br.com.weather.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OpenMeteoResponseDTO {
    @JsonProperty("current")
    private CurrentDTO currentDTO;

}
