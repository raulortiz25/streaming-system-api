# Streaming System API

REST API for a streaming platform developed with **Java and Spring Boot**.

The project implements authentication using **JWT**, role-based authorization and subscription plans to control access to movies.

It was developed as a practice project focused on **Spring Security, JWT authentication, roles and authorization rules**.

---

##  Technologies

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- JWT (java-jwt)
- MySQL
- Maven
- Lombok
- Bean Validation

---

##  Authentication

The API uses **JWT (JSON Web Token)** authentication.

After a successful login, the server returns a JWT that must be included in protected requests:

```http
Authorization: Bearer <JWT_TOKEN>
```

The application uses a **stateless authentication system**, so the server does not maintain user sessions.

---

 👥Roles and Plans

The system contains three roles:

| Role | Description |
|------|-------------|
| `CUSTOMER` | User with access to standard content |
| `PREMIUM` | User with access to standard and premium content |
| `ADMIN` | Administrator with movie management permissions |

Users can register using the `CUSTOMER` or `PREMIUM` plan.

The `ADMIN` role is reserved for system administration.

### Content access

| Role | CUSTOMER Movies | PREMIUM Movies | Manage Movies |
|------|:---------------:|:--------------:|:-------------:|
| CUSTOMER | ✅ | ❌ | ❌ |
| PREMIUM | ✅ | ✅ | ❌ |
| ADMIN | ✅ | ✅ | ✅ |

---

##  Features

### Authentication

- User registration
- User login
- JWT generation
- Password encryption with BCrypt
- Stateless authentication

### Subscription management

- CUSTOMER and PREMIUM plans
- Change subscription plan
- Access movies according to the authenticated user's plan

### Movies

Users can:

- List available movies
- Find a movie by ID
- Access content according to their subscription

Administrators can:

- Create movies
- Update movies
- Delete movies
- Access CUSTOMER and PREMIUM content

### Other features

- DTO validation with Bean Validation
- Global exception handling
- Custom exceptions
- Role-based authorization with Spring Security
- Repository layer using Spring Data JPA

---

##  API Endpoints

### Authentication

| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/auth/register` | Public | Register a new user |
| POST | `/auth/login` | Public | Login and receive a JWT |
| PATCH | `/auth/change` | Authenticated | Change the current user's plan |

### Movies

| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | `/movie` | Authenticated | List movies available for the user's plan |
| GET | `/movie/{idMovie}` | Authenticated | Find a movie by ID |
| POST | `/movie` | ADMIN | Create a movie |
| PUT | `/movie/{idMovie}` | ADMIN | Update a movie |
| DELETE | `/movie/{idMovie}` | ADMIN | Delete a movie |

---

##  Request Examples

### Register

```http
POST /auth/register
Content-Type: application/json
```

```json
{
  "email": "user@example.com",
  "username": "exampleUser",
  "password": "password",
  "plan": "CUSTOMER"
}
```

Available plans:

```text
CUSTOMER
PREMIUM
```

---

### Login

```http
POST /auth/login
Content-Type: application/json
```

```json
{
  "username": "exampleUser",
  "password": "password"
}
```

A successful authentication returns a response containing the JWT:

```json
{
  "username": "exampleUser",
  "message": "Login successful",
  "jwt": "<JWT_TOKEN>",
  "status": true
}
```

---

### Change subscription plan

```http
PATCH /auth/change
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

```json
{
  "changePlan": "PREMIUM"
}
```

The authenticated user is identified through the JWT, so credentials do not need to be sent again.

---

### Create movie

Requires the `ADMIN` role.

```http
POST /movie
Authorization: Bearer <ADMIN_JWT_TOKEN>
Content-Type: application/json
```

```json
{
  "title": "Interstellar",
  "description": "A science fiction movie about space exploration.",
  "moviePlan": "PREMIUM"
}
```

---

### Update movie

```http
PUT /movie/1
Authorization: Bearer <ADMIN_JWT_TOKEN>
Content-Type: application/json
```

```json
{
  "title": "Interstellar",
  "description": "Updated movie description.",
  "moviePlan": "PREMIUM"
}
```

---

## Environment Variables

Sensitive information is not stored directly in the repository.

The following environment variables are required:

| Variable | Description |
|----------|-------------|
| `DB_USERNAME` | MySQL username |
| `DB_PASSWORD` | MySQL password |
| `JWT_SECRET` | Secret key used to sign JWT tokens |
| `ADMIN_PASSWORD` | Initial administrator password |

The application reads these values from environment variables:

```properties
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

jwt.key.private=${JWT_SECRET}

admin.password=${ADMIN_PASSWORD}
```



---

##  Running the Project

### Requirements

Make sure you have installed:

- Java 17+
- MySQL
- Maven or Maven Wrapper

### 1. Clone the repository

```bash
git clone https://github.com/raulortiz25/streaming-system-api.git
```

Enter the project:

```bash
cd streaming-system-api
```

### 2. Configure MySQL

Create the database required by the application and verify the database URL configured in:

```text
src/main/resources/application.properties
```

### 3. Configure environment variables

Example on Linux:

```bash
export DB_USERNAME="your_mysql_username"
export DB_PASSWORD="your_mysql_password"
export JWT_SECRET="your_jwt_secret"
export ADMIN_PASSWORD="your_admin_password"
```

### 4. Run the application

Using the Maven Wrapper:

```bash
./mvnw spring-boot:run
```

---

##  Project Structure

```text
src/main/java/com/Streaming/StreamingSystem
│
├── Controller
│   ├── AuthController
│   └── MovieController
│
├── DTO
│   ├── AuthCreateUserRequest
│   ├── AuthLoginRequest
│   ├── AuthResponse
│   ├── ChangePlanRequest
│   ├── MovieCreateRequest
│   ├── MovieResponse
│   └── MovieUpdateRequest
│
├── Exception
│   ├── Custom
│   ├── DtoException
│   └── GlobalExceptionHandler
│
├── Model
│   ├── Enums
│   ├── MovieEntity
│   ├── RoleEntity
│   └── UserEntity
│
├── Repository
│   ├── MovieRepository
│   ├── RoleRepository
│   └── UserRepository
│
├── Security
│   ├── JwtTokenValidator
│   ├── JwtUtils
│   ├── SecurityConfig
│   └── UserDetailsServiceImpl
│
├── Service
│   ├── MovieService
│   └── MovieServiceImpl
│
└── StreamingSystemApplication
```

---

##  Security

The project uses Spring Security with:

- JWT authentication
- Stateless sessions
- BCrypt password hashing
- Role-based endpoint authorization
- Custom JWT validation filter
- Protected movie management endpoints

Authorization rules ensure that regular users cannot access administrator operations.

In addition, movie access is restricted according to the subscription plan of the authenticated user.

---

## Project Purpose

This project was created to practice and strengthen concepts related to:

- Spring Security
- JWT authentication
- Authentication vs authorization
- Role-based access control
- Subscription-based content access
- REST API design
- DTOs
- Exception handling
- Spring Data JPA
- Secure management of application secrets

---

## Author

**Raul Ortiz**

Backend development practice project built with Java and Spring Boot.