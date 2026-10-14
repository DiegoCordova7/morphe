package dev.morphe.lexer.kind;

/**
 * Represents punctuation and structural delimiters recognized by the
 * Morphe lexer.
 */
public enum PunctuationTokenKind implements ITokenKind {

  /** Left parenthesis. */
  LEFT_PAREN("("),

  /** Right parenthesis. */
  RIGHT_PAREN(")"),

  /** Left brace. */
  LEFT_BRACE("{"),

  /** Right brace. */
  RIGHT_BRACE("}"),

  /** Left bracket. */
  LEFT_BRACKET("["),

  /** Right bracket. */
  RIGHT_BRACKET("]"),

  /** Statement terminator. */
  SEMICOLON(";"),

  /** Comma separator. */
  COMMA(","),

  /** Type or declaration separator. */
  COLON(":"),

  /**
   * Field access operator.
   */
  DOT(".");

  private final String lexeme;

  PunctuationTokenKind(String lexeme) {
    this.lexeme = lexeme;
  }

  /**
   * Returns the source-level representation of this punctuation token.
   *
   * @return the punctuation lexeme
   */
  public String lexeme() {
    return lexeme;
  }
}