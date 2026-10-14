package dev.morphe.semantic.exception.expression;

import dev.morphe.ast.statement.IStatement;
import dev.morphe.semantic.exception.SemanticException;

/**
 * Thrown when the last statement of an attempt expression body does not
 * produce a value.
 *
 * <p>The last statement of an attempt body must be an
 * {@code ExpressionStatement}, because its expression determines the
 * resulting value of the attempted body.</p>
 */
public final class InvalidAttemptBodyException extends SemanticException {

  private final IStatement statement;

  /**
   * Creates an exception for an invalid final statement.
   *
   * @param statement the statement that cannot produce the body result
   */
  public InvalidAttemptBodyException(IStatement statement) {
    super("Last statement of attempt body must be an expression statement");
    this.statement = statement;
  }

  /**
   * Returns the invalid final statement.
   *
   * @return the statement that caused the error
   */
  public IStatement statement() {
    return statement;
  }
}