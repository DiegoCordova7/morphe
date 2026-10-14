package dev.morphe.vm.executor;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.ScopeOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.bytecode.program.FunctionDefinition;
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.bytecode.type.PrimitiveBytecodeType;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.scope.Scope;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScopeExecutorTest {

  private static void execute(VirtualMachine vm, ScopeOpCode opcode, int... operands) {
    new ScopeExecutor().execute(vm, program(), new Instruction<>(opcode, operands));
  }

  private static BytecodeProgram program() {
    FunctionBytecodeType functionType = new FunctionBytecodeType(List.of(), PrimitiveBytecodeType.INTEGER);
    FunctionDefinition main = new FunctionDefinition("main", functionType, 0);
    return new BytecodeProgram(List.of(), List.of(), List.of(), List.of(main), List.of(), 0);
  }

  @Test
  void declareShouldCreateVariableSlot() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    execute(vm, ScopeOpCode.DECLARE, 0);
    assertEquals(0, vm.context().scope().load(0));
  }

  @Test
  void loadShouldPushVariableValue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().scope().declare(0, 0);
    vm.context().memory().store(vm.context().scope().load(0), new IntegerValue(42));
    execute(vm, ScopeOpCode.LOAD, 0);
    assertEquals(new IntegerValue(42), vm.context().stack().pop());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void storeShouldStoreTopStackValue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().scope().declare(0, 0);
    IntegerValue value = new IntegerValue(42);
    vm.context().stack().push(value);
    execute(vm, ScopeOpCode.STORE, 0);
    assertEquals(value, vm.context().memory().load(vm.context().scope().load(0)));
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void loadCaptureShouldPushCapturedValue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().scope().declare(0, 0);
    IntegerValue value = new IntegerValue(42);
    vm.context().memory().store(vm.context().scope().load(0), value);
    vm.context().currentFrame().environment().capture(vm.context().scope().resolveCell(vm.context().memory(), 0));
    execute(vm, ScopeOpCode.LOAD_CAPTURE, 0);
    assertEquals(value, vm.context().stack().pop());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void storeCaptureShouldStoreTopStackValue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().scope().declare(0, 0);
    vm.context().memory().store(vm.context().scope().load(0), new IntegerValue(10));
    vm.context().currentFrame().environment().capture(vm.context().scope().resolveCell(vm.context().memory(), 0));
    IntegerValue value = new IntegerValue(42);
    vm.context().stack().push(value);
    execute(vm, ScopeOpCode.STORE_CAPTURE, 0);
    assertEquals(value, vm.context().memory().load(vm.context().scope().load(0)));
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void enterScopeShouldCreateNestedScope() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    Scope parent = vm.context().scope();
    execute(vm, ScopeOpCode.ENTER_SCOPE);
    assertEquals(parent, vm.context().scope().parent());
  }

  @Test
  void exitScopeShouldRestoreParentScope() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    Scope parent = vm.context().scope();
    execute(vm, ScopeOpCode.ENTER_SCOPE);
    execute(vm, ScopeOpCode.EXIT_SCOPE);
    assertEquals(parent, vm.context().scope());
  }
}