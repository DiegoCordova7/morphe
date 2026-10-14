package dev.morphe.vm.exception.internal.type;

import dev.morphe.bytecode.type.IBytecodeType;
import dev.morphe.vm.exception.internal.VMInternalException;

/**
 * Thrown when the virtual machine encounters a bytecode type that it
 * does not know how to resolve into a runtime type.
 *
 * <p>
 * Bytecode types are resolved by the VM when execution requires runtime
 * type information. Every supported implementation of
 * {@link IBytecodeType} must have a corresponding resolution strategy.
 * </p>
 *
 * <p>
 * This exception indicates an unsupported or unrecognized bytecode type
 * rather than an invalid value produced by a Morphe program. It typically
 * indicates that the bytecode module introduced a new type that the
 * virtual machine does not yet support.
 * </p>
 */
public final class UnsupportedBytecodeTypeException extends VMInternalException {

  /**
   * Creates an exception indicating that the specified bytecode type
   * cannot be resolved by the virtual machine.
   *
   * @param type the unsupported bytecode type
   */
  public UnsupportedBytecodeTypeException(IBytecodeType type) {
    super("Unsupported bytecode type '%s'", type.getClass().getName());
  }

  /**
   * Creates an exception indicating that the specified bytecode type
   * cannot be resolved by the virtual machine, preserving the original cause.
   *
   * @param cause the underlying cause
   * @param type the unsupported bytecode type
   */
  public UnsupportedBytecodeTypeException(Throwable cause, IBytecodeType type) {
    super(cause, "Unsupported bytecode type '%s'", type.getClass().getName());
  }
}