package dev.morphe.vm.exception.internal.memory;

import dev.morphe.vm.exception.internal.VMInternalException;

/**
 * Thrown when attempting to release an already freed memory address.
 */
public final class MemoryAddressAlreadyFreedException extends VMInternalException {

  /**
   * Creates an exception for an already freed memory address.
   *
   * @param address the memory address that has already been freed
   */
  public MemoryAddressAlreadyFreedException(int address) {
    super("Memory address already freed: %d", address);
  }
}