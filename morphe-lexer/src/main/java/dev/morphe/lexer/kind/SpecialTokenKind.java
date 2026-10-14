package dev.morphe.lexer.kind;

/**
 * Represents special lexical tokens that do not correspond to source text.
 */
public enum SpecialTokenKind implements ITokenKind {

  /**
   * End-of-file token indicating that no more source input is available.
   */
  EOF(null);

  private final String lexeme;

  /**
   * Creates a special token kind with the specified lexeme.
   *
   * @param lexeme the source-level representation of the token, or
   *     {@code null} if the token has no source representation
   */
  SpecialTokenKind(String lexeme) {
    this.lexeme = lexeme;
  }

  /**
   * Returns the source-level representation of this special token.
   *
   * @return the token lexeme, or {@code null} if the token has no
   *     source-level representation
   */
  @Override
  public String lexeme() {
    return lexeme;
  }
}