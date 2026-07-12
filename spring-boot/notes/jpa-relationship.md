# One-to-One Relationship Mapping (`@OneToOne`) in Spring Data JPA

## What is One-to-One Relationship?

A **One-to-One relationship** means **one record of one table is associated with only one record of another table**.

In simple words:

- One **User** has **one Profile**
- One **Person** has **one Passport**
- One **Employee** has **one Laptop**
- One **Student** has **one Library Card**

### Example

```text
Person
+----+--------+
| id | name   |
+----+--------+
| 1  | John   |
+----+--------+

Passport
+----+------------+
| id | number     |
+----+------------+
| 1  | NP123456   |
+----+------------+
```

Here,

- John has only one Passport.
- Passport `NP123456` belongs to only John.

This is called a **One-to-One Relationship**.

---

# Why do we use One-to-One Mapping?

Sometimes, storing everything in a single table is not a good idea.

For example:

```text
User
---------------------------------------------------------
id | name | phone | address | bio | image | dateOfBirth
```

As the application grows, this table becomes large and difficult to manage.

Instead, we can split it into two tables.

```text
User
--------------
id | name

Profile
---------------------------------------
id | phone | address | bio | user_id
```

Now,

- User information remains separate.
- Profile information remains separate.
- The database becomes cleaner and easier to maintain.

---

# What is `@OneToOne`?

`@OneToOne` is a JPA annotation used to define a **One-to-One relationship** between two entities.

### Syntax

```java
@OneToOne
private Passport passport;
```

It tells Hibernate:

> "This entity has exactly one associated entity."

---

# Example

## Passport Entity

```java
import jakarta.persistence.*;

@Entity
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String passportNumber;

}
```

---

## Person Entity

```java
import jakarta.persistence.*;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "passport_id")
    private Passport passport;

}
```

---

# What is `@JoinColumn`?

`@JoinColumn` specifies **which column will store the foreign key**.

Example

```java
@JoinColumn(name = "passport_id")
```

Database

```text
Person
----------------------------
id
name
passport_id
```

The value stored inside `passport_id` points to the Passport table.

Example

```text
Person

id   name    passport_id
-------------------------
1    John        5
```

```text
Passport

id   passport_number
--------------------
5    NP123456
```

Here,

`passport_id = 5`

means John is linked with Passport having ID **5**.

---

# What is a Foreign Key?

A **Foreign Key** is a column that stores the **Primary Key (ID)** of another table.

It is used to create a relationship between two tables.

Example

```text
Person

id   name    passport_id
-------------------------
1    John        5
```

`passport_id` is a foreign key because it refers to

```text
Passport

id = 5
```

This is how two tables are connected.

---

# Saving Data

```java
Passport passport = new Passport();
passport.setPassportNumber("NP123456");

Person person = new Person();
person.setName("John");
person.setPassport(passport);

personRepository.save(person);
```

> **Note:** The above code works only if the appropriate **Cascade Type** is configured. Otherwise, you must save the `Passport` entity before saving the `Person`.

---

# SQL Generated

Hibernate may execute SQL similar to:

```sql
insert into passport(passport_number)
values ('NP123456');

insert into person(name, passport_id)
values ('John', 1);
```

---

# Common Use Cases

- User ↔ Profile
- Person ↔ Passport
- Employee ↔ Laptop
- Student ↔ Library Card
- Customer ↔ Address (when only one address is allowed)

---

# Important Points

- `@OneToOne` creates a one-to-one relationship between two entities.
- One record can be associated with only one record of another table.
- `@JoinColumn` specifies the foreign key column.
- A foreign key connects one table to another using the referenced table's primary key.
- Spring Data JPA and Hibernate automatically manage the relationship.
- One-to-One mapping helps keep the database normalized and organized.

---

# Quick Revision

| Annotation | Purpose |
|------------|---------|
| `@OneToOne` | Creates a one-to-one relationship between two entities |
| `@JoinColumn` | Specifies the foreign key column |
| Foreign Key | Connects one table to another using the referenced table's primary key |

---

# Unidirectional and Bidirectional Relationship in Spring Data JPA

Whenever we create a relationship between two entities in JPA, we can define it in **two ways**:

1. **Unidirectional Relationship**
2. **Bidirectional Relationship**

These concepts apply to all JPA relationships:

- `@OneToOne`
- `@OneToMany`
- `@ManyToOne`
- `@ManyToMany`

The difference is **how entities can access each other**.

---

# 1. Unidirectional Relationship

## What is a Unidirectional Relationship?

A **Unidirectional Relationship** means **only one entity knows about the other entity**.

```text
Person -----------> Passport
```

Here,

- `Person` knows about `Passport`.
- `Passport` does **not** know about `Person`.

The relationship can be accessed in **only one direction**.

---

## How to Achieve a Unidirectional Relationship?

A Unidirectional relationship is achieved by defining the relationship annotation in **only one entity**.

### Passport Entity

```java
import jakarta.persistence.*;

@Entity
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String passportNumber;

}
```

---

### Person Entity

```java
import jakarta.persistence.*;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "passport_id")
    private Passport passport;

}
```

---

## Database

```text
Person
--------------------------------
id
name
passport_id (FK)

Passport
----------------------
id
passport_number
```

---

## Accessing Data

You can access the passport from the person.

```java
Person person = personRepository.findById(1L).get();

Passport passport = person.getPassport();
```

But you **cannot** access the person from the passport because `Passport` has no reference to `Person`.

```java
Passport passport = passportRepository.findById(1L).get();

passport.getPerson(); // ❌ Compile-time error
```

---

## When to Use Unidirectional Relationship?

Use a Unidirectional relationship when:

- You only need to navigate from one entity to another.
- The reverse navigation is never required.
- You want to keep the entity model simple.

Example:

- User → Profile
- Person → Passport
- Employee → Laptop

---

# 2. Bidirectional Relationship

## What is a Bidirectional Relationship?

A **Bidirectional Relationship** means **both entities know about each other**.

```text
Person <-----------> Passport
```

Here,

- `Person` knows about `Passport`.
- `Passport` also knows about `Person`.

The relationship can be accessed from **both directions**.

---

## How to Achieve a Bidirectional Relationship?

A Bidirectional relationship is achieved by defining the relationship in **both entities**.

One entity becomes the **owner of the relationship**, while the other references it using the `mappedBy` attribute.

### Passport Entity

```java
import jakarta.persistence.*;

@Entity
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String passportNumber;

    @OneToOne(mappedBy = "passport")
    private Person person;

}
```

---

### Person Entity

```java
import jakarta.persistence.*;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "passport_id")
    private Passport passport;

}
```

---

## Database

```text
Person
--------------------------------
id
name
passport_id (FK)

Passport
----------------------
id
passport_number
```

Notice that the database structure is **exactly the same** as the unidirectional relationship.

The difference is only in the Java entity classes.

---

## Accessing Data

From Person

```java
Person person = personRepository.findById(1L).get();

Passport passport = person.getPassport();
```

From Passport

```java
Passport passport = passportRepository.findById(1L).get();

Person person = passport.getPerson();
```

Now both entities can access each other.

---

## When to Use Bidirectional Relationship?

Use a Bidirectional relationship when:

- Both entities need to access each other.
- You frequently navigate in both directions.
- Your business logic requires access from both sides.

Example:

- Person ↔ Passport
- Student ↔ Library Card
- Employee ↔ Parking Slot

---

# Difference Between Unidirectional and Bidirectional

| Unidirectional | Bidirectional |
|---------------|---------------|
| Only one entity knows about the other. | Both entities know about each other. |
| Relationship is defined in one entity only. | Relationship is defined in both entities. |
| Navigation is one-way. | Navigation is two-way. |
| Simpler to implement. | Slightly more complex. |
| `mappedBy` is not required. | `mappedBy` is required on the non-owning side. |

---

# Key Points

- JPA relationships can be **Unidirectional** or **Bidirectional**.
- A **Unidirectional Relationship** allows navigation in only one direction.
- A **Bidirectional Relationship** allows navigation in both directions.
- To create a **Unidirectional Relationship**, define the relationship annotation in only one entity.
- To create a **Bidirectional Relationship**, define the relationship annotation in both entities and use `mappedBy` on one side.
- The database structure is usually the same; only the entity classes differ.

---
