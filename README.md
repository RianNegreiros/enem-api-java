# Enem API

Esta é uma API Java Spring Boot para acessar e provas e questões do ENEM (Exame Nacional do Ensino Médio).

## Funcionalidades
- Fornece endpoints para acessar detalhes e questões do ENEM por ano

## Primeiros Passos

### Pré-requisitos
- Java 17 ou superior
- Gradle (ou utilize o wrapper `gradlew` incluído)

### Build e Execução

```
./gradlew build
./gradlew bootRun
```

A API estará disponível em `http://localhost:8080` por padrão.

### Endpoints da API

- `/api/exams` – Lista os exames disponíveis
- `/api/exams/{year}` – Detalhes de um ano específico
- `/api/exams/{year}/questions` – Questões de um ano específico

## Documentação Swagger

Após iniciar a aplicação, acesse:

- `http://localhost:8080/swagger-ui.html`

## Configuração
- Edite `src/main/resources/application.properties` para alterar configurações do servidor ou outras opções.

## Dados Estáticos
- Os dados das provas estão em `src/main/resources/static/` organizados por ano (ex: `2009/details.json`, `2009/questions/`).

## Licença

Este projeto está licenciado sob a Licença GENERAL PUBLIC LICENSE. Veja [LICENSE](LICENSE) para mais detalhes.

## Contribuindo

Pull requests são bem-vindos! Para mudanças maiores, abra uma issue primeiro para discutir o que você gostaria de alterar.
