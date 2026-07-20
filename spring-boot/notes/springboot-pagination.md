# Spring Boot Pagination with Spring Data JPA

## Table of Contents

1. Introduction
2. What is Pagination?
3. Why do we need Pagination?
4. How Pagination Works
5. Important Interfaces
    - Pageable
    - Page
    - PageRequest
6. Step-by-Step Implementation
7. Pagination Response
8. Pagination with Sorting
9. Pagination with Custom Finder Methods
10. Custom Pagination Response
11. Best Practices
12. Interview Questions

---

# 1. Introduction

Pagination is one of the most commonly used features in web applications.

Instead of fetching all records from the database at once, pagination divides the records into multiple pages.

For example, suppose your database contains **1,00,000 products**.

Without pagination:

```
Client
   │
   ▼
Database
   │
Returns 1,00,000 Products
```

Problems:

- Very slow API
- High memory consumption
- Large network traffic
- Poor user experience

Instead, return only a small number of records.

Example:

```
Page 0 → 10 Products

Page 1 → 10 Products

Page 2 → 10 Products
```

This is called Pagination.

---

# 2. What is Pagination?

Pagination means dividing a large collection of records into smaller pages.

Example:

Database contains

```
Product 1
Product 2
Product 3
.
.
.
Product 100
```

Instead of returning all 100 products,

Return

```
Page 0

Product 1

Product 2

Product 3

...

Product 10
```

Next request

```
Page 1

Product 11

Product 12

...

Product 20
```

Every request returns only a limited number of records.

---

# 3. Why do we need Pagination?

Imagine an E-Commerce website.

Database

```
5,00,000 Products
```

If every API returns all products,

Problems:

- Huge response
- Slow loading
- High RAM usage
- Slow database query
- Bad user experience

With Pagination

```
Client

↓

Request only 20 products

↓

Database returns only 20 products
```

Benefits

- Faster API
- Less Memory
- Better Performance
- Better User Experience
- Lower Network Usage

---

# 4. How Pagination Works

Suppose database contains

```
100 Products
```

Each page contains

```
10 Products
```

Then

```
Page 0

1 - 10
```

```
Page 1

11 - 20
```

```
Page 2

21 - 30
```

```
Page 3

31 - 40
```

and so on.

---

# 5. How Spring Boot Implements Pagination

Spring Boot provides built-in support using

- Pageable
- Page
- PageRequest

Flow

```
Client

↓

Controller

↓

Service

↓

Repository

↓

Database

↓

Page<Product>

↓

Client
```

---

# 6. Important Classes and Interfaces

---

## Pageable

`Pageable` is an interface.

It contains instructions about how data should be fetched.

It does NOT contain actual data.

It contains:

- Page Number
- Page Size
- Sorting Information

Think of it as:

```
Database

↓

Please give me

Page = 2

Size = 10

Sort = price descending
```

That request is represented by Pageable.

---

## Page

`Page` is also an interface.

Unlike Pageable,

Page contains

- Actual Data
- Total Pages
- Total Elements
- Current Page
- Page Size
- First Page
- Last Page

Think of it as

```
Requested Data

+

Pagination Information
```

---

## PageRequest

PageRequest is the implementation of Pageable.

Since Pageable is an interface,

We cannot create its object directly.

Wrong

```java
Pageable pageable = new Pageable();
```

Correct

```java
Pageable pageable =
PageRequest.of(0,10);
```

Internally,

```
Pageable

▲

|

PageRequest
```

---

# 7. Project Structure

```
src

 ├── controller

 │      ProductController

 │

 ├── service

 │      ProductService

 │

 ├── repository

 │      ProductRepository

 │

 ├── entity

 │      Product

 │

 └── dto
```

---

# Step 1 Create Entity

```java
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Double price;
}
```

Explanation

This entity represents one row of Product table.

---

# Step 2 Create Repository

```java
@Repository
public interface ProductRepository
extends JpaRepository<Product,Long> {

}
```

Notice

No pagination code is written.

Why?

Because JpaRepository already provides pagination methods.

---

# Step 3 Create Service

```java
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Page<Product> getProducts(
            int page,
            int size
    ){

        Pageable pageable =
                PageRequest.of(page,size);

        return repository.findAll(pageable);

    }

}
```

Let's understand each line.

---

## Line 1

```java
Pageable pageable =
PageRequest.of(page,size);
```

Suppose

```
page = 2

size = 5
```

Spring creates

```
Page Number

2

Page Size

5
```

Internally

```
LIMIT 5

OFFSET 10
```

because

```
Offset

=

Page × Size

=

2 × 5

=

10
```

Generated SQL

```sql
SELECT *

FROM product

LIMIT 5

OFFSET 10;
```

---

## Line 2

```java
repository.findAll(pageable);
```

Spring automatically reads Pageable.

Generates SQL.

Executes Query.

Returns Page<Product>.

---

# Step 4 Controller

```java
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping
    public Page<Product> getProducts(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size

    ){

        return service.getProducts(page,size);

    }

}
```

---

## Understanding Request Parameters

```
page
```

Current page number.

```
size
```

How many records should come.

Example

```
GET

/products?page=0&size=5
```

means

```
Give me first page

with

5 products
```

---

# API Examples

First Page

```
GET

/products?page=0&size=5
```

Second Page

```
GET

/products?page=1&size=5
```

Third Page

```
GET

/products?page=2&size=5
```

---

# SQL Generated by Spring

Suppose

```
Page = 0

Size = 10
```

SQL

```sql
SELECT *

FROM product

LIMIT 10

OFFSET 0;
```

---

Suppose

```
Page = 1

Size = 10
```

SQL

```sql
SELECT *

FROM product

LIMIT 10

OFFSET 10;
```

---

Suppose

```
Page = 2

Size = 10
```

SQL

```sql
SELECT *

FROM product

LIMIT 10

OFFSET 20;
```

---

Formula

```
Offset

=

Page Number

×

Page Size
```

---

# Response

Spring returns

```json
{
    "content":[
        {
            "id":1,
            "title":"Laptop",
            "price":55000
        },
        {
            "id":2,
            "title":"Phone",
            "price":25000
        }
    ],

    "totalPages":10,

    "totalElements":100,

    "size":10,

    "number":0,

    "first":true,

    "last":false,

    "numberOfElements":2,

    "empty":false
}
```

---

# Understanding Response

## content

Actual records.

---

## totalPages

Total number of pages.

Example

```
100 Products

Page Size = 10

Total Pages = 10
```

---

## totalElements

Total rows in database.

Example

```
100 Products
```

Returns

```
100
```

---

## number

Current page.

---

## size

Maximum records in one page.

---

## first

Returns

```
true
```

if current page is first page.

---

## last

Returns

```
true
```

if current page is last page.

---

## empty

Returns

```
true
```

if no records found.

---

# Pagination with Sorting

Spring allows sorting while paging.

Example

```java
Pageable pageable =
PageRequest.of(

        page,

        size,

        Sort.by("price").descending()

);
```

Generated SQL

```sql
SELECT *

FROM product

ORDER BY price DESC

LIMIT 10

OFFSET 0;
```

Ascending

```java
Sort.by("price").ascending()
```

Descending

```java
Sort.by("price").descending()
```

---

# Dynamic Sorting

Controller

```java
@GetMapping
public Page<Product> getProducts(

        @RequestParam int page,

        @RequestParam int size,

        @RequestParam String sortBy,

        @RequestParam String direction

){

    Sort sort =
            direction.equalsIgnoreCase("asc")

            ?

            Sort.by(sortBy).ascending()

            :

            Sort.by(sortBy).descending();

    Pageable pageable =
            PageRequest.of(page,size,sort);

    return repository.findAll(pageable);

}
```

Request

```
GET

/products?page=0&size=10&sortBy=price&direction=desc
```

---

# Pagination with Custom Finder Method

Repository

```java
Page<Product> findByTitleContaining(
        String keyword,
        Pageable pageable
);
```

Service

```java
Pageable pageable =
PageRequest.of(page,size);

return repository.findByTitleContaining(
        keyword,
        pageable
);
```

Generated SQL

```sql
SELECT *

FROM product

WHERE title LIKE '%phone%'

LIMIT 10

OFFSET 0;
```

---

# Creating Custom Pagination Response

Returning Page directly is acceptable, but in production applications it is common to create your own response object.

Example

```java
@Getter
@Setter
public class PageResponse<T> {

    private List<T> content;

    private int pageNumber;

    private int pageSize;

    private long totalElements;

    private int totalPages;

    private boolean last;
}
```

Service

```java
Page<Product> page =
repository.findAll(pageable);

PageResponse<Product> response =
new PageResponse<>();

response.setContent(page.getContent());
response.setPageNumber(page.getNumber());
response.setPageSize(page.getSize());
response.setTotalElements(page.getTotalElements());
response.setTotalPages(page.getTotalPages());
response.setLast(page.isLast());

return response;
```

Benefits

- Cleaner API response
- Hide unnecessary fields
- Easy to maintain
- Consistent API structure

---

# Best Practices

✅ Always return DTOs instead of entities.

✅ Validate page number (page >= 0).

✅ Validate page size (size > 0).

✅ Set a maximum page size to prevent very large responses.

Example

```java
size = Math.min(size, 100);
```

✅ Use sorting together with pagination whenever needed.

✅ Return a custom response object if you want consistent API responses.

---

# Common Mistakes

❌ Using negative page numbers.

```java
page = -1
```

❌ Using page size as 0.

```java
size = 0
```

❌ Returning entities directly when DTOs are required.

❌ Forgetting sorting when the order of records matters.

---

# Interview Questions

### 1. What is Pagination?

Pagination is the process of dividing a large number of records into smaller pages to improve performance and user experience.

---

### 2. What is Pageable?

Pageable is an interface that contains pagination instructions such as page number, page size, and sorting information.

---

### 3. What is Page?

Page is an interface that contains both the paginated data and metadata like total pages, total elements, current page, and page size.

---

### 4. Why can't we create a Pageable object?

Because Pageable is an interface. We use its implementation, `PageRequest`.

---

### 5. What is PageRequest?

PageRequest is the default implementation of the Pageable interface used to create pagination requests.

---

### 6. How is OFFSET calculated?

```
OFFSET = Page Number × Page Size
```

Example:

```
Page = 3

Size = 20

OFFSET = 60
```

---

### 7. Which repository method supports pagination?

```java
findAll(Pageable pageable)
```

You can also use custom finder methods that accept a `Pageable` parameter.

---

# Summary

- **Pagination** divides large datasets into smaller pages.
- **Pageable** defines *how* data should be fetched (page number, page size, sort).
- **PageRequest** is the concrete implementation of `Pageable`.
- **Page** contains the fetched data along with pagination metadata.
- Spring Data JPA automatically converts a `Pageable` into SQL using `LIMIT` and `OFFSET`.
- Pagination can be combined with sorting and custom query methods.
- For production APIs, prefer returning DTOs wrapped in a custom pagination response instead of exposing entities or the raw `Page` object.