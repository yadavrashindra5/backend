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
