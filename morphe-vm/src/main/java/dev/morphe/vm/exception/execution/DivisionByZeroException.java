package dev.morphe.vm.exception.execution;

/**
 * Indicates that an arithmetic division was attempted with a zero divisor.
 *
 * <p>This exception is raised when the VM attempts to divide a numeric value
 * by zero.</p>
 */
public final class DivisionByZeroException extends VMExecutionException {

  /**
   * Creates a division by zero exception.
   *
   * @param dividend the value being divided
   * @param divisor the value by which the dividend is being divided
   */
  public DivisionByZeroException(double dividend, double divisor) {
    super("Division by zero: %s / %s", dividend, divisor);
  }
}