package dev.morphe.vm.operations;

import dev.morphe.runtime.type.collection.ArrayType;
import dev.morphe.runtime.type.primitive.IntegerType;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.collection.ArrayValue;
import dev.morphe.runtime.value.primitive.NullValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.runtime.value.reference.ReferenceValue;
import dev.morphe.vm.ExecutionContext;
import dev.morphe.vm.exception.argument.InvalidArrayDimensionsException;
import dev.morphe.vm.exception.argument.NegativeArrayDimensionException;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.execution.InvalidArrayIndexException;
import dev.morphe.vm.exception.internal.type.ExpectedArrayValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayOperationsTest {

  private ArrayType integerArrayType() {
    return new ArrayType(IntegerType.INSTANCE);
  }

  private ArrayType integerMatrixType() {
    return new ArrayType(new ArrayType(IntegerType.INSTANCE));
  }

  private ReferenceValue arrayReference(ExecutionContext context, int size) {
    return ArrayOperations.newArray(context, integerArrayType(), size);
  }

  @Test
  void shouldCreateArrayOnHeap() {
    ExecutionContext context = new ExecutionContext();
    ArrayType type = integerArrayType();
    ReferenceValue reference = ArrayOperations.newArray(context, type, 3);
    IValue value = context.heap().load(reference.address());
    assertInstanceOf(ArrayValue.class, value);
    ArrayValue array = (ArrayValue) value;
    assertEquals(type, array.type());
    assertEquals(3, array.size());
  }

  @Test
  void shouldInitializeArrayElementsWithNullValue() {
    ExecutionContext context = new ExecutionContext();
    ReferenceValue reference = arrayReference(context, 3);
    ArrayValue array = (ArrayValue) context.heap().load(reference.address());
    assertEquals(3, array.values().size());
    assertTrue(array.values().stream().allMatch(NullValue.class::isInstance));
  }

  @Test
  void shouldCreateEmptyArray() {
    ExecutionContext context = new ExecutionContext();
    ReferenceValue reference = arrayReference(context, 0);
    ArrayValue array = (ArrayValue) context.heap().load(reference.address());
    assertEquals(0, array.size());
    assertTrue(array.values().isEmpty());
  }

  @Test
  void shouldRejectNullContextWhenCreatingArray() {
    assertThrows(
        NullArgumentException.class,
        () -> ArrayOperations.newArray(null, integerArrayType(), 3)
    );
  }

  @Test
  void shouldRejectNullTypeWhenCreatingArray() {
    assertThrows(
        NullArgumentException.class,
        () -> ArrayOperations.newArray(new ExecutionContext(), null, 3)
    );
  }

  @Test
  void shouldRejectNegativeArraySize() {
    assertThrows(
        NegativeArrayDimensionException.class,
        () -> ArrayOperations.newArray(new ExecutionContext(), integerArrayType(), -1)
    );
  }

  @Test
  void shouldCreateMultidimensionalArray() {
    ExecutionContext context = new ExecutionContext();
    ReferenceValue reference = ArrayOperations.newMultiArray(context, integerMatrixType(), new int[] {2, 3});
    ArrayValue outer = (ArrayValue) context.heap().load(reference.address());
    assertEquals(2, outer.size());
    assertEquals(2, outer.values().size());
    for (IValue value : outer.values()) {
      assertInstanceOf(ReferenceValue.class, value);
      ReferenceValue innerReference = (ReferenceValue) value;
      ArrayValue inner = (ArrayValue) context.heap().load(innerReference.address());
      assertEquals(3, inner.size());
      assertTrue(inner.values().stream().allMatch(NullValue.class::isInstance));
    }
  }

  @Test
  void shouldAllocateNestedArraysIndependently() {
    ExecutionContext context = new ExecutionContext();
    ReferenceValue reference = ArrayOperations.newMultiArray(context, integerMatrixType(), new int[] {2, 3});
    ArrayValue outer = (ArrayValue) context.heap().load(reference.address());
    ReferenceValue first = (ReferenceValue) outer.values().get(0);
    ReferenceValue second = (ReferenceValue) outer.values().get(1);
    assertNotEquals(first.address(), second.address());
  }

  @Test
  void shouldCreateOneDimensionalMultidimensionalArray() {
    ExecutionContext context = new ExecutionContext();
    ReferenceValue reference = ArrayOperations.newMultiArray(context, integerArrayType(), new int[] {4});
    ArrayValue array = (ArrayValue) context.heap().load(reference.address());
    assertEquals(4, array.size());
    assertTrue(array.values().stream().allMatch(NullValue.class::isInstance));
  }

  @Test
  void shouldCreateMultidimensionalArrayWithZeroDimension() {
    ExecutionContext context = new ExecutionContext();
    ReferenceValue reference = ArrayOperations.newMultiArray(context, integerMatrixType(), new int[] {0, 3});
    ArrayValue outer = (ArrayValue) context.heap().load(reference.address());
    assertEquals(0, outer.size());
  }

  @Test
  void shouldRejectNullContextWhenCreatingMultidimensionalArray() {
    assertThrows(
        NullArgumentException.class, () -> ArrayOperations.newMultiArray(null, integerMatrixType(), new int[] {2, 3})
    );
  }

  @Test
  void shouldRejectNullTypeWhenCreatingMultidimensionalArray() {
    assertThrows(
        NullArgumentException.class, () -> ArrayOperations.newMultiArray(new ExecutionContext(), null, new int[] {2, 3})
    );
  }

  @Test
  void shouldRejectNullDimensionsWhenCreatingMultidimensionalArray() {
    assertThrows(
        NullArgumentException.class, () -> ArrayOperations.newMultiArray(new ExecutionContext(), integerMatrixType(), null)
    );
  }

  @Test
  void shouldRejectEmptyDimensions() {
    assertThrows(
        InvalidArrayDimensionsException.class,
        () -> ArrayOperations.newMultiArray(new ExecutionContext(), integerMatrixType(), new int[] {})
    );
  }

  @Test
  void shouldRejectNegativeNestedDimension() {
    assertThrows(
        NegativeArrayDimensionException.class,
        () -> ArrayOperations.newMultiArray(new ExecutionContext(), integerMatrixType(), new int[] {2, -1})
    );
  }

  @Test
  void shouldGetArrayElement() {
    ExecutionContext context = new ExecutionContext();
    ReferenceValue reference = arrayReference(context, 2);
    ArrayOperations.set(context, reference, 0, new IntegerValue(42));
    IValue result = ArrayOperations.get(context, reference, 0);
    assertEquals(new IntegerValue(42), result);
  }

  @Test
  void shouldGetFirstAndLastArrayElements() {
    ExecutionContext context = new ExecutionContext();
    ReferenceValue reference = arrayReference(context, 2);
    ArrayOperations.set(context, reference, 0, new IntegerValue(10));
    ArrayOperations.set(context, reference, 1, new IntegerValue(20));
    assertEquals(new IntegerValue(10), ArrayOperations.get(context, reference, 0));
    assertEquals(new IntegerValue(20), ArrayOperations.get(context, reference, 1));
  }

  @Test
  void shouldRejectNegativeIndexWhenGettingElement() {
    ExecutionContext context = new ExecutionContext();
    ReferenceValue reference = arrayReference(context, 2);
    assertThrows(InvalidArrayIndexException.class, () -> ArrayOperations.get(context, reference, -1));
  }

  @Test
  void shouldRejectIndexAfterLastElementWhenGettingElement() {
    ExecutionContext context = new ExecutionContext();
    ReferenceValue reference = arrayReference(context, 2);
    assertThrows(InvalidArrayIndexException.class, () -> ArrayOperations.get(context, reference, 2));
  }

  @Test
  void shouldSetArrayElement() {
    ExecutionContext context = new ExecutionContext();
    ReferenceValue reference = arrayReference(context, 2);
    ArrayOperations.set(context, reference, 1, new IntegerValue(99));
    assertEquals(new IntegerValue(99), ArrayOperations.get(context, reference, 1));
  }

  @Test
  void shouldPreserveReferenceAddressWhenSettingElement() {
    ExecutionContext context = new ExecutionContext();
    ReferenceValue reference = arrayReference(context, 2);
    int address = reference.address();
    ArrayOperations.set(context, reference, 0, new IntegerValue(42));
    assertEquals(address, reference.address());
    assertEquals(new IntegerValue(42), ArrayOperations.get(context, reference, 0));
  }

  @Test
  void shouldRejectNullValueWhenSettingElement() {
    ExecutionContext context = new ExecutionContext();
    ReferenceValue reference = arrayReference(context, 2);
    assertThrows(NullArgumentException.class, () -> ArrayOperations.set(context, reference, 0, null));
  }

  @Test
  void shouldRejectInvalidIndexWhenSettingElement() {
    ExecutionContext context = new ExecutionContext();
    ReferenceValue reference = arrayReference(context, 2);
    assertThrows(
        InvalidArrayIndexException.class, () -> ArrayOperations.set(context, reference, 2, new IntegerValue(42))
    );
  }

  @Test
  void shouldReturnArrayLength() {
    ExecutionContext context = new ExecutionContext();
    ReferenceValue reference = arrayReference(context, 4);
    assertEquals(4, ArrayOperations.length(context, reference));
  }

  @Test
  void shouldReturnZeroForEmptyArrayLength() {
    ExecutionContext context = new ExecutionContext();
    ReferenceValue reference = arrayReference(context, 0);
    assertEquals(0, ArrayOperations.length(context, reference));
  }

  @Test
  void shouldRejectNullContextWhenGettingElement() {
    assertThrows(NullArgumentException.class, () -> ArrayOperations.get(null, new ReferenceValue(0), 0));
  }

  @Test
  void shouldRejectNullReferenceWhenGettingElement() {
    assertThrows(NullArgumentException.class, () -> ArrayOperations.get(new ExecutionContext(), null, 0));
  }

  @Test
  void shouldRejectNullContextWhenSettingElement() {
    assertThrows(
        NullArgumentException.class, () -> ArrayOperations.set(null, new ReferenceValue(0), 0, new IntegerValue(42))
    );
  }

  @Test
  void shouldRejectNullReferenceWhenSettingElement() {
    assertThrows(
        NullArgumentException.class, () -> ArrayOperations.set(new ExecutionContext(), null, 0, new IntegerValue(42))
    );
  }

  @Test
  void shouldRejectNullContextWhenGettingLength() {
    assertThrows(NullArgumentException.class, () -> ArrayOperations.length(null, new ReferenceValue(0)));
  }

  @Test
  void shouldRejectNullReferenceWhenGettingLength() {
    assertThrows(NullArgumentException.class, () -> ArrayOperations.length(new ExecutionContext(), null));
  }

  @Test
  void shouldRejectReferenceToNonArrayValue() {
    ExecutionContext context = new ExecutionContext();
    int address = context.heap().allocate(new IntegerValue(42));
    ReferenceValue reference = new ReferenceValue(address);
    assertThrows(ExpectedArrayValueException.class, () -> ArrayOperations.get(context, reference, 0));
  }
}