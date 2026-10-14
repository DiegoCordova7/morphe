package dev.morphe.vm.exception.internal.stack;

import dev.morphe.vm.exception.internal.VMInternalException;

/**
 * Base exception for errors involving the Morphe VM value stack.
 *
 * <p>This exception represents failures related to stack operations
 * performed during VM execution.</p>
 */
public abstract class StackException extends VMInternalException {

  /**
   * Creates a stack-related exception.
   *
   * @param message the message format
   * @param args the arguments used to format the message
   */
  protected StackException(String message, Object... args) {
    super(message, args);
  }

  /**
   * Creates a stack-related exception with an underlying cause.
   *
   * @param cause the underlying cause
   * @param message the message format
   * @param args the arguments used to format the message
   */
  protected StackException(Throwable cause, String message, Object... args) {
    super(cause, message, args);
  }
}