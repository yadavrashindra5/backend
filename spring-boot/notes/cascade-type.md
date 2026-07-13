# Cascading in Spring Data JPA

## What is Cascading?

**Cascading** is a JPA feature that allows an operation performed on a **parent entity** to be automatically applied to its **child entity/entities**.

In simple words:

> **Cascade means "do the same operation on the related entity automatically."**

Without cascading, you have to perform operations on both parent and child manually.

---

## Example

Suppose you have:

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

    @OneToOne(cascade = CascadeType.ALL)
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

## Without Cascade

Without cascade, JPA only knows how to save the **User**.

```java
Address address = new Address();
address.setCity("Delhi");

User user = new User();
user.setName("Rashindra");
user.setAddress(address);

userRepository.save(user);
```

This will throw an exception because **Address is not saved**.

You must save it manually.

```java
addressRepository.save(address);
userRepository.save(user);
```

---

## With Cascade

Now add cascade.

```java
@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "address_id")
private Address address;
```

Now this code is enough.

```java
Address address = new Address();
address.setCity("Delhi");

User user = new User();
user.setName("Rashindra");
user.setAddress(address);

userRepository.save(user);
```

Hibernate automatically saves both entities.

```
Save User
    │
    ▼
Save Address
```

Only one line was executed.

```java
userRepository.save(user);
```

---

# Why Do We Need Cascade?

Suppose every User has one Address.

Whenever

- User is saved
- User is updated
- User is deleted

you also want the same operation on Address.

Instead of writing this every time

```java
addressRepository.save(address);
userRepository.save(user);
```

you simply write

```java
userRepository.save(user);
```

Cascade performs the remaining work automatically.

---

# Cascade Types

JPA provides six cascade types.

```
CascadeType.PERSIST
CascadeType.MERGE
CascadeType.REMOVE
CascadeType.REFRESH
CascadeType.DETACH
CascadeType.ALL
```

---

# 1. CascadeType.PERSIST

## What does it do?

`PERSIST` automatically saves the child entity when the parent entity is saved.

### Example

```java
@OneToOne(cascade = CascadeType.PERSIST)
private Address address;
```

```java
Address address = new Address();
address.setCity("Delhi");

User user = new User();
user.setName("John");
user.setAddress(address);

userRepository.save(user);
```

### What happens?

```
Save User
    │
    ▼
Save Address
```

Hibernate executes something similar to

```sql
INSERT INTO address (...);

INSERT INTO user (...);
```

### Remember

`PERSIST` only works for **saving new entities**.

It does **not** update or delete child entities.

---

# 2. CascadeType.MERGE

## What does it do?

`MERGE` automatically updates the child entity when the parent entity is updated.

### Example

```java
@OneToOne(cascade = CascadeType.MERGE)
private Address address;
```

Suppose data already exists.

```
User
 └── Address
```

Now update both.

```java
user.setName("Rahul");
user.getAddress().setCity("Mumbai");

userRepository.save(user);
```

### What happens?

```
Update User
      │
      ▼
Update Address
```

Hibernate updates both records.

### Remember

`MERGE` is used for **updating existing entities**.

---

# 3. CascadeType.REMOVE

## What does it do?

`REMOVE` automatically deletes the child entity when the parent entity is deleted.

### Example

```java
@OneToOne(cascade = CascadeType.REMOVE)
private Address address;
```

```java
userRepository.delete(user);
```

### What happens?

```
Delete User
      │
      ▼
Delete Address
```

Hibernate executes something similar to

```sql
DELETE FROM user;

DELETE FROM address;
```

### Remember

Use `REMOVE` only when the child entity should not exist without the parent.

Example:

```
Order
   └── OrderItem
```

Deleting the Order should also delete its OrderItems.

---

# 4. CascadeType.REFRESH

## What does it do?

`REFRESH` reloads the parent and child entity from the database.

Suppose someone changed the database directly.

Current database

```
User
Address = Delhi
```

But your application still has

```
Address = Mumbai
```

Now call

```java
entityManager.refresh(user);
```

### What happens?

```
Refresh User
      │
      ▼
Refresh Address
```

The latest values are fetched from the database.

### Remember

`REFRESH` discards local changes and loads fresh data from the database.

---

# 5. CascadeType.DETACH

## What does it do?

`DETACH` removes both parent and child from Hibernate's Persistence Context.

```java
@OneToOne(cascade = CascadeType.DETACH)
private Address address;
```

```java
entityManager.detach(user);
```

### What happens?

```
Detach User
      │
      ▼
Detach Address
```

Now Hibernate no longer tracks them.

If you change their values

```java
user.setName("Rahul");
```

and commit the transaction,

nothing will be updated because both entities are detached.

### Remember

Detached entities are **not managed by Hibernate**.

---

# 6. CascadeType.ALL

## What does it do?

`ALL` means **apply every cascade type**.

```java
@OneToOne(cascade = CascadeType.ALL)
private Address address;
```

It is equivalent to

```java
cascade = {
    CascadeType.PERSIST,
    CascadeType.MERGE,
    CascadeType.REMOVE,
    CascadeType.REFRESH,
    CascadeType.DETACH
}
```

### What happens?

Whenever you

- Save
- Update
- Delete
- Refresh
- Detach

the parent entity,

the same operation is automatically performed on the child entity.

```
Save
Update
Delete
Refresh
Detach
      │
      ▼
Child Entity
```

---

# Which Cascade Type Should I Use?

| Cascade Type | Use When |
|--------------|----------|
| `PERSIST` | Save child automatically |
| `MERGE` | Update child automatically |
| `REMOVE` | Delete child automatically |
| `REFRESH` | Reload child from database |
| `DETACH` | Detach child from Persistence Context |
| `ALL` | Apply every cascade operation |

---

# When Should We Use Cascade?

Use cascade when the child entity depends on the parent.

Examples:

```
User
 └── Address
```

```
Order
 └── OrderItems
```

```
Blog
 └── Comments
```

In these cases, the child usually has no meaning without the parent.

---

# When Should We Avoid Cascade?

Avoid cascade when the child entity is shared by multiple parent entities.

Example:

```
Student
    \
     \
      Course
     /
Teacher
```

Deleting one Student should **not** delete the Course because it is also used by other Students and Teachers.

---

# Interview Questions

## Does Cascade create a relationship between entities?

**No.**

Relationship is created using annotations like

- `@OneToOne`
- `@OneToMany`
- `@ManyToOne`
- `@ManyToMany`

Cascade only tells JPA what operations should automatically propagate to related entities.

---

## Does Cascade create a foreign key?

**No.**

Foreign keys are created by relationship annotations such as `@JoinColumn`.

Cascade only controls persistence operations like save, update, and delete.

---

## Does Cascade work only with `@OneToOne`?

No.

It works with all relationship annotations.

```java
@OneToOne(cascade = CascadeType.ALL)

@OneToMany(cascade = CascadeType.ALL)

@ManyToOne(cascade = CascadeType.ALL)

@ManyToMany(cascade = CascadeType.ALL)
```

---

# Summary

> **Cascade means automatically performing the same persistence operation (save, update, delete, refresh, or detach) on related child entities whenever that operation is performed on the parent entity. It reduces manual code and keeps related entities synchronized.**