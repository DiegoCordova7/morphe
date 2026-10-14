package dev.morphe.vm.exception.internal.instruction;

import dev.morphe.bytecode.instructions.opcodes.IOpCode;
import dev.morphe.vm.exception.internal.VMInternalException;

/**
 * Thrown when the virtual machine encounters an opcode for which
 * no executor has been registered.
 *
 * <p>
 * This exception indicates an invalid internal VM state and should
 * never occur during the execution of valid bytecode.
 * </p>
 *
 * <p>
 * If this exception is thrown, it typically indicates malformed
 * bytecode, an unsupported bytecode version, or a missing executor
 * registration in the virtual machine.
 * </p>
 */
public final class UnsupportedOpcodeException extends VMInternalException {

  /**
   * Creates an exception indicating that the specified opcode
   * is not supported by the virtual machine.
   *
   * @param opcode the unsupported opcode
   */
  public UnsupportedOpcodeException(IOpCode opcode) {
    super("Unsupported opcode '%s'", opcode);
  }

  /**
   * Creates an exception indicating that the specified opcode
   * is not supported by the virtual machine, preserving the
   * original cause.
   *
   * @param cause the underlying cause
   * @param opcode the unsupported opcode
   */
  public UnsupportedOpcodeException(Throwable cause, IOpCode opcode) {
    super(cause, "Unsupported opcode '%s'", opcode);
  }
}
