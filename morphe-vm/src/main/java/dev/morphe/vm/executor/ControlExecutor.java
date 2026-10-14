package dev.morphe.vm.executor;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.ControlOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.exception.internal.type.ExpectedBooleanValueException;
import dev.morphe.vm.operations.ControlOperations;

/**
 * Executes control-flow-related bytecode instructions.
 *
 * <p>
 * This executor acts as the bridge between control-flow bytecode instructions
 * and the corresponding operations provided by {@link ControlOperations}.
 * </p>
 *
 * <p>
 * Supported operations include:
 * </p>
 *
 * <ul>
 *   <li>{@code HALT} – stops execution of the virtual machine.</li>
 *   <li>{@code JMP} – unconditionally jumps to the specified instruction.</li>
 *   <li>{@code JMP_IF_FALSE} – jumps when the stack condition is false.</li>
 *   <li>{@code JMP_IF_TRUE} – jumps when the stack condition is true.</li>
 * </ul>
 *
 * <p>
 * Conditional jumps consume their condition from the operand stack before
 * evaluating it.
 * </p>
 */
public final class ControlExecutor implements IExecutor<ControlOpCode> {

  /**
   * Executes a single control-flow instruction.
   *
   * @param vm the virtual machine executing the instruction
   * @param program the program containing the instruction
   * @param instruction the instruction to execute
   *
   * @throws ExpectedBooleanValueException if a conditional jump encounters
   *     a non-boolean value on the operand stack
   */
  @Override
  public void execute(VirtualMachine vm, BytecodeProgram program, Instruction<ControlOpCode> instruction) {
    switch (instruction.opcode()) {
      case HALT -> halt(vm);
      case JMP -> jump(vm, instruction);
      case JMP_IF_FALSE -> jumpIfFalse(vm, instruction);
      case JMP_IF_TRUE -> jumpIfTrue(vm, instruction);
    }
  }

  /**
   * Halts the virtual machine.
   *
   * @param vm the virtual machine to halt
   */
  private void halt(VirtualMachine vm) {
    ControlOperations.halt(vm);
  }

  /**
   * Performs an unconditional jump to the target instruction.
   *
   * @param vm the virtual machine
   * @param instruction the jump instruction containing the target
   */
  private void jump(VirtualMachine vm, Instruction<ControlOpCode> instruction) {
    int target = instruction.operand(0);
    ControlOperations.jump(vm, target);
  }

  /**
   * Performs a conditional jump when the stack condition is false.
   *
   * <p>
   * The condition is removed from the operand stack before being evaluated.
   * </p>
   *
   * @param vm the virtual machine
   * @param instruction the conditional jump instruction containing the target
   *
   * @throws ExpectedBooleanValueException if the stack value is not boolean
   */
  private void jumpIfFalse(VirtualMachine vm, Instruction<ControlOpCode> instruction) {
    int target = instruction.operand(0);
    IValue value = vm.context().stack().pop();
    if (!(value instanceof BooleanValue condition)) throw new ExpectedBooleanValueException(value);
    ControlOperations.jumpIfFalse(vm, condition, target);
  }

  /**
   * Performs a conditional jump when the stack condition is true.
   *
   * <p>
   * The condition is removed from the operand stack before being evaluated.
   * </p>
   *
   * @param vm the virtual machine
   * @param instruction the conditional jump instruction containing the target
   *
   * @throws ExpectedBooleanValueException if the stack value is not boolean
   */
  private void jumpIfTrue(VirtualMachine vm, Instruction<ControlOpCode> instruction) {
    int target = instruction.operand(0);
    IValue value = vm.context().stack().pop();
    if (!(value instanceof BooleanValue condition)) throw new ExpectedBooleanValueException(value);
    ControlOperations.jumpIfTrue(vm, condition, target);
  }
}