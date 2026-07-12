# Spring Data JPA Annotations

These annotations are used to map **Java classes and fields** to **database tables and columns**.

---

# 1. `@Entity`

## What is `@Entity`?

`@Entity` tells Spring Boot (Hibernate) that **this Java class is a database entity** and should be mapped to a database table.

Without `@Entity`, Hibernate will ignore the class.

> **Simple Definition:**  
> `@Entity` marks a Java class as a database table.

---

## Example

```java
import jakarta.persistence.Entity;

@Entity
public class Student {

}
```

Hibernate treats the `Student` class as an entity.

By default, Hibernate creates a table with the same name.

```
Student
```

---

## Example

```java
@Entity
public class Student {

    private Long id;
    private String name;

}
```

Database Table

| id | name |
|----|------|

---

## Key Points

- Used on a class.
- Makes the class a JPA Entity.
- Every entity must have a primary key (`@Id`).
- By default, the table name is the same as the class name.

---

# 2. `@Table`

## What is `@Table`?

`@Table` is used to specify the **database table name**.

If you don't use `@Table`, Hibernate uses the class name as the table name.

> **Simple Definition:**  
> `@Table` is used to customize the database table name.

---

## Example

Without `@Table`

```java
@Entity
public class Student {

}
```

Table created

```
Student
```

---

Using `@Table`

```java
@Entity
@Table(name = "students")
public class Student {

}
```

Table created

```
students
```

---

## Why do we use `@Table`?

Sometimes the database table already exists.

Example

Database

```
tbl_student
```

Java Class

```java
@Entity
@Table(name = "tbl_student")
public class Student {

}
```

Now Hibernate maps the class to the existing table.

---

## Key Points

- Used on a class.
- Changes the table name.
- Optional annotation.
- If omitted, the class name becomes the table name.

---

# 3. `@Id`

## What is `@Id`?

`@Id` marks a field as the **Primary Key** of the table.

Every entity must have one primary key.

> **Simple Definition:**  
> `@Id` identifies the unique field of an entity.

---

## Example

```java
@Entity
public class Student {

    @Id
    private Long id;

    private String name;

}
```

Database

| id (PK) | name |
|----------|------|

---

## Why Primary Key?

A primary key uniquely identifies each row.

Example

| id | name |
|----|------|
| 1 | John |
| 2 | David |

The `id` value is unique.

---

## Key Points

- Used on a field.
- Represents the Primary Key.
- Mandatory for every entity.

---

# 4. `@Column`

## What is `@Column`?

`@Column` is used to customize the database column.

If you don't use it, Hibernate uses the field name as the column name.

> **Simple Definition:**  
> `@Column` is used to configure a database column.

---

## Example

Without `@Column`

```java
private String studentName;
```

Database column

```
student_name
```

(or depending on naming strategy)

---

Using `@Column`

```java
@Column(name = "student_name")
private String name;
```

Database

| student_name |
|---------------|

---

## Common Properties

### Change Column Name

```java
@Column(name = "student_name")
```

---

### Make Column Unique

```java
@Column(unique = true)
private String email;
```

No duplicate emails are allowed.

---

### Make Column Mandatory

```java
@Column(nullable = false)
private String name;
```

Database will not allow NULL values.

---

### Limit Length

```java
@Column(length = 100)
private String address;
```

Maximum 100 characters.

---

## Key Points

- Used on fields.
- Customizes database columns.
- Optional annotation.
- Supports many properties like:
    - `name`
    - `nullable`
    - `unique`
    - `length`

---

# 5. `@GeneratedValue`

## What is `@GeneratedValue`?

`@GeneratedValue` tells Hibernate to generate the primary key value automatically.

You don't need to assign the ID manually.

> **Simple Definition:**  
> `@GeneratedValue` automatically generates the primary key value.

---

## Example

```java
@Entity
public class Student {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

}
```

Saving

```java
Student student = new Student();
student.setName("John");

studentRepository.save(student);
```

Hibernate automatically generates:

```
id = 1
```

---

## Generation Strategies

### AUTO

```java
@GeneratedValue(strategy = GenerationType.AUTO)
```

Hibernate automatically chooses the best strategy.

---

### IDENTITY (Most Common with MySQL)

```java
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

Uses MySQL's `AUTO_INCREMENT`.

Database

| id |
|----|
| 1 |
| 2 |
| 3 |

---

### SEQUENCE

```java
@GeneratedValue(strategy = GenerationType.SEQUENCE)
```

Uses a database sequence.

Mostly used in Oracle and PostgreSQL.

---

### TABLE

```java
@GeneratedValue(strategy = GenerationType.TABLE)
```

Stores generated IDs in a separate table.

Rarely used.

---

## Key Points

- Used with `@Id`.
- Automatically generates primary key values.
- Common strategy for MySQL is `GenerationType.IDENTITY`.

---

# Complete Example

```java
import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_name", nullable = false)
    private String name;

    @Column(unique = true)
    private String email;

}
```

Database Table

| id | student_name | email |
|----|--------------|-------|
| 1 | John | john@gmail.com |

---

# Annotation Summary

| Annotation | Purpose |
|------------|---------|
| `@Entity` | Marks a class as a database entity (table). |
| `@Table` | Specifies the database table name. |
| `@Id` | Marks a field as the primary key. |
| `@Column` | Customizes a database column. |
| `@GeneratedValue` | Automatically generates primary key values. |

---

# Easy Way to Remember

| Annotation | Think of it as... |
|------------|-------------------|
| `@Entity` | **This class is a database table.** |
| `@Table` | **Give the table a custom name.** |
| `@Id` | **This field is the Primary Key.** |
| `@Column` | **Customize the column.** |
| `@GeneratedValue` | **Generate the ID automatically.** |

---

# One-Line Summary

- `@Entity` → Converts a Java class into a database table.
- `@Table` → Changes the table name.
- `@Id` → Defines the primary key.
- `@Column` → Configures a database column.
- `@GeneratedValue` → Automatically generates the primary key value.