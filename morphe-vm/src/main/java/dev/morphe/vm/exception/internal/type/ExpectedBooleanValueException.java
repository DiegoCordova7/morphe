package dev.morphe.vm.exception.internal.type;

import dev.morphe.runtime.value.IValue;

/**
 * Thrown when the virtual machine expects a Boolean runtime value
 * but receives a value of another kind.
 */
public final class ExpectedBooleanValueException extends ExpectedTypeValueException {

  /**
   * Creates an exception for an unexpected Boolean value.
   *
   * @param value the value received by the virtual machine
   */
  public ExpectedBooleanValueException(IValue value) {
    super("Boolean", value);
  }

  /**
   * Creates an exception for an unexpected Boolean value with a cause.
   *
   * @param cause the cause of this exception
   * @param value the value received by the virtual machine
   */
  public ExpectedBooleanValueException(Throwable cause, IValue value) {
    super(cause, "Boolean", value);
  }
}