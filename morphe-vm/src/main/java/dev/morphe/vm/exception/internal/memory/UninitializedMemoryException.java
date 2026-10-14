package dev.morphe.vm.exception.internal.memory;

import dev.morphe.vm.exception.internal.VMInternalException;

/**
 * Thrown when the virtual machine attempts to read from a memory address
 * that has not been initialized.
 *
 * <p>
 * This exception indicates an invalid internal execution state rather than
 * an error in a Morphe program. Under normal circumstances, the compiler
 * guarantees that every memory load is preceded by a corresponding store,
 * making this exception unreachable for valid bytecode.
 * </p>
 *
 * <p>
 * If this exception is thrown, it usually indicates one of the following:
 * </p>
 *
 * <ul>
 *   <li>Malformed or corrupted bytecode.</li>
 *   <li>An invalid compiler code generation.</li>
 *   <li>A bug in the virtual machine implementation.</li>
 * </ul>
 */
public final class UninitializedMemoryException extends VMInternalException {

  /**
   * Creates an exception indicating that the specified memory address
   * has not been initialized.
   *
   * @param address the uninitialized memory address
   */
  public UninitializedMemoryException(int address) {
    super("Memory address '%d' has not been initialized", address);
  }

  /**
   * Creates an exception indicating that the specified memory address
   * has not been initialized, preserving the original cause.
   *
   * @param cause the underlying cause
   * @param address the uninitialized memory address
   */
  public UninitializedMemoryException(Throwable cause, int address) {
    super(cause, "Memory address '%d' has not been initialized", address);
  }
}