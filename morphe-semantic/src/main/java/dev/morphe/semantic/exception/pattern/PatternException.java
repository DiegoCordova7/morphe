package dev.morphe.semantic.exception.pattern;

import dev.morphe.semantic.exception.SemanticException;

/**
 * Base exception for semantic errors related to pattern analysis.
 *
 * <p>Pattern exceptions represent invalid pattern constructs or pattern
 * semantics detected during semantic analysis.</p>
 */
public abstract class PatternException extends SemanticException {

  /**
   * Creates a pattern semantic exception with the specified message.
   *
   * @param message the description of the semantic error
   */
  protected PatternException(String message) {
    super(message);
  }
}