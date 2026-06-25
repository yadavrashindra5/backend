# Spring Boot Starter
A Spring Boot Starter is a pre-configured dependency that bundles together a set of libraries commonly used for a specific feature. Instead of adding many individual dependencies yourself, you add a single starter dependency, and Spring Boot brings in everything needed.

Think of it like a kit
- ***Without a starter:*** You choose every library individually.
- ***With a starter:*** You choose the feature, and Spring Boot selects compatible libraries for you.

# Why do we need Spring Boot Starters?
Imagine you're building a REST API.

Without a starter, you would need to add dependencies like:

- Spring Core
- Spring MVC
- Jackson (JSON serialization)
- Validation API
- Embedded Tomcat
- Logging
- Several transitive dependencies

***This becomes difficult because:***

- You need to know every required dependency.
- You must ensure compatible versions.
- Version conflicts can occur.

***Spring Boot solves this by providing:***

```aiignore
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```
That's it.

***Spring Boot downloads all required libraries automatically.***

# What are Spring Boot Profiles?
A profile allows you to have different configurations for different environments, such as:
- Development (dev)
- Test (test)
- Staging (staging)
- Production (prod)

Instead of changing configuration files manually before deployment, you create separate configuration and tell spring boot which one to use.

# Why do we need Profiles?
Imagine your application connects to different databases in different environments.

- Development:
```
spring.datasource.url=jdbc:mysql://localhost:3306/dev_db
```
- Production:
```aiignore
spring.datasource.url=jdbc:mysql://prod-server:3306/prod_db
```

Without profiles, you would need to change the configuration file every time you switch environments, which is error-prone.
With profiles, Spring Boot automatically loads the appropriate configuration based on the active profile.

# How to create profile specific configuration files
- application.properties 
- application-dev.properties 
- application-test.properties 
- application-prod.properties

***Example***
***application-dev.properties***
```aiignore
spring.datasource.url=jdbc:mysql://localhost:3306/dev_db
spring.datasource.username=root
```

***application-prod.properties***
```aiignore
spring.datasource.url=jdbc:mysql://prod-server:3306/prod_db
spring.datasource.username=prod_user
```

# Activating a Profile
***Method 1: In application.properties***
```aiignore
spring.profiles.active=dev
```

Now Spring Boot loads:
```aiignore
application.properties
+
application-dev.properties
```

Properties in ```application-dev.properties``` override properties in ```application.properties```.if the same key exists.

***Method 2:Command Line***
```aiignore
java -jar app.jar --spring.profiles.active=prod
```

# Activating Multiple Profiles
You can activate more than one profile.
```aiignore
spring.profiles.active=dev,cloud

```

or 

```aiignore
java -jar app.jar --spring.profiles.active=dev,cloud
```
# What is @Profile?
***@Profile*** is used to tell spring:
- create this bean only if the specified profile is active.

A profile represents an environment, such as:
- dev (Development)
- test (Testing)
- prod (Production)

This allows the same application to behave differently in different environments without changing the code.

***Example***
Suppose application sends notifications.

During development, you don't want to send real notifications, so you use a fake service.

During production, you want to send real notifications.

***Development Services***
```aiignore
@Service
@Profile("dev")
public class FakeNotificationService {

    public void send() {
        System.out.println("Fake notification sent");
    }
}
```

***Production Services***
```aiignore
@Service
@Profile("prod")
public class RealNotificationService {

    public void send() {
        System.out.println("Real notification sent");
    }
}
```
If Active Profile is ```dev```,
```spring.profiles.active=dev```

Spring creates: FakeNotificationService and ignores RealNotificationService.

If Active Profile is ```prod```,
```spring.profiles.active=prod```

Spring creates: RealNotificationService and ignores FakeNotificationService.

# Reading Data from `application.properties` in Spring Boot using `@Value` annotation
use `@Value` when you need to read one or two properties.
***application.properties***
```aiignore
app.name=Employee Management System
```
```aiignore
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppInfo {

    @Value("${app.name}")
    private String appName;

    public void print() {
        System.out.println(appName);
    }
}
```

# Reading Data from `application.properties` in Spring Boot using `@ConfigurationProperties` annotation
When you need to read more than one property,`@ConfigurationProperties` is the best option.

***application.properties***
```aiignore
app.name=Employee Management System
app.version=1.0
app.author=Rashindra
```

```aiignore
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String name;
    private String version;
    private String author;

    // Getters and Setters
}
```

Spring maps the properties automatically.
| Property      | Field     |
| ------------- | --------- |
| `app.name`    | `name`    |
| `app.version` | `version` |
| `app.author`  | `author`  |


