package dev.morphe.vm.exception.internal.constant;

import dev.morphe.bytecode.constants.IConstant;
import dev.morphe.vm.exception.internal.VMInternalException;

/**
 * Thrown when the VM encounters a constant that it cannot resolve
 * into a runtime value.
 */
public final class InvalidConstantException extends VMInternalException {

  /**
   * Creates an exception for an unsupported bytecode constant.
   *
   * @param constant the invalid constant
   */
  public InvalidConstantException(IConstant constant) {
    super("Cannot resolve bytecode constant of type '%s'", constant.getClass().getSimpleName());
  }

}