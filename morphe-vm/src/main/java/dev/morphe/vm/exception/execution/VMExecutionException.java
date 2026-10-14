package dev.morphe.vm.exception.execution;

import dev.morphe.vm.exception.VMException;

/**
 * Base exception for errors that occur during bytecode execution.
 *
 * <p>
 * Execution exceptions represent failures caused by operations performed
 * while the Morphe VM is running a valid bytecode program.
 * </p>
 *
 * <p>
 * These errors originate from runtime operations requested by bytecode
 * instructions, such as invalid arithmetic operations, invalid conversions,
 * or other execution-time failures.
 * </p>
 */
public abstract class VMExecutionException extends VMException {

  /**
   * Creates a new execution exception.
   *
   * @param message the exception message format
   * @param args the arguments used to format the message
   */
  protected VMExecutionException(String message, Object... args) {
    super(message, args);
  }

  /**
   * Creates a new execution exception with a cause.
   *
   * @param cause the underlying cause of this exception
   * @param message the exception message format
   * @param args the arguments used to format the message
   */
  protected VMExecutionException(Throwable cause, String message, Object... args) {
    super(cause, message, args);
  }
}