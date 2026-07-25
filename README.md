# 💳 Banking Portal API

A production-oriented **Digital Banking Backend** built with **Java** and **Spring Boot**. This project is being developed to explore secure authentication, scalable backend architecture, and modern software engineering practices used in real-world banking applications.

> 🚧 This project is under active development, with new features and improvements added incrementally.

---

# ✨ Features

### Authentication
- Secure User Registration
- User Login
- JWT Token Generation
- Password Encryption using BCrypt

### Backend
- RESTful API Design
- Layered Architecture
- Request Validation
- MySQL Database Integration
- Spring Data JPA

---

# 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| Language | Java 17 |
| Framework | Spring Boot |
| Security | Spring Security, JWT |
| Database | MySQL |
| ORM | Spring Data JPA / Hibernate |
| Build Tool | Maven |
| Utilities | Lombok |

---

# 📂 Project Structure

```text
src
├── controller
├── service
├── repository
├── entity
├── dto
├── security
├── config
├── util
└── resources
```

---

# 📌 Current API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register a new user |
| POST | /api/auth/login | Authenticate user |

---

# 🚀 Development Roadmap

## Authentication
- ✅ User Registration
- ✅ User Login
- ✅ JWT Token Generation
- ⏳ JWT Request Filter
- ⏳ Role-Based Authorization

## Banking Features
- ⏳ User Profile API
- ⏳ Bank Account Management
- ⏳ Deposit Money
- ⏳ Withdraw Money
- ⏳ Fund Transfer
- ⏳ Transaction History

## Developer Experience
- ⏳ Swagger / OpenAPI Documentation
- ⏳ Global Exception Handling
- ⏳ Unit Testing
- ⏳ Docker Support
- ⏳ GitHub Actions CI/CD

---

# ⚙️ Running the Project

Clone the repository

```bash
git clone https://github.com/virag185/Banking-Portal-API.git
```

Move into the project

```bash
cd Banking-Portal-API
```

Configure your database credentials inside:

```text
src/main/resources/application.properties
```

Run the application

```bash
./mvnw spring-boot:run
```

---

# 🎯 Project Goals

This project is being built to strengthen my backend engineering skills by implementing production-style features such as authentication, authorization, secure API design, database management, and scalable application architecture using Spring Boot.

---

# 📈 Future Improvements

- Docker Containerization
- CI/CD Pipeline
- API Documentation with Swagger
- Role-Based Access Control
- Refresh Tokens
- Logging & Monitoring
- Integration Testing

---

# 👨‍💻 Author

**Virag Khade**

Backend Developer | Java | Spring Boot | SQL | REST APIs

- GitHub: https://github.com/virag185
- LinkedIn: https://www.linkedin.com/in/viragkhade/