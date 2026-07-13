# Fetching in Spring Data JPA

## What is Fetching?

**Fetching** is the process of loading related entities from the database.

In simple words:

> **Fetching tells JPA when to load the child entity from the database.**

Whenever you retrieve a parent entity, JPA decides whether the related child entity should also be loaded immediately or later.

---

## Example

Suppose we have:

```
User (Parent)
   |
   └── Address (Child)
```

### User Entity

```java
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;
}
```

### Address Entity

```java
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    private String country;
}
```

---

# Why Do We Need Fetching?

Suppose you only want to display the user's name.

```
User
 └── Address
```

Do you always need the Address?

**No.**

Sometimes you only need the User.

Sometimes you need both User and Address.

Fetching allows JPA to decide **when the related entity should be loaded.**

---

# Fetch Types

JPA provides two fetch types.

```
FetchType.EAGER
FetchType.LAZY
```

---

# 1. FetchType.EAGER

## What does it do?

`EAGER` means the child entity is loaded **immediately** whenever the parent entity is loaded.

### Example

```java
@OneToOne(fetch = FetchType.EAGER)
private Address address;
```

Now retrieve the user.

```java
User user = userRepository.findById(1L).get();
```

### What happens?

```
Load User
    │
    ▼
Load Address
```

Even if you never use the Address, JPA still loads it.

### Example SQL

```sql
SELECT * FROM user WHERE id = 1;

SELECT * FROM address WHERE id = ?;
```

or sometimes a JOIN query depending on Hibernate.

### Remember

With `EAGER`, the child entity is always loaded along with the parent.

---

# 2. FetchType.LAZY

## What does it do?

`LAZY` means the child entity is **not loaded immediately**.

It is loaded **only when you actually access it.**

### Example

```java
@OneToOne(fetch = FetchType.LAZY)
private Address address;
```

Retrieve the user.

```java
User user = userRepository.findById(1L).get();
```

### What happens?

```
Load User

Address NOT loaded
```

Only the User is loaded.

Now access the Address.

```java
System.out.println(user.getAddress().getCity());
```

Now JPA loads the Address.

```
Load User
      │
      ▼
Access Address
      │
      ▼
Load Address
```

### Example SQL

First query

```sql
SELECT * FROM user WHERE id = 1;
```

Later, when Address is accessed

```sql
SELECT * FROM address WHERE id = ?;
```

### Remember

With `LAZY`, the child entity is loaded **only when it is needed.**

---

# EAGER vs LAZY

| Feature | EAGER | LAZY |
|----------|--------|-------|
| When child is loaded? | Immediately | Only when accessed |
| Performance | Slower if child is not needed | Faster because unnecessary data is not loaded |
| Database queries | More queries or joins | Queries only when needed |
| Best for | Data always required | Large or optional relationships |

---

# Visual Difference

## FetchType.EAGER

```
Find User
    │
    ▼
Load User
    │
    ▼
Load Address
```

Both entities are loaded together.

---

## FetchType.LAZY

```
Find User
    │
    ▼
Load User
```

Address is **not** loaded.

When you call

```java
user.getAddress()
```

then

```
Load Address
```

---

# Default Fetch Types

| Relationship | Default Fetch Type |
|--------------|--------------------|
| `@OneToOne` | EAGER |
| `@ManyToOne` | EAGER |
| `@OneToMany` | LAZY |
| `@ManyToMany` | LAZY |

### Example

```java
@OneToOne
private Address address;
```

If you don't specify anything,

```java
@OneToOne
```

JPA automatically uses

```java
@OneToOne(fetch = FetchType.EAGER)
```

---

# Why is LAZY Recommended?

Suppose one User has 500 Orders.

```
User
 └── Orders (500)
```

If fetching is **EAGER**, all 500 Orders are loaded even if you only want the User's name.

This wastes:

- Memory
- Database time
- Network bandwidth

With **LAZY**, Orders are loaded only when required.

---

# Common Example

### Get only User

```java
User user = userRepository.findById(1L).get();

System.out.println(user.getName());
```

### EAGER

```
User Loaded ✔
Address Loaded ✔
```

Even though Address is never used.

---

### LAZY

```
User Loaded ✔
Address Not Loaded ✔
```

Address is fetched only if this line is executed.

```java
user.getAddress();
```

---

# Common Problem with LAZY

Suppose the transaction is already closed.

```java
User user = userRepository.findById(1L).get();
```

Later

```java
user.getAddress();
```

Hibernate may throw

```
LazyInitializationException
```

because it can no longer fetch data from the database.

### Why?

The database session is already closed, so Hibernate cannot load the lazy entity.

---

# When Should We Use EAGER?

Use **EAGER** when the child entity is **always required**.

Example:

```
Employee
 └── Department
```

If every time you display an Employee, you also display the Department, then EAGER may be acceptable.

---

# When Should We Use LAZY?

Use **LAZY** when the child entity is **not always required**.

Example:

```
User
 └── Orders
```

Sometimes you only need User information.

There is no need to load all Orders every time.

---

# Interview Questions

## Does FetchType create a relationship?

**No.**

Relationship is created by

- `@OneToOne`
- `@OneToMany`
- `@ManyToOne`
- `@ManyToMany`

Fetch type only decides **when related entities are loaded.**

---

## Does FetchType affect saving or deleting?

**No.**

Fetching only controls **reading data**.

Saving and deleting are controlled by **Cascade Types**.

---

## Difference Between Cascade and Fetch

| Cascade | Fetch |
|----------|-------|
| Controls persistence operations | Controls data loading |
| Used while Save, Update, Delete, Refresh, Detach | Used while Reading data |
| Example: Save User → Save Address | Example: Load User → Load Address now or later |

---

# Summary

> **Fetching determines when related entities are loaded from the database. `FetchType.EAGER` loads related entities immediately, while `FetchType.LAZY` loads them only when they are first accessed.**