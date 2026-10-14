package dev.morphe.runtime.value.primitive;

import dev.morphe.runtime.kind.ValueKind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BooleanValueTest {

  @Test
  void storesBooleanValue() {
    BooleanValue value = new BooleanValue(true);
    assertTrue(value.value());
  }

  @Test
  void reportsCorrectKind() {
    BooleanValue value = new BooleanValue(true);
    assertEquals(ValueKind.BOOLEAN, value.kind());
  }

  @Test
  void trueFactoryReturnsCanonicalTrueInstance() {
    assertSame(BooleanValue.TRUE, BooleanValue.of(true));
  }

  @Test
  void falseFactoryReturnsCanonicalFalseInstance() {
    assertSame(BooleanValue.FALSE, BooleanValue.of(false));
  }

  @Test
  void canonicalTrueInstanceContainsTrueValue() {
    assertEquals(true, BooleanValue.TRUE.value());
  }

  @Test
  void canonicalFalseInstanceContainsFalseValue() {
    assertFalse(BooleanValue.FALSE.value());
  }

  @Test
  void considersValuesWithSameValueEqual() {
    BooleanValue first = new BooleanValue(true);
    BooleanValue second = new BooleanValue(true);
    assertEquals(first, second);
  }

  @Test
  void considersValuesWithDifferentValuesNotEqual() {
    BooleanValue first = new BooleanValue(true);
    BooleanValue second = new BooleanValue(false);
    assertNotEquals(first, second);
  }

  @Test
  void isNotEqualToNull() {
    BooleanValue value = new BooleanValue(true);
    assertNotEquals(null, value);
  }

  @Test
  void isNotEqualToAnotherType() {
    BooleanValue value = new BooleanValue(true);
    assertNotEquals(value, true);
  }

  @Test
  void producesSameHashCodeForEqualValues() {
    BooleanValue first = new BooleanValue(true);
    BooleanValue second = new BooleanValue(true);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void producesExpectedStringRepresentation() {
    BooleanValue value = new BooleanValue(true);
    assertEquals("true", value.toString());
  }
}