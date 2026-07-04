# `@JsonFormat` in Spring Boot

## What is `@JsonFormat`?

`@JsonFormat` is a Jackson annotation used to **control how Java objects are converted to JSON and how JSON is converted back to Java objects.**

It is most commonly used with **Date**, **LocalDate**, **LocalDateTime**, and **LocalTime**.

### In simple words

> `@JsonFormat` tells Jackson **how a field should look in JSON**.

---

# Why do we need `@JsonFormat`?

Suppose you have the following class:

```java
public class Employee {

    private String name;
    private LocalDate dateOfBirth;

    // Getters and Setters
}
```

And your controller returns an `Employee` object.

```java
@RestController
public class EmployeeController {

    @GetMapping("/employee")
    public Employee getEmployee() {

        Employee employee = new Employee();
        employee.setName("Rashindra");
        employee.setDateOfBirth(LocalDate.of(2000, 5, 15));

        return employee;
    }
}
```

### Default JSON Response

```json
{
    "name": "Rashindra",
    "dateOfBirth": "2000-05-15"
}
```

This is Jackson's default date format.

But what if your frontend wants the date in this format?

```json
{
    "dateOfBirth": "15-05-2000"
}
```

This is where `@JsonFormat` is used.

---

# Syntax

```java
@JsonFormat(pattern = "dd-MM-yyyy")
private LocalDate dateOfBirth;
```

---

# Example

```java
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class Employee {

    private String name;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    // Getters and Setters
}
```

### Response

```json
{
    "name": "Rashindra",
    "dateOfBirth": "15-05-2000"
}
```

---

# How does it work?

Without `@JsonFormat`

```java
private LocalDate date;
```

JSON

```json
{
    "date": "2026-07-04"
}
```

With

```java
@JsonFormat(pattern = "dd/MM/yyyy")
private LocalDate date;
```

JSON

```json
{
    "date": "04/07/2026"
}
```

Notice that **only the format changes, not the actual date**.

---

# Common Pattern Symbols

| Pattern | Meaning | Example |
|----------|---------|---------|
| dd | Day | 04 |
| MM | Month (Number) | 07 |
| MMM | Short Month Name | Jul |
| MMMM | Full Month Name | July |
| yyyy | Year | 2026 |
| HH | Hour (24 Hours) | 15 |
| mm | Minutes | 30 |
| ss | Seconds | 45 |

---

# Common Date Patterns

| Pattern | Output |
|----------|---------|
| yyyy-MM-dd | 2026-07-04 |
| dd-MM-yyyy | 04-07-2026 |
| dd/MM/yyyy | 04/07/2026 |
| MM/dd/yyyy | 07/04/2026 |
| dd MMM yyyy | 04 Jul 2026 |
| dd MMMM yyyy | 04 July 2026 |
| yyyy-MM-dd HH:mm:ss | 2026-07-04 15:30:45 |

---

# Formatting LocalDateTime

```java
@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
private LocalDateTime createdAt;
```

### Response

```json
{
    "createdAt": "04-07-2026 15:30:45"
}
```

---

# Formatting LocalTime

```java
@JsonFormat(pattern = "HH:mm:ss")
private LocalTime loginTime;
```

### Response

```json
{
    "loginTime": "18:45:20"
}
```

---

# Formatting Date

```java
@JsonFormat(pattern = "dd MMM yyyy")
private Date joiningDate;
```

### Response

```json
{
    "joiningDate": "04 Jul 2026"
}
```

---

# Using Time Zone

Sometimes your application serves users from different countries.

In that case, you can specify the time zone.

```java
@JsonFormat(
    pattern = "dd-MM-yyyy HH:mm:ss",
    timezone = "Asia/Kolkata"
)
private Date createdAt;
```

Now Jackson converts the date using the Indian time zone.

---

# Deserialization (Reading JSON)

`@JsonFormat` is not only used while sending data.

It is also used while reading JSON.

Suppose the client sends

```json
{
    "dateOfBirth": "15-05-2000"
}
```

Java class

```java
@JsonFormat(pattern = "dd-MM-yyyy")
private LocalDate dateOfBirth;
```

Jackson understands that the incoming date follows the **dd-MM-yyyy** format and converts it into a `LocalDate` object.

---

# Real-Life Example

Suppose your company has customers in different countries.

India writes dates as

```text
04-07-2026
```

USA writes dates as

```text
07/04/2026
```

Japan writes dates as

```text
2026-07-04
```

The date is the same.

Only the format changes.

`@JsonFormat` helps you control that format.

---

# Complete Example

## Employee.java

```java
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class Employee {

    private int id;

    private String name;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    public Employee() {
    }

    public Employee(int id, String name, LocalDate dateOfBirth) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters and Setters
}
```

---

## EmployeeController.java

```java
@RestController
public class EmployeeController {

    @GetMapping("/employee")
    public Employee getEmployee() {

        return new Employee(
                1,
                "Rashindra",
                LocalDate.of(2000, 5, 15)
        );
    }
}
```

---

## Response

```json
{
    "id": 1,
    "name": "Rashindra",
    "dateOfBirth": "15-05-2000"
}
```

---

# When should you use `@JsonFormat`?

Use `@JsonFormat` when:

- You want dates in a specific format.
- Your frontend expects a particular date format.
- You want consistent date formatting across your APIs.
- You need to specify a time zone.

---

# Important Points

- `@JsonFormat` **does not change the actual value** stored in the object.
- It **only changes how the value appears in JSON**.
- It is commonly used with:
    - `LocalDate`
    - `LocalDateTime`
    - `LocalTime`
    - `Date`
- It works for both:
    - Serialization (Java → JSON)
    - Deserialization (JSON → Java)

---

# Summary

- `@JsonFormat` controls the JSON representation of a field.
- It is mainly used for formatting date and time values.
- The `pattern` attribute defines how the value should appear.
- The `timezone` attribute lets you control the time zone.
- It helps ensure your API returns dates in the format expected by clients.

### Easy way to remember

Without `@JsonFormat`

```json
{
    "date": "2026-07-04"
}
```

With

```java
@JsonFormat(pattern = "dd-MM-yyyy")
```

JSON becomes

```json
{
    "date": "04-07-2026"
}
```

**The value stays the same, only the display format changes.**