package dev.morphe.vm.exception.internal.type;

import dev.morphe.runtime.value.IValue;

/**
 * Thrown when the virtual machine expects a numeric runtime value
 * but receives a value of another kind.
 */
public final class ExpectedNumericValueException extends ExpectedTypeValueException {

  /**
   * Creates an exception for an unexpected numeric value.
   *
   * @param value the value received by the virtual machine
   */
  public ExpectedNumericValueException(IValue value) {
    super("numeric", value);
  }

  /**
   * Creates an exception for an unexpected numeric value with a cause.
   *
   * @param cause the cause of this exception
   * @param value the value received by the virtual machine
   */
  public ExpectedNumericValueException(Throwable cause, IValue value) {
    super(cause, "numeric", value);
  }
}