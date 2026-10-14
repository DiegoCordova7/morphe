package dev.morphe.vm.exception.internal.type;

import dev.morphe.runtime.value.IValue;

/**
 * Thrown when the virtual machine expects a reference runtime value
 * but receives a value of another kind.
 */
public final class ExpectedReferenceValueException extends ExpectedTypeValueException {

  /**
   * Creates an exception indicating that a reference runtime value
   * was expected but another value kind was received.
   *
   * @param value the unexpected runtime value
   */
  public ExpectedReferenceValueException(IValue value) {
    super("reference", value);
  }

  /**
   * Creates an exception indicating that a reference runtime value
   * was expected but another value kind was received, preserving
   * the original cause.
   *
   * @param cause the underlying cause
   * @param value the unexpected runtime value
   */
  public ExpectedReferenceValueException(Throwable cause, IValue value) {
    super(cause, "reference", value);
  }
}
