package dev.morphe.semantic.exception.symbol;

/**
 * Thrown when a referenced symbol cannot be found in the active lexical
 * scope or any of its parent scopes.
 */
public final class UnknownSymbolException extends SymbolResolutionException {

  /**
   * Creates an exception for an unresolved symbol.
   *
   * @param name the name of the unresolved symbol
   */
  public UnknownSymbolException(String name) {
    super("Unknown symbol: " + name);
  }
}