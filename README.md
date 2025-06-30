# Enem API - Spring Boot Implementation Requirements

This document provides a detailed specification for building the Enem API using Java and the Spring Boot framework.

## General Requirements

*   The API must be implemented using Java and Spring Boot.
*   The API should be stateless.
*   All endpoints should be implemented within controllers annotated with `@RestController`.
*   Standard Spring Boot conventions should be followed for project structure.
*   JSON responses and request bodies should be handled using Data Transfer Objects (DTOs).

## API Versions

The API must support two versions, `v1` and `v2`, accessible through the corresponding URL paths (e.g., `/api/v1/...` and `/api/v2/...`).

*   `v1`
*   `v2`

## Authentication

No authentication is required for any of the API endpoints.

---

## API v1

All v1 endpoints should be under the `/api/v1` path. This can be achieved with a base `@RequestMapping("/api/v1")` on the controller.

### OpenAPI Specification

#### `GET /api/v1`

This endpoint must provide the OpenAPI 3.0 documentation for the v1 API in JSON format. Spring Boot developers can use the `springdoc-openapi-starter-webmvc-ui` dependency to automatically generate this.

**Test with:**

```bash
curl http://localhost:8080/api/v1
```

### Exams

#### `GET /api/v1/exams`

This endpoint must return a list of all available exams.

**Implementation Notes:**
*   Use `@GetMapping("/exams")`.
*   The response should be a `List<ExamDto>`.

**`ExamDto` Structure:**
```json
[
  {
    "year": 2022,
    "name": "ENEM 2022"
  }
]
```

#### `GET /api/v1/exams/{year}`

This endpoint must return details for a specific exam year.

**Path Variable:**
*   `{year}`: The year of the exam.

**Implementation Notes:**
*   Use `@GetMapping("/exams/{year}")`.
*   The `year` should be captured with `@PathVariable`.
*   If no exam is found for the given year, a `404 Not Found` response must be returned.

**`ExamDetailDto` Structure:**
```json
{
  "year": 2022,
  "name": "ENEM 2022",
  "languages": [
    { "name": "Inglês", "value": "en" },
    { "name": "Espanhol", "value": "es" }
  ],
  "disciplines": [
    { "name": "Linguagens e Códigos", "value": "lc" },
    { "name": "Ciências Humanas", "value": "ch" },
    { "name": "Ciências da Natureza", "value": "cn" },
    { "name": "Matemática", "value": "mt" }
  ],
  "questions": [
    { "index": 1, "discipline": "lc", "language": null },
    { "index": 2, "discipline": "lc", "language": "en" }
  ]
}
```

### Questions

#### `GET /api/v1/exams/{year}/questions`

This endpoint must return a paginated and filtered list of questions for a specific exam year.

**Path Variable:**
*   `{year}`: The year of the exam.

**Query Parameters:**
*   `limit` (optional, `Integer`): The maximum number of questions to return. Default is 10, max is 50.
*   `offset` (optional, `Integer`): The number of questions to skip. Default is 0.
*   `language` (optional, `String`): Filter questions by language (`en` or `es`).
*   `discipline` (optional, `String`): Filter questions by discipline.

**Implementation Notes:**
*   Use `@GetMapping("/exams/{year}/questions")`.
*   Parameters should be captured with `@RequestParam`. Provide default values for `limit` and `offset`.
*   A `400 Bad Request` should be returned if the `limit` exceeds 50.

**`QuestionsResponseDto` Structure:**
```json
{
  "metadata": {
    "limit": 5,
    "offset": 0,
    "total": 45,
    "hasMore": true
  },
  "questions": [
    {
      "index": 6,
      "text": "...",
      "options": { "A": "...", "B": "...", "C": "...", "D": "...", "E": "..." },
      "answer": "A",
      "discipline": "lc",
      "language": "en"
    }
  ]
}
```

#### `POST /api/v1/exams/{year}/questions`

This endpoint must fetch a specific set of questions for a given exam year, based on a list of indices provided in the request body.

**Path Variable:**
*   `{year}`: The year of the exam.

**Request Body (`QuestionIndicesDto`):**
*   `indices`: An array of question indices (`Integer`) to fetch. A `400 Bad Request` must be returned if this is missing or empty.

**Query Parameters:**
*   `limit` (optional, `Integer`): The maximum number of questions to return. Default is 10, max is 50.
*   `offset` (optional, `Integer`): The number of questions to skip. Default is 0.
*   `language` (optional, `String`): The language of the questions (`en` or `es`).

**Implementation Notes:**
*   Use `@PostMapping("/exams/{year}/questions")`.
*   The request body should be mapped to a DTO using `@RequestBody`.

#### `GET /api/v1/exams/{year}/questions/{index}`

This endpoint must return a single question by its index for a specific exam year.

**Path Variables:**
*   `{year}`: The year of the exam.
*   `{index}`: The index of the question.

**Query Parameters:**
*   `language` (optional, `String`): The language of the question (`en` or `es`).

**Implementation Notes:**
*   Use `@GetMapping("/exams/{year}/questions/{index}")`.
*   If no question is found, a `404 Not Found` response must be returned.

**`QuestionDetailDto` Structure:**
```json
{
  "index": 6,
  "text": "...",
  "options": { "A": "...", "B": "...", "C": "...", "D": "...", "E": "..." },
  "answer": "A",
  "discipline": "lc",
  "language": "en"
}
```

---

## API v2

All v2 endpoints should be under the `/api/v2` path.

### OpenAPI Specification

#### `GET /api/v2`

This endpoint must provide the OpenAPI 3.0 documentation for the v2 API in JSON format.

**Test with:**

```bash
curl http://localhost:8080/api/v2
```

### Exams

#### `GET /api/v2/exams`

This endpoint must return a list of all available exams. The key difference from v1 is the inclusion of an `id` field.

**Implementation Notes:**
*   Use `@GetMapping("/exams")`.
*   The response should be a `List<ExamV2Dto>`.

**`ExamV2Dto` Structure:**
```json
[
  {
    "id": "2022",
    "year": 2022,
    "name": "ENEM 2022"
  }
]
```

#### `GET /api/v2/exams/{id}`

This endpoint must return details for a specific exam, identified by its `id`.

**Path Variable:**
*   `{id}`: The ID of the exam. This could be the year or another unique identifier.

**Implementation Notes:**
*   Use `@GetMapping("/exams/{id}")`.
*   If no exam is found for the given `id`, a `404 Not Found` response must be returned.
*   The response DTO is the same as `ExamDetailDto` from v1.

### Questions

#### `GET /api/v2/exams/{id}/questions`

This endpoint must return a paginated and filtered list of questions for a specific exam.

**Path Variable:**
*   `{id}`: The ID of the exam.

**Query Parameters:**
*   `limit` (optional, `Integer`): The maximum number of questions to return. Default is 10, max is 50.
*   `offset` (optional, `Integer`): The number of questions to skip. Default is 0.
*   `language` (optional, `String`): Filter questions by language (`en` or `es`).
*   `discipline` (optional, `String`): Filter questions by discipline.

**Implementation Notes:**
*   Uses the same `QuestionsResponseDto` as the v1 endpoint.

#### `POST /api/v2/exams/{id}/questions`

This endpoint must fetch a specific set of questions for a given exam, based on a list of indices provided in the request body.

**Path Variable:**
*   `{id}`: The ID of the exam.

**Request Body (`QuestionIndicesDto`):**
*   `indices`: An array of question indices (`Integer`) to fetch.

**Query Parameters:**
*   `limit` (optional, `Integer`): Default is 10, max is 50.
*   `offset` (optional, `Integer`): Default is 0.
*   `language` (optional, `String`): The language of the questions (`en` or `es`).

**Implementation Notes:**
*   Uses the same `QuestionIndicesDto` as the v1 endpoint for the request body.
*   Returns a `QuestionsResponseDto` as in v1.

#### `GET /api/v2/exams/{id}/questions/{index}`

This endpoint must return a single question by its index for a specific exam.

**Path Variables:**
*   `{id}`: The ID of the exam.
*   `{index}`: The index of the question.

**Query Parameters:**
*   `language` (optional, `String`): The language of the question (`en` or `es`).

**Implementation Notes:**
*   If no question is found, a `404 Not Found` response must be returned.
*   Returns the same `QuestionDetailDto` as v1.
