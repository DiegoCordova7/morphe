package dev.morphe.runtime.value.collection;

import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.exception.type.TypeMismatchException;
import dev.morphe.runtime.exception.value.NullElementValueException;
import dev.morphe.runtime.kind.ValueKind;
import dev.morphe.runtime.type.collection.ArrayType;
import dev.morphe.runtime.type.primitive.IntegerType;
import dev.morphe.runtime.type.primitive.StringType;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.StringValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrayValueTest {

  private final ArrayType integerArrayType = new ArrayType(IntegerType.INSTANCE);

  @Test
  void storesArrayType() {
    ArrayValue value = createValidArray();
    assertEquals(integerArrayType, value.type());
  }

  @Test
  void storesElementValues() {
    List<IValue> values = List.of(new IntegerValue(1), new IntegerValue(2));
    ArrayValue value = new ArrayValue(integerArrayType, values);
    assertEquals(values, value.values());
  }

  @Test
  void reportsCorrectKind() {
    ArrayValue value = createValidArray();
    assertEquals(ValueKind.ARRAY, value.kind());
  }

  @Test
  void reportsCorrectSize() {
    ArrayValue value = new ArrayValue(integerArrayType, List.of(new IntegerValue(1), new IntegerValue(2)));
    assertEquals(2, value.size());
  }

  @Test
  void reportsEmptyArray() {
    ArrayValue value = new ArrayValue(integerArrayType, List.of());
    assertTrue(value.isEmpty());
    assertEquals(0, value.size());
  }

  @Test
  void reportsNonEmptyArray() {
    ArrayValue value = createValidArray();
    assertFalse(value.isEmpty());
  }

  @Test
  void rejectsNullType() {
    assertThrows(NullArgumentException.class, () -> new ArrayValue(null, List.of()));
  }

  @Test
  void rejectsNullValues() {
    assertThrows(NullArgumentException.class, () -> new ArrayValue(integerArrayType, null));
  }

  @Test
  void rejectsNullElement() {
    List<IValue> values = new ArrayList<>();
    values.add(null);
    assertThrows(NullElementValueException.class, () -> new ArrayValue(integerArrayType, values));
  }

  private ArrayValue createValidArray() {
    return new ArrayValue(integerArrayType, List.of(new IntegerValue(1), new IntegerValue(2)));
  }

  @Test
  void createsDefensiveCopyOfValues() {
    List<IValue> values = new ArrayList<>();
    values.add(new IntegerValue(1));
    ArrayValue value = new ArrayValue(integerArrayType, values);
    values.add(new IntegerValue(2));
    assertEquals(1, value.size());
  }

  @Test
  void exposesImmutableValuesList() {
    ArrayValue value = createValidArray();
    assertThrows(
        UnsupportedOperationException.class,
        () -> value.values().add(new IntegerValue(3))
    );
  }

  @Test
  void considersArraysWithSameTypeAndValuesEqual() {
    ArrayValue first = new ArrayValue(integerArrayType, List.of(new IntegerValue(1), new IntegerValue(2)));
    ArrayValue second = new ArrayValue(integerArrayType, List.of(new IntegerValue(1), new IntegerValue(2)));
    assertEquals(first, second);
  }

  @Test
  void considersArraysWithDifferentValuesNotEqual() {
    ArrayValue first = new ArrayValue(integerArrayType, List.of(new IntegerValue(1), new IntegerValue(2)));
    ArrayValue second = new ArrayValue(integerArrayType, List.of(new IntegerValue(1), new IntegerValue(3)));
    assertNotEquals(first, second);
  }

  @Test
  void considersArraysWithDifferentTypesNotEqual() {
    ArrayType stringArrayType = new ArrayType(StringType.INSTANCE);
    ArrayValue first = new ArrayValue(integerArrayType, List.of(new IntegerValue(1)));
    ArrayValue second = new ArrayValue(stringArrayType, List.of(new StringValue("1")));
    assertNotEquals(first, second);
  }

  @Test
  void isNotEqualToNull() {
    ArrayValue value = createValidArray();
    assertNotEquals(null, value);
  }

  @Test
  void isNotEqualToAnotherType() {
    ArrayValue value = createValidArray();
    assertNotEquals(value, List.of(new IntegerValue(1), new IntegerValue(2)));
  }

  @Test
  void producesSameHashCodeForEqualArrays() {
    ArrayValue first = new ArrayValue(
        integerArrayType, List.of(new IntegerValue(1), new IntegerValue(2))
    );
    ArrayValue second = new ArrayValue(
        integerArrayType, List.of(new IntegerValue(1), new IntegerValue(2))
    );
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void producesExpectedStringRepresentation() {
    ArrayValue value = createValidArray();
    assertEquals("[1, 2]", value.toString());
  }
}