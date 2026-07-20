# Spring Validation (Bean Validation)

## What is Spring Validation?

Spring Validation is a feature provided by Spring Boot that helps us **validate the data** before our application processes it.

In simple words,

> **Validation means checking whether the data received from the user is correct or not.**

For example,

Suppose a user is registering on your website.

```text
Name      : Rahul
Email     : rahul@gmail.com
Age        : 22
Password : password123
```

This is valid data.

But what if the user sends

```text
Name      :
Email     : abc
Age        : -10
Password : 12
```

This data is invalid because

- Name is empty
- Email format is incorrect
- Age cannot be negative
- Password is too short

Without validation, this invalid data will be saved into the database.

Validation prevents this.

---

# Why do we need Validation?

Without validation

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
Database
```

Even invalid data reaches the database.

With validation

```
Client
   │
   ▼
Validation
   │
   ├── Valid Data
   │      │
   │      ▼
   │   Controller
   │      │
   │      ▼
   │   Database
   │
   └── Invalid Data
           │
           ▼
     Error Response
```

The request never reaches the controller if validation fails.

---

# Spring Validation vs Manual Validation

## Manual Validation

Without Spring Validation we write

```java
if(user.getName()==null || user.getName().trim().isEmpty()){
    throw new RuntimeException("Name is required");
}

if(user.getAge()<18){
    throw new RuntimeException("Age should be greater than 18");
}
```

Problems

- Too much code
- Difficult to maintain
- Repeated everywhere
- Hard to read

---

## Spring Validation

```java
@NotBlank
private String name;

@Min(18)
private int age;
```

That's it.

Spring automatically validates the object.

Much cleaner.

---

# Bean Validation

Spring Boot uses **Jakarta Bean Validation**.

It validates Java objects (Beans) using annotations.

Example

```java
public class UserRequest {

    @NotBlank
    private String name;

    @Email
    private String email;

    @Min(18)
    private int age;

}
```

Each annotation tells Spring a validation rule.

---

# How Validation Works

```
Client
   │
   ▼
JSON Request
   │
   ▼
@RequestBody
   │
   ▼
Java Object
   │
   ▼
@Valid
   │
   ▼
Validation Engine
   │
   ├── Success
   │       │
   │       ▼
   │   Controller Method Executes
   │
   └── Failure
           │
           ▼
MethodArgumentNotValidException
           │
           ▼
Error Response
```

---

# Dependency

Validation is not available automatically.

Add dependency

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

---

# Step 1 : Create DTO

Validation should be placed on DTO instead of Entity.

```java
public class UserRequest {

    @NotBlank(message="Name is required")
    private String name;

    @Email(message="Invalid email")
    private String email;

    @Min(value=18,message="Age should be at least 18")
    private int age;

}
```

---

# Why use DTO instead of Entity?

Entity

```java
@Entity
public class User{

    @Id
    private Long id;

    private String name;

}
```

Entity represents database tables.

DTO represents request and response.

Validation belongs to the incoming request.

Therefore

```
Client
   │
   ▼
DTO
   │
   ▼
Validation
   │
   ▼
Service
   │
   ▼
Entity
   │
   ▼
Database
```

This keeps the Entity clean.

---

# Step 2 : Use @Valid

```java
@PostMapping
public ResponseEntity<String> saveUser(
        @Valid @RequestBody UserRequest request){

    return ResponseEntity.ok("User Saved");
}
```

The important annotation is

```java
@Valid
```

Without it,

Spring will never validate the object.

---

# What exactly does @Valid do?

Suppose

```java
@Valid
@RequestBody UserRequest request
```

Spring checks every validation annotation inside UserRequest.

Example

```java
@NotBlank
private String name;

@Email
private String email;
```

Spring checks

```
Is name blank?

Is email valid?

Is age valid?
```

If yes

Controller executes.

Otherwise

Validation exception is thrown.

---

# Common Validation Annotations

## @NotNull

Checks only null.

```java
@NotNull
private String name;
```

Allowed

```
""
" "
"Rahul"
```

Not Allowed

```
null
```

---

## @NotEmpty

Checks

- null
- empty

```java
@NotEmpty
private String name;
```

Allowed

```
" "
"Rahul"
```

Not Allowed

```
null
""
```

---

## @NotBlank

Checks

- null
- empty
- whitespace

```java
@NotBlank
private String name;
```

Allowed

```
Rahul
```

Not Allowed

```
null
""
"      "
```

This is the most commonly used validation for Strings.

---

## @Size

Checks minimum and maximum length.

```java
@Size(min=3,max=20)
private String username;
```

Valid

```
Rahul
```

Invalid

```
ab
abcdefghijklmnopqrstuvwxyz
```

---

## @Email

```java
@Email
private String email;
```

Valid

```
abc@gmail.com
```

Invalid

```
abc
gmail.com
abc@
```

---

## @Min

Minimum value.

```java
@Min(18)
private int age;
```

Valid

```
20
```

Invalid

```
15
```

---

## @Max

Maximum value.

```java
@Max(60)
private int age;
```

---

## @Positive

```java
@Positive
private double salary;
```

Only greater than zero.

---

## @PositiveOrZero

```java
@PositiveOrZero
private int quantity;
```

Allows

```
0
10
```

---

## @Negative

```java
@Negative
private int temperature;
```

Only values below zero.

---

## @NegativeOrZero

```java
@NegativeOrZero
private int number;
```

Allows

```
0
-5
```

---

## @Pattern

Used for Regex.

Phone Number

```java
@Pattern(
regexp="^[0-9]{10}$",
message="Phone number should contain 10 digits"
)
private String phone;
```

---

## @Past

```java
@Past
private LocalDate dob;
```

Only past dates.

---

## @Future

```java
@Future
private LocalDate joiningDate;
```

Only future dates.

---

## @PastOrPresent

```java
@PastOrPresent
private LocalDate createdDate;
```

---

## @FutureOrPresent

```java
@FutureOrPresent
private LocalDate bookingDate;
```

---

# Validation Message

Instead of default message

```
must not be blank
```

We can write

```java
@NotBlank(message="Name is mandatory")
```

Now response becomes

```
Name is mandatory
```

---

# Validating Nested Objects

Suppose

```java
public class Address{

    @NotBlank
    private String city;

}
```

User DTO

```java
public class UserRequest{

    @NotBlank
    private String name;

    @Valid
    private Address address;

}
```

Notice

```
@Valid
private Address address;
```

Without @Valid

Address object will not be validated.

---

# Validating Collections

```java
public class OrderRequest{

    @Valid
    private List<ProductRequest> products;

}
```

Spring validates every ProductRequest inside the list.

---

# @Valid vs @Validated

Many beginners get confused.

## @Valid

Used for validating objects.

```java
@PostMapping
public void save(@Valid @RequestBody UserRequest request){

}
```

---

## @Validated

Used for validating method parameters.

```java
@RestController
@Validated
public class UserController{

    @GetMapping("/{id}")
    public User getUser(
        @Min(1)
        @PathVariable Long id){

        return null;
    }

}
```

Without @Validated

Validation on PathVariable and RequestParam won't work.

---

# Handling Validation Errors

Suppose request

```json
{
    "name":"",
    "email":"abc",
    "age":10
}
```

Spring throws

```
MethodArgumentNotValidException
```

Instead of returning ugly error messages, we create a Global Exception Handler.

---

# Global Exception Handler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>>
    handleValidation(MethodArgumentNotValidException ex){

        Map<String,String> errors = new HashMap<>();

        ex.getBindingResult()
          .getFieldErrors()
          .forEach(error -> {

              errors.put(
                      error.getField(),
                      error.getDefaultMessage()
              );

          });

        return ResponseEntity.badRequest().body(errors);

    }

}
```

Now response becomes

```json
{
    "name":"Name is required",
    "email":"Invalid email",
    "age":"Age should be at least 18"
}
```

This is much cleaner.

---

# Custom Validation

Sometimes built-in annotations are not enough.

Example

Suppose company policy says

```
Username cannot contain admin
```

There is no annotation for this.

We create our own validation.

---

## Step 1 : Create Annotation

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
public @interface ValidUsername {

    String message() default "Invalid Username";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
```

---

## Step 2 : Create Validator

```java
public class UsernameValidator
implements ConstraintValidator<ValidUsername,String>{

    @Override
    public boolean isValid(String value,
                           ConstraintValidatorContext context){

        if(value==null)
            return true;

        return !value.toLowerCase().contains("admin");
    }

}
```

---

## Step 3 : Use Annotation

```java
public class UserRequest{

    @ValidUsername
    private String username;

}
```

Now

```
admin123
```

Invalid

```
rahul123
```

Valid.

---

# Another Custom Validation Example

Suppose password must contain

- One uppercase letter
- One lowercase letter
- One digit
- One special character

You can create

```java
@StrongPassword
private String password;
```

Internally

Your validator checks all these rules.

---

# Best Practices

✔ Always validate DTOs, not Entities.

✔ Always use custom messages.

✔ Handle validation exceptions using `@RestControllerAdvice`.

✔ Use `@Valid` for nested objects.

✔ Use `@Validated` for PathVariable and RequestParam validation.

✔ Create custom validators when business rules cannot be expressed using built-in annotations.

✔ Keep validation logic close to the DTO to make the code clean and easy to maintain.

---

# Interview Questions

### What is Spring Validation?

Spring Validation is a mechanism that validates incoming request data before it reaches the business logic using Jakarta Bean Validation annotations.

---

### Why do we use DTO instead of Entity?

Because validation belongs to the request layer, while entities represent the database. Keeping validation in DTOs separates concerns and keeps entities clean.

---

### What is @Valid?

`@Valid` tells Spring to validate the object based on the validation annotations present on its fields.

---

### What is @Validated?

`@Validated` is a Spring annotation used for validating method parameters (`@PathVariable`, `@RequestParam`) and for validation groups.

---

### What happens when validation fails?

Spring throws a `MethodArgumentNotValidException`, which can be handled globally using `@RestControllerAdvice`.

---

### How do you create custom validation?

1. Create a custom annotation.
2. Implement `ConstraintValidator`.
3. Link the validator using `@Constraint`.
4. Use the custom annotation on the desired field.