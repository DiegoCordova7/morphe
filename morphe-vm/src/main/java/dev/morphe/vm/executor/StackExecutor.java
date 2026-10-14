package dev.morphe.vm.executor;

import dev.morphe.bytecode.constants.IConstant;
import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.StackOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.runtime.value.IValue;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.constant.ConstantResolver;
import dev.morphe.vm.operations.StackOperations;

/**
 * Executes stack-related bytecode instructions.
 *
 * <p>
 * This executor maps {@link StackOpCode stack opcodes} to their corresponding
 * stack operations. The actual stack and memory manipulation is delegated to
 * {@link StackOperations}.
 * </p>
 *
 * <p>
 * Supported operations include:
 * </p>
 *
 * <ul>
 *   <li>{@code PUSH} – loads a constant from the constant pool onto the stack.</li>
 *   <li>{@code POP} – removes the value at the top of the stack.</li>
 *   <li>{@code DUP} – duplicates the value at the top of the stack.</li>
 *   <li>{@code OVER} – duplicates the second value from the top of the stack.</li>
 * </ul>
 */
public final class StackExecutor implements IExecutor<StackOpCode> {

  /**
   * Executes a single stack instruction.
   *
   * @param vm the virtual machine executing the instruction
   * @param program the program containing the instruction
   * @param instruction the instruction to execute
   */
  @Override
  public void execute(VirtualMachine vm, BytecodeProgram program, Instruction<StackOpCode> instruction) {
    switch (instruction.opcode()) {
      case PUSH -> push(vm, program, instruction);
      case POP -> StackOperations.pop(vm.context().stack());
      case DUP -> StackOperations.dup(vm.context().stack());
      case DUP2 -> StackOperations.dup2(vm.context().stack());
      case OVER -> StackOperations.over(vm.context().stack());
      case SWAP -> StackOperations.swap(vm.context().stack());
      case ROT -> StackOperations.rot(vm.context().stack());
    }
  }

  /**
   * Resolves the constant referenced by the instruction and pushes the
   * resulting runtime value onto the operand stack.
   *
   * <p>
   * The instruction operand identifies an entry in the program constant pool.
   * The referenced constant is converted into its runtime representation
   * through {@link ConstantResolver}.
   * </p>
   *
   * @param vm the virtual machine executing the instruction
   * @param program the program containing the constant pool
   * @param instruction the {@code PUSH} instruction
   */
  private void push(VirtualMachine vm, BytecodeProgram program, Instruction<StackOpCode> instruction) {
    int constantIndex = instruction.operand(0);
    IConstant constant = program.constantAt(constantIndex);
    IValue value = ConstantResolver.resolve(constant);
    StackOperations.push(vm.context().stack(), value);
  }
}