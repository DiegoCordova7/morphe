package dev.morphe.lexer.kind;

/**
 * Represents lexical tokens corresponding to literal values in Morphe
 * source code.
 */
public enum LiteralTokenKind implements ITokenKind {

  /** Integer literal. */
  INTEGER(null),

  /** Double literal. */
  DOUBLE(null),

  /** String literal. */
  STRING(null),

  /** Boolean literal. */
  BOOLEAN(null),

  /** Null literal. */
  NULL("null");

  private final String lexeme;

  /**
   * Creates a literal token kind with the specified fixed lexeme.
   *
   * @param lexeme the source-level representation of the literal, or
   *     {@code null} if the lexeme is determined by the literal value
   */
  LiteralTokenKind(String lexeme) {
    this.lexeme = lexeme;
  }

  /**
   * Returns the source-level representation of this literal token.
   *
   * @return the literal lexeme, or {@code null} if the token does not have
   *     a fixed lexeme
   */
  @Override
  public String lexeme() {
    return lexeme;
  }
}