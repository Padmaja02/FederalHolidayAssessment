# Holiday API

## 📌 Overview
This project provides a RESTful API to manage federal holidays for supported countries (USA and Canada).
It supports CRUD operations, bulk upload via CSV, idempotency, and robust error handling.

---

## 🚀 Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database (in-memory)
- Lombok
- Swagger (OpenAPI)
- JUnit & MockMvc

---

## ⚙️ Features

### ✅ Core APIs
- Add Holiday
- Update Holiday
- List Holidays (by country & year)

### 📂 File Upload
- Upload holidays via CSV
- Supports bulk ingestion
- Handles invalid rows gracefully (partial processing)

### 🔁 Idempotency
- Prevents duplicate holidays (same date + country)
- Enforced at:
    - Database level (unique constraint)
    - Service level (pre-check)

### ❗ Exception Handling
- Global exception handler (`@RestControllerAdvice`)
- Structured error response:
    - message
    - status

### 🧪 Testing
- Unit tests (service layer)
- Integration tests (controller layer)
- Covers edge cases (invalid input, duplicates, upload errors)

---

## ▶️ How to Run

### Run with H2
mvn spring-boot:run

In any IDE:
- clean package
- Run the HolidayApiApplication.java

Access:
- Swagger UI → http://localhost:8081/swagger-ui.html
- H2 Console → http://localhost:8081/h2-console

H2 Config:
- JDBC URL: jdbc:h2:mem:testdb
- Username: user
- Password: root1@

---

## 📘 API Endpoints

### 1. Create Holiday
POST /api/v1/holidays

{
"name": "Independence Day",
"date": "2026-07-04",
"country": "USA"
}

---

### 2. Update Holiday
PUT /api/v1/holidays/{id}

---

### 3. Get Holidays
GET /api/v1/holidays?country=USA&year=2026

---

### 4. Upload CSV
POST /api/v1/holidays/upload  
Content-Type: multipart/form-data

---

## 📂 CSV Format

name,date,country
Christmas,2026-12-25,USA
Canada Day,2026-07-01,CANADA

---

## 🧪 CSV Test Scenarios - Uploaded file in FileUploadSample Folder

### ✅ Valid File
- Proper format
- Valid dates
- Supported countries

### ❌ Invalid Cases
- Invalid date format
- Invalid country (not in enum)
- Incorrect column count
- Empty or header-only file

### 🔁 Mixed Data (Advanced Handling)
- Valid rows are processed
- Invalid rows are skipped
- Response includes:
    - successCount
    - failureCount
    - error messages

---

## 📊 Sample Upload Response

{
"successCount": 2,
"failureCount": 2,
"errors": [
"Invalid date format at line: Bad Date,2026-99-99,USA",
"Invalid country value at line: Holiday,2026-01-01,INDIA"
]
}

---

## ❗ Error Handling

### Invalid Country in Query
{
"message": "Invalid value 'CAADA' for parameter 'country'. Allowed values are: [USA, CANADA]",
"status": 400
}

### Duplicate Holiday
{
"message": "Holiday already exists for given date and country",
"status": 409
}

---

## 🧠 Design Decisions
- Used @RequestParam for file upload (multipart/form-data standard)
- Constructor injection with final fields for immutability
- DTO + Mapper pattern for clean separation
- Global exception handling for consistent responses
- Partial processing for CSV upload to improve robustness

---

## 🔮 Future Improvements
- Pagination & sorting for GET APIs
- Support more countries dynamically
- External holiday provider integration
- Authentication & authorization
- Caching (Redis)

---

## 📬 Postman Collection
Included in project for easy API testing.

---

## 🧑‍💻 Author Notes
This implementation focuses on:
- Clean code practices
- Robust error handling
- Extensibility
- Developer-friendly APIs