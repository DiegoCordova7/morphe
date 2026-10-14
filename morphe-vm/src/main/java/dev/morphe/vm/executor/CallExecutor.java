package dev.morphe.vm.executor;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.CallOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.operations.CallOperations;

/**
 * Executes function-related bytecode instructions.
 *
 * <p>
 * The {@code CallExecutor} acts as the bridge between function-related
 * bytecode instructions and their runtime implementation. It delegates
 * function invocation, closure creation, and function return semantics to
 * {@link CallOperations}.
 * </p>
 *
 * <p>
 * The supported instructions are:
 * </p>
 *
 * <ul>
 *   <li>{@link CallOpCode#CALL} invokes a function identified by the
 *       instruction operands.</li>
 *   <li>{@link CallOpCode#CALL_VALUE} invokes a function value obtained
 *       from the operand stack.</li>
 *   <li>{@link CallOpCode#CREATE_CLOSURE} creates a closure for a function
 *       identified by the instruction operand.</li>
 *   <li>{@link CallOpCode#RETURN} returns from the current function.</li>
 * </ul>
 */
public final class CallExecutor implements IExecutor<CallOpCode> {

  /**
   * Executes a single function-related bytecode instruction.
   *
   * <p>
   * The instruction opcode determines which function operation is performed.
   * The corresponding operation is delegated to {@link CallOperations}.
   * </p>
   *
   * @param vm the virtual machine executing the instruction
   * @param program the bytecode program containing the function definitions
   * @param instruction the instruction to execute
   */
  @Override
  public void execute(VirtualMachine vm, BytecodeProgram program, Instruction<CallOpCode> instruction) {
    switch (instruction.opcode()) {
      case CALL -> call(vm, program, instruction);
      case CALL_VALUE -> callValue(vm, program);
      case CREATE_CLOSURE -> createClosure(vm, program, instruction);
      case RETURN -> ret(vm);
    }
  }

  /**
   * Invokes the function referenced by the instruction.
   *
   * <p>
   * The instruction operands identify the function and provide the
   * information required by {@link CallOperations#call}.
   * </p>
   *
   * @param vm the virtual machine executing the instruction
   * @param program the bytecode program containing the function definition
   * @param instruction the call instruction
   */
  private void call(VirtualMachine vm, BytecodeProgram program, Instruction<CallOpCode> instruction) {
    CallOperations.call(vm, program, instruction.operand(0), instruction.operand(1));
  }

  /**
   * Invokes the function value on top of the operand stack.
   *
   * <p>
   * The function value and its call information are obtained from the
   * current VM execution context by {@link CallOperations#callValue}.
   * </p>
   *
   * @param vm the virtual machine executing the instruction
   * @param program the bytecode program containing the function definitions
   */
  private void callValue(VirtualMachine vm, BytecodeProgram program) {
    CallOperations.callValue(vm, program);
  }

  /**
   * Creates a closure for the function referenced by the instruction.
   *
   * <p>
   * The instruction identifies the function definition whose closure is
   * created by {@link CallOperations#createClosure}.
   * </p>
   *
   * @param vm the virtual machine executing the instruction
   * @param program the bytecode program containing the function definition
   * @param instruction the closure creation instruction
   */
  private void createClosure(VirtualMachine vm, BytecodeProgram program, Instruction<CallOpCode> instruction) {
    CallOperations.createClosure(vm, program, instruction.operand(0));
  }

  /**
   * Returns from the current function.
   *
   * <p>
   * The current call frame is handled by {@link CallOperations#ret}.
   * </p>
   *
   * @param vm the virtual machine executing the instruction
   */
  private void ret(VirtualMachine vm) {
    CallOperations.ret(vm);
  }

}