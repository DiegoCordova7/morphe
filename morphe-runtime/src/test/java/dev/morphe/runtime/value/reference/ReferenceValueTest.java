package dev.morphe.runtime.value.reference;

import dev.morphe.runtime.kind.ValueKind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ReferenceValueTest {

  @Test
  void storesHeapAddress() {
    ReferenceValue reference = new ReferenceValue(42);
    assertEquals(42, reference.address());
  }

  @Test
  void reportsCorrectKind() {
    ReferenceValue reference = new ReferenceValue(42);
    assertEquals(ValueKind.REFERENCE, reference.kind());
  }

  @Test
  void considersReferencesWithSameAddressEqual() {
    ReferenceValue first = new ReferenceValue(42);
    ReferenceValue second = new ReferenceValue(42);
    assertEquals(first, second);
  }

  @Test
  void considersReferencesWithDifferentAddressesNotEqual() {
    ReferenceValue first = new ReferenceValue(42);
    ReferenceValue second = new ReferenceValue(10);
    assertNotEquals(first, second);
  }

  @Test
  void isNotEqualToNull() {
    ReferenceValue reference = new ReferenceValue(42);
    assertNotEquals(null, reference);
  }

  @Test
  void isNotEqualToAnotherType() {
    ReferenceValue reference = new ReferenceValue(42);
    assertNotEquals(reference, 42);
  }

  @Test
  void producesSameHashCodeForEqualReferences() {
    ReferenceValue first = new ReferenceValue(42);
    ReferenceValue second = new ReferenceValue(42);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void producesExpectedStringRepresentation() {
    ReferenceValue reference = new ReferenceValue(42);
    assertEquals("Reference(42)", reference.toString());
  }
}