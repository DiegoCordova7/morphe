package dev.morphe.runtime.value.primitive.numeric;

import dev.morphe.runtime.kind.ValueKind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleValueTest {

  @Test
  void storesDoubleValue() {
    DoubleValue value = new DoubleValue(42.5);
    assertEquals(42.5, value.value());
  }

  @Test
  void reportsCorrectKind() {
    DoubleValue value = new DoubleValue(42.5);
    assertEquals(ValueKind.DOUBLE, value.kind());
  }

  @Test
  void considersValuesWithSameValueEqual() {
    DoubleValue first = new DoubleValue(42.5);
    DoubleValue second = new DoubleValue(42.5);
    assertEquals(first, second);
  }

  @Test
  void considersValuesWithDifferentValuesNotEqual() {
    DoubleValue first = new DoubleValue(42.5);
    DoubleValue second = new DoubleValue(10.5);
    assertNotEquals(first, second);
  }

  @Test
  void isNotEqualToNull() {
    DoubleValue value = new DoubleValue(42.5);
    assertNotEquals(null, value);
  }

  @Test
  void isNotEqualToAnotherType() {
    DoubleValue value = new DoubleValue(42.5);
    assertNotEquals(value, 42.5);
  }

  @Test
  void considersNaNValuesEqual() {
    DoubleValue first = new DoubleValue(Double.NaN);
    DoubleValue second = new DoubleValue(Double.NaN);
    assertEquals(first, second);
  }

  @Test
  void considersPositiveAndNegativeZeroNotEqual() {
    DoubleValue positiveZero = new DoubleValue(0.0);
    DoubleValue negativeZero = new DoubleValue(-0.0);
    assertNotEquals(positiveZero, negativeZero);
  }

  @Test
  void producesSameHashCodeForEqualValues() {
    DoubleValue first = new DoubleValue(42.5);
    DoubleValue second = new DoubleValue(42.5);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void producesExpectedStringRepresentation() {
    DoubleValue value = new DoubleValue(42.5);
    assertEquals("42.5", value.toString());
  }

  @Test
  void equalNaNValuesProduceSameHashCode() {
    DoubleValue first = new DoubleValue(Double.NaN);
    DoubleValue second = new DoubleValue(Double.NaN);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void positiveAndNegativeZeroProduceDifferentHashCodes() {
    DoubleValue positiveZero = new DoubleValue(0.0);
    DoubleValue negativeZero = new DoubleValue(-0.0);
    assertNotEquals(positiveZero.hashCode(), negativeZero.hashCode());
  }

  @Test
  void considersEqualDoubleAndIntegerValuesEqual() {
    DoubleValue doubleValue = new DoubleValue(42.0);
    IntegerValue integer = new IntegerValue(42);
    assertEquals(doubleValue, integer);
  }

  @Test
  void considersDifferentDoubleAndIntegerValuesNotEqual() {
    DoubleValue doubleValue = new DoubleValue(42.5);
    IntegerValue integer = new IntegerValue(42);
    assertNotEquals(doubleValue, integer);
  }

  @Test
  void producesSameHashCodeForEqualDoubleAndIntegerValues() {
    DoubleValue doubleValue = new DoubleValue(42.0);
    IntegerValue integer = new IntegerValue(42);
    assertEquals(doubleValue.hashCode(), integer.hashCode());
  }

  @Test
  void reportsNonIntegerRepresentation() {
    DoubleValue value = new DoubleValue(42.5);
    assertFalse(value.isInteger());
  }

  @Test
  void returnsValueAsDouble() {
    DoubleValue value = new DoubleValue(42.5);
    assertEquals(42.5, value.doubleValue());
  }
}