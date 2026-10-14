package dev.morphe.vm.executor;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.IOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.vm.VirtualMachine;

/**
 * Defines the contract for components responsible for executing
 * bytecode instructions belonging to a specific opcode family.
 *
 * <p>Each executor receives the current virtual machine, the bytecode
 * program being executed, and the instruction to execute. The executor
 * is responsible for interpreting the instruction according to the
 * semantics of its supported opcode family and applying the corresponding
 * changes to the virtual machine execution state.</p>
 *
 * <p>Implementations are typically associated with a specific opcode
 * category, such as stack, arithmetic, or comparison operations.</p>
 */
public interface IExecutor<T extends IOpCode> {

  /**
   * Executes a bytecode instruction.
   *
   * @param vm the virtual machine executing the instruction
   * @param program the bytecode program containing the instruction
   * @param instruction the instruction to execute
   */
  void execute(VirtualMachine vm, BytecodeProgram program, Instruction<T> instruction);
}