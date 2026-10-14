package dev.morphe.vm.exception.internal.type;

import dev.morphe.runtime.value.IValue;
import dev.morphe.vm.exception.internal.VMInternalException;

/**
 * Thrown when the virtual machine expects a runtime value of a specific
 * type but receives a value of another type.
 *
 * <p>
 * This exception represents an invalid internal execution state rather
 * than a normal error caused by a Morphe program.
 * </p>
 */
public abstract class ExpectedTypeValueException extends VMInternalException {

  /**
   * Creates an exception indicating that a value of the expected type
   * was not received.
   *
   * @param expectedType the expected runtime type
   * @param actualValue the unexpected runtime value
   */
  public ExpectedTypeValueException(String expectedType, IValue actualValue) {
    super("Expected a %s value but received '%s'", expectedType, actualValue.kind().name());
  }

  /**
   * Creates an exception indicating that a value of the expected type
   * was not received, preserving the original cause.
   *
   * @param cause the underlying cause
   * @param expectedType the expected runtime type
   * @param actualValue the unexpected runtime value
   */
  public ExpectedTypeValueException(Throwable cause, String expectedType, IValue actualValue) {
    super(cause, "Expected a %s value but received '%s'", expectedType, actualValue.kind().name());
  }
}