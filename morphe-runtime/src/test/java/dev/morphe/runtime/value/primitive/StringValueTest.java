package dev.morphe.runtime.value.primitive;

import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.kind.ValueKind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringValueTest {

  @Test
  void storesStringValue() {
    StringValue value = new StringValue("Hello");
    assertEquals("Hello", value.value());
  }

  @Test
  void reportsCorrectKind() {
    StringValue value = new StringValue("Hello");
    assertEquals(ValueKind.STRING, value.kind());
  }

  @Test
  void considersValuesWithSameValueEqual() {
    StringValue first = new StringValue("Hello");
    StringValue second = new StringValue("Hello");
    assertEquals(first, second);
  }

  @Test
  void considersValuesWithDifferentValuesNotEqual() {
    StringValue first = new StringValue("Hello");
    StringValue second = new StringValue("World");
    assertNotEquals(first, second);
  }

  @Test
  void isNotEqualToNull() {
    StringValue value = new StringValue("Hello");
    assertNotEquals(null, value);
  }

  @Test
  void isNotEqualToAnotherType() {
    StringValue value = new StringValue("Hello");
    assertNotEquals(value, "Hello");
  }

  @Test
  void producesSameHashCodeForEqualValues() {
    StringValue first = new StringValue("Hello");
    StringValue second = new StringValue("Hello");
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void producesExpectedStringRepresentation() {
    StringValue value = new StringValue("Hello");
    assertEquals("Hello", value.toString());
  }

  @Test
  void rejectsNullValue() {
    assertThrows(NullArgumentException.class, () -> new StringValue(null));
  }
}