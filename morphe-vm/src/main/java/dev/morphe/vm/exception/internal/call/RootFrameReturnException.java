package dev.morphe.vm.exception.internal.call;

import dev.morphe.vm.exception.internal.VMInternalException;

/**
 * Thrown when attempting to return from the root call frame.
 *
 * <p>
 * The root call frame represents the initial execution context of the
 * virtual machine and has no caller to return to. Therefore, executing
 * a return operation while the root frame is active is an invalid VM
 * operation.
 * </p>
 */
public final class RootFrameReturnException extends VMInternalException {

  /**
   * Creates a new exception indicating that the root call frame cannot
   * be returned from.
   */
  public RootFrameReturnException() {
    super("Cannot return from the root call frame.");
  }

}
