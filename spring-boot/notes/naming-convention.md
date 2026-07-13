# Naming Conventions Cheat Sheet

| Convention | Example | Used For |
|------------|---------|-----------|
| **camelCase** | `firstName`, `getUserById()` | Variables, methods, functions, object properties |
| **PascalCase** | `UserService`, `EmployeeController` | Classes, interfaces, enums, React components, TypeScript types |
| **snake_case** | `first_name`, `user_id` | Database tables/columns, SQL, Python variables & functions |
| **SCREAMING_SNAKE_CASE** | `MAX_SIZE`, `API_KEY` | Constants (`final` variables), enum constants, environment variables |
| **kebab-case** | `user-profile`, `login-page` | URLs, REST endpoints, CSS classes, route names, frontend file names |
| **lowercase** | `com.example.app` | Java package names, folders (commonly) |
| **dot.case** | `application.properties`, `user.service.ts` | Configuration files, namespaces, file names with extensions |
| **UPPERCASE** | `README`, `LICENSE` | Special project files, acronyms |
| **Train-Case** *(Rare)* | `User-Profile` | Document titles and headings |

---

## Quick Rules

- **camelCase** → Variables, methods, functions.
- **PascalCase** → Classes, interfaces, React components, TypeScript types.
- **snake_case** → Database tables/columns, SQL, Python variables/functions.
- **SCREAMING_SNAKE_CASE** → Constants, enum constants, environment variables.
- **kebab-case** → URLs, REST endpoints, CSS classes, route names.
- **lowercase** → Java package names.
- **dot.case** → Configuration files, namespaces, file names.
- **UPPERCASE** → Special project files (`README`, `LICENSE`).
- **Train-Case** → Titles and headings (rarely used in programming).

---

## Examples

```java
// camelCase
String firstName;
void getUserById() {}

// PascalCase
class UserService {}
interface PaymentService {}

// SCREAMING_SNAKE_CASE
public static final int MAX_SIZE = 100;
```

```sql
-- snake_case
CREATE TABLE user_details (
    user_id INT,
    first_name VARCHAR(100)
);
```

```css
/* kebab-case */
.user-profile {
    color: blue;
}
```

```text
// lowercase (Java package)
com.example.todo

// dot.case
application.properties
user.service.ts

// UPPERCASE
README.md
LICENSE

// Train-Case
User-Profile
```