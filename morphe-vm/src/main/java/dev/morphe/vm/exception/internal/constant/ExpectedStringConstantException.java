package dev.morphe.vm.exception.internal.constant;

import dev.morphe.bytecode.constants.IConstant;
import dev.morphe.vm.exception.internal.VMInternalException;

/**
 * Thrown when the VM expects a string constant but encounters a different
 * bytecode constant type.
 */
public final class ExpectedStringConstantException extends VMInternalException {

  /**
   * Creates an exception for a constant that is not a string constant.
   *
   * @param constant the unexpected constant
   */
  public ExpectedStringConstantException(IConstant constant) {
    super("Expected string constant but found '%s'", constant.getClass().getSimpleName());
  }

}