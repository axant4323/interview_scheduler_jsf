[WARP.md](https://github.com/user-attachments/files/23242266/WARP.md)
# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

Interview Scheduler ISS is a Spring Boot web application for managing interview scheduling between candidates and interviewers. It uses Spring Security for role-based access control, JPA for database operations, and Thymeleaf for server-side rendering.

## Build & Run Commands

### Build
```pwsh
.\mvnw.cmd clean install
```

### Run Application
```pwsh
.\mvnw.cmd spring-boot:run
```

### Run Tests
```pwsh
.\mvnw.cmd test
```

### Run Single Test
```pwsh
.\mvnw.cmd test -Dtest=ClassName#methodName
```

### Package Application
```pwsh
.\mvnw.cmd package
```

## Architecture

### Core Domain Model

The application centers around two main entities with a many-to-one relationship:

- **User**: Represents all system users (admins, candidates, interviewers) with role-based differentiation
- **Interview**: Links a candidate and interviewer to a specific date/time with a status

### Role-Based Access Pattern

The application uses enum-based roles (`Role.ADMIN`, `Role.CANDIDATE`, `Role.INTERVIEWER`) rather than separate user tables. Spring Security enforces access control:

- `/admin/**` routes → ADMIN role only
- `/candidate/**` routes → CANDIDATE role only  
- Root route (`/`) redirects based on authenticated user's role

### Key Architectural Decisions

1. **Security Layer**: `SecurityConfig` disables CSRF (likely for development) and uses form-based authentication. `CustomUserDetailsService` bridges Spring Security to the domain `User` entity via `SecurityUser`.

2. **Service Layer Logic**: `InterviewService.schedule()` enforces business rules to prevent double-booking of interviewers and candidates at the same date/time using repository existence checks.

3. **Data Initialization**: `DataLoader` creates three default users on startup if they don't exist:
   - admin@iss.local / password (ADMIN)
   - candidate@iss.local / password (CANDIDATE)  
   - interviewer@iss.local / password (INTERVIEWER)

4. **Controller Separation**: Controllers are organized by role (`AdminController`, `CandidateController`, `HomeController`) rather than by resource, reflecting the role-based workflow.

### Package Structure

- `com.iss.model` - JPA entities and enums
- `com.iss.repository` - Spring Data JPA repositories
- `com.iss.service` - Business logic layer with transaction management
- `com.iss.controller` - Spring MVC controllers mapped to roles
- `com.iss.security` - Security configuration and custom UserDetailsService
- `com.iss.config` - Application configuration including data loading

### View Layer

Thymeleaf templates in `src/main/resources/templates/`:
- `login.html` - Login page
- `admin/interviews/list.html` - Admin interview management
- `candidate/interviews.html` - Candidate interview view

## Database Configuration

The application uses MySQL connector with Spring Data JPA. Database configuration must be added to `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/interview_scheduler
spring.datasource.username={{db_username}}
spring.datasource.password={{db_password}}
spring.jpa.hibernate.ddl-auto=update
```

## Technology Stack

- **Spring Boot 3.5.7** (Java 17)
- **Spring Security** with BCrypt password encoding
- **Spring Data JPA** with Hibernate
- **Thymeleaf** for server-side templates
- **MySQL** database
- **Maven** build tool (wrapper included)
- **JUnit 5** for testing

## Development Notes

### When Adding New Features

- New user roles require changes in `Role` enum, `SecurityConfig` authorization rules, and routing in `HomeController`
- Interview business rules are enforced in `InterviewService` - add validation there before repository operations
- Security rules map controller paths to roles - update `SecurityConfig.filterChain()` for new protected endpoints

### Password Encoding

All passwords must use `PasswordEncoder` bean (BCrypt). Never store plain text passwords. See `DataLoader` for examples.

### Transaction Boundaries

Service methods that modify data should be annotated with `@Transactional`. Currently only `InterviewService.schedule()` uses this - apply to other mutating operations as needed.
