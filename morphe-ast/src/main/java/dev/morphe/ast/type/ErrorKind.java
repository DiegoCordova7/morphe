package dev.morphe.ast.type;

/**
 * Identifies the kinds of errors that may be represented by the
 * Morphe language type system.
 *
 * <p>Error kinds are independent of the concrete exception classes
 * used by the runtime or virtual machine.</p>
 */
public enum ErrorKind {
  DIVISION_BY_ZERO,
  MODULO_BY_ZERO,
  INVALID_ARRAY_INDEX
}