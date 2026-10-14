package dev.morphe.ast.exception.argument;

import dev.morphe.ast.exception.ASTException;

/**
 * Base exception for errors involving invalid arguments provided to
 * AST components.
 *
 * <p>
 * This exception represents arguments that violate the requirements
 * of an AST node or AST-related operation.
 * </p>
 */
public abstract class ArgumentException extends ASTException {

  /**
   * Creates an argument-related exception.
   *
   * @param message the message format
   * @param args the arguments used to format the message
   */
  protected ArgumentException(String message, Object... args) {
    super(message, args);
  }

  /**
   * Creates an argument-related exception with an underlying cause.
   *
   * @param cause the underlying cause
   * @param message the message format
   * @param args the arguments used to format the message
   */
  protected ArgumentException(Throwable cause, String message, Object... args) {
    super(cause, message, args);
  }
}