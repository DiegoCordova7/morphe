package dev.morphe.runtime.value.primitive;

import dev.morphe.runtime.kind.ValueKind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class NullValueTest {

  @Test
  void reportsCorrectKind() {
    NullValue value = new NullValue();
    assertEquals(ValueKind.NULL, value.kind());
  }

  @Test
  void considersAllNullValuesEqual() {
    NullValue first = new NullValue();
    NullValue second = new NullValue();
    assertEquals(first, second);
  }

  @Test
  void producesSameHashCodeForAllNullValues() {
    NullValue first = new NullValue();
    NullValue second = new NullValue();
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void isNotEqualToNull() {
    NullValue value = new NullValue();
    assertNotEquals(null, value);
  }

  @Test
  void isNotEqualToAnotherType() {
    NullValue value = new NullValue();
    assertNotEquals(value, "null");
  }

  @Test
  void producesExpectedStringRepresentation() {
    NullValue value = new NullValue();
    assertEquals("null", value.toString());
  }
}