package dev.morphe.vm.type;

import dev.morphe.runtime.type.collection.ArrayType;
import dev.morphe.runtime.type.composite.CompositeType;
import dev.morphe.runtime.type.primitive.IntegerType;
import dev.morphe.runtime.value.collection.ArrayValue;
import dev.morphe.runtime.value.primitive.StringValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.runtime.value.reference.ReferenceValue;
import dev.morphe.vm.ExecutionContext;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.memory.Heap;
import dev.morphe.vm.memory.Memory;
import dev.morphe.vm.memory.MemoryAllocator;
import dev.morphe.vm.scope.CallStack;
import dev.morphe.vm.stack.ValueStack;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RuntimeTypeCheckerTest {

  private ArrayType integerArrayType() {
    return new ArrayType(IntegerType.INSTANCE);
  }

  private ArrayType integerMatrixType() {
    return new ArrayType(integerArrayType());
  }

  @Test
  void shouldAcceptCompatiblePrimitiveValue() {
    assertTrue(RuntimeTypeChecker.accepts(new ExecutionContext(), IntegerType.INSTANCE, new IntegerValue(42)));
  }

  @Test
  void shouldRejectIncompatiblePrimitiveValue() {
    assertFalse(RuntimeTypeChecker.accepts(new ExecutionContext(), IntegerType.INSTANCE, new StringValue("X")));
  }

  @Test
  void shouldRejectNullContext() {
    assertThrows(
        NullArgumentException.class, () -> RuntimeTypeChecker.accepts(null, IntegerType.INSTANCE, new IntegerValue(42))
    );
  }

  @Test
  void shouldRejectNullExpectedType() {
    assertThrows(NullArgumentException.class, () -> RuntimeTypeChecker.accepts(new ExecutionContext(), null, new IntegerValue(42)));
  }

  @Test
  void shouldRejectNullValue() {
    assertThrows(NullArgumentException.class, () -> RuntimeTypeChecker.accepts(new ExecutionContext(), IntegerType.INSTANCE, null));
  }

  @Test
  void shouldAcceptCompatibleArray() {
    ExecutionContext context = new ExecutionContext();
    ArrayType type = integerArrayType();
    ArrayValue array = new ArrayValue(type, List.of(new IntegerValue(1), new IntegerValue(2)));
    assertTrue(RuntimeTypeChecker.accepts(context, type, array));
  }

  @Test
  void shouldRejectArrayWithIncompatibleElement() {
    ExecutionContext context = new ExecutionContext();
    ArrayType type = integerArrayType();
    ArrayValue array = new ArrayValue(type, List.of(new StringValue("X")));
    assertFalse(RuntimeTypeChecker.accepts(context, type, array));
  }

  @Test
  void shouldAcceptReferencedArray() {
    ExecutionContext context = new ExecutionContext();
    ArrayType type = integerArrayType();
    ArrayValue array = new ArrayValue(type, List.of(new IntegerValue(1), new IntegerValue(2)));
    int address = context.heap().allocate(array);
    ReferenceValue reference = new ReferenceValue(address);
    assertTrue(RuntimeTypeChecker.accepts(context, type, reference));
  }

  @Test
  void shouldRejectValueThatIsNotAnArray() {
    assertFalse(RuntimeTypeChecker.accepts(new ExecutionContext(), integerArrayType(), new IntegerValue(42)));
  }

  @Test
  void shouldAcceptCompatibleMultidimensionalArray() {
    ExecutionContext context = new ExecutionContext();
    ArrayType type = integerMatrixType();
    ArrayValue inner = new ArrayValue(integerArrayType(), List.of(new IntegerValue(1), new IntegerValue(2)));
    int innerAddress = context.heap().allocate(inner);
    ArrayValue outer = new ArrayValue(type, List.of(new ReferenceValue(innerAddress)));
    assertTrue(RuntimeTypeChecker.accepts(context, type, outer));
  }

  @Test
  void shouldRejectIncompatibleArrayType() {
    ExecutionContext context = new ExecutionContext();
    ArrayType integerArray = new ArrayType(IntegerType.INSTANCE);
    ArrayType nestedArray = new ArrayType(integerArray);
    ArrayValue array = new ArrayValue(integerArray, List.of(new IntegerValue(42)));
    assertFalse(RuntimeTypeChecker.accepts(context, nestedArray, array));
  }

  @Test
  void shouldRejectNonReferenceForCompositeType() {
    CompositeType type = new CompositeType("Person", List.of());
    assertFalse(RuntimeTypeChecker.accepts(new ExecutionContext(), type, new IntegerValue(42)));
  }
}