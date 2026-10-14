package dev.morphe.vm.executor;

import dev.morphe.bytecode.constants.IConstant;
import dev.morphe.bytecode.constants.IntegerConstant;
import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.StackOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.bytecode.program.FunctionDefinition;
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.bytecode.type.PrimitiveBytecodeType;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.vm.VirtualMachine;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StackExecutorTest {

  private static void execute(VirtualMachine vm, BytecodeProgram program, StackOpCode opcode, int... operands) {
    new StackExecutor().execute(vm, program, new Instruction<>(opcode, operands));
  }

  private static BytecodeProgram program(IConstant... constants) {
    FunctionBytecodeType functionType = new FunctionBytecodeType(List.of(), PrimitiveBytecodeType.INTEGER);
    FunctionDefinition main = new FunctionDefinition("main", functionType, 0);
    return new BytecodeProgram(List.of(), List.of(constants), List.of(), List.of(main), List.of(), 0);
  }

  @Test
  void pushShouldResolveConstantAndPushValue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    execute(vm, program(new IntegerConstant(42)), StackOpCode.PUSH, 0);
    assertEquals(new IntegerValue(42), vm.context().stack().pop());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void popShouldRemoveTopValue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(42));
    execute(vm, program(), StackOpCode.POP);
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void dupShouldDuplicateTopValue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(10));
    execute(vm, program(), StackOpCode.DUP);
    assertEquals(new IntegerValue(10), vm.context().stack().pop());
    assertEquals(new IntegerValue(10), vm.context().stack().pop());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void dup2ShouldDuplicateTopTwoValues() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(10));
    vm.context().stack().push(new IntegerValue(20));
    execute(vm, program(), StackOpCode.DUP2);
    assertEquals(new IntegerValue(20), vm.context().stack().pop());
    assertEquals(new IntegerValue(10), vm.context().stack().pop());
    assertEquals(new IntegerValue(20), vm.context().stack().pop());
    assertEquals(new IntegerValue(10), vm.context().stack().pop());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void overShouldDuplicateSecondValueFromTop() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(10));
    vm.context().stack().push(new IntegerValue(20));
    execute(vm, program(), StackOpCode.OVER);
    assertEquals(new IntegerValue(10), vm.context().stack().pop());
    assertEquals(new IntegerValue(20), vm.context().stack().pop());
    assertEquals(new IntegerValue(10), vm.context().stack().pop());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void swapShouldExchangeTopTwoValues() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(10));
    vm.context().stack().push(new IntegerValue(20));
    execute(vm, program(), StackOpCode.SWAP);
    assertEquals(new IntegerValue(10), vm.context().stack().pop());
    assertEquals(new IntegerValue(20), vm.context().stack().pop());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void rotShouldRotateTopThreeValues() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(10));
    vm.context().stack().push(new IntegerValue(20));
    vm.context().stack().push(new IntegerValue(30));
    execute(vm, program(), StackOpCode.ROT);
    assertEquals(new IntegerValue(10), vm.context().stack().pop());
    assertEquals(new IntegerValue(30), vm.context().stack().pop());
    assertEquals(new IntegerValue(20), vm.context().stack().pop());
    assertTrue(vm.context().stack().isEmpty());
  }
}