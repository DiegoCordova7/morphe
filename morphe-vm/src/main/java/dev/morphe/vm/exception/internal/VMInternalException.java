package dev.morphe.vm.exception.internal;

import dev.morphe.vm.exception.VMException;

/**
 * Base exception for errors that indicate an invalid internal state of the
 * Morphe virtual machine.
 *
 * <p>
 * Internal exceptions represent failures caused by invalid bytecode, corrupted
 * VM state, or violations of assumptions required for correct VM execution.
 * These errors are not expected to be recoverable by user programs.
 * </p>
 *
 * <p>
 * Examples of internal failures include invalid constant references,
 * inconsistent VM state, or malformed execution data.
 * </p>
 */
public abstract class VMInternalException extends VMException {

  /**
   * Creates a new internal VM exception.
   *
   * @param message the exception message format
   * @param args the arguments used to format the message
   */
  protected VMInternalException(String message, Object... args) {
    super(message, args);
  }

  /**
   * Creates a new internal VM exception with a cause.
   *
   * @param cause the underlying cause of this exception
   * @param message the exception message format
   * @param args the arguments used to format the message
   */
  protected VMInternalException(Throwable cause, String message, Object... args) {
    super(cause, message, args);
  }
}