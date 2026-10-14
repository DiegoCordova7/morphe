package dev.morphe.vm.exception.internal.stack;

/**
 * Thrown when an operation requiring a value from the VM stack
 * is performed while the stack is empty.
 */
public final class EmptyStackException extends StackException {

  /**
   * Creates an exception indicating that the VM stack is empty.
   */
  public EmptyStackException() {
    super("Stack is empty");
  }

  /**
   * Creates an exception indicating that the VM stack is empty.
   *
   * @param cause the underlying cause
   */
  public EmptyStackException(Throwable cause) {
    super(cause, "Stack is empty");
  }
}