package dev.morphe.vm.executor;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.ComparisonOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.bytecode.program.FunctionDefinition;
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.bytecode.type.PrimitiveBytecodeType;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.vm.VirtualMachine;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComparisonExecutorTest {

  private static void execute(VirtualMachine vm, ComparisonOpCode opcode) {
    new ComparisonExecutor().execute(vm, program(), new Instruction<>(opcode));
  }

  private static BytecodeProgram program() {
    FunctionBytecodeType functionType = new FunctionBytecodeType(List.of(), PrimitiveBytecodeType.INTEGER);
    FunctionDefinition main = new FunctionDefinition("main", functionType, 0);
    return new BytecodeProgram(List.of(), List.of(), List.of(), List.of(main), List.of(), 0);
  }

  @Test
  void eqShouldPushTrueForEqualValues() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(10));
    vm.context().stack().push(new IntegerValue(10));
    execute(vm, ComparisonOpCode.EQ);
    assertEquals(new BooleanValue(true), vm.context().stack().pop());
  }

  @Test
  void neqShouldPushTrueForDifferentValues() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(10));
    vm.context().stack().push(new IntegerValue(20));
    execute(vm, ComparisonOpCode.NEQ);
    assertEquals(new BooleanValue(true), vm.context().stack().pop());
  }

  @Test
  void ltShouldPreserveOperandOrder() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(10));
    vm.context().stack().push(new IntegerValue(20));
    execute(vm, ComparisonOpCode.LT);
    assertEquals(new BooleanValue(true), vm.context().stack().pop());
  }

  @Test
  void lteShouldReturnTrueForEqualValues() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(10));
    vm.context().stack().push(new IntegerValue(10));
    execute(vm, ComparisonOpCode.LTE);
    assertEquals(new BooleanValue(true), vm.context().stack().pop());
  }

  @Test
  void gtShouldPreserveOperandOrder() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(20));
    vm.context().stack().push(new IntegerValue(10));
    execute(vm, ComparisonOpCode.GT);
    assertEquals(new BooleanValue(true), vm.context().stack().pop());
  }

  @Test
  void gteShouldReturnTrueForEqualValues() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(10));
    vm.context().stack().push(new IntegerValue(10));
    execute(vm, ComparisonOpCode.GTE);
    assertEquals(new BooleanValue(true), vm.context().stack().pop());
  }
}