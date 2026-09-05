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

        // Coordenadas padrão (fallback - Machado / Poços)
        String latitude = "-21.6811";
        String longitude = "-45.9231";

        // Configuração de Headers padrão para APIs externas
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "WeatherApp/1.0 (seu-email@dominio.com)"); // Mantenha um e-mail válido
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 2. API NOMINATIM
        try {
            String queryBusca = nomeCidade + ", " + nomeUF + ", Brazil";

            java.net.URI uriNominatim = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/search")
                    .queryParam("q", queryBusca)
                    .queryParam("format", "json")
                    .queryParam("limit", "1")
                    .build()
                    .encode()
                    .toUri();

            System.out.println("Chamando Nominatim: " + uriNominatim.toString());

            ResponseEntity<NominatimResponseDTO[]> responseNominatim = restTemplate.exchange(
                    uriNominatim,
                    HttpMethod.GET,
                    entity,
                    NominatimResponseDTO[].class
            );

            NominatimResponseDTO[] locais = responseNominatim.getBody();

            if (locais != null && locais.length > 0) {
                if (locais[0].getLatitude() != null) latitude = locais[0].getLatitude();
                if (locais[0].getLongitude() != null) longitude = locais[0].getLongitude();
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
            String latFormatted = latitude.replace(",", ".");
            String lonFormatted = longitude.replace(",", ".");

            String urlOpenMeteo = String.format(
                    java.util.Locale.US,
                    "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current=temperature_2m,relative_humidity_2m,wind_speed_10m,weather_code",
                    latFormatted, lonFormatted
            );

            ResponseEntity<OpenMeteoResponseDTO> responseOpenMeteo = restTemplate.exchange(
                    urlOpenMeteo,
                    HttpMethod.GET,
                    entity,
                    OpenMeteoResponseDTO.class
            );

            OpenMeteoResponseDTO openMeteoDTO = responseOpenMeteo.getBody();

            if (openMeteoDTO != null && openMeteoDTO.getCurrent() != null) {
                var current = openMeteoDTO.getCurrent();

                if (current.getTemperatura() != null) temperaturaReal = current.getTemperatura();
                if (current.getVelocidadeDoVento() != null) velocidadeVentoReal = current.getVelocidadeDoVento();
                if (current.getUmidade() != null) umidadeReal = current.getUmidade();
                if (current.getWeathercode() != null) {
                    condicaoTempoReal = WmoCodeUtil.traduzirCodigo(current.getWeathercode());
                }
            }
        } catch (Exception e) {
            System.err.println("Erro detalhado no OpenMeteo: " + e.getMessage());
            e.printStackTrace();
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