# JPA and Hibernate Notes

## What is JPA?

**JPA (Java Persistence API)** is a **Specification (set of rules)** that defines how Java objects should be stored and retrieved from a database.

It does **not** perform any database operations by itself. It only provides interfaces that ORM frameworks must implement.

> **Simple Definition:**
> JPA is a blueprint (or contract) that defines the standard way to interact with databases using Java objects.

### Example

JPA provides interfaces like:

```java
EntityManager
EntityTransaction
Query
```

These are only interfaces; they don't contain the actual implementation.

---

## What is Hibernate?

**Hibernate** is an **ORM (Object Relational Mapping) Framework** that **implements JPA**.

It follows JPA's rules and performs the actual work of saving, updating, deleting, and fetching data from the database.

> **Simple Definition:**
> Hibernate converts Java objects into database records and database records into Java objects.

### Example

```java
User user = new User();
user.setName("John");

entityManager.persist(user);
```

Hibernate automatically generates SQL like:

```sql
INSERT INTO users(name)
VALUES ('John');
```

You don't need to write SQL manually.

---

# How JPA and Hibernate Work Together

```
Java Application
       │
       ▼
      JPA
 (Defines Rules)
       │
       ▼
   Hibernate
(Implements JPA)
       │
       ▼
    Database
```

---

## Simple Analogy

Imagine you want to build a house.

- **JPA** → Blueprint (Design)
- **Hibernate** → Builder (Builds the house)

Without the builder, the blueprint cannot create the house.

---

# JPA vs Hibernate

| JPA | Hibernate |
|------|-----------|
| Specification | ORM Framework |
| Defines rules | Implements those rules |
| Cannot work alone | Works by implementing JPA |
| Provides interfaces | Provides actual implementation |
| Does not generate SQL | Generates SQL automatically |

---

# Example

### Using JPA (through Hibernate)

```java
User user = new User();
user.setName("John");

entityManager.persist(user);
```

Behind the scenes, Hibernate converts it into:

```sql
INSERT INTO users(name)
VALUES ('John');
```

---

# Key Points

## JPA

- Java Persistence API
- Specification (Rules)
- Provides interfaces
- Does not perform database operations
- Needs an implementation like Hibernate

## Hibernate

- ORM Framework
- Implements JPA
- Maps Java objects to database tables
- Generates SQL automatically
- Performs CRUD operations

---

# Interview Questions

### Is JPA a framework?

**No.**

JPA is a **Specification** (a set of rules), not a framework.

---

### Can JPA work without Hibernate?

**No.**

JPA needs an implementation such as:

- Hibernate (Most Popular)
- EclipseLink
- OpenJPA

---

### Can Hibernate work without JPA?

**Yes.**

Hibernate is a standalone ORM framework and can be used directly.

However, in modern Spring Boot applications, Hibernate is usually used **through JPA**.

---

# Easy Way to Remember

| JPA | Hibernate |
|------|-----------|
| Rules | Implementation |
| Blueprint | Builder |
| What to do | How to do it |



# ORM (Object Relational Mapping)

## What is ORM?

**ORM (Object Relational Mapping)** is a technique that allows us to interact with a database using **Java objects** instead of writing SQL queries manually.

> **Simple Definition:**  
> ORM automatically maps **Java Objects** to **Database Tables**.

---

## Why do we need ORM?

Without ORM, we have to write SQL queries ourselves.

### Without ORM

```java
String sql = "INSERT INTO users(name) VALUES (?)";
```

### With ORM

```java
User user = new User();
user.setName("John");

entityManager.persist(user);
```

ORM automatically generates:

```sql
INSERT INTO users(name)
VALUES ('John');
```

---

## How ORM Works

```
Java Object
     │
     ▼
    ORM
(Hibernate)
     │
     ▼
Database Table
```

---

## Example

### Java Class

```java
@Entity
public class User {

    @Id
    private Long id;

    private String name;
}
```

### Database Table

| id | name |
|----|------|
| 1 | John |

ORM automatically maps:

- `User` class → `users` table
- `id` → `id` column
- `name` → `name` column

---

## Advantages of ORM

- No need to write SQL for basic CRUD operations.
- Works with Java objects instead of database rows.
- Reduces boilerplate code.
- Makes code easier to read and maintain.

---

## ORM in Spring Boot

The most popular ORM framework is **Hibernate**.

```
Spring Boot
      │
      ▼
Spring Data JPA
      │
      ▼
Hibernate (ORM)
      │
      ▼
Database
```

---

## Easy Way to Remember

Think of ORM as a **Translator**.

- Java understands **Objects**.
- Database understands **Tables**.

ORM translates between them.

```
Java Objects  ⇄  ORM  ⇄  Database Tables
```

---

## Key Points

- ORM stands for **Object Relational Mapping**.
- It maps Java objects to database tables.
- It lets you work with Java objects instead of writing SQL.
- **Hibernate** is the most popular ORM framework in Java.

---

## One-Line Summary

> **ORM is a technique that automatically converts Java objects into database records and database records into Java objects.**




# Spring Data JPA vs Spring JDBC

## What is Spring JDBC?

**Spring JDBC** is a module of Spring Framework that simplifies working with JDBC.

It helps you execute SQL queries with less boilerplate code, but **you still have to write SQL manually**.

### Example

```java
String sql = "SELECT * FROM users WHERE id = ?";

User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
```

You are responsible for writing the SQL query.

---

## What is Spring Data JPA?

**Spring Data JPA** is a Spring module built on top of **JPA**.

It allows you to perform database operations using Java objects without writing SQL for basic CRUD operations.

### Example

```java
public interface UserRepository extends JpaRepository<User, Long> {

}
```

Now you can simply write:

```java
userRepository.findById(1L);
userRepository.save(user);
userRepository.deleteById(1L);
```

No SQL is required for these operations.

---

# Spring JDBC vs Spring Data JPA

| Spring JDBC | Spring Data JPA |
|--------------|-----------------|
| Works with SQL queries | Works with Java Objects (Entities) |
| SQL must be written manually | SQL is generated automatically for CRUD operations |
| Uses `JdbcTemplate` | Uses `JpaRepository` |
| Faster because SQL is directly executed | Slightly slower due to ORM overhead |
| More control over SQL | Less control for basic CRUD |
| Best for complex and optimized queries | Best for standard CRUD applications |
| No ORM | Uses ORM (Hibernate by default) |

---

# Code Comparison

## Spring JDBC

```java
String sql = "INSERT INTO users(name, email) VALUES (?, ?)";

jdbcTemplate.update(sql, "John", "john@gmail.com");
```

You write the SQL yourself.

---

## Spring Data JPA

```java
User user = new User();
user.setName("John");
user.setEmail("john@gmail.com");

userRepository.save(user);
```

Hibernate automatically generates the SQL.

---

# When to Use Spring JDBC?

Use Spring JDBC when:

- You want full control over SQL.
- Your application has complex SQL queries.
- Performance is critical.
- You already have existing SQL scripts or stored procedures.

---

# When to Use Spring Data JPA?

Use Spring Data JPA when:

- Your application mainly performs CRUD operations.
- You want to write less code.
- You prefer working with Java objects instead of SQL.
- You want faster development.

---

# Which One is Better?

There is **no absolute winner**.

- **Spring JDBC** is better when you need **full control** over SQL and maximum performance.
- **Spring Data JPA** is better when you want **faster development** with less code.

Many real-world applications use **both**:

- **Spring Data JPA** for CRUD operations.
- **Spring JDBC** for complex reports, bulk updates, or highly optimized queries.

---

# Easy Way to Remember

### Spring JDBC

> **"I write the SQL."**

```
Java Code
     │
     ▼
Spring JDBC
     │
     ▼
SQL (Written by You)
     │
     ▼
Database
```

---

### Spring Data JPA

> **"I work with Java objects."**

```
Java Object
     │
     ▼
Spring Data JPA
     │
     ▼
Hibernate (ORM)
     │
     ▼
SQL (Generated Automatically)
     │
     ▼
Database
```

---

# Key Points

### Spring JDBC

- Uses `JdbcTemplate`.
- SQL is written manually.
- No ORM.
- Better control over database queries.

### Spring Data JPA

- Uses `JpaRepository`.
- Works with entities (Java objects).
- Uses Hibernate (ORM) by default.
- Automatically generates SQL for CRUD operations.

---

# One-Line Summary

- **Spring JDBC:** You write SQL manually and Spring helps execute it.
- **Spring Data JPA:** You work with Java objects, and Hibernate generates the SQL automatically.




# Configuring Database in Spring Data JPA (`application.properties`)

Before Spring Boot can store or retrieve data from a database, it needs to know **which database to connect to** and **how to connect to it**.

We provide this information in the **`application.properties`** file.

---

# Step 1: Add Required Dependencies

To use Spring Data JPA with MySQL, add these dependencies in your `pom.xml`.

### Spring Data JPA

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

### MySQL Driver

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```

---

# Step 2: Configure `application.properties`

```properties
# ==========================
# Database Configuration
# ==========================

spring.datasource.url=jdbc:mysql://localhost:3306/student_db
spring.datasource.username=root
spring.datasource.password=root123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ==========================
# JPA / Hibernate Configuration
# ==========================

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

---

# Explanation of Each Property

## 1. Database URL

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_db
```

This property tells Spring Boot **where the database is located**.

### Breakdown

```
jdbc:mysql://localhost:3306/student_db
```

- **jdbc** → Java Database Connectivity
- **mysql** → Database type
- **localhost** → Database is running on your computer
- **3306** → MySQL default port
- **student_db** → Database name

If your database name is `company_db`, then:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/company_db
```

---

## 2. Database Username

```properties
spring.datasource.username=root
```

This is the username used to log in to your database.

Example:

```
Username : root
```

---

## 3. Database Password

```properties
spring.datasource.password=root123
```

This is the password of your database user.

Example:

```
Password : root123
```

---

## 4. Driver Class Name

```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

A **driver** is a software component that allows Java to communicate with the database.

For MySQL, the driver class is:

```
com.mysql.cj.jdbc.Driver
```

> **Note:** In modern Spring Boot versions, you usually don't need to write this property because Spring Boot detects it automatically when the MySQL dependency is added.

---

## 5. Hibernate Dialect

```properties
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

A **Dialect** tells Hibernate **which database you are using** so it can generate the correct SQL.

Different databases use slightly different SQL syntax.

Examples:

| Database | Dialect |
|----------|----------|
| MySQL | `MySQLDialect` |
| PostgreSQL | `PostgreSQLDialect` |
| Oracle | `OracleDialect` |

> **Note:** In recent Spring Boot versions, this property is also optional because Hibernate can usually detect the database automatically.

---

## 6. `ddl-auto`

```properties
spring.jpa.hibernate.ddl-auto=update
```

This property tells Hibernate **what to do with your database tables** when the application starts.

### Available Options

| Value | Meaning |
|--------|---------|
| `none` | Do nothing |
| `validate` | Check if tables match entities. Don't create or update tables. |
| `update` | Create new tables or update existing ones without deleting data. |
| `create` | Delete old tables and create new ones every time the application starts. |
| `create-drop` | Create tables on startup and delete them when the application stops. |

### Example

Suppose your entity is:

```java
@Entity
public class Student {

    @Id
    private Long id;

    private String name;
}
```

If the `student` table doesn't exist:

```
ddl-auto=update
```

Hibernate automatically creates it.

Later, if you add a new field:

```java
private String email;
```

Hibernate automatically adds the `email` column without deleting existing data.

---

## 7. Show SQL

```properties
spring.jpa.show-sql=true
```

This tells Hibernate to display the generated SQL in the console.

Example:

```sql
insert into student(name)
values ('John');
```

This is useful for debugging and learning.

---

## 8. Format SQL

```properties
spring.jpa.properties.hibernate.format_sql=true
```

This makes the SQL output easier to read.

Without formatting:

```sql
select id,name,email from student;
```

With formatting:

```sql
select
    id,
    name,
    email
from
    student;
```

---

# Complete Configuration

```properties
# ==========================
# Database Configuration
# ==========================

spring.datasource.url=jdbc:mysql://localhost:3306/student_db
spring.datasource.username=root
spring.datasource.password=root123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ==========================
# Hibernate / JPA Configuration
# ==========================

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

---

# How Spring Boot Uses These Properties

```
application.properties
          │
          ▼
Reads Database Configuration
          │
          ▼
Creates Database Connection (DataSource)
          │
          ▼
Configures Hibernate
          │
          ▼
Connects to MySQL Database
```

---

# Notes

### `spring.datasource.*`

These properties are used to **connect your application to the database**.

- URL
- Username
- Password
- Driver

---

### `spring.jpa.*`

These properties are used to **configure Hibernate and JPA**.

Examples:

- Create or update tables
- Show SQL
- Format SQL
- Select database dialect

---

# Best Practice

For development:

```properties
spring.jpa.hibernate.ddl-auto=update
```

For production:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

Avoid using:

```properties
spring.jpa.hibernate.ddl-auto=create
```

in production because it deletes existing tables and data.

---

# Key Points

- `spring.datasource.*` configures the **database connection**.
- `spring.jpa.*` configures **JPA and Hibernate**.
- `ddl-auto=update` automatically creates or updates tables.
- `show-sql=true` prints SQL queries in the console.
- `format_sql=true` makes SQL output readable.
- In modern Spring Boot versions, `driver-class-name` and `database-platform` are usually optional.

---

# One-Line Summary

> **The `application.properties` file stores the database connection details and Hibernate settings, allowing Spring Boot to connect to the database and manage entities automatically.**