package dev.morphe.semantic.exception.scope;

import dev.morphe.semantic.exception.SemanticException;

/**
 * Base exception for invalid operations performed on semantic scopes.
 *
 * <p>This exception represents an operation that violates the structural
 * rules of the semantic scope hierarchy.</p>
 */
public abstract class InvalidScopeOperationException extends SemanticException {

  /**
   * Creates an invalid scope operation exception with the specified message.
   *
   * @param message the message describing the invalid scope operation
   */
  public InvalidScopeOperationException(String message) {
    super(message);
  }
}