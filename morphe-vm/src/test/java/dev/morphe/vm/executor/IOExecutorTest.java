package dev.morphe.vm.executor;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.IOOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.bytecode.program.FunctionDefinition;
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.bytecode.type.PrimitiveBytecodeType;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.state.VMExecutionState;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IOExecutorTest {

  private static void execute(VirtualMachine vm, IOOpCode opcode) {
    new IOExecutor().execute(vm, program(), new Instruction<>(opcode));
  }

  private static BytecodeProgram program() {
    FunctionBytecodeType functionType = new FunctionBytecodeType(List.of(), PrimitiveBytecodeType.INTEGER);
    FunctionDefinition main = new FunctionDefinition("main", functionType, 0);
    return new BytecodeProgram(List.of(), List.of(), List.of(), List.of(main), List.of(), 0);
  }

  @Test
  void printShouldConsumeValueFromStack() {
    StringBuilder output = new StringBuilder();
    VirtualMachine vm = new VirtualMachine(output::append);
    vm.context().stack().push(new IntegerValue(42));
    execute(vm, IOOpCode.PRINT);
    assertEquals("42", output.toString());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void inputStringShouldWaitForInput() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    execute(vm, IOOpCode.INPUT_STRING);
    assertEquals(VMExecutionState.WAITING_FOR_INPUT, vm.state());
  }

  @Test
  void inputIntegerShouldWaitForInput() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    execute(vm, IOOpCode.INPUT_INTEGER);
    assertEquals(VMExecutionState.WAITING_FOR_INPUT, vm.state());
  }

  @Test
  void inputDoubleShouldWaitForInput() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    execute(vm, IOOpCode.INPUT_DOUBLE);
    assertEquals(VMExecutionState.WAITING_FOR_INPUT, vm.state());
  }

  @Test
  void inputBooleanShouldWaitForInput() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    execute(vm, IOOpCode.INPUT_BOOLEAN);
    assertEquals(VMExecutionState.WAITING_FOR_INPUT, vm.state());
  }
}