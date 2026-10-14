package dev.morphe.semantic.exception.symbol;

import dev.morphe.semantic.exception.SemanticException;

/**
 * Base exception for failures that occur while resolving symbols during
 * semantic analysis.
 *
 * <p>Symbol resolution includes locating symbols in the active lexical
 * scope and validating that the resolved symbols are appropriate for the
 * semantic operation being performed.</p>
 */
public abstract class SymbolResolutionException extends SemanticException {

  /**
   * Creates a symbol resolution exception.
   *
   * @param message the detail message describing the resolution failure
   */
  protected SymbolResolutionException(String message) {
    super(message);
  }
}