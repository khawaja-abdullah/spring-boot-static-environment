package io.github.khawajaabdullah.boot.env.internal;

import io.github.khawajaabdullah.boot.env.StaticEnvironment;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource("classpath:/test-application.properties")
class StaticEnvironmentPostProcessorTest {

  @Test
  void shouldRetrievePropertyWithStringValueFromSpringEnvironment() {
    assertThat(StaticEnvironment.getProperty("some.string-valued.property.key")).isEqualTo("someStringValue");
  }

  @Test
  void shouldRetrievePropertyWithDefaultStringValueFromSpringEnvironment() {
    assertThat(StaticEnvironment.getProperty("missing.string-valued.property.key", "someDefaultStringValue"))
        .isEqualTo("someDefaultStringValue");
  }

  @Test
  void shouldRetrievePropertyWithReferenceTypeValueFromSpringEnvironment() {
    assertThat(StaticEnvironment.getProperty("some.reference-type-valued.property.key", String.class))
        .isInstanceOf(String.class)
        .isEqualTo("someReferenceTypeValue");
  }

  @Test
  void shouldRetrievePropertyWithDefaultReferenceTypeValueFromSpringEnvironment() {
    assertThat(StaticEnvironment.getProperty("missing.reference-type-valued.property.key", String.class, "someDefaultReferenceTypeValue"))
        .isInstanceOf(String.class)
        .isEqualTo("someDefaultReferenceTypeValue");
  }

  @SpringBootApplication(scanBasePackages = "io.github.khawajaabdullah")
  static class TestApp {
  }

}
