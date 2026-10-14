package dev.morphe.vm.exception.internal.memory;

import dev.morphe.vm.exception.internal.VMInternalException;

/**
 * Thrown when the virtual machine attempts to access a heap address
 * that is not currently allocated.
 *
 * <p>
 * This exception indicates an invalid internal execution state rather
 * than a normal runtime error in a Morphe program. Heap addresses are
 * allocated by the virtual machine and must refer to an existing heap
 * entry when accessed.
 * </p>
 *
 * <p>
 * If this exception is thrown, it typically indicates malformed
 * bytecode, an invalid runtime reference, corrupted VM state, or
 * a bug in the virtual machine implementation.
 * </p>
 */
public final class InvalidHeapAddressException extends VMInternalException {

  /**
   * Creates an exception indicating that the specified heap address
   * is not currently allocated.
   *
   * @param address the invalid heap address
   */
  public InvalidHeapAddressException(int address) {
    super("Invalid heap address: %d", address);
  }

  /**
   * Creates an exception indicating that the specified heap address
   * is not currently allocated, preserving the original cause.
   *
   * @param cause the underlying cause
   * @param address the invalid heap address
   */
  public InvalidHeapAddressException(Throwable cause, int address) {
    super(cause, "Invalid heap address: %d", address);
  }
}