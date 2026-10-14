package dev.morphe.vm.operations;

import dev.morphe.bytecode.type.CompositeBytecodeType;
import dev.morphe.bytecode.type.PrimitiveBytecodeType;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.composite.CompositeValue;
import dev.morphe.runtime.value.primitive.StringValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.runtime.value.reference.ReferenceValue;
import dev.morphe.vm.ExecutionContext;
import dev.morphe.vm.exception.internal.composite.InvalidCompositeReferenceException;
import dev.morphe.runtime.exception.type.TypeMismatchException;
import dev.morphe.vm.memory.Heap;
import dev.morphe.vm.memory.Memory;
import dev.morphe.vm.memory.MemoryAllocator;
import dev.morphe.vm.scope.CallStack;
import dev.morphe.vm.stack.ValueStack;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompositeOperationsTest {

  @Test
  void shouldCreateCompositeAndAllocateItInHeap() {
    ExecutionContext context = new ExecutionContext();
    CompositeBytecodeType type = new CompositeBytecodeType(
        "Person",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("name", PrimitiveBytecodeType.STRING),
            new CompositeBytecodeType.CompositeBytecodeField("age", PrimitiveBytecodeType.INTEGER)
        )
    );
    context.stack().push(new StringValue("Diego"));
    context.stack().push(new IntegerValue(25));
    ReferenceValue reference = CompositeOperations.create(context, type);
    IValue stored = context.heap().load(reference.address());
    assertInstanceOf(CompositeValue.class, stored);
    CompositeValue composite = (CompositeValue) stored;
    assertEquals(new StringValue("Diego"), composite.get("name"));
    assertEquals(new IntegerValue(25), composite.get("age"));
  }

  @Test
  void shouldCreateCompositeUsingCorrectStackOrder() {
    ExecutionContext context = new ExecutionContext();
    CompositeBytecodeType type = new CompositeBytecodeType(
        "Person",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("name", PrimitiveBytecodeType.STRING),
            new CompositeBytecodeType.CompositeBytecodeField("age", PrimitiveBytecodeType.INTEGER)
        )
    );
    context.stack().push(new StringValue("Diego"));
    context.stack().push(new IntegerValue(25));
    ReferenceValue reference = CompositeOperations.create(context, type);
    CompositeValue composite = (CompositeValue) context.heap().load(reference.address());
    assertEquals(new StringValue("Diego"), composite.get("name"));
    assertEquals(new IntegerValue(25), composite.get("age"));
  }

  @Test
  void shouldConsumeFieldValuesFromStack() {
    ExecutionContext context = new ExecutionContext();
    CompositeBytecodeType type = new CompositeBytecodeType(
        "Person",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("name", PrimitiveBytecodeType.STRING),
            new CompositeBytecodeType.CompositeBytecodeField("age", PrimitiveBytecodeType.INTEGER)
        )
    );
    context.stack().push(new StringValue("Diego"));
    context.stack().push(new IntegerValue(25));
    CompositeOperations.create(context, type);
    assertTrue(context.stack().isEmpty());
  }

  @Test
  void shouldGetCompositeField() {
    ExecutionContext context = new ExecutionContext();
    CompositeBytecodeType type = new CompositeBytecodeType(
        "Person",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("name", PrimitiveBytecodeType.STRING)
        )
    );
    context.stack().push(new StringValue("Diego"));
    ReferenceValue reference = CompositeOperations.create(context, type);
    IValue result = CompositeOperations.get(context, reference, "name");
    assertEquals(new StringValue("Diego"), result);
  }

  @Test
  void shouldSetCompositeField() {
    ExecutionContext context = new ExecutionContext();
    CompositeBytecodeType type = new CompositeBytecodeType(
        "Person",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("name", PrimitiveBytecodeType.STRING)
        )
    );
    context.stack().push(new StringValue("Diego"));
    ReferenceValue reference = CompositeOperations.create(context, type);
    CompositeOperations.set(context, reference, "name", new StringValue("Carlos"));
    assertEquals(new StringValue("Carlos"), CompositeOperations.get(context, reference, "name"));
  }

  @Test
  void shouldRejectInvalidFieldTypeDuringCreation() {
    ExecutionContext context = new ExecutionContext();
    CompositeBytecodeType type = new CompositeBytecodeType(
        "Person",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("age", PrimitiveBytecodeType.INTEGER)
        )
    );
    context.stack().push(new StringValue("not an integer"));
    assertThrows(TypeMismatchException.class, () -> CompositeOperations.create(context, type));
  }

  @Test
  void shouldRejectInvalidFieldTypeDuringAssignment() {
    ExecutionContext context = new ExecutionContext();
    CompositeBytecodeType type = new CompositeBytecodeType(
        "Person",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("age", PrimitiveBytecodeType.INTEGER)
        )
    );
    context.stack().push(new IntegerValue(25));
    ReferenceValue reference = CompositeOperations.create(context, type);
    assertThrows(
        TypeMismatchException.class,
        () -> CompositeOperations.set(context, reference, "age", new StringValue("not an integer"))
    );
  }

  @Test
  void shouldRejectReferenceThatDoesNotPointToCompositeOnGet() {
    ExecutionContext context = new ExecutionContext();
    int address = context.heap().allocate(new StringValue("not a composite"));
    ReferenceValue reference = new ReferenceValue(address);
    assertThrows(InvalidCompositeReferenceException.class, () -> CompositeOperations.get(context, reference, "value"));
  }

  @Test
  void shouldRejectReferenceThatDoesNotPointToCompositeOnSet() {
    ExecutionContext context = new ExecutionContext();
    int address = context.heap().allocate(new StringValue("not a composite"));
    ReferenceValue reference = new ReferenceValue(address);
    assertThrows(
        InvalidCompositeReferenceException.class,
        () -> CompositeOperations.set(context, reference, "value", new StringValue("test"))
    );
  }

  @Test
  void shouldAllowUpdatingFieldWithCompatibleValue() {
    ExecutionContext context = new ExecutionContext();
    CompositeBytecodeType type = new CompositeBytecodeType(
        "Person",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("age", PrimitiveBytecodeType.INTEGER)
        )
    );
    context.stack().push(new IntegerValue(20));
    ReferenceValue reference = CompositeOperations.create(context, type);
    CompositeOperations.set(context, reference, "age", new IntegerValue(30));
    assertEquals(new IntegerValue(30), CompositeOperations.get(context, reference, "age"));
  }
}