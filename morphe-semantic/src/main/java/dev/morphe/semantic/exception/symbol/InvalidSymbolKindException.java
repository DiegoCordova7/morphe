package dev.morphe.semantic.exception.symbol;

import dev.morphe.semantic.symbol.SymbolKind;

/**
 * Thrown when a resolved symbol has a semantic kind that is not valid for
 * the operation being performed.
 */
public final class InvalidSymbolKindException extends SymbolResolutionException {

  /**
   * Creates an exception for a symbol with an invalid semantic kind.
   *
   * @param name the name of the symbol with the invalid kind
   * @param kind the expected kind
   */
  public InvalidSymbolKindException(String name, SymbolKind kind) {
    super("Symbol '" + name + "' has invalid kind: " + kind);
  }
}