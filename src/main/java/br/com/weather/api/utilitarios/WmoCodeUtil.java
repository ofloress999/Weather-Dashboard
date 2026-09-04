package br.com.weather.api.utilitarios;

public class WmoCodeUtil {

    public static String traduzirCodigo(Integer code) {
        if(code == null) return "Desconhecido";

        return switch (code) {
            case 0 -> "Céu limpo";
            case 1, 2, 3 -> "Parcialmente Nublado";
            case 45, 48 -> "Névoa / Ensolarado com Nevoeiro";
            case 51, 53, 55 -> "Garoa Fininha";
            case 61, 63, 65 -> "Chuva";
            case 80, 81, 82 -> "Pancadas de Chuva";
            case 95, 96, 99 -> "Tempestade";
            default -> "Nublado";
        };
    }
}
