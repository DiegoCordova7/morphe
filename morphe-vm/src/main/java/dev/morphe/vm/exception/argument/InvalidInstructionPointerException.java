package dev.morphe.vm.exception.argument;

/**
 * Thrown when an instruction pointer value is invalid.
 *
 * <p>
 * This exception indicates that a component of the virtual machine attempted
 * to assign a negative instruction pointer.
 * </p>
 */
public final class InvalidInstructionPointerException extends ArgumentException {

  /**
   * Creates an exception for a negative instruction pointer.
   *
   * @param instructionPointer the invalid instruction pointer
   */
  public InvalidInstructionPointerException(int instructionPointer) {
    super("Invalid instruction pointer '%d'. Instruction pointer cannot be negative.", instructionPointer);
  }
}