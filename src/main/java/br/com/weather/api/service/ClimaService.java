package br.com.weather.api.service;

import br.com.weather.api.dto.NominatimResponseDTO;
import br.com.weather.api.dto.OpenMeteoResponseDTO;
import br.com.weather.api.dto.ViaCepResponseDTO;
import br.com.weather.api.model.ClimaModel;
import br.com.weather.api.utilitarios.WmoCodeUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class ClimaService {

    private final RestTemplate restTemplate = new RestTemplate();

    public ClimaModel obterClima(String cep) {

        String nomeCidade = "Cidade Desconhecida";
        String nomeUF = "";
        String cepFormatado = cep;

        // 1. API VIA CEP
        try {
            String urlViaCep = "https://viacep.com.br/ws/" + cep.replaceAll("\\D", "") + "/json/";
            ViaCepResponseDTO viaCepResponseDTO = restTemplate.getForObject(urlViaCep, ViaCepResponseDTO.class);

            if (viaCepResponseDTO != null) {
                if (viaCepResponseDTO.getCidade() != null) nomeCidade = viaCepResponseDTO.getCidade();
                if (viaCepResponseDTO.getUf() != null) nomeUF = viaCepResponseDTO.getUf();
                if (viaCepResponseDTO.getCep() != null) cepFormatado = viaCepResponseDTO.getCep();
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar CEP no ViaCep: " + e.getMessage());
        }

        // Coordenadas padrão (fallback)
        String latitude = "-21.6811";
        String longitude = "-45.9231";

        // 2. API NOMINATIM
        try {
            String queryBusca = nomeCidade + ", " + nomeUF + ", Brazil";
            String urlNominatim = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/search")
                    .queryParam("q", queryBusca)
                    .queryParam("format", "json")
                    .queryParam("limit", "1")
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) WeatherApp/1.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<NominatimResponseDTO[]> responseNominatim = restTemplate.exchange(
                    urlNominatim,
                    HttpMethod.GET,
                    entity,
                    NominatimResponseDTO[].class
            );

            NominatimResponseDTO[] locais = responseNominatim.getBody();

            if (locais != null && locais.length > 0 && locais[0].getLatitude() != null && locais[0].getLongitude() != null) {
                latitude = locais[0].getLatitude();
                longitude = locais[0].getLongitude();
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar coordenadas no Nominatim: " + e.getMessage());
        }

        // 3. API OpenMeteo
        BigDecimal temperaturaReal = new BigDecimal("0.0");
        Double velocidadeVentoReal = 0.0;
        Integer umidadeReal = 0;
        String condicaoTempoReal = "Desconhecido";

        try {
            String urlOpenMeteo = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude
                    + "&longitude=" + longitude
                    + "&current=temperature_2m,relative_humidity_2m,wind_speed_10m,weather_code";

            OpenMeteoResponseDTO openMeteoDTO = restTemplate.getForObject(urlOpenMeteo, OpenMeteoResponseDTO.class);

            if (openMeteoDTO != null && openMeteoDTO.getCurrentDTO() != null) {
                var current = openMeteoDTO.getCurrentDTO();

                if (current.getTemperatura() != null) temperaturaReal = current.getTemperatura();
                if (current.getVelocidadeDoVento() != null) velocidadeVentoReal = current.getVelocidadeDoVento();
                if (current.getUmidade() != null) umidadeReal = current.getUmidade();
                if (current.getWeathercode() != null) {
                    condicaoTempoReal = WmoCodeUtil.traduzirCodigo(current.getWeathercode());
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar clima no OpenMeteo: " + e.getMessage());
        }

        return ClimaModel.builder()
                .cep(cepFormatado)
                .cidade(nomeCidade)
                .uf(nomeUF)
                .temperatura(temperaturaReal)
                .condicaoTempo(condicaoTempoReal)
                .umidade(umidadeReal)
                .velocidadeDoVento(velocidadeVentoReal)
                .data(LocalDate.now())
                .build();
    }
}