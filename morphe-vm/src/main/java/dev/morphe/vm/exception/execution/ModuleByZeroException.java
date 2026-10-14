package dev.morphe.vm.exception.execution;

/**
 * Thrown when a modulo operation is attempted with a zero divisor.
 */
public final class ModuleByZeroException extends VMExecutionException {

  /**
   * Creates a modulo by zero exception.
   *
   * @param dividend the left operand of the modulo operation
   * @param divisor the right operand of the modulo operation
   */
  public ModuleByZeroException(double dividend, double divisor) {
    super("Modulo by zero: %s %% %s", dividend, divisor);
  }
}