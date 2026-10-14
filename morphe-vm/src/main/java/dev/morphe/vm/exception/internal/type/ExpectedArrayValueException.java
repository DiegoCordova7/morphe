package dev.morphe.vm.exception.internal.type;

import dev.morphe.runtime.value.IValue;

/**
 * Thrown when the virtual machine expects an array runtime value
 * but encounters a value of another runtime kind.
 *
 * <p>
 * This exception represents an invalid internal execution state rather
 * than an error caused directly by a Morphe program. Array bytecode
 * instructions are expected to operate on runtime values representing
 * arrays.
 * </p>
 *
 * <p>
 * If this exception is thrown, it typically indicates malformed bytecode,
 * invalid compiler output, or a bug in the virtual machine.
 * </p>
 */
public final class ExpectedArrayValueException extends ExpectedTypeValueException {

  /**
   * Creates an exception indicating that an array runtime value was
   * expected but another value kind was encountered.
   *
   * @param value the unexpected runtime value
   */
  public ExpectedArrayValueException(IValue value) {
    super("array", value);
  }

  /**
   * Creates an exception indicating that an array runtime value was
   * expected but another value kind was encountered, preserving
   * the original cause.
   *
   * @param cause the underlying cause
   * @param value the unexpected runtime value
   */
  public ExpectedArrayValueException(Throwable cause, IValue value) {
    super(cause, "array", value);
  }
}