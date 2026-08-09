# Student Management REST API

A production-style Student Management REST API built using Spring Boot, Spring Data JPA, MySQL, Bean Validation, and Swagger/OpenAPI.

The application provides complete CRUD operations along with search, pagination, sorting, validation, centralized exception handling, duplicate-email detection, and automated testing.

---

## 🚀 Features

- Student CRUD operations
- Create, read, update and delete students
- Email uniqueness validation
- Request validation using Jakarta Bean Validation
- Global exception handling
- Standardized error responses
- Search students by name
- Search students by department
- Combined name + department search
- Pagination
- Dynamic sorting
- Sort direction validation
- Configurable page size
- MySQL database integration
- H2 database for testing
- Spring Data JPA
- Swagger/OpenAPI documentation
- CORS configuration
- Automated unit and integration tests
- Environment-variable based database configuration

---

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot 3.5.5 | Backend framework |
| Spring Web | REST API development |
| Spring Data JPA | Database persistence |
| Hibernate | ORM |
| MySQL | Production database |
| H2 | Test database |
| Jakarta Validation | Request validation |
| Gradle | Build and dependency management |
| Swagger / OpenAPI | API documentation |
| JUnit | Testing |
| Mockito | Mock-based testing |
| Git / GitHub | Version control |

---

## 🏗️ Architecture

The application follows a layered architecture:

```text
Client
   │
   ▼
REST Controller
   │
   ▼
Service Layer
   │
   ▼
Repository Layer
   │
   ▼
MySQL Database

### Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── subrata/
│   │           └── studentmanagement/
│   │               ├── config/
│   │               │   ├── CorsConfig.java
│   │               │   ├── OpenAPIConfig.java
│   │               │   └── WebConfig.java
│   │               │
│   │               ├── controller/
│   │               │   └── StudentController.java
│   │               │
│   │               ├── dto/
│   │               │   ├── PageResponseDTO.java
│   │               │   ├── StudentRequestDTO.java
│   │               │   └── StudentResponseDTO.java
│   │               │
│   │               ├── entity/
│   │               │   └── Student.java
│   │               │
│   │               ├── exception/
│   │               │   ├── EmailAlreadyExistsException.java
│   │               │   ├── ErrorResponse.java
│   │               │   ├── GlobalExceptionHandler.java
│   │               │   ├── InvalidPaginationException.java
│   │               │   └── StudentNotFoundException.java
│   │               │
│   │               ├── repository/
│   │               │   └── StudentRepository.java
│   │               │
│   │               ├── service/
│   │               │   ├── StudentService.java
│   │               │   └── StudentServiceImpl.java
│   │               │
│   │               └── StudentManagementApplication.java
│   │
│   └── resources/
│       └── application.properties
│
└── test/
    ├── java/
    │   └── com/subrata/studentmanagement/
    │       ├── controller/
    │       ├── repository/
    │       ├── service/
    │       └── StudentManagementApplicationTests.java
    │
    └── resources/
        ├── application.properties
        └── application-test.properties

### Architecture Flow

```text
                    ┌─────────────────────┐
                    │       Client        │
                    │ Postman / Frontend  │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │  REST Controller    │
                    │ StudentController   │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │   Service Layer     │
                    │ StudentServiceImpl  │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │ Repository Layer    │
                    │ StudentRepository   │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │   MySQL Database    │
                    │ student_management   │
                    └─────────────────────┘


       ┌─────────────────────────────────────────┐
       │       Global Exception Handler          │
       │   Validation / 404 / 409 / 400 / 500   │
       └─────────────────────────────────────────┘

# 🔌 API Endpoints

Base URL:

```text
http://localhost:8080/api/students

1. Create Student

Creates a new student in the database.

HTTP Request
POST /api/students
Content-Type: application/json
Request Body
{
  "name": "Rahul Kumar",
  "email": "rahul@example.com",
  "department": "CSE",
  "year": 4,
  "phone": "9123456789"
}
Successful Response

HTTP 201 Created

{
  "id": 11,
  "name": "Rahul Kumar",
  "email": "rahul@example.com",
  "department": "CSE",
  "year": 4,
  "phone": "9123456789"
}
Possible Responses
Status	Description
201 Created	Student created successfully
400 Bad Request	Request validation failed
409 Conflict	Email already exists
2. Get Student by ID

Retrieves a single student using the student's unique ID.

HTTP Request
GET /api/students/{id}
Example
GET /api/students/4
Successful Response

HTTP 200 OK

{
  "id": 4,
  "name": "Subrata Mallick",
  "email": "subrata2@gmail.com",
  "department": "CSE",
  "year": 4,
  "phone": "9123456789"
}
Student Not Found

If the requested student does not exist:

HTTP 404 Not Found

{
  "timestamp": "2026-08-09T06:21:45",
  "status": 404,
  "error": "Not Found",
  "message": "Student with ID 999 not found"
}
3. Get All Students

Retrieves all students from the database.

The endpoint supports:

Pagination
Sorting
Ascending order
Descending order
Configurable page size
HTTP Request
GET /api/students
Default Request
GET /api/students

Default values:

page = 0
size = 5
sortBy = id
direction = asc
Example Response
{
  "content": [
    {
      "id": 4,
      "name": "Subrata Mallick",
      "email": "subrata2@gmail.com",
      "department": "CSE",
      "year": 4,
      "phone": "9123456789"
    },
    {
      "id": 11,
      "name": "Rahul Kumar",
      "email": "rahul@example.com",
      "department": "CSE",
      "year": 4,
      "phone": "9123456789"
    }
  ],
  "page": 0,
  "size": 5,
  "totalElements": 2,
  "totalPages": 1
}
4. Pagination

Pagination prevents the API from returning a large number of records in a single response.

Parameters
Parameter	Description	Default
page	Zero-based page number	0
size	Number of students per page	5
Example
GET /api/students?page=0&size=5
Second Page
GET /api/students?page=1&size=5
Page Size of 10
GET /api/students?page=0&size=10
Page Size Restrictions

The API validates the requested page size.

Minimum size: 1
Maximum size: 100

Invalid examples:

GET /api/students?page=-1&size=5
GET /api/students?page=0&size=0
GET /api/students?page=0&size=101

These requests result in:

HTTP 400 Bad Request

5. Sorting

The API supports dynamic sorting.

Supported Sort Fields
id
name
email
department
year
phone
Sort by Name - Ascending
GET /api/students?sortBy=name&direction=asc
Sort by Name - Descending
GET /api/students?sortBy=name&direction=desc
Sort by Department
GET /api/students?sortBy=department&direction=asc
Sort by Year
GET /api/students?sortBy=year&direction=desc
Sorting with Pagination
GET /api/students?page=0&size=5&sortBy=name&direction=asc
Invalid Sort Field
GET /api/students?sortBy=password&direction=asc

Response:

HTTP 400 Bad Request

{
  "timestamp": "2026-08-09T06:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid sort field: password"
}
Invalid Sort Direction
GET /api/students?sortBy=name&direction=random

Response:

HTTP 400 Bad Request

{
  "timestamp": "2026-08-09T06:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Sort direction must be either 'asc' or 'desc'"
}
6. Update Student

Updates an existing student's information.

HTTP Request
PUT /api/students/{id}
Content-Type: application/json
Example
PUT /api/students/4
Request Body
{
  "name": "Subrata Mallick",
  "email": "subrata2@gmail.com",
  "department": "ECE",
  "year": 4,
  "phone": "9876543210"
}
Successful Response

HTTP 200 OK

{
  "id": 4,
  "name": "Subrata Mallick",
  "email": "subrata2@gmail.com",
  "department": "ECE",
  "year": 4,
  "phone": "9876543210"
}
Update Validation

The same validation rules used during student creation are applied during updates.

Possible Responses
Status	Description
200 OK	Student updated successfully
400 Bad Request	Validation failed
404 Not Found	Student does not exist
409 Conflict	Email belongs to another student
7. Delete Student

Deletes an existing student from the database.

HTTP Request
DELETE /api/students/{id}
Example
DELETE /api/students/4
Successful Response

HTTP 204 No Content

The student is successfully deleted and the API returns no response body.

Student Not Found
DELETE /api/students/999

Response:

HTTP 404 Not Found

{
  "timestamp": "2026-08-09T06:24:56",
  "status": 404,
  "error": "Not Found",
  "message": "Student with ID 999 not found"
}
8. Search Student by Name

Searches for students whose names contain the specified text.

The search is:

Case-insensitive
Partial-match based
HTTP Request
GET /api/students/search/name?name={name}
Example
GET /api/students/search/name?name=Subrata

The following can match the same student:

Subrata
subrata
SUBRATA
Example Response
[
  {
    "id": 4,
    "name": "Subrata Mallick",
    "email": "subrata2@gmail.com",
    "department": "CSE",
    "year": 4,
    "phone": "9123456789"
  }
]
9. Search Student by Department

Searches students based on their department.

The search is case-insensitive.

HTTP Request
GET /api/students/search/department?department={department}
Example
GET /api/students/search/department?department=ECE
Example Response
[
  {
    "id": 4,
    "name": "Subrata Mallick",
    "email": "subrata2@gmail.com",
    "department": "ECE",
    "year": 4,
    "phone": "9876543210"
  }
]
10. Combined Search

Searches students using both:

Student name
Department

The combined search also supports:

Pagination
Sorting
Case-insensitive name search
Case-insensitive department matching
HTTP Request
GET /api/students/search
Basic Search
GET /api/students/search?name=Subrata&department=ECE
Search with Pagination
GET /api/students/search?name=Subrata&department=ECE&page=0&size=5
Search with Sorting
GET /api/students/search?name=Subrata&department=ECE&page=0&size=5&sortBy=name&direction=asc
Example Response
{
  "content": [
    {
      "id": 4,
      "name": "Subrata Mallick",
      "email": "subrata2@gmail.com",
      "department": "ECE",
      "year": 4,
      "phone": "9876543210"
    }
  ],
  "page": 0,
  "size": 5,
  "totalElements": 1,
  "totalPages": 1
}
11. Request Validation

The API uses Jakarta Bean Validation to validate incoming requests.

Name Validation

The name cannot be empty.

{
  "name": "",
  "email": "student@example.com",
  "department": "CSE",
  "year": 4,
  "phone": "9123456789"
}

Response:

HTTP 400 Bad Request

{
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "name": "Name cannot be empty"
  }
}
Email Validation

The email must be valid.

Invalid example:

{
  "name": "Student",
  "email": "student@",
  "department": "CSE",
  "year": 4,
  "phone": "9123456789"
}

Response:

HTTP 400 Bad Request

{
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "email": "Enter a valid email"
  }
}
Department Validation

Department cannot be empty.

Department cannot be empty
Year Validation

The year must be between 1 and 4.

Minimum: 1
Maximum: 4

Examples:

Valid:   1
Valid:   2
Valid:   3
Valid:   4

Invalid: 0
Invalid: 5
Phone Validation

The phone number must contain exactly 10 digits.

Valid
9123456789
Invalid
912345678
91234567890
91234abc89

Example error response:

{
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "phone": "Phone number must contain exactly 10 digits"
  }
}
12. Duplicate Email Exception

Email addresses are unique in the system.

If an email already exists, creating another student with the same email is rejected.

Example Request
POST /api/students
{
  "name": "Another Student",
  "email": "subrata2@gmail.com",
  "department": "CSE",
  "year": 3,
  "phone": "9000000000"
}
Response

HTTP 409 Conflict

{
  "timestamp": "2026-08-09T06:18:36",
  "status": 409,
  "error": "Conflict",
  "message": "Email already exists"
}

The same duplicate-email rule is also applied when updating a student.

13. Student Not Found Exception

If a requested student ID does not exist, the API returns a 404 Not Found response.

Example
GET /api/students/999
Response
{
  "timestamp": "2026-08-09T06:21:45",
  "status": 404,
  "error": "Not Found",
  "message": "Student with ID 999 not found"
}

This exception is also used for:

GET /api/students/{id}
PUT /api/students/{id}
DELETE /api/students/{id}
14. Pagination and Sorting Validation

The API validates pagination and sorting parameters before executing database queries.

Invalid Page Number
GET /api/students?page=-1&size=5

Response:

{
  "status": 400,
  "error": "Bad Request",
  "message": "Page number cannot be negative"
}
Invalid Page Size
GET /api/students?page=0&size=0

Response:

{
  "status": 400,
  "error": "Bad Request",
  "message": "Page size must be greater than 0"
}
Page Size Greater Than Maximum
GET /api/students?page=0&size=101

Response:

{
  "status": 400,
  "error": "Bad Request",
  "message": "Page size cannot be greater than 100"
}
15. Global Exception Handling

The application uses a centralized GlobalExceptionHandler with @RestControllerAdvice.

The following exceptions are handled centrally:

Exception	HTTP Status	Purpose
StudentNotFoundException	404	Student does not exist
EmailAlreadyExistsException	409	Duplicate email
MethodArgumentNotValidException	400	Request validation failure
IllegalArgumentException	400	Invalid parameters
Exception	500	Unexpected server error

This provides consistent error responses throughout the API.

16. Standard HTTP Status Codes

The API follows standard HTTP semantics.

HTTP Status	Meaning	Example
200 OK	Successful request	Get / Update
201 Created	Resource created	Create Student
204 No Content	Resource deleted	Delete Student
400 Bad Request	Invalid request	Validation / Pagination
404 Not Found	Resource unavailable	Student ID not found
409 Conflict	Resource conflict	Duplicate email
500 Internal Server Error	Unexpected server error	Internal failure

### Complete CRUD Flow

                    CREATE
                      │
                      ▼
              POST /api/students
                      │
                      ▼
                    READ
                      │
          ┌───────────┴───────────┐
          ▼                       ▼
 GET /api/students        GET /api/students/{id}
          │
          ▼
   PAGINATION
          │
          ▼
     SORTING
          │
          ▼
      SEARCH
          │
          ├── Search by Name
          │
          ├── Search by Department
          │
          └── Combined Search
                      │
                      ▼
                    UPDATE
                      │
                      ▼
             PUT /api/students/{id}
                      │
                      ▼
                    DELETE
                      │
                      ▼
            DELETE /api/students/{id}

---

## ⚠️ Error Handling

The application uses centralized exception handling through `@RestControllerAdvice`.

All API errors are returned in a consistent JSON format.

### 1. Student Not Found — 404

Occurs when a requested student ID does not exist.

**Example Request**

```http
GET /api/students/999

Response

{
  "timestamp": "2026-08-09T06:21:45.3065418",
  "status": 404,
  "error": "Not Found",
  "message": "Student with ID 999 not found"
}
2. Duplicate Email — 409

Occurs when attempting to create or update a student using an email address that already exists.

Example Request

POST /api/students

Response

{
  "timestamp": "2026-08-09T06:18:36.8196042",
  "status": 409,
  "error": "Conflict",
  "message": "Email already exists"
}
3. Validation Error — 400

Occurs when the request contains invalid data.

Invalid Email
{
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "email": "Enter a valid email"
  },
  "timestamp": "2026-08-09T06:25:53.1074967",
  "status": 400
}
Invalid Phone Number
{
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "phone": "Phone number must contain exactly 10 digits"
  },
  "timestamp": "2026-08-09T06:26:45.4266117",
  "status": 400
}
4. Invalid Pagination or Sorting — 400

The API validates pagination and sorting parameters.

Examples of invalid requests:

GET /api/students?page=-1
GET /api/students?size=0
GET /api/students?size=101
GET /api/students?sortBy=invalidField
GET /api/students?direction=random

The API returns HTTP 400 Bad Request with an explanatory error message.

5. Internal Server Error — 500

Unexpected application errors are handled centrally and return:

{
  "timestamp": "2026-08-09T00:00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred"
}
🔐 Validation Rules

The following validation rules are applied when creating or updating a student:

Field	Validation
name	Cannot be blank
email	Cannot be blank and must be a valid email
department	Cannot be blank
year	Must be between 1 and 4
phone	Must contain exactly 10 digits
Example Valid Request
{
  "name": "Subrata Mallick",
  "email": "subrata2@gmail.com",
  "department": "ECE",
  "year": 4,
  "phone": "9123456789"
}
📄 Pagination

The API supports pagination through query parameters.

Parameters
Parameter	Default	Description
page	0	Zero-based page number
size	5	Number of records per page
sortBy	id	Field used for sorting
direction	asc	Sorting direction
Example
GET /api/students?page=0&size=5&sortBy=name&direction=asc
Example Response
{
  "content": [
    {
      "id": 4,
      "name": "Subrata Mallick",
      "email": "subrata2@gmail.com",
      "department": "ECE",
      "year": 4,
      "phone": "9123456789"
    },
    {
      "id": 11,
      "name": "Rahul Kumar",
      "email": "rahul@example.com",
      "department": "CSE",
      "year": 4,
      "phone": "9123456789"
    }
  ],
  "page": 0,
  "size": 5,
  "totalElements": 2,
  "totalPages": 1
}
🔎 Search
Search by Name
GET /api/students/search/name?name=Subrata

Returns students whose names contain the supplied text.

The search is case-insensitive.

Search by Department
GET /api/students/search/department?department=ECE

Returns students belonging to the specified department.

The search is case-insensitive.

Combined Search
GET /api/students/search?name=Subrata&department=ECE&page=0&size=5&sortBy=id&direction=asc

The combined search supports:

Name filtering
Department filtering
Pagination
Sorting
Sort direction
📚 API Documentation

Swagger/OpenAPI documentation is available when the application is running.

Swagger UI
http://localhost:8080/swagger-ui/index.html
OpenAPI Specification
http://localhost:8080/v3/api-docs

Swagger provides an interactive interface for:

Viewing available endpoints
Understanding request parameters
Viewing request/response schemas
Executing API requests
Testing validation and error responses
🗄️ Database Configuration

The application uses MySQL as the primary database.

Database configuration is supplied through environment variables.

Environment Variables
DB_URL
DB_USERNAME
DB_PASSWORD

Example:

DB_URL=jdbc:mysql://localhost:3306/student_management
DB_USERNAME=root
DB_PASSWORD=your_password

Database credentials should never be committed to GitHub.

The .gitignore file excludes local environment and configuration files containing secrets.

🧪 Testing

The project contains automated tests for the major application layers.

Test Coverage Includes
Application context loading
Student service operations
Student creation
Student retrieval
Student update
Student deletion
Duplicate email handling
Student-not-found handling
Validation handling
Pagination
Sorting
Search operations
Repository operations
Controller endpoints

The project uses:

JUnit
Mockito
Spring Boot Test
Spring MockMvc
H2 in-memory database
Run Tests

Windows:

.\gradlew test

Linux/macOS:

./gradlew test

Expected result:

BUILD SUCCESSFUL

student-management/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/subrata/studentmanagement/
│   │   │       │
│   │   │       ├── config/
│   │   │       │   ├── CorsConfig.java
│   │   │       │   ├── OpenAPIConfig.java
│   │   │       │   └── WebConfig.java
│   │   │       │
│   │   │       ├── controller/
│   │   │       │   └── StudentController.java
│   │   │       │
│   │   │       ├── dto/
│   │   │       │   ├── PageResponseDTO.java
│   │   │       │   ├── StudentRequestDTO.java
│   │   │       │   └── StudentResponseDTO.java
│   │   │       │
│   │   │       ├── entity/
│   │   │       │   └── Student.java
│   │   │       │
│   │   │       ├── exception/
│   │   │       │   ├── EmailAlreadyExistsException.java
│   │   │       │   ├── ErrorResponse.java
│   │   │       │   ├── GlobalExceptionHandler.java
│   │   │       │   ├── InvalidPaginationException.java
│   │   │       │   └── StudentNotFoundException.java
│   │   │       │
│   │   │       ├── repository/
│   │   │       │   └── StudentRepository.java
│   │   │       │
│   │   │       ├── service/
│   │   │       │   ├── StudentService.java
│   │   │       │   └── StudentServiceImpl.java
│   │   │       │
│   │   │       └── StudentManagementApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       ├── java/
│       │   └── com/subrata/studentmanagement/
│       │       ├── controller/
│       │       ├── repository/
│       │       ├── service/
│       │       └── StudentManagementApplicationTests.java
│       │
│       └── resources/
│           ├── application.properties
│           └── application-test.properties
│
├── gradle/
├── postman/
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── .gitignore
└── README.md

Client
   │
   │ HTTP Request
   ▼
StudentController
   │
   │ Validated Request DTO
   ▼
StudentService
   │
   │ Business Logic
   ▼
StudentRepository
   │
   │ JPA / Hibernate
   ▼
MySQL Database
   │
   │ Student Data
   ▼
StudentRepository
   │
   ▼
StudentService
   │
   │ Response DTO
   ▼
StudentController
   │
   │ JSON Response
   ▼
Client

🧩 Design Highlights
Layered Architecture

The application separates responsibilities into:

Controller layer
Service layer
Repository layer
Entity layer
DTO layer
Exception handling layer
Configuration layer
DTO Pattern

Separate request and response DTOs are used instead of exposing the JPA entity directly through the REST API.

Centralized Exception Handling

GlobalExceptionHandler provides consistent error responses across the application.

Database Safety

Database credentials are provided using environment variables instead of hard-coded credentials.

Validation

Jakarta Bean Validation prevents invalid student data from reaching the service layer.

Pagination and Sorting

Spring Data's Pageable and Sort APIs are used for efficient database-level pagination and sorting.

🚀 Running the Application
1. Clone the Repository
git clone https://github.com/MallickSayan407/student-management.git
cd student-management
2. Configure MySQL

Create the database:

CREATE DATABASE student_management;

Set the required environment variables:

DB_URL=jdbc:mysql://localhost:3306/student_management
DB_USERNAME=root
DB_PASSWORD=your_password
3. Run the Application

Windows:

.\gradlew bootRun

Linux/macOS:

./gradlew bootRun

The application starts on:

http://localhost:8080
4. Open Swagger
http://localhost:8080/swagger-ui/index.html
📌 Future Improvements

Possible future enhancements include:

Authentication and authorization using Spring Security
JWT-based authentication
Role-based access control
Docker containerization
Docker Compose for MySQL and the application
CI/CD using GitHub Actions
API response caching
Advanced filtering
Database migration using Flyway or Liquibase
Production deployment to AWS/Azure/GCP
Frontend using React or Angular
Monitoring and application metrics
👨‍💻 Author

Subrata Mallick

B.Tech — Electronics & Communication Engineering

Interested in:

Backend Development
Java & Spring Boot
REST API Development
Database Systems
IoT
Cloud Computing
