# 💳 Banking Portal API

A production-oriented **Digital Banking REST API** built with **Java** and **Spring Boot**. Banking Portal API provides secure authentication, account management, banking transactions, transaction history, validation, centralized exception handling, and database persistence.

The project demonstrates practical **backend engineering, secure REST API development, and modern Spring Boot architecture**.

---

## ✨ Features

### 🔐 Authentication
- Secure User Registration
- User Login
- JWT Token Generation
- Password Encryption using BCrypt
- JWT-based Authentication
- Account Ownership Validation

### 🏦 Banking Operations
- Bank Account Creation
- Account Details
- User Account Listing
- Deposit Money
- Withdraw Money
- Fund Transfer
- Transaction History
- Account Closing

### 🛡️ Validation & Exception Handling
- Request Validation
- Global Exception Handling
- User Not Found Handling
- Account Not Found Handling
- Insufficient Balance Handling
- Inactive Account Handling
- Same Account Transfer Prevention
- Duplicate Email Handling
- Invalid Credentials Handling
- Structured Error Responses

### 📖 API Documentation
- Swagger / OpenAPI Documentation
- Interactive API Testing
- JWT Bearer Authentication

---

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| Language | Java 17 |
| Framework | Spring Boot |
| Security | Spring Security, JWT |
| Database | MySQL |
| ORM | Spring Data JPA / Hibernate |
| Build Tool | Maven |
| Validation | Jakarta Bean Validation |
| Documentation | Swagger / OpenAPI |
| Testing | JUnit, Spring Boot Test |
| Utilities | Lombok |
| Version Control | Git, GitHub |

---

## 📂 Project Structure

```text
Finedge API /
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── virag/
│   │   │           └── finedge/
│   │   │               ├── controller/
│   │   │               ├── service/
│   │   │               ├── repository/
│   │   │               ├── entity/
│   │   │               ├── dto/
│   │   │               ├── security/
│   │   │               ├── config/
│   │   │               └── exception/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│
├── .github/
│   └── workflows/
│
├── .gitignore
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

## 📌 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Authenticate user and generate JWT |
| POST | `/api/accounts` | Create a bank account |
| GET | `/api/accounts` | Get authenticated user's accounts |
| GET | `/api/accounts/{accountNumber}` | Get account details |
| POST | `/api/accounts/{accountNumber}/deposit` | Deposit money |
| POST | `/api/accounts/{accountNumber}/withdraw` | Withdraw money |
| POST | `/api/accounts/{accountNumber}/transfer` | Transfer money |
| GET | `/api/accounts/{accountNumber}/transactions` | Get transaction history |
| PATCH | `/api/accounts/{accountNumber}/close` | Close a bank account |

> 🔐 Protected endpoints require a valid JWT token.

---

## 🔑 Authentication

FinEdgeAPI uses **JWT-based authentication** with Spring Security.

User authentication follows this flow:

User Registration → User Login → JWT Generation → JWT Validation → Access Protected APIs

For authenticated requests, use:

Authorization: Bearer <JWT_TOKEN>

---

## 🛡️ Security

The application uses **Spring Security and JWT** to protect banking operations.

Security features include:

- JWT-based Authentication
- BCrypt Password Encryption
- Protected REST APIs
- Authentication Validation
- Account Ownership Verification
- Unauthorized Request Handling
- Secure Access to Banking Operations

Users can only access and perform transactions on accounts associated with their authenticated identity.

---

## ⚠️ Exception Handling

FinEdgeAPI uses **centralized exception handling** to provide consistent and meaningful API error responses.

The application handles:

- User Not Found
- Account Not Found
- Insufficient Balance
- Account Not Active
- Same Account Transfer
- Invalid Credentials
- Duplicate Email
- Request Validation Errors
- Unexpected Server Errors

Example error response:

{
  "timestamp": "2026-08-17T00:30:29",
  "status": 400,
  "error": "Bad Request",
  "message": "Insufficient balance"
}

---

## 💾 Database

FinEdgeAPI uses **MySQL** with **Spring Data JPA / Hibernate** for database persistence.

The database manages:

- User Information
- Bank Account Information
- Transaction Records

Spring Data JPA repositories are used for database operations while Hibernate provides object-relational mapping.

---

## 💰 Banking Operations

FinEdgeAPI supports the following core banking operations:

- Create Bank Account
- View Account Details
- View User Accounts
- Deposit Money
- Withdraw Money
- Transfer Funds
- View Transaction History
- Close Bank Account

Before performing banking operations, the application validates:

- User ownership
- Account existence
- Account status
- Available balance
- Transfer conditions
- Same-account transfers

---

## 🧪 Testing

The project includes automated tests for application and service-level functionality.

Run the complete test suite on Windows PowerShell:

.\mvnw.cmd clean test

For Linux or macOS:

./mvnw clean test

The project has been successfully verified using Maven tests with a successful build.

---

## 📖 Swagger / OpenAPI Documentation

FinEdgeAPI provides interactive API documentation using **Swagger / OpenAPI**.

After starting the application, open:

http://localhost:8080/swagger-ui/index.html

Swagger UI allows you to:

- Explore API endpoints
- View request and response structures
- Authorize using JWT
- Test protected APIs
- Test banking operations

---

## ⚙️ Running the Project

### 1. Clone the Repository

git clone https://github.com/virag185/FinEdgeAPI.git

### 2. Navigate to the Project

cd FinEdgeAPI

### 3. Configure MySQL

Create a MySQL database and configure the database connection in:

src/main/resources/application.properties

Use environment variables for sensitive credentials where possible.

Example configuration:

spring.datasource.url=jdbc:mysql://localhost:3306/finedge
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

> ⚠️ Never commit real database passwords, JWT secrets, API keys, or other sensitive credentials to GitHub.

### 4. Build the Project

Windows PowerShell:

.\mvnw.cmd clean install

Linux / macOS:

./mvnw clean install

### 5. Run the Application

Windows PowerShell:

.\mvnw.cmd spring-boot:run

Linux / macOS:

./mvnw spring-boot:run

---

## 🔄 Application Workflow

Register User
      ↓
Login
      ↓
Receive JWT
      ↓
Create Bank Account
      ↓
Deposit Money
      ↓
Withdraw / Transfer Money
      ↓
View Transaction History
      ↓
Close Account

---

## 🎯 Project Goals

FinEdgeAPI was developed to strengthen practical backend engineering skills through the implementation of:

- Secure Authentication
- RESTful API Design
- Banking Business Logic
- Database Persistence
- JWT-based Security
- Spring Security
- Input Validation
- Ownership-based Access Control
- Centralized Exception Handling
- Automated Testing
- API Documentation
- Layered Application Architecture

---

## 📈 Future Improvements

- Docker Containerization
- Refresh Token Implementation
- Role-Based Access Control
- Advanced Integration Testing
- Logging & Monitoring
- API Rate Limiting
- Production Deployment
- Frontend Banking Dashboard
- CI/CD Pipeline Enhancements

---

## 👨‍💻 Author

**Virag Khade**

**Java Full Stack Developer | Backend Focused**

Java | Spring Boot | REST APIs | SQL | React | Node.js

GitHub: https://github.com/virag185

LinkedIn: https://www.linkedin.com/in/viragkhade/

---

⭐ If you find this project useful, consider giving the repository a star.
