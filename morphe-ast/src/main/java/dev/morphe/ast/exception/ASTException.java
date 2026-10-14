package dev.morphe.ast.exception;

/**
 * Base exception for errors originating from the Morphe abstract syntax tree
 * layer.
 *
 * <p>
 * All AST-specific exceptions extend this class, allowing callers to handle
 * AST failures through a common exception type while preserving more specific
 * exception categories when needed.
 * </p>
 *
 * <p>
 * Exception messages are formatted using {@link String#format(String,
 * Object...)}.
 * </p>
 */
public abstract class ASTException extends RuntimeException {

  /**
   * Creates an AST exception with a formatted message.
   *
   * @param message the message format
   * @param args the arguments used to format the message
   */
  protected ASTException(String message, Object... args) {
    super(String.format(message, args));
  }

  /**
   * Creates an AST exception with a formatted message and underlying cause.
   *
   * @param cause the underlying cause
   * @param message the message format
   * @param args the arguments used to format the message
   */
  protected ASTException(Throwable cause, String message, Object... args) {
    super(String.format(message, args), cause);
  }
}