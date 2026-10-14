package dev.morphe.lexer.kind;

/**
 * Identifies lexical tokens representing recoverable error kinds
 * supported by the Morphe language.
 *
 * <p>Each error kind defines the lexeme used to represent it in
 * Morphe source code.</p>
 */
public enum ErrorTokenKind implements ITokenKind {

  /**
   * Represents a division-by-zero error.
   */
  DIVISION_BY_ZERO("DivisionByZero"),

  /**
   * Represents a modulo-by-zero error.
   */
  MODULO_BY_ZERO("ModuloByZero"),

  /**
   * Represents an invalid array index error.
   */
  INVALID_ARRAY_INDEX("InvalidArrayIndex");

  private final String lexeme;

  ErrorTokenKind(String lexeme) {
    this.lexeme = lexeme;
  }

  /**
   * Returns the source lexeme associated with this error kind.
   *
   * @return the error kind lexeme
   */
  @Override
  public String lexeme() {
    return lexeme;
  }
}