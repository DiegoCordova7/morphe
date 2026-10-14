package dev.morphe.semantic.exception.expression;

import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when a composite instance expression references a type that
 * cannot be instantiated as a composite type.
 *
 * <p>The referenced type exists, but its kind does not represent a
 * {@code CompositeType}.</p>
 */
public final class InvalidCompositeInstanceException extends SemanticException {

  /**
   * Creates an invalid composite instance exception.
   *
   * @param name the name of the referenced type
   * @param type the resolved AST type
   */
  public InvalidCompositeInstanceException(String name, IASTType type) {
    super("Type '" + name + "' cannot be instantiated as a composite type" + " (resolved as '" + type + "')");
  }
}