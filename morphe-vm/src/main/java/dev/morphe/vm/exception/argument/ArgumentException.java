package dev.morphe.vm.exception.argument;

import dev.morphe.vm.exception.VMException;

/**
 * Base exception for errors caused by invalid arguments provided
 * to Morphe VM operations or components.
 *
 * <p>This exception represents failures caused by arguments that
 * violate the requirements of a VM operation or component.</p>
 */
public abstract class ArgumentException extends VMException {

  /**
   * Creates an argument-related exception.
   *
   * @param message the message format
   * @param args the arguments used to format the message
   */
  protected ArgumentException(String message, Object... args) {
    super(message, args);
  }

  /**
   * Creates an argument-related exception with an underlying cause.
   *
   * @param cause the underlying cause
   * @param message the message format
   * @param args the arguments used to format the message
   */
  protected ArgumentException(Throwable cause, String message, Object... args) {
    super(cause, message, args);
  }
}