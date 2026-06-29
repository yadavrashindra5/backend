# Logging in Spring Boot

## What is Logging?

Logging is the process of recording information about your application's execution.

Instead of:

```java
System.out.println("User Created");
```

Use:

```java
logger.info("User Created");
```

### Why use Logging?

* Debug application
* Track application flow
* Record errors
* Monitor application behavior
* Different log levels for different situations

---

# Logger in Spring Boot

Spring Boot uses:

* **SLF4J** → Logging API
* **Logback** → Default Logging Implementation

> **SLF4J** provides the logging methods, and **Logback** prints the logs to the console/file.

---

# Creating a Logger

## Without Lombok

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class UserController {

    private static final Logger logger =
            LoggerFactory.getLogger(UserController.class);
}
```

---

## With Lombok (Recommended)

```java
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserController {

}
```

Use directly:

```java
log.info("Application Started");
```

---

# What is a Log Level?

A **Log Level** represents the **priority (severity)** of a log message.

Example

```java
logger.info("User Logged In");
logger.warn("Invalid Password");
logger.error("Database Connection Failed");
```

* `INFO` → Normal information.
* `WARN` → Something unexpected happened.
* `ERROR` → Something failed.

Log levels help us decide **which logs should be printed**.

---

# Why do we need Log Levels?

Suppose your application contains these logs:

```java
logger.trace("Method Started");
logger.debug("User Object: {}", user);
logger.info("User Logged In");
logger.warn("Invalid Password");
logger.error("Database Connection Failed");
```

If every log is printed, the console becomes noisy.

Using log levels, we can filter the logs.

---

# Log Level Hierarchy

```text
TRACE
   ↓
DEBUG
   ↓
INFO
   ↓
WARN
   ↓
ERROR
```

If a level is enabled, **that level and all higher-severity levels are printed**.

| Configured Level | Logs Printed                    |
| ---------------- | ------------------------------- |
| TRACE            | TRACE, DEBUG, INFO, WARN, ERROR |
| DEBUG            | DEBUG, INFO, WARN, ERROR        |
| INFO             | INFO, WARN, ERROR               |
| WARN             | WARN, ERROR                     |
| ERROR            | ERROR                           |

---

# Example

## Configuration

```properties
logging.level.root=INFO
```

Code

```java
logger.trace("Method Started");
logger.debug("User Object");
logger.info("User Logged In");
logger.warn("Invalid Password");
logger.error("Database Failed");
```

Console

```text
INFO  User Logged In
WARN  Invalid Password
ERROR Database Failed
```

`TRACE` and `DEBUG` are ignored.

---

If configuration is

```properties
logging.level.root=DEBUG
```

Console

```text
DEBUG User Object
INFO  User Logged In
WARN  Invalid Password
ERROR Database Failed
```

---

If configuration is

```properties
logging.level.root=ERROR
```

Console

```text
ERROR Database Failed
```

Only errors are printed.

---

# Logging Levels

## TRACE

Most detailed logs.

Used for:

* Method entry
* Method exit
* Variable values
* Internal execution flow

```java
logger.trace("Entered getUsers()");
```

---

## DEBUG

Used while developing and debugging.

```java
logger.debug("Request Body: {}", user);
```

---

## INFO

Used for normal application events.

```java
logger.info("User Registered Successfully");
```

Examples:

* User logged in
* Order created
* Application started

---

## WARN

Used when something unexpected happens, but the application continues.

```java
logger.warn("User not found with id {}", id);
```

---

## ERROR

Used when an operation fails.

```java
try {
    int result = 10 / 0;
} catch (Exception e) {
    logger.error("Calculation Failed", e);
}
```

---

# Logging Variables

❌ Don't

```java
logger.info("User Id: " + id);
```

✅ Do

```java
logger.info("User Id: {}", id);
```

Multiple variables

```java
logger.info("User {} logged in from {}", username, city);
```

**Why?**

* Better performance
* Cleaner code
* String formatting happens only if the log is actually written

---

# Logging Objects

```java
logger.info("User Details: {}", user);
```

Output depends on the object's `toString()` method.

---

# Logging Exceptions

❌ Don't

```java
logger.error(e.getMessage());
```

✅ Do

```java
logger.error("Unable to save user", e);
```

This prints:

* Error message
* Complete stack trace

---

# Change Log Level

`application.properties`

```properties
logging.level.root=INFO
```

For a specific package

```properties
logging.level.com.example=DEBUG
```

---

# Example

```java
@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger =
            LoggerFactory.getLogger(UserController.class);

    @GetMapping("/{id}")
    public String getUser(@PathVariable int id) {

        logger.info("Fetching user {}", id);

        if (id <= 0) {
            logger.warn("Invalid user id {}", id);
            return "Invalid User";
        }

        logger.debug("Returning user details");

        return "User " + id;
    }
}
```

---

# Logger vs System.out.println()

| Logger              | System.out.println() |
| ------------------- | -------------------- |
| Supports log levels | No log levels        |
| Can disable logs    | Cannot disable       |
| Better performance  | Slower               |
| Used in production  | Not recommended      |
| Can log exceptions  | No built-in support  |

---

# Best Practices

✅ Use placeholders

```java
logger.info("User Id: {}", id);
```

---

✅ Log exceptions with stack trace

```java
logger.error("Unable to save user", e);
```

---

✅ Never log passwords or sensitive information.

❌

```java
logger.info("Password: {}", password);
```

---

✅ Use the correct log level.

| Situation                  | Log Level |
| -------------------------- | --------- |
| Method entry/exit          | TRACE     |
| Debugging                  | DEBUG     |
| Normal application event   | INFO      |
| Unexpected but recoverable | WARN      |
| Failure/Exception          | ERROR     |

---

# Quick Revision

| Method    | Purpose                   |
| --------- | ------------------------- |
| `trace()` | Detailed execution flow   |
| `debug()` | Debugging                 |
| `info()`  | Normal application events |
| `warn()`  | Warning messages          |
| `error()` | Errors and exceptions     |

---

# Interview Questions

### Why use Logger instead of `System.out.println()`?

* Supports log levels.
* Better performance.
* Can be enabled/disabled.
* Can log exceptions.
* Used in production.

---

### Why use `{}` instead of `+`?

```java
logger.info("User {}", id);
```

Instead of

```java
logger.info("User " + id);
```

Because placeholders delay string formatting until the log message is actually written, improving performance.

---

### Which logging framework does Spring Boot use by default?

* **Logging API:** SLF4J
* **Implementation:** Logback

---

### Which way is recommended to create a logger?

**Without Lombok**

```java
private static final Logger logger =
        LoggerFactory.getLogger(UserController.class);
```

**With Lombok (Recommended)**

```java
@Slf4j
```

Then use:

```java
log.info("Application Started");
```
