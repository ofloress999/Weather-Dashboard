package br.com.weather.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClimaModel {
    private String cidade;
    private String uf;
    private String cep;
    private BigDecimal temperatura;
    private LocalDate data;
    private Integer umidade;
    private Double velocidadeDoVento;
    private String condicaoTempo;


}
