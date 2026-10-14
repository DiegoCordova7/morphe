package dev.morphe.vm.operations;

import dev.morphe.runtime.value.IValue;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.io.IVMIOHandler;

/**
 * Provides operations for interacting with the virtual machine I/O handler.
 *
 * <p>
 * These operations delegate input and output behavior to an
 * {@link IVMIOHandler}, keeping I/O environment details outside the
 * virtual machine execution logic.
 * </p>
 */
public final class IOOperations {

  /**
   * Prevents instantiation of this utility class.
   */
  private IOOperations() {}

  /**
   * Prints a runtime value through the specified I/O handler.
   *
   * <p>
   * The value is converted to its textual representation using
   * {@link Object#toString()} before being passed to the I/O handler.
   * </p>
   *
   * @param ioHandler the I/O handler responsible for output
   * @param value the runtime value to print
   *
   * @throws NullArgumentException if {@code ioHandler} or {@code value}
   *     is {@code null}
   */
  public static void print(IVMIOHandler ioHandler, IValue value) {
    if (ioHandler == null) throw new NullArgumentException("ioHandler");
    if (value == null) throw new NullArgumentException("value");
    ioHandler.print(value.toString());
  }
}