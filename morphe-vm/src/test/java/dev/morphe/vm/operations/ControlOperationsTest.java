package dev.morphe.vm.operations;

import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.exception.argument.InvalidInstructionPointerException;
import dev.morphe.vm.state.VMExecutionState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControlOperationsTest {

  @Test
  void shouldHaltVirtualMachine() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    assertNotEquals(VMExecutionState.HALTED, vm.state());
    ControlOperations.halt(vm);
    assertEquals(VMExecutionState.HALTED, vm.state());
  }

  @Test
  void shouldJumpToTargetInstruction() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    ControlOperations.jump(vm, 10);
    assertEquals(10, vm.instructionPointer());
  }

  @Test
  void shouldJumpWhenConditionIsFalse() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    ControlOperations.jumpIfFalse(vm, BooleanValue.FALSE, 10);
    assertEquals(10, vm.instructionPointer());
  }

  @Test
  void shouldNotJumpWhenConditionIsTrue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    ControlOperations.jumpIfFalse(vm, BooleanValue.TRUE, 10);
    assertEquals(0, vm.instructionPointer());
  }

  @Test
  void shouldJumpWhenConditionIsTrue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    ControlOperations.jumpIfTrue(vm, BooleanValue.TRUE, 10);
    assertEquals(10, vm.instructionPointer());
  }

  @Test
  void shouldNotJumpWhenConditionIsFalse() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    ControlOperations.jumpIfTrue(vm, BooleanValue.FALSE, 10);
    assertEquals(0, vm.instructionPointer());
  }

  @Test
  void shouldDelegateJumpValidationToVirtualMachine() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    assertThrows(InvalidInstructionPointerException.class, () -> ControlOperations.jump(vm, -1));
  }
}