package io.github.khawajaabdullah.boot.env;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

class StaticEnvironmentTest {

  @AfterEach
  void reset() {
    StaticEnvironment.setEnvironment(null);
  }

  @Test
  void shouldRetrievePropertyWhenStaticEnvironmentIsInitialized() {
    MockEnvironment mockEnvironment = new MockEnvironment();
    mockEnvironment.setProperty("some.property.key", "someValue");
    StaticEnvironment.setEnvironment(mockEnvironment);

    assertThat(StaticEnvironment.getProperty("some.property.key")).isEqualTo("someValue");
  }

  @Test
  void shouldThrowIllegalArgumentExceptionOnPropertyRetrievalWhenStaticEnvironmentIsNotInitialized() {
    Assertions.assertThatThrownBy(() -> StaticEnvironment.getProperty("some.property.key"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("StaticEnvironment has not been initialized");
  }

  @Test
  void shouldReturnNullOnPropertyRetrievalWhenPropertyIsNotDefined() {
    StaticEnvironment.setEnvironment(new MockEnvironment());

    assertThat(StaticEnvironment.getProperty("missing.property.key")).isNull();
  }

}
