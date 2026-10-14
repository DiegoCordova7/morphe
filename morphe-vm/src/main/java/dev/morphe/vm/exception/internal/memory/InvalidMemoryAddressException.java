package dev.morphe.vm.exception.internal.memory;

import dev.morphe.vm.exception.internal.VMInternalException;

/**
 * Thrown when an invalid memory address is supplied to the memory allocator.
 *
 * <p>
 * A memory address is considered invalid if it was never allocated by the
 * allocator or if it is negative.
 * </p>
 */
public final class InvalidMemoryAddressException extends VMInternalException {

  /**
   * Creates an exception indicating that the specified memory address is invalid.
   *
   * @param address the invalid memory address
   */
  public InvalidMemoryAddressException(int address) {
    super("Invalid memory address: %d", address);
  }

}