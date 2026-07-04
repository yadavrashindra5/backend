# Exception Handling in Spring Boot

# Table of Contents

1. What is an Exception?
2. Why Do We Need Exception Handling?
3. Exception Flow in Spring Boot
4. Types of Exceptions
5. Ways to Handle Exceptions
6. Using try-catch
7. Using throws
8. Using @ExceptionHandler
9. Using @ControllerAdvice (Global Exception Handling)
10. Creating Custom Exceptions
11. Returning Custom Error Responses
12. Best Practices
13. Complete Flow Example
14. Summary

---

# 1. What is an Exception?

An **Exception** is an unexpected event or error that occurs while the application is running and interrupts the normal flow of the program.

For example:

- User requests data that does not exist.
- Database connection fails.
- Invalid input is provided.
- File is not found.
- Division by zero.

Example:

```java
int result = 10 / 0;
```

Output

```
ArithmeticException: / by zero
```

Instead of crashing the application, Java throws an **Exception**.

---

# 2. Why Do We Need Exception Handling?

Imagine you have this API.

```java
@GetMapping("/users/{id}")
public User getUser(@PathVariable int id) {
    return userService.getUser(id);
}
```

Suppose user **10** does not exist.

Without exception handling:

```
GET /users/10
```

Response

```json
{
    "timestamp": "...",
    "status": 500,
    "error": "Internal Server Error"
}
```

The client doesn't know what actually happened.

Instead, we should return

```json
{
    "status":404,
    "message":"User not found"
}
```

This makes the API much easier to understand.

---

# 3. Exception Flow in Spring Boot

Suppose your application has the following layers.

```
Client
   │
   ▼
Controller
   │
   ▼
Service
   │
   ▼
Repository
```

Suppose Repository throws an exception.

```java
throw new UserNotFoundException("User not found");
```

The exception travels upward.

```
Repository
     ▲
Service
     ▲
Controller
     ▲
Spring Boot
     ▲
Client
```

If nobody handles it,

Spring Boot returns

```
500 Internal Server Error
```

---

# 4. Types of Exceptions

## Checked Exception

These exceptions **must be handled** using try-catch or throws.

Example

```java
public void readFile() throws IOException {

}
```

Examples

- IOException
- SQLException

---

## Unchecked Exception

These exceptions extend RuntimeException.

They do not need to be handled explicitly.

Example

```java
throw new RuntimeException("Something went wrong");
```

Examples

- NullPointerException
- IllegalArgumentException
- ArithmeticException

> In Spring Boot, most custom exceptions extend **RuntimeException**.

---

# 5. Ways to Handle Exceptions in Spring Boot

Spring Boot provides multiple ways to handle exceptions.

| Method | Scope | Recommended |
|----------|--------|-------------|
| try-catch | Single method | ✅ For local handling |
| throws | Pass exception to caller | ✅ Sometimes |
| @ExceptionHandler | Single Controller | ✅ Good |
| @ControllerAdvice | Entire Application | ⭐⭐⭐ Recommended |

---

# 6. Handling Exception using try-catch

Use when you want to handle an exception inside a method.

Example

```java
public void calculate() {

    try {

        int result = 10 / 0;

    } catch (ArithmeticException ex) {

        System.out.println(ex.getMessage());

    }

}
```

Output

```
/ by zero
```

### Advantages

- Easy to use
- Good for local exceptions

### Disadvantages

- Code becomes repetitive
- Not suitable for APIs

---

# 7. Handling Exception using throws

Instead of handling the exception, you can pass it to the caller.

Example

```java
public void readFile() throws IOException {

}
```

Now the caller must handle it.

```java
try {

    readFile();

} catch (IOException ex) {

}
```

---

# 8. Handling Exception using @ExceptionHandler

Suppose your service throws

```java
throw new UserNotFoundException("User not found");
```

Controller

```java
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {

        throw new UserNotFoundException("User not found");

    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleException(UserNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());

    }

}
```

Request

```
GET /users/10
```

Response

```
404 Not Found
```

Body

```json
User not found
```

## Important

`@ExceptionHandler` only handles exceptions for the controller in which it is declared.

If you have another controller,

```
ProductController
```

it will NOT use this handler.

---

# 9. Global Exception Handling using @ControllerAdvice

Instead of writing exception handlers inside every controller, we create one global class.

This is the recommended approach.

## Step 1

Create Custom Exception

```java
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {

        super(message);

    }

}
```

---

## Step 2

Throw Exception

```java
@Service
public class UserService {

    public User getUser(int id) {

        if(id != 1){

            throw new UserNotFoundException("User not found");

        }

        return new User(1,"John");

    }

}
```

---

## Step 3

Controller

```java
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id){

        return service.getUser(id);

    }

}
```

Notice that there is **no try-catch** and **no @ExceptionHandler**.

---

## Step 4

Create Global Exception Handler

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());

    }

}
```

Now every controller in the project can use this handler.

---

# How @ControllerAdvice Works

```
                Request
                   │
                   ▼
             UserController
                   │
                   ▼
             UserService
                   │
                   ▼
        UserNotFoundException
                   │
                   ▼
        GlobalExceptionHandler
                   │
                   ▼
      ResponseEntity(404)
                   │
                   ▼
                 Client
```

Spring Boot automatically detects the exception and sends it to the matching `@ExceptionHandler` method inside the `@ControllerAdvice` class.

---

# 10. Creating Multiple Exception Handlers

One global class can handle many exceptions.

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUser(UserNotFoundException ex){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegal(IllegalArgumentException ex){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex){

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong");

    }

}
```

Notice the last handler.

```java
@ExceptionHandler(Exception.class)
```

This acts as a fallback for all unhandled exceptions.

---

# 11. Returning Custom Error Response

Returning only a String is not recommended.

Instead, create a custom response object.

```java
public class ErrorResponse {

    private int status;
    private String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // getters and setters

}
```

Global Handler

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUser(UserNotFoundException ex){

        ErrorResponse response = new ErrorResponse(
                404,
                ex.getMessage()
        );

        return ResponseEntity.status(404)
                .body(response);

    }

}
```

Response

```json
{
    "status":404,
    "message":"User not found"
}
```

This format is much cleaner and easier for frontend applications to consume.

---

# 12. Best Practices

✅ Create custom exceptions for business logic.

Example

```java
UserNotFoundException
OrderNotFoundException
ProductNotFoundException
```

❌ Avoid throwing generic RuntimeException everywhere.

---

✅ Use `@ControllerAdvice` for global exception handling.

---

✅ Return meaningful HTTP status codes.

Examples

| Status Code | Meaning |
|-------------|----------|
| 200 | Success |
| 201 | Created |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Resource Not Found |
| 500 | Internal Server Error |

---

✅ Return a consistent error response structure.

Example

```json
{
    "status":404,
    "message":"User not found"
}
```

---

# 13. Complete Flow Example

```
Client
   │
   ▼
GET /users/10
   │
   ▼
Controller
   │
   ▼
Service
   │
   ▼
UserNotFoundException
   │
   ▼
@ControllerAdvice
   │
   ▼
ResponseEntity
   │
   ▼
Client receives

HTTP 404

{
    "status":404,
    "message":"User not found"
}
```

---

# 14. Summary

| Concept | Description |
|----------|-------------|
| Exception | Error that interrupts the normal flow of execution |
| try-catch | Handles exceptions locally inside a method |
| throws | Passes the exception to the caller |
| @ExceptionHandler | Handles exceptions for a specific controller |
| @ControllerAdvice | Handles exceptions globally across all controllers |
| RuntimeException | Unchecked exception commonly used for custom exceptions |
| Checked Exception | Must be handled using try-catch or throws |
| Custom Exception | Exception created by the developer for business logic |
| ResponseEntity | Used to return custom HTTP status and response body |

---

# Key Takeaways

- An exception is an unexpected error that interrupts the normal execution of a program.
- If no exception handler is present, Spring Boot returns a default **500 Internal Server Error** response.
- Use **try-catch** for handling exceptions within a single method.
- Use **throws** to delegate exception handling to the calling method.
- Use **@ExceptionHandler** to handle exceptions within a specific controller.
- Use **@ControllerAdvice** to centralize exception handling across the entire application. This is the recommended approach for REST APIs.
- Create **custom exceptions** (such as `UserNotFoundException`) to represent business-specific errors.
- Return a consistent and meaningful error response (using `ResponseEntity` and an `ErrorResponse` class) so clients can easily understand what went wrong.