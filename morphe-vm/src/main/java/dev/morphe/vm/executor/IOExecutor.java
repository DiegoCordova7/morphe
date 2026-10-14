package dev.morphe.vm.executor;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.IOOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.runtime.value.IValue;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.io.PendingInputKind;

import static dev.morphe.vm.operations.IOOperations.print;

/**
 * Executes input/output-related bytecode instructions.
 *
 * <p>
 * This executor handles output by consuming a runtime value from the operand
 * stack and delegating it to the virtual machine's I/O handler. Input
 * instructions suspend execution and mark the virtual machine as waiting for
 * an external value of the requested type.
 * </p>
 *
 * <p>
 * Supported operations include:
 * </p>
 *
 * <ul>
 *   <li>{@code PRINT} – consumes the top value from the operand stack and
 *       sends its textual representation to the I/O handler.</li>
 *   <li>{@code INPUT_STRING} – waits for a string input.</li>
 *   <li>{@code INPUT_INTEGER} – waits for an integer input.</li>
 *   <li>{@code INPUT_DOUBLE} – waits for a double input.</li>
 *   <li>{@code INPUT_BOOLEAN} – waits for a boolean input.</li>
 * </ul>
 *
 * <p>
 * Input instructions do not read the external input directly. Instead, they
 * transition the virtual machine into its input-waiting state through
 * {@link VirtualMachine#pauseForInput(PendingInputKind)}. The corresponding
 * runtime value is supplied when the pending input is completed.
 * </p>
 */
public final class IOExecutor implements IExecutor<IOOpCode> {

  /**
   * Executes an I/O bytecode instruction.
   *
   * @param vm the virtual machine executing the instruction
   * @param program the bytecode program containing the instruction
   * @param instruction the instruction to execute
   */
  @Override
  public void execute(VirtualMachine vm, BytecodeProgram program, Instruction<IOOpCode> instruction) {
    switch (instruction.opcode()) {
      case PRINT -> {
        IValue value = vm.context().stack().pop();
        print(vm.ioHandler(), value);
      }
      case INPUT_STRING -> vm.pauseForInput(PendingInputKind.STRING);
      case INPUT_INTEGER -> vm.pauseForInput(PendingInputKind.INTEGER);
      case INPUT_DOUBLE -> vm.pauseForInput(PendingInputKind.DOUBLE);
      case INPUT_BOOLEAN -> vm.pauseForInput(PendingInputKind.BOOLEAN);
    }
  }
}