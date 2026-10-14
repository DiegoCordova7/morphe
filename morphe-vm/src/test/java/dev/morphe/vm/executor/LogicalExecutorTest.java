package dev.morphe.vm.executor;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.LogicalOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.bytecode.program.FunctionDefinition;
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.bytecode.type.PrimitiveBytecodeType;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.vm.VirtualMachine;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogicalExecutorTest {

  private static void execute(VirtualMachine vm, LogicalOpCode opcode) {
    new LogicalExecutor().execute(vm, program(), new Instruction<>(opcode));
  }

  private static BytecodeProgram program() {
    FunctionBytecodeType functionType = new FunctionBytecodeType(List.of(), PrimitiveBytecodeType.INTEGER);
    FunctionDefinition main = new FunctionDefinition("main", functionType, 0);
    return new BytecodeProgram(List.of(), List.of(), List.of(), List.of(main), List.of(), 0);
  }

  @Test
  void orShouldPushOperationResult() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new BooleanValue(false));
    vm.context().stack().push(new BooleanValue(true));
    execute(vm, LogicalOpCode.OR);
    assertEquals(new BooleanValue(true), vm.context().stack().pop());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void andShouldPushOperationResult() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new BooleanValue(true));
    vm.context().stack().push(new BooleanValue(false));
    execute(vm, LogicalOpCode.AND);
    assertEquals(new BooleanValue(false), vm.context().stack().pop());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void notShouldPushNegatedValue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new BooleanValue(true));
    execute(vm, LogicalOpCode.NOT);
    assertEquals(new BooleanValue(false), vm.context().stack().pop());
    assertTrue(vm.context().stack().isEmpty());
  }
}