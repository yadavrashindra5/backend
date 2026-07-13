# Spring Boot Naming Conventions

| Component | Naming Convention | Example |
|-----------|-------------------|---------|
| Package | lowercase | `com.example.todo.service` |
| Main Class | PascalCase + `Application` | `TodoApplication` |
| Entity | PascalCase | `User`, `Employee` |
| Repository | PascalCase + `Repository` | `UserRepository` |
| Service | PascalCase + `Service` | `UserService` |
| Service Implementation *(optional)* | PascalCase + `ServiceImpl` | `UserServiceImpl` |
| Controller | PascalCase + `Controller` | `UserController` |
| REST Endpoint | kebab-case | `/user-profile`, `/employee-details` |
| DTO | PascalCase + `Dto` | `UserDto`, `LoginRequestDto` |
| Request DTO | PascalCase + `Request` | `LoginRequest`, `CreateUserRequest` |
| Response DTO | PascalCase + `Response` | `LoginResponse`, `UserResponse` |
| Configuration Class | PascalCase + `Config` | `SecurityConfig` |
| Exception Class | PascalCase + `Exception` | `UserNotFoundException` |
| Exception Handler | PascalCase + `ExceptionHandler` | `GlobalExceptionHandler` |
| Utility Class | PascalCase + `Util` / `Utils` | `DateUtil` |
| Enum | PascalCase | `OrderStatus` |
| Enum Constants | SCREAMING_SNAKE_CASE | `PENDING`, `COMPLETED` |
| Variables | camelCase | `firstName`, `userService` |
| Methods | camelCase | `getUserById()`, `saveUser()` |
| Constants | SCREAMING_SNAKE_CASE | `MAX_RETRY_COUNT` |
| Database Table | snake_case | `user_details` |
| Database Column | snake_case | `first_name`, `created_at` |
| application.properties Keys | lowercase with dot (`.`) | `server.port`, `spring.datasource.url` |

---

# Scalable Spring Boot Project Structure

```text
com.example.todo
│
├── TodoApplication.java                 # Main Spring Boot application
│
├── config/                             # Configuration classes
│   ├── SecurityConfig.java
│   ├── SwaggerConfig.java
│   ├── WebConfig.java
│   └── JacksonConfig.java
│
├── controller/                         # REST Controllers
│   ├── UserController.java
│   └── AuthController.java
│
├── service/                            # Business logic interfaces
│   ├── UserService.java
│   └── AuthService.java
│
├── service/
│   └── impl/                           # Service implementations
│       ├── UserServiceImpl.java
│       └── AuthServiceImpl.java
│
├── repository/                         # Database layer
│   ├── UserRepository.java
│   └── RoleRepository.java
│
├── entity/                             # JPA Entities
│   ├── User.java
│   ├── Role.java
│   └── Address.java
│
├── dto/                                # Common DTOs (optional)
│   └── UserDto.java
│
├── request/                            # Request DTOs
│   ├── CreateUserRequest.java
│   ├── UpdateUserRequest.java
│   └── LoginRequest.java
│
├── response/                           # Response DTOs
│   ├── UserResponse.java
│   ├── LoginResponse.java
│   └── ApiResponse.java
│
├── exception/                          # Custom exceptions
│   ├── UserNotFoundException.java
│   ├── ResourceNotFoundException.java
│   └── DuplicateRecordException.java
│
├── handler/                            # Global exception handlers
│   └── GlobalExceptionHandler.java
│
├── security/                           # JWT, Authentication, Authorization
│   ├── JwtAuthenticationFilter.java
│   ├── JwtUtil.java
│   └── CustomUserDetailsService.java
│
├── util/                               # Utility classes
│   ├── DateUtil.java
│   ├── ValidationUtil.java
│   └── FileUtil.java
│
├── mapper/                             # Entity ↔ DTO mapping
│   └── UserMapper.java
│
├── validator/                          # Custom validators
│   └── PasswordValidator.java
│
├── constants/                          # Application constants
│   ├── AppConstants.java
│   └── ErrorMessages.java
│
├── enums/                              # Enums
│   ├── Role.java
│   └── UserStatus.java
│
├── interceptor/                        # Request interceptors
│   └── RequestLoggingInterceptor.java
│
├── filter/                             # Servlet filters
│   └── RequestFilter.java
│
├── aspect/                             # AOP classes
│   └── LoggingAspect.java
│
├── event/                              # Spring Events
│   └── UserCreatedEvent.java
│
├── listener/                           # Event listeners
│   └── UserCreatedListener.java
│
├── specification/                      # Dynamic JPA Specifications
│   └── UserSpecification.java
│
├── client/                             # REST Clients / Feign Clients
│   └── NotificationClient.java
│
├── scheduler/                          # Scheduled jobs
│   └── CleanupScheduler.java
│
├── cache/                              # Cache configuration/services
│   └── CacheConfig.java
│
├── properties/                         # @ConfigurationProperties classes
│   └── JwtProperties.java
│
└── resources/
    ├── application.properties
    ├── application-dev.properties
    ├── application-prod.properties
    ├── static/
    └── templates/
```

---

# Folder Purpose

| Folder | Purpose |
|---------|----------|
| **config** | Spring configuration classes |
| **controller** | Handle HTTP requests |
| **service** | Business logic interfaces |
| **service/impl** | Business logic implementation |
| **repository** | Database operations |
| **entity** | JPA Entities |
| **dto** | Shared DTOs (optional) |
| **request** | Request models received from clients |
| **response** | Response models returned to clients |
| **exception** | Custom exceptions |
| **handler** | Global exception handling |
| **security** | Authentication & Authorization |
| **util** | Helper/utility classes |
| **mapper** | Convert Entity ↔ DTO |
| **validator** | Custom validations |
| **constants** | Constant values and messages |
| **enums** | Enumerations |
| **filter** | Servlet filters |
| **interceptor** | Spring MVC interceptors |
| **aspect** | Aspect-Oriented Programming (AOP) |
| **event** | Spring events |
| **listener** | Event listeners |
| **specification** | Dynamic database queries (JPA Specification) |
| **client** | External API/Feign clients |
| **scheduler** | Scheduled/background jobs |
| **cache** | Cache configuration and services |
| **properties** | Classes annotated with `@ConfigurationProperties` |
| **resources** | Properties, templates, static files |

---

# Which Structure Should You Use?

### Beginner / Learning Project
```
controller
service
repository
entity
dto
exception
config
```

### Production / Scalable Project
Use the complete structure shown above. Add folders only when your project requires them. For example:
- If you don't use JWT, omit `security/`.
- If you don't call external APIs, omit `client/`.
- If you don't use scheduled tasks, omit `scheduler/`.

This keeps the project clean while allowing it to scale as features grow.