package dev.morphe.vm.exception;

/**
 * Base exception for errors raised by the Morphe virtual machine.
 *
 * <p>All VM-specific exceptions extend this class, allowing callers to
 * catch either a specific error or any error originating from the VM.</p>
 */
public abstract class VMException extends RuntimeException {

  /**
   * Creates a VM exception with a formatted message.
   *
   * @param message the message format
   * @param args the arguments used to format the message
   */
  protected VMException(String message, Object... args) {
    super(String.format(message, args));
  }

  /**
   * Creates a VM exception with a formatted message and an underlying cause.
   *
   * @param cause the underlying cause
   * @param message the message format
   * @param args the arguments used to format the message
   */
  protected VMException(Throwable cause, String message, Object... args) {
    super(String.format(message, args), cause);
  }
}