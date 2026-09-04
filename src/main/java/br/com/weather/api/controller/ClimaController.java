package br.com.weather.api.controller;

import br.com.weather.api.model.ClimaModel;
import br.com.weather.api.service.ClimaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/clima")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClimaController {
    
    private final ClimaService climaService;

    @GetMapping("/{cep}")
    public ResponseEntity<ClimaModel> obterClimaTeste(@PathVariable String cep) {
        return ResponseEntity.ok(climaService.obterClima(cep));
    }


}
