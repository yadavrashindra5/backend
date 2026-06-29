# @Component
- It tells Spring to create and manage an object (called a bean) of this class.
- Instead of creating the object yourself using new, Spring creates it and gives it to other classes when needed.

# Example
```aiignore
@Component
public class EmailService {

    public void sendEmail() {
        System.out.println("Email Sent");
    }
}
```
***Note: Spring IOC container is responsible for creating and managing beans.***

# @RequestMapping
- It maps an incoming HTTP request (URL + HTTP method) to a Java method.
- When this URL is requested, execute this method.

# Why do we need @RequestMapping?
Imagine your application has multiple APIs.
```aiignore
GET  /users
GET  /products
POST /login
DELETE /users/10
```
- When a request reaches your Spring Boot application, how does Spring know which Java method should execute?

# @Controller
- It tells Spring that this class is responsible for handling incoming HTTP requests.
- A Controller receives a request from the client, processes it (usually with the help of a service), and returns a response.

# Why do we need @Controller?
- Imagine you open your browser and type:
- http://localhost:8080/users
- The request reaches your Spring Boot application. 
 
Now the application needs to answer two questions:

Which class should handle this request?
Which method inside that class should execute?

The answer is:

A class annotated with @Controller.

# @ResponseBody
- @ResponseBody tells Spring, dont treat the return value as a view name.Write it directly into the HTTP response body.
- Without it, spring assumes that the returned Spring is the name of an HTML page (View).

# Without @ResponseBody
```aiignore
@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "about";
    }
}
```
- when hit the url: http://localhost:8080/home,
- Method returns: about.html
- Spring interprets this as: Find about.html inside the resources/templates folder.
- So, it renders template about.html.

Browser
│
GET /home
│
▼
Controller
│
returns "about"
│
▼
View Resolver
│
▼
templates/about.html
│
▼
HTML Page

# With @ResponseBody
```aiignore
@Controller
public class HomeController {

    @ResponseBody
    @GetMapping("/home")
    public String home() {
        return "about";
    }
}
```
Now Visit:
http://localhost:8080/home

The Browser shows: about
***Note:Spring doesnt search for about.html, it sends the string directly in the HTTP response.

Browser
│
GET /home
│
▼
Controller
│
returns "about"
│
▼
@ResponseBody
│
▼
HTTP Response Body
│
▼
Browser displays:
about

# @RestController
- It is a combination of @Controller and @ResponseBody.
- It tells spring that this class handles HTTP requests and that every method should return data directly in the HTTP response body.
- use ***RestController*** when you want to return a JSON, text, Number or objects. not HTML pages.

# Why was @RestController introduced?
- Before @RestController,existed, developers had to write this.
```aiignore
@Controller
@ResponseBody
public class UserController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
```
Since almost every REST API needed both annotations, Spring introduced:

```aiignore
@RestController
public class UserController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
```
Internally, this is equivalent to:

```aiignore
@Controller
@ResponseBody
public class UserController {
}
```
so, @RestController is simply a shortcut for @Controller and @ResponseBody.

# @GetMapping, @PostMapping, @PutMapping, @DeleteMapping, @PatchMapping
- These annotations are spring boot shortcut annotations for @RequestMapping, used to map HTTP request to a Java method inside a controller.

Instead of writing:
```aiignore
@RequestMapping(value = "/users", method = RequestMethod.GET)
```
We can write:
```aiignore
@GetMapping("/users")
```
It is shorter, cleaner, and easier to read.

# Why do we use these annotations?
When a client (browser, mobile app, Postman, frontend application) sends an HTTP request to your Spring Boot application, Spring needs to know:

# @RequestBody
- @RequestBody tells Spring take the data sent in the HTTP request body, convert it inot a Java object, and pass it to method.

# Why do we need @RequestBody?
- Imagine a client (frontend, Postman or mobile) sends this request:
```aiignore
POST /users
Content-Type: application/json

{
    "name": "Rashindra",
    "age": 25
}
```
This JSON is present in the request body.

Without @RequestBody, Spring doesnt know that this JSON should be converted into a Java object.

With @RequestBody, Spring automatically:
- Read the JSON from the request body.
- Convert it into a Java object.
- Pass the Java object to the method.

# `@JsonIgnore` and `@JsonProperty` in Spring Boot

## What is `@JsonIgnore`?

`@JsonIgnore` tells Jackson to **ignore a field during JSON serialization and deserialization**.

* Field is **not read** from the request body.
* Field is **not sent** in the response.

### Example

```java
public class User {

    private String name;

    @JsonIgnore
    private String password;

    // getters and setters
}
```

### Request

```json
{
    "name": "Rashindra",
    "password": "12345"
}
```

### Java Object

```java
user.getName();      // Rashindra
user.getPassword();  // null
```

### Response

```json
{
    "name": "Rashindra"
}
```

> **Use Case:** Hide internal or sensitive fields completely.

---

# `@JsonProperty`

`@JsonProperty` controls how a field is read from or written to JSON.

---

## 1. `WRITE_ONLY`

The field is:

* ✅ Read from the request body.
* ❌ Not included in the response.

### Example

```java
public class User {

    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // getters and setters
}
```

### Request

```json
{
    "name": "Rashindra",
    "password": "12345"
}
```

### Java Object

```java
user.getPassword(); // 12345
```

### Response

```json
{
    "name": "Rashindra"
}
```

> **Use Case:** Password fields.

---

## 2. `READ_ONLY`

The field is:

* ❌ Ignored from the request body.
* ✅ Included in the response.

### Example

```java
public class User {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String name;

    // getters and setters
}
```

### Request

```json
{
    "id": 100,
    "name": "Rashindra"
}
```

### Java Object

```java
user.getId(); // null
```

### Response

```json
{
    "id": 1,
    "name": "Rashindra"
}
```

> **Use Case:** Auto-generated fields like `id`, `createdAt`, etc.

---

# Difference

| Annotation                  | Read from Request | Sent in Response | Use Case              |
| --------------------------- | ----------------- | ---------------- | --------------------- |
| `@JsonIgnore`               | ❌ No              | ❌ No             | Hide field completely |
| `@JsonProperty(WRITE_ONLY)` | ✅ Yes             | ❌ No             | Password              |
| `@JsonProperty(READ_ONLY)`  | ❌ No              | ✅ Yes            | ID, Created Date      |

---

# Quick Revision

* `@JsonIgnore` → Ignore field in both request and response.
* `@JsonProperty(WRITE_ONLY)` → Accept in request, hide in response.
* `@JsonProperty(READ_ONLY)` → Ignore in request, show in response.

> **Interview Tip:**
> If you want to accept a password in `@RequestBody` but never expose it in the API response, use **`@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)`**, **not** `@JsonIgnore`.


# `ResponseEntity` in Spring Boot

## What is `ResponseEntity`?

`ResponseEntity` is used to return a **custom HTTP response** from a controller.

It allows you to control:

* Response Body
* HTTP Status Code
* HTTP Headers

---

## Syntax

```java
return ResponseEntity.status(HttpStatus.OK).body(data);
```

---

## Example 1: Return Data

```java
@GetMapping("/users")
public ResponseEntity<String> getUser() {
    return ResponseEntity.ok("User Found");
}
```

**Response**

```http
200 OK
```

```text
User Found
```

---

## Example 2: Resource Created

```java
@PostMapping("/users")
public ResponseEntity<String> createUser() {
    return ResponseEntity.status(HttpStatus.CREATED)
            .body("User Created");
}
```

**Response**

```http
201 Created
```

```text
User Created
```

---

## Example 3: Resource Not Found

```java
@GetMapping("/users/{id}")
public ResponseEntity<String> getUser(@PathVariable int id) {

    if (id != 1) {
        return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok("User Found");
}
```

---

## Common Methods

| Method                                                 | Status          |
| ------------------------------------------------------ | --------------- |
| `ResponseEntity.ok(body)`                              | 200 OK          |
| `ResponseEntity.status(HttpStatus.CREATED).body(body)` | 201 Created     |
| `ResponseEntity.badRequest().body(body)`               | 400 Bad Request |
| `ResponseEntity.notFound().build()`                    | 404 Not Found   |
| `ResponseEntity.noContent().build()`                   | 204 No Content  |

---

## Why use `ResponseEntity`?

* Return custom HTTP status codes.
* Return response body with status.
* Add custom headers if needed.

---

## Quick Revision

* `ResponseEntity` = Full HTTP Response.
* Controls **Body + Status Code + Headers**.
* Commonly used in REST APIs to send meaningful responses.


# `@RequestParam` in Spring Boot

## What is `@RequestParam`?

`@RequestParam` is used to read **query parameters** from the URL.

**Example URL**

```text
/users?page=1&size=10
```

Here:

* `page` = 1
* `size` = 10

---

## Syntax

```java
@RequestParam DataType variableName
```

---

## Example 1: Single Parameter

```java
@GetMapping("/greet")
public String greet(@RequestParam String name) {
    return "Hello " + name;
}
```

**Request**

```text
GET /greet?name=Rashindra
```

**Response**

```text
Hello Rashindra
```

---

## Example 2: Multiple Parameters

```java
@GetMapping("/users")
public String getUsers(
        @RequestParam int page,
        @RequestParam int size) {

    return "Page: " + page + ", Size: " + size;
}
```

**Request**

```text
GET /users?page=1&size=10
```

---

## Example 3: Custom Parameter Name

```java
@GetMapping("/search")
public String search(@RequestParam("q") String keyword) {
    return keyword;
}
```

**Request**

```text
GET /search?q=spring
```

---

## Example 4: Optional Parameter

```java
@GetMapping("/users")
public String getUsers(
        @RequestParam(required = false) String city) {

    return city;
}
```

**Works with**

```text
GET /users
```

or

```text
GET /users?city=Delhi
```

---

## Example 5: Default Value

```java
@GetMapping("/users")
public String getUsers(
        @RequestParam(defaultValue = "1") int page) {

    return "Page: " + page;
}
```

**Request**

```text
GET /users
```

**Response**

```text
Page: 1
```

---

# Quick Revision

| Annotation               | Purpose                                    |
| ------------------------ | ------------------------------------------ |
| `@RequestParam`          | Read query parameters from URL             |
| `required = false`       | Makes parameter optional                   |
| `defaultValue = "value"` | Uses default value if parameter is missing |
| `@RequestParam("q")`     | Maps a different query parameter name      |

> **Remember:** `@RequestParam` reads data after the `?` in the URL.


# `@PathVariable` in Spring Boot

## What is `@PathVariable`?

`@PathVariable` is used to read **values from the URL path**.

**Example**

```text
/users/101
```

Here, `101` is the path variable.

---

# Syntax

```java
@PathVariable DataType variableName
```

---

# Example 1: Single Path Variable

```java
@GetMapping("/users/{id}")
public String getUser(@PathVariable int id) {
    return "User Id: " + id;
}
```

**Request**

```text
GET /users/101
```

**Response**

```text
User Id: 101
```

---

# Example 2: Multiple Path Variables

```java
@GetMapping("/users/{userId}/orders/{orderId}")
public String getOrder(
        @PathVariable int userId,
        @PathVariable int orderId) {

    return "User: " + userId + ", Order: " + orderId;
}
```

**Request**

```text
GET /users/101/orders/5001
```

**Response**

```text
User: 101, Order: 5001
```

---

# Does Order Matter?

Yes, the **URL structure matters**, not the order of method parameters.

### Correct

```java
@GetMapping("/users/{userId}/orders/{orderId}")
public String getOrder(
        @PathVariable int userId,
        @PathVariable int orderId) {
    return "";
}
```

Request

```text
/users/101/orders/5001
```

Result

```text
userId = 101
orderId = 5001
```

---

### Method parameter order can change

```java
@GetMapping("/users/{userId}/orders/{orderId}")
public String getOrder(
        @PathVariable("orderId") int orderId,
        @PathVariable("userId") int userId) {
    return "";
}
```

This also works because Spring matches by **name**, not parameter position.

---

# When do I need to pass the variable name?

## Case 1: Same Name (No Need)

```java
@GetMapping("/users/{id}")
public String getUser(@PathVariable int id) {
    return "";
}
```

`{id}` → `id`

No need to specify the name.

---

## Case 2: Different Name (Required)

```java
@GetMapping("/users/{id}")
public String getUser(@PathVariable("id") int userId) {
    return "";
}
```

Spring maps

```text
{id} → userId
```

Without `"id"` Spring won't know which path variable to bind.

---

# Multiple Variables with Different Names

```java
@GetMapping("/users/{id}/orders/{orderId}")
public String getOrder(
        @PathVariable("id") int userId,
        @PathVariable("orderId") int orderNo) {

    return "";
}
```

---

# `@PathVariable` vs `@RequestParam`

| `@PathVariable`               | `@RequestParam`                                 |
| ----------------------------- | ----------------------------------------------- |
| Reads value from URL path     | Reads value from query parameter                |
| `/users/101`                  | `/users?id=101`                                 |
| Usually identifies a resource | Usually used for filtering, sorting, pagination |

---

# Quick Revision

* `@PathVariable` reads values from the **URL path**.
* If method parameter name and URL variable name are the **same**, no argument is needed.
* If names are **different**, pass the variable name:

  ```java
  @PathVariable("id")
  ```
* Spring matches path variables by **name**, not by the order of method parameters.
