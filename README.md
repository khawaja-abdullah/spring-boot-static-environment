# spring-boot-static-environment

Static access to Spring's `Environment` for classes outside the Spring-managed bean lifecycle — DTOs, plain Java objects, Jackson deserializers, Hibernate types, and anything else Spring does not instantiate.

```java
public class SomeDto {
    private final String somePropertyValue = StaticEnvironment.getProperty("some.property.key");
}
```

---

## The problem

Spring's `@Value` and `@ConfigurationProperties` only work on beans. The moment you need a property value in a class that Spring does not manage, your options are ugly — pass it as a constructor argument, reach for a static context holder, or restructure your design. `spring-boot-static-environment` gives you a clean static API that reads from the same `Environment` Spring uses, with full profile and property source resolution.

---

## Installation

**Spring Boot 3.x**

```xml
<dependency>
    <groupId>io.github.khawaja-abdullah</groupId>
    <artifactId>spring-boot-static-environment</artifactId>
    <version>${version}</version>
</dependency>
```

No additional configuration required. The provider initializes automatically before any beans are created.

---

## Usage

```java
// String property
String somePropertyValue = StaticEnvironment.getProperty("some.property.key");
```

Property resolution follows Spring's standard priority order:

1. JVM system properties (`-Dkey=value`)
2. Environment variables
3. Profile-specific properties (`application-{profile}.yml`)
4. Default properties (`application.yml`)

---

## Requirements

| | |
|---|---|
| Java | 17+ |
| Spring Boot | 3.x |

---

## Testing

In unit tests that do not start a Spring context, initialize the provider manually with a `MockEnvironment`:

```java
@BeforeEach
void setup() {
    MockEnvironment mockEnvironment = new MockEnvironment();
    mockEnvironment.setProperty("some.property.key", "someValue");
    StaticEnvironment.setEnvironment(mockEnvironment);
}

@AfterEach
void teardown() {
    StaticEnvironment.setEnvironment(null);
}

@Test
void resolvesProperty() {
    assertThat(StaticEnvironment.getProperty("some.property.key")).isEqualTo("someValue");
}
```

---

## How it works

`StaticEnvironmentPostProcessor` implements Spring Boot's `EnvironmentPostProcessor` interface and is registered via `META-INF/spring.factories`. It fires during the `ApplicationEnvironmentPreparedEvent` phase — before any beans are created — and passes the fully prepared `Environment` to `StaticEnvironment` for static access.

---

## License

[MIT](LICENSE)