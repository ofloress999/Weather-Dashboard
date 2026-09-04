# 🌤️ Weather Dashboard - API & Frontend de Clima por CEP

Uma aplicação web full-stack desenvolvida em **Java com Spring Boot** no back-end e **HTML5/Tailwind CSS** no front-end, que permite consultar as condições climáticas em tempo real de qualquer cidade do Brasil a partir do CEP.

---

## 📸 Demonstração & Recursos Visuais

- **Busca Rápida por CEP:** Integração automatizada com serviços de geolocalização e previsão do tempo.
- **Interface Dinâmica (Glassmorphism):** Layout moderno estilizado com Tailwind CSS.
- **Tema Adaptativo (Dia & Noite):** O fundo da tela e os ícones mudam automaticamente de acordo com o clima e o horário do dia (ex: *Céu limpo à noite exibe tons de azul escuro e ícone de lua*).

---

## 🛠️ Arquitetura & Fluxo de Dados

A aplicação consome e encadeia três APIs REST externas para entregar os dados ao usuário:

1. **ViaCEP API:** Recebe o CEP digitado, valida a estrutura e retorna o nome da cidade e o estado (UF).
2. **Nominatim (OpenStreetMap) API:** Recebe o nome da cidade/UF e retorna as coordenadas geográficas exatas (*latitude* e *longitude*).
3. **Open-Meteo API:** Recebe as coordenadas geográficas e retorna os dados meteorológicos atuais em tempo real (temperatura, umidade relativa do ar, velocidade do vento e código de condição climática WMO).

---

## 🚀 Tecnologias Utilizadas

### Back-end
- **Java 17 / 21**
- **Spring Boot 3** (Web)
- **Lombok** (Redução de código boilerplate)
- **Jackson** (Mapeamento e desserialização de JSON)
- **RestTemplate / UriComponentsBuilder** (Consumo de APIs externas)

### Front-end
- **HTML5 & JavaScript Vanilla (ES6+)**
- **Tailwind CSS (via CDN)**
- **Lucide Icons** (Ícones dinâmicos)

---