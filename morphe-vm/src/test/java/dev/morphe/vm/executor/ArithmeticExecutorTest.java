package dev.morphe.vm.executor;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.ArithmeticOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.bytecode.program.FunctionDefinition;
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.bytecode.type.PrimitiveBytecodeType;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.StringValue;
import dev.morphe.runtime.value.primitive.numeric.DoubleValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.exception.internal.type.ExpectedNumericValueException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArithmeticExecutorTest {

  @Test
  void addShouldPushNumericResult() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(10));
    vm.context().stack().push(new IntegerValue(5));
    execute(vm, ArithmeticOpCode.ADD);
    assertEquals(new IntegerValue(15), vm.context().stack().pop());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void addShouldConcatenateStrings() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new StringValue("Hello "));
    vm.context().stack().push(new StringValue("World"));
    execute(vm, ArithmeticOpCode.ADD);
    assertEquals(new StringValue("Hello World"), vm.context().stack().pop());
  }

  @Test
  void addShouldConcatenateWhenOneOperandIsString() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    IValue number = new IntegerValue(42);
    IValue string = new StringValue("Value: ");
    vm.context().stack().push(string);
    vm.context().stack().push(number);
    execute(vm, ArithmeticOpCode.ADD);
    assertEquals(new StringValue("Value: 42"), vm.context().stack().pop());
  }

  @Test
  void subShouldPreserveOperandOrder() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(10));
    vm.context().stack().push(new IntegerValue(3));
    execute(vm, ArithmeticOpCode.SUB);
    assertEquals(new IntegerValue(7), vm.context().stack().pop());
  }

  @Test
  void mulShouldPushNumericResult() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(6));
    vm.context().stack().push(new IntegerValue(7));
    execute(vm, ArithmeticOpCode.MUL);
    assertEquals(new IntegerValue(42), vm.context().stack().pop());
  }

  @Test
  void divShouldPreserveOperandOrder() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(10));
    vm.context().stack().push(new IntegerValue(4));
    execute(vm, ArithmeticOpCode.DIV);
    assertEquals(new DoubleValue(2.5), vm.context().stack().pop());
  }

  @Test
  void modShouldPreserveOperandOrder() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(10));
    vm.context().stack().push(new IntegerValue(3));
    execute(vm, ArithmeticOpCode.MOD);
    assertEquals(new IntegerValue(1), vm.context().stack().pop());
  }

  @Test
  void negShouldPushNegatedValue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(42));
    execute(vm, ArithmeticOpCode.NEG);
    assertEquals(new IntegerValue(-42), vm.context().stack().pop());
  }

  @Test
  void binaryNumericOperationShouldRejectNonNumericOperand() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(10));
    vm.context().stack().push(new StringValue("invalid"));
    assertThrows(ExpectedNumericValueException.class, () -> execute(vm, ArithmeticOpCode.SUB));
  }

  @Test
  void negShouldRejectNonNumericOperand() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new StringValue("invalid"));
    assertThrows(ExpectedNumericValueException.class, () -> execute(vm, ArithmeticOpCode.NEG));
  }

  private static void execute(VirtualMachine vm, ArithmeticOpCode opcode) {
    ArithmeticExecutor executor = new ArithmeticExecutor();
    Instruction<ArithmeticOpCode> instruction = new Instruction<>(opcode);
    executor.execute(vm, program(), instruction);
  }

  private static BytecodeProgram program() {
    FunctionBytecodeType type = new FunctionBytecodeType(List.of(), PrimitiveBytecodeType.INTEGER);
    FunctionDefinition main = new FunctionDefinition("main", type, 0);
    return new BytecodeProgram(List.of(), List.of(), List.of(), List.of(main), List.of(), 0);
  }
}