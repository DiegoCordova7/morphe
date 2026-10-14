package dev.morphe.vm.executor;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.ArrayOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.bytecode.program.FunctionDefinition;
import dev.morphe.bytecode.type.ArrayBytecodeType;
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.bytecode.type.IBytecodeType;
import dev.morphe.bytecode.type.PrimitiveBytecodeType;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.StringValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.runtime.value.reference.ReferenceValue;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.exception.internal.type.ExpectedNumericValueException;
import dev.morphe.vm.exception.internal.type.ExpectedReferenceValueException;
import dev.morphe.vm.exception.internal.type.UnsupportedBytecodeTypeException;
import dev.morphe.vm.operations.ArrayOperations;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArrayExecutorTest {

  private ArrayBytecodeType array(IBytecodeType type) {
    return new ArrayBytecodeType(type);
  }

  @Test
  void newArrayShouldPushArrayReference() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(3));
    execute(vm, program(array(PrimitiveBytecodeType.INTEGER)), ArrayOpCode.ARRAY_NEW, 0);
    ReferenceValue reference = assertInstanceOf(ReferenceValue.class, vm.context().stack().pop());
    assertEquals(3, ArrayOperations.length(vm.context(), reference));
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void newMultiArrayShouldPushArrayReference() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(2));
    vm.context().stack().push(new IntegerValue(3));
    execute(vm, program(array(array(PrimitiveBytecodeType.INTEGER))), ArrayOpCode.ARRAY_NEW_MULTI, 0, 2);
    ReferenceValue reference = assertInstanceOf(ReferenceValue.class, vm.context().stack().pop());
    assertEquals(2, ArrayOperations.length(vm.context(), reference));
    IValue nestedValue = ArrayOperations.get(vm.context(), reference, 0);
    ReferenceValue nestedReference = assertInstanceOf(ReferenceValue.class, nestedValue);
    assertEquals(3, ArrayOperations.length(vm.context(), nestedReference));
  }

  @Test
  void getShouldPushArrayElement() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    ReferenceValue reference = newArray(vm, 3);
    IValue value = new IntegerValue(42);
    ArrayOperations.set(vm.context(), reference, 1, value);
    vm.context().stack().push(reference);
    vm.context().stack().push(new IntegerValue(1));
    execute(vm, program(), ArrayOpCode.ARRAY_GET);
    assertEquals(value, vm.context().stack().pop());
  }

  @Test
  void setShouldStoreArrayElement() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    ReferenceValue reference = newArray(vm, 3);
    IValue value = new IntegerValue(42);
    vm.context().stack().push(reference);
    vm.context().stack().push(new IntegerValue(1));
    vm.context().stack().push(value);
    execute(vm, program(), ArrayOpCode.ARRAY_SET);
    assertEquals(value, ArrayOperations.get(vm.context(), reference, 1));
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void lengthShouldPushArrayLength() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    ReferenceValue reference = newArray(vm, 5);
    vm.context().stack().push(reference);
    execute(vm, program(), ArrayOpCode.ARRAY_LENGTH);
    assertEquals(new IntegerValue(5), vm.context().stack().pop());
  }

  @Test
  void newArrayShouldRejectNonArrayBytecodeType() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(3));
    assertThrows(
        UnsupportedBytecodeTypeException.class,
        () -> execute(vm, program(PrimitiveBytecodeType.INTEGER), ArrayOpCode.ARRAY_NEW, 0)
    );
  }

  @Test
  void newArrayShouldRejectNonIntegerSize() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new StringValue("3"));
    assertThrows(
        ExpectedNumericValueException.class,
        () -> execute(vm, program(array(PrimitiveBytecodeType.INTEGER)), ArrayOpCode.ARRAY_NEW, 0)
    );
  }

  @Test
  void getShouldRejectNonIntegerIndex() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    ReferenceValue reference = newArray(vm, 3);
    vm.context().stack().push(reference);
    vm.context().stack().push(new StringValue("invalid"));
    assertThrows(ExpectedNumericValueException.class, () -> execute(vm, program(), ArrayOpCode.ARRAY_GET));
  }

  @Test
  void getShouldRejectNonReferenceArray() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(42));
    vm.context().stack().push(new IntegerValue(0));
    assertThrows(ExpectedReferenceValueException.class, () -> execute(vm, program(), ArrayOpCode.ARRAY_GET));
  }

  @Test
  void setShouldRejectNonIntegerIndex() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    ReferenceValue reference = newArray(vm, 3);
    vm.context().stack().push(reference);
    vm.context().stack().push(new StringValue("invalid"));
    vm.context().stack().push(new IntegerValue(42));
    assertThrows(ExpectedNumericValueException.class, () -> execute(vm, program(), ArrayOpCode.ARRAY_SET));
  }

  @Test
  void setShouldRejectNonReferenceArray() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(42));
    vm.context().stack().push(new IntegerValue(0));
    vm.context().stack().push(new IntegerValue(10));
    assertThrows(ExpectedReferenceValueException.class, () -> execute(vm, program(), ArrayOpCode.ARRAY_SET));
  }

  @Test
  void lengthShouldRejectNonReferenceArray() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(42));
    assertThrows(ExpectedReferenceValueException.class, () -> execute(vm, program(), ArrayOpCode.ARRAY_LENGTH));
  }

  private static ReferenceValue newArray(VirtualMachine vm, int size) {
    vm.context().stack().push(new IntegerValue(size));
    execute(
        vm, program(new ArrayBytecodeType(PrimitiveBytecodeType.INTEGER)), ArrayOpCode.ARRAY_NEW, 0
    );
    return assertInstanceOf(ReferenceValue.class, vm.context().stack().pop());
  }

  private static void execute(VirtualMachine vm, BytecodeProgram program, ArrayOpCode opcode, int... operands) {
    new ArrayExecutor().execute(vm, program, new Instruction<>(opcode, operands));
  }

  private static BytecodeProgram program(IBytecodeType... types) {
    FunctionBytecodeType functionType = new FunctionBytecodeType(List.of(), PrimitiveBytecodeType.INTEGER);
    FunctionDefinition main = new FunctionDefinition("main", functionType, 0);
    return new BytecodeProgram(List.of(), List.of(), List.of(types), List.of(main), List.of(), 0);
  }
}