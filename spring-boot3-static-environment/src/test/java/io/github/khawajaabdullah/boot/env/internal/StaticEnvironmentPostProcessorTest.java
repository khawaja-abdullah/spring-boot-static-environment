package io.github.khawajaabdullah.boot.env.internal;

import io.github.khawajaabdullah.boot.env.StaticEnvironment;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "some.property.key=someValue")
class StaticEnvironmentPostProcessorTest {

  @Test
  void shouldRetrievePropertyFromSpringEnvironment() {
    assertThat(StaticEnvironment.getProperty("some.property.key")).isEqualTo("someValue");
  }

  @SpringBootApplication
  static class TestApp {
  }

}
