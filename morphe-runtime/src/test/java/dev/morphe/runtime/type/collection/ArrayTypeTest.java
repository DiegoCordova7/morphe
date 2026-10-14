package dev.morphe.runtime.type.collection;

import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.kind.TypeKind;
import dev.morphe.runtime.type.primitive.IntegerType;
import dev.morphe.runtime.type.primitive.StringType;
import dev.morphe.runtime.value.collection.ArrayValue;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.runtime.value.primitive.NullValue;
import dev.morphe.runtime.value.primitive.StringValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArrayTypeTest {

  private final IntegerType integerType = IntegerType.INSTANCE;
  private final ArrayType type = new ArrayType(integerType);

  @Test
  void storesElementType() {
    assertEquals(integerType, type.elementType());
  }

  @Test
  void reportsCorrectName() {
    assertEquals("Integer[]", type.name());
  }

  @Test
  void reportsCorrectKind() {
    assertEquals(TypeKind.ARRAY, type.kind());
  }

  @Test
  void acceptsCompatibleArrayValue() {
    ArrayValue value = new ArrayValue(
        type, List.of(new IntegerValue(1), new IntegerValue(2))
    );
    assertTrue(type.accepts(value));
  }

  @Test
  void acceptsEmptyArray() {
    ArrayValue value = new ArrayValue(type, List.of());
    assertTrue(type.accepts(value));
  }

  @Test
  void rejectsNonArrayValue() {
    assertFalse(type.accepts(new IntegerValue(42)));
    assertFalse(type.accepts(new StringValue("42")));
    assertFalse(type.accepts(new BooleanValue(true)));
  }

  @Test
  void rejectsNullValue() {
    assertFalse(type.accepts(null));
  }

  @Test
  void rejectsArrayWithDifferentArrayType() {
    ArrayType stringArrayType = new ArrayType(StringType.INSTANCE);
    ArrayValue value = new ArrayValue(
        stringArrayType, List.of(new StringValue("Hello"))
    );
    assertFalse(type.accepts(value));
  }

  @Test
  void rejectsNullElementType() {
    assertThrows(NullArgumentException.class, () -> new ArrayType(null));
  }

  @Test
  void considersArrayTypesWithEqualElementTypesEqual() {
    ArrayType first = new ArrayType(IntegerType.INSTANCE);
    ArrayType second = new ArrayType(IntegerType.INSTANCE);
    assertEquals(first, second);
  }

  @Test
  void considersArrayTypesWithDifferentElementTypesNotEqual() {
    ArrayType integerArray = new ArrayType(IntegerType.INSTANCE);
    ArrayType stringArray = new ArrayType(StringType.INSTANCE);
    assertNotEquals(integerArray, stringArray);
  }

  @Test
  void isNotEqualToNull() {
    assertNotEquals(null, type);
  }

  @Test
  void isNotEqualToAnotherType() {
    assertNotEquals(type, IntegerType.INSTANCE);
  }

  @Test
  void producesSameHashCodeForEqualArrayTypes() {
    ArrayType first = new ArrayType(IntegerType.INSTANCE);
    ArrayType second = new ArrayType(IntegerType.INSTANCE);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void producesExpectedStringRepresentation() {
    assertEquals("Integer[]", type.toString());
  }

  @Test
  void supportsMultidimensionalArrayTypes() {
    ArrayType integerMatrixType = new ArrayType(type);
    assertEquals("Integer[][]", integerMatrixType.name());
    assertEquals(TypeKind.ARRAY, integerMatrixType.kind());
    assertEquals(type, integerMatrixType.elementType());
  }

  @Test
  void acceptsNullValue() {
    assertTrue(type.accepts(new NullValue()));
  }
}