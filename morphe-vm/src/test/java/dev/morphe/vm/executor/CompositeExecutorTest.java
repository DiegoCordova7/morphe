package dev.morphe.vm.executor;

import dev.morphe.bytecode.constants.StringConstant;
import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.CompositeOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.bytecode.program.FunctionDefinition;
import dev.morphe.bytecode.type.CompositeBytecodeType;
import dev.morphe.bytecode.type.CompositeBytecodeType.CompositeBytecodeField;
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.bytecode.type.IBytecodeType;
import dev.morphe.bytecode.type.PrimitiveBytecodeType;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.runtime.value.reference.ReferenceValue;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.exception.internal.type.ExpectedReferenceValueException;
import dev.morphe.vm.exception.internal.type.UnsupportedBytecodeTypeException;
import dev.morphe.vm.operations.CompositeOperations;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompositeExecutorTest {


  private static ReferenceValue newComposite(VirtualMachine vm) {
    CompositeBytecodeType type = compositeType();
    vm.context().stack().push(new IntegerValue(10));
    vm.context().stack().push(new IntegerValue(20));
    execute(vm, program(type), CompositeOpCode.COMPOSITE_NEW, 0);
    return assertInstanceOf(ReferenceValue.class, vm.context().stack().pop());
  }

  private static CompositeBytecodeType compositeType() {
    return new CompositeBytecodeType(
        "Person",
        List.of(
            new CompositeBytecodeField("age", PrimitiveBytecodeType.INTEGER),
            new CompositeBytecodeField("score", PrimitiveBytecodeType.INTEGER)
        )
    );
  }

  private static BytecodeProgram program(IBytecodeType... types) {
    FunctionBytecodeType functionType = new FunctionBytecodeType(List.of(), PrimitiveBytecodeType.INTEGER);
    FunctionDefinition main = new FunctionDefinition("main", functionType, 0);
    return new BytecodeProgram(List.of(), List.of(new StringConstant("age")), List.of(types), List.of(main), List.of(), 0);
  }

  private static void execute(VirtualMachine vm, BytecodeProgram program, CompositeOpCode opcode, int... operands) {
    new CompositeExecutor().execute(vm, program, new Instruction<>(opcode, operands));
  }

  @Test
  void newCompositeShouldPushCompositeReference() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    CompositeBytecodeType type = compositeType();
    vm.context().stack().push(new IntegerValue(10));
    vm.context().stack().push(new IntegerValue(20));
    execute(vm, program(type), CompositeOpCode.COMPOSITE_NEW, 0);
    ReferenceValue reference = assertInstanceOf(ReferenceValue.class, vm.context().stack().pop());
    assertEquals(new IntegerValue(10), CompositeOperations.get(vm.context(), reference, "age"));
    assertEquals(new IntegerValue(20), CompositeOperations.get(vm.context(), reference, "score"));
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void newCompositeShouldRejectNonCompositeBytecodeType() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    assertThrows(
        UnsupportedBytecodeTypeException.class,
        () -> execute(vm, program(PrimitiveBytecodeType.INTEGER), CompositeOpCode.COMPOSITE_NEW, 0)
    );
  }

  @Test
  void getFieldShouldPushFieldValue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    ReferenceValue reference = newComposite(vm);
    vm.context().stack().push(reference);
    execute(vm, program(compositeType()), CompositeOpCode.GET_FIELD, 0);
    assertEquals(new IntegerValue(10), vm.context().stack().pop());
  }

  @Test
  void setFieldShouldStoreFieldValue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    ReferenceValue reference = newComposite(vm);
    IValue value = new IntegerValue(42);
    vm.context().stack().push(reference);
    vm.context().stack().push(value);
    execute(vm, program(compositeType()), CompositeOpCode.SET_FIELD, 0);
    assertEquals(value, CompositeOperations.get(vm.context(), reference, "age"));
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void getFieldShouldRejectNonReferenceValue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(42));
    assertThrows(
        ExpectedReferenceValueException.class,
        () -> execute(vm, program(compositeType()), CompositeOpCode.GET_FIELD, 0)
    );
  }

  @Test
  void setFieldShouldRejectNonReferenceValue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(42));
    vm.context().stack().push(new IntegerValue(10));
    assertThrows(
        ExpectedReferenceValueException.class,
        () -> execute(vm, program(compositeType()), CompositeOpCode.SET_FIELD, 0)
    );
  }
}