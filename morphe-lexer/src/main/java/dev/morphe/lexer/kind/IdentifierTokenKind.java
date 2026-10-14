package dev.morphe.lexer.kind;

/**
 * Represents lexical tokens used for identifiers in Morphe source code.
 */
public enum IdentifierTokenKind implements ITokenKind {

  /**
   * Identifier token representing a user-defined name.
   */
  IDENTIFIER;

  /**
   * Returns the source-level representation of this token.
   *
   * <p>Identifiers do not have a fixed lexeme because their source
   * representation is determined by the identifier text itself.</p>
   *
   * @return {@code null} because this token has no fixed lexeme
   */
  @Override
  public String lexeme() {
    return null;
  }
}