# ENEM API – Endpoints Documentation

## Endpoints

---

### List all exams

**GET** `/exams`

- Returns a list of all available ENEM exams.
- **Response:** 200 OK, JSON array of exams.

---

### Get exam details by year

**GET** `/exams/{year}`

- Returns details for a specific exam year.
- **Path parameter:** `year` (number, e.g. 2023)
- **Response:** 200 OK, JSON object with exam details.
- **Errors:** 404 if the year is not found.

---

### List questions for an exam year

**GET** `/exams/{year}/questions`

- Returns a paginated list of questions for a given exam year.
- **Path parameter:** `year` (number)
- **Query parameters:**
  - `limit` (max 50)
  - `offset`
  - `language`
  - `discipline`
- **Response:** 200 OK, JSON array of questions.

---

### Get details for a specific question

**GET** `/exams/{year}/questions/{index}`

- Returns details for a specific question in an exam year.
- **Path parameters:**
  - `year` (number)
  - `index` (number)
- **Query parameters:**
  - `language`
- **Response:** 200 OK, JSON object with question details.
- **Errors:** 404 if not found.

---

### Get multiple questions by index (batch)

**POST** `/exams/{year}/questions`

- Returns details for multiple questions by their indices.
- **Path parameter:** `year` (number)
- **Request body:** JSON with a list of indices.
- **Response:** 200 OK, JSON array of question details.

---

## Error Responses

- Standard error responses include status codes and error messages for invalid requests, not found, and rate limits.

---
