package dev.morphe.semantic.symbol;

/**
 * Identifies the semantic category of a {@link Symbol}.
 */
public enum SymbolKind {

  /**
   * A mutable variable declared in the program.
   */
  VARIABLE,

  /**
   * An immutable value declared in the program.
   */
  CONSTANT,

  /**
   * A parameter declared by a function or lambda.
   */
  PARAMETER,

  /**
   * A function declaration.
   */
  FUNCTION,

  /**
   * A named type declaration.
   */
  TYPE
}