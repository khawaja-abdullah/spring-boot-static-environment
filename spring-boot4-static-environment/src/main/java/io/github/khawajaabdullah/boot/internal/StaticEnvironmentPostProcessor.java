package io.github.khawajaabdullah.boot.internal;

import io.github.khawajaabdullah.boot.env.StaticEnvironment;
import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Binds Spring's {@link ConfigurableEnvironment} to {@link StaticEnvironment} early in the application lifecycle —
 * before any beans are created.
 *
 * <p>Registered automatically via {@code META-INF/spring.factories}:
 * <pre>{@code
 * org.springframework.boot.EnvironmentPostProcessor=\
 *   io.github.khawajaabdullah.boot.internal.StaticEnvironmentPostProcessor
 * }</pre>
 *
 * <p>This processor fires during the {@code ApplicationEnvironmentPreparedEvent} phase, which means
 * {@link StaticEnvironment#getProperty(String)} is safe to call from any code that runs after the Spring context has
 * started — including field initializers on non-bean classes instantiated post-startup.
 *
 * <p>No manual registration or configuration is required. Adding
 * {@code spring-boot4-static-environment} to your classpath is sufficient.
 *
 * @see StaticEnvironment
 * @see EnvironmentPostProcessor
 * @since 1.2.0
 */
public final class StaticEnvironmentPostProcessor implements EnvironmentPostProcessor {

  /**
   * Passes the fully prepared {@link ConfigurableEnvironment} — including all property sources, active profiles,
   * and system properties — to {@link StaticEnvironment} for static access.
   *
   * @param environment the environment to bind to {@link StaticEnvironment}
   * @param application the running {@link SpringApplication}; not used directly
   */
  @Override
  public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
    StaticEnvironment.setEnvironment(environment);
  }

}
