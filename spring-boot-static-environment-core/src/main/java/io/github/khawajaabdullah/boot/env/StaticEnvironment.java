package io.github.khawajaabdullah.boot.env;

import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Static gateway to Spring's {@link Environment} for use outside the Spring-managed bean lifecycle — DTOs, plain
 * Java objects, Jackson deserializers, Hibernate types, and any class that Spring does not instantiate.
 *
 * <p>Initialized automatically at application startup by
 * {@code io.github.khawajaabdullah.boot.env.internal.StaticEnvironmentPostProcessor}
 * before any beans are created, making it safe to call from field initializers and static blocks
 * as long as the Spring context has started.
 *
 * <p>Property resolution follows Spring's standard {@link Environment} priority order:
 * <ol>
 *   <li>JVM system properties ({@code -Dkey=value})</li>
 *   <li>Environment variables</li>
 *   <li>Profile-specific application properties ({@code application-{profile}.yml})</li>
 *   <li>Default application properties ({@code application.yml})</li>
 * </ol>
 *
 * <p><strong>Thread safety:</strong> {@code setEnvironment} is called once during application startup on the main
 * thread. Subsequent reads via {@code getProperty} are safe to call from any thread.
 *
 * <p><strong>Testing:</strong> In unit tests that do not start a Spring context, call
 * {@link #setEnvironment(Environment)} directly with a {@code MockEnvironment} to initialize
 * the provider before assertions. Always reset with {@code setEnvironment(null)} in teardown
 * to avoid state leaking between tests.
 *
 * @see Environment
 * @since 1.0.0
 */
public final class StaticEnvironment {

  private static final AtomicReference<Environment> environmentAtomicReference = new AtomicReference<>();

  private StaticEnvironment() {
  }

  /**
   * Initializes the provider with the application's {@link Environment}.
   *
   * <p>Called internally by
   * {@code io.github.khawajaabdullah.boot.env.internal.StaticEnvironmentPostProcessor}
   * during the {@code ApplicationEnvironmentPreparedEvent} phase — before any beans are created.
   *
   * <p>Exposed as {@code public} solely to support unit testing without a Spring context.
   * Pass {@code null} to reset the provider during test teardown.
   *
   * @param environment the Spring {@link Environment} to bind; may be {@code null} to reset
   */
  public static void setEnvironment(Environment environment) {
    StaticEnvironment.environmentAtomicReference.set(environment);
  }

  /**
   * Returns the value of the property bound to the given key, or {@code null} if no property
   * with that key exists.
   *
   * @param key the property key; must not be {@code null}
   * @return the resolved property value, or {@code null} if not found
   * @throws IllegalStateException if called before the Spring context has started
   * @see Environment#getProperty(String)
   */
  @Nullable
  public static String getProperty(String key) {
    return getEnvironment().getProperty(key);
  }

  /**
   * Returns the value of the property bound to the given key, or {@code defaultValue} if no
   * property with that key exists.
   *
   * @param key          the property key; must not be {@code null}
   * @param defaultValue the value to return if the property is not found
   * @return the resolved property value, or {@code defaultValue} if not found
   * @throws IllegalStateException if called before the Spring context has started
   * @see Environment#getProperty(String, String)
   */
  public static String getProperty(String key, String defaultValue) {
    return getEnvironment().getProperty(key, defaultValue);
  }

  /**
   * Returns the value of the property bound to the given key, converted to the given target type,
   * or {@code null} if no property with that key exists.
   *
   * @param key        the property key; must not be {@code null}
   * @param targetType the expected type of the property value
   * @param <T>        the target type
   * @return the resolved property value converted to {@code targetType}, or {@code null} if not found
   * @throws IllegalStateException if called before the Spring context has started
   * @see Environment#getProperty(String, Class)
   */
  @Nullable
  public static <T> T getProperty(String key, Class<T> targetType) {
    return getEnvironment().getProperty(key, targetType);
  }

  /**
   * Returns the value of the property bound to the given key, converted to the given target type,
   * or {@code defaultValue} if no property with that key exists.
   *
   * @param key          the property key; must not be {@code null}
   * @param targetType   the expected type of the property value
   * @param defaultValue the value to return if the property is not found
   * @param <T>          the target type
   * @return the resolved property value converted to {@code targetType}, or {@code defaultValue} if not found
   * @throws IllegalStateException if called before the Spring context has started
   * @see Environment#getProperty(String, Class, Object)
   */
  public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
    return getEnvironment().getProperty(key, targetType, defaultValue);
  }

  /**
   * Returns the cached {@link Environment}, ensuring it has been initialized.
   *
   * @return the initialized environment
   * @throws IllegalStateException if {@link #setEnvironment(Environment)} has not been called yet
   */
  private static Environment getEnvironment() {
    Environment environment = environmentAtomicReference.get();
    if (environment == null) {
      throw new IllegalStateException(
          "StaticEnvironment has not been initialized. "
              + "Ensure spring-boot-static-environment is on the classpath "
              + "and the application context has started.");
    }
    return environment;
  }

}
