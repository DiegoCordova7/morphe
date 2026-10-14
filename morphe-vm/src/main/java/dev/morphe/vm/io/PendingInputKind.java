package dev.morphe.vm.io;

/**
 * Represents the runtime value type expected by the virtual machine when
 * waiting for external input.
 *
 * <p>
 * The virtual machine uses this type to determine how pending input should be
 * converted into a runtime value before being pushed onto the operand stack.
 * </p>
 */
public enum PendingInputKind {

  /**
   * Indicates that the pending input should be interpreted as a string value.
   */
  STRING,

  /**
   * Indicates that the pending input should be interpreted as an integer value.
   */
  INTEGER,

  /**
   * Indicates that the pending input should be interpreted as a double value.
   */
  DOUBLE,

  /**
   * Indicates that the pending input should be interpreted as a boolean value.
   */
  BOOLEAN
}