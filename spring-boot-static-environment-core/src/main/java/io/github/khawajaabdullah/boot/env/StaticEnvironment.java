package io.github.khawajaabdullah.boot.env;

import org.springframework.core.env.Environment;

/**
 * Static gateway to Spring's {@link Environment} for use outside the Spring-managed bean lifecycle — DTOs, plain
 * Java objects, Jackson deserializers, Hibernate types, and any class that Spring does not instantiate.
 *
 * <p>Initialized automatically at application startup before any beans are created, making it safe to call from
 * field initializers and static blocks as long as the Spring context has started.
 *
 * <p>Example usage:
 * <pre>{@code
 * public class SomeDto {
 *     private final String someValue = StaticEnvironment.getProperty("some.property.key");
 * }
 * }</pre>
 *
 * <p><strong>Thread safety:</strong> {@code setEnvironment} is called once during application startup on the main
 * thread. Subsequent reads via {@code getProperty} are safe to call from any thread.
 *
 * <p><strong>Testing:</strong> In unit tests that do not start a Spring context, call {@link #setEnvironment(Environment)}
 * directly with a {@code MockEnvironment} to initialize the provider before assertions.
 *
 * @since 1.0.0
 */
public final class StaticEnvironment {

  private static Environment environment;

  private StaticEnvironment() {
  }

  /**
   * Initializes the provider with the application's {@link Environment}.
   *
   * <p>Called internally during the {@code ApplicationEnvironmentPreparedEvent} phase.
   *
   * @param environment the Spring {@link Environment} to bind to this provider
   */
  public static void setEnvironment(Environment environment) {
    StaticEnvironment.environment = environment;
  }

  /**
   * Returns the value of the property bound to the given key, or {@code null} if no property with that key exists.
   *
   * <p>Property value resolution follows Spring's standard {@link Environment} priority order:
   * <ol>
   *   <li>JVM system properties ({@code -Dkey=value})</li>
   *   <li>Environment variables</li>
   *   <li>Profile-specific application properties ({@code application-{profile}.yml})</li>
   *   <li>Default application properties ({@code application.yml})</li>
   * </ol>
   *
   * @param key the property key; must not be {@code null}
   * @return the resolved property value, or {@code null} if not found
   * @throws IllegalStateException if called before the Spring context has started and the provider has not been
   *                               initialized
   * @see Environment#getProperty(String)
   */
  public static String getProperty(String key) {
    checkInitialized();
    return environment.getProperty(key);
  }

  private static void checkInitialized() {
    if (environment == null) {
      throw new IllegalStateException(
          "StaticEnvironment has not been initialized. Ensure spring-boot-static-environment is on the classpath and the application context has started.");
    }
  }

}
