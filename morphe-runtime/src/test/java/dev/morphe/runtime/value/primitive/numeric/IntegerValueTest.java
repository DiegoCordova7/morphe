package dev.morphe.runtime.value.primitive.numeric;

import dev.morphe.runtime.kind.ValueKind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntegerValueTest {

  @Test
  void storesIntegerValue() {
    IntegerValue value = new IntegerValue(42);
    assertEquals(42, value.value());
  }

  @Test
  void reportsCorrectKind() {
    IntegerValue value = new IntegerValue(42);
    assertEquals(ValueKind.INTEGER, value.kind());
  }

  @Test
  void considersValuesWithSameValueEqual() {
    IntegerValue first = new IntegerValue(42);
    IntegerValue second = new IntegerValue(42);
    assertEquals(first, second);
  }

  @Test
  void considersValuesWithDifferentValuesNotEqual() {
    IntegerValue first = new IntegerValue(42);
    IntegerValue second = new IntegerValue(10);
    assertNotEquals(first, second);
  }

  @Test
  void considersEqualIntegerAndDoubleValuesEqual() {
    IntegerValue integer = new IntegerValue(42);
    DoubleValue doubleValue = new DoubleValue(42.0);
    assertEquals(integer, doubleValue);
  }

  @Test
  void considersDifferentIntegerAndDoubleValuesNotEqual() {
    IntegerValue integer = new IntegerValue(42);
    DoubleValue doubleValue = new DoubleValue(42.5);
    assertNotEquals(integer, doubleValue);
  }

  @Test
  void maintainsNumericEqualitySymmetryWithDoubleValues() {
    IntegerValue integer = new IntegerValue(42);
    DoubleValue doubleValue = new DoubleValue(42.0);
    assertEquals(integer, doubleValue);
    assertEquals(doubleValue, integer);
  }

  @Test
  void producesSameHashCodeForEqualValues() {
    IntegerValue first = new IntegerValue(42);
    IntegerValue second = new IntegerValue(42);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void producesSameHashCodeForEqualIntegerAndDoubleValues() {
    IntegerValue integer = new IntegerValue(42);
    DoubleValue doubleValue = new DoubleValue(42.0);
    assertEquals(integer.hashCode(), doubleValue.hashCode());
  }

  @Test
  void isNotEqualToNull() {
    IntegerValue value = new IntegerValue(42);
    assertNotEquals(null, value);
  }

  @Test
  void isNotEqualToAnotherType() {
    IntegerValue value = new IntegerValue(42);
    assertNotEquals(value, 42);
  }

  @Test
  void producesExpectedStringRepresentation() {
    IntegerValue value = new IntegerValue(42);
    assertEquals("42", value.toString());
  }

  @Test
  void returnsValueAsDouble() {
    IntegerValue value = new IntegerValue(42);
    assertEquals(42.0, value.doubleValue());
  }

  @Test
  void reportsIntegerRepresentation() {
    IntegerValue value = new IntegerValue(42);
    assertTrue(value.isInteger());
  }
}