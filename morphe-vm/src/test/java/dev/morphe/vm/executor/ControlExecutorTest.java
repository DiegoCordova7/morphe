package dev.morphe.vm.executor;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.ControlOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.bytecode.program.FunctionDefinition;
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.bytecode.type.PrimitiveBytecodeType;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.exception.internal.type.ExpectedBooleanValueException;
import dev.morphe.vm.state.VMExecutionState;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControlExecutorTest {


  private static void execute(VirtualMachine vm, ControlOpCode opcode, int... operands) {
    new ControlExecutor().execute(vm, program(), new Instruction<>(opcode, operands));
  }

  private static BytecodeProgram program() {
    FunctionBytecodeType functionType = new FunctionBytecodeType(List.of(), PrimitiveBytecodeType.INTEGER);
    FunctionDefinition main = new FunctionDefinition("main", functionType, 0);
    return new BytecodeProgram(List.of(), List.of(), List.of(), List.of(main), List.of(), 0);
  }

  @Test
  void haltShouldHaltVirtualMachine() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    execute(vm, ControlOpCode.HALT);
    assertEquals(VMExecutionState.HALTED, vm.state());
  }

  @Test
  void jumpShouldSetInstructionPointer() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    execute(vm, ControlOpCode.JMP, 10);
    assertEquals(10, vm.instructionPointer());
  }

  @Test
  void jumpIfFalseShouldJumpWhenConditionIsFalse() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new BooleanValue(false));
    execute(vm, ControlOpCode.JMP_IF_FALSE, 10);
    assertEquals(10, vm.instructionPointer());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void jumpIfFalseShouldNotJumpWhenConditionIsTrue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new BooleanValue(true));
    vm.jumpTo(5);
    execute(vm, ControlOpCode.JMP_IF_FALSE, 10);
    assertEquals(5, vm.instructionPointer());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void jumpIfTrueShouldJumpWhenConditionIsTrue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new BooleanValue(true));
    execute(vm, ControlOpCode.JMP_IF_TRUE, 10);
    assertEquals(10, vm.instructionPointer());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void jumpIfTrueShouldNotJumpWhenConditionIsFalse() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new BooleanValue(false));
    vm.jumpTo(5);
    execute(vm, ControlOpCode.JMP_IF_TRUE, 10);
    assertEquals(5, vm.instructionPointer());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void jumpIfFalseShouldRejectNonBooleanValue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(1));
    assertThrows(
        ExpectedBooleanValueException.class,
        () -> execute(vm, ControlOpCode.JMP_IF_FALSE, 10)
    );
  }

  @Test
  void jumpIfTrueShouldRejectNonBooleanValue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(1));
    assertThrows(
        ExpectedBooleanValueException.class,
        () -> execute(vm, ControlOpCode.JMP_IF_TRUE, 10)
    );
  }
}