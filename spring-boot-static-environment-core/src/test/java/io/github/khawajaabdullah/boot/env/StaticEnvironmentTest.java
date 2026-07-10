package io.github.khawajaabdullah.boot.env;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StaticEnvironmentTest {

  @AfterEach
  void reset() {
    StaticEnvironment.setEnvironment(null);
  }

  @Test
  void shouldRetrievePropertyWithStringValueWhenStaticEnvironmentIsInitialized() {
    MockEnvironment mockEnvironment = new MockEnvironment();
    mockEnvironment.setProperty("some.property.key", "someStringValue");
    StaticEnvironment.setEnvironment(mockEnvironment);

    assertThat(StaticEnvironment.getProperty("some.property.key")).isEqualTo("someStringValue");
  }

  @Test
  void shouldThrowIllegalStateExceptionOnPropertyRetrievalWithStringValueWhenStaticEnvironmentIsNotInitialized() {
    assertThatThrownBy(() -> StaticEnvironment.getProperty("some.property.key"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("StaticEnvironment has not been initialized");
  }

  @Test
  void shouldReturnNullOnPropertyRetrievalWithStringValueWhenPropertyIsNotDefined() {
    StaticEnvironment.setEnvironment(new MockEnvironment());

    assertThat(StaticEnvironment.getProperty("missing.property.key")).isNull();
  }

  @Test
  void shouldRetrievePropertyWithDefaultStringValueWhenStaticEnvironmentIsInitialized() {
    MockEnvironment mockEnvironment = new MockEnvironment();
    mockEnvironment.setProperty("some.property.key", "someStringValue");
    StaticEnvironment.setEnvironment(mockEnvironment);

    assertThat(StaticEnvironment.getProperty("some.property.key", "someDefaultStringValue"))
        .isEqualTo("someStringValue");
  }

  @Test
  void shouldThrowIllegalStateExceptionOnPropertyRetrievalWithDefaultStringValueWhenStaticEnvironmentIsNotInitialized() {
    assertThatThrownBy(() -> StaticEnvironment.getProperty("some.property.key", "someDefaultStringValue"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("StaticEnvironment has not been initialized");
  }

  @Test
  void shouldReturnDefaultStringValueOnPropertyRetrievalWithDefaultStringValueWhenPropertyIsNotDefined() {
    StaticEnvironment.setEnvironment(new MockEnvironment());

    assertThat(StaticEnvironment.getProperty("missing.property.key", "someDefaultStringValue"))
        .isEqualTo("someDefaultStringValue");
  }

  @Test
  void shouldRetrievePropertyWithReferenceTypeValueWhenStaticEnvironmentIsInitialized() {
    MockEnvironment mockEnvironment = new MockEnvironment();
    String value = "someValue";
    mockEnvironment.setProperty("some.property.key", value);
    StaticEnvironment.setEnvironment(mockEnvironment);

    assertThat(StaticEnvironment.getProperty("some.property.key", String.class))
        .isInstanceOf(String.class)
        .isEqualTo(value);
  }

  @Test
  void shouldThrowIllegalStateExceptionOnPropertyRetrievalWithReferenceTypeValueWhenStaticEnvironmentIsNotInitialized() {
    assertThatThrownBy(() -> StaticEnvironment.getProperty("some.property.key", String.class))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("StaticEnvironment has not been initialized");
  }

  @Test
  void shouldReturnNullOnPropertyRetrievalWithReferenceTypeValueWhenPropertyIsNotDefined() {
    StaticEnvironment.setEnvironment(new MockEnvironment());

    assertThat(StaticEnvironment.getProperty("missing.property.key", String.class))
        .isNull();
  }

  @Test
  void shouldRetrievePropertyWithDefaultReferenceTypeValueWhenStaticEnvironmentIsInitialized() {
    MockEnvironment mockEnvironment = new MockEnvironment();
    String value = "someValue";
    String defaultValue = "someDefaultValue";
    mockEnvironment.setProperty("some.property.key", value);
    StaticEnvironment.setEnvironment(mockEnvironment);

    assertThat(StaticEnvironment.getProperty("some.property.key", String.class, defaultValue))
        .isInstanceOf(String.class)
        .isEqualTo(value);
  }

  @Test
  void shouldThrowIllegalStateExceptionOnPropertyRetrievalWithDefaultReferenceTypeValueWhenStaticEnvironmentIsNotInitialized() {
    String defaultValue = "someDefaultValue";

    assertThatThrownBy(() -> StaticEnvironment.getProperty("some.property.key", String.class, defaultValue))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("StaticEnvironment has not been initialized");
  }

  @Test
  void shouldReturnDefaultReferenceTypeValueOnPropertyRetrievalWithDefaultReferenceTypeValueWhenPropertyIsNotDefined() {
    String defaultValue = "someDefaultValue";
    StaticEnvironment.setEnvironment(new MockEnvironment());

    assertThat(StaticEnvironment.getProperty("missing.property.key", String.class, defaultValue))
        .isInstanceOf(String.class)
        .isEqualTo(defaultValue);
  }

}
