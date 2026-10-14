package dev.morphe.vm.io;

/**
 * Provides output operations required by the Morphe virtual machine.
 *
 * <p>
 * The virtual machine depends only on this abstraction and does not directly
 * interact with the underlying execution environment. Implementations may
 * redirect VM output to a console, file, buffer, or another output target.
 * </p>
 */
public interface IVMIOHandler {

  /**
   * Writes text to the execution output.
   *
   * @param output the text to write
   */
  void print(String output);
}