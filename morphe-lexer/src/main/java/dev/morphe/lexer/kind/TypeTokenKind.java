package dev.morphe.lexer.kind;

/**
 * Represents type keywords recognized by the Morphe lexer.
 */
public enum TypeTokenKind implements ITokenKind {

  /** Integer type. */
  INTEGER("Integer"),

  /** Double type. */
  DOUBLE("Double"),

  /** Boolean type. */
  BOOLEAN("Boolean"),

  /** String type. */
  STRING("String"),

  /** Void type. */
  VOID_TYPE("Void");

  private final String lexeme;

  /**
   * Creates a type token kind with the specified source-level lexeme.
   *
   * @param lexeme the source-level representation of the type keyword
   */
  TypeTokenKind(String lexeme) {
    this.lexeme = lexeme;
  }

  /**
   * Returns the source-level representation of this type token.
   *
   * @return the type keyword lexeme
   */
  @Override
  public String lexeme() {
    return lexeme;
  }
}