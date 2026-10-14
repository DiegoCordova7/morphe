package dev.morphe.lexer.kind;

/**
 * Represents a category of lexical tokens recognized by the Morphe lexer.
 *
 * <p>Token kinds are divided into specialized categories such as keywords,
 * types, literals, operators, and punctuation.</p>
 */
public interface ITokenKind {

  /**
   * Returns the source-level lexeme associated with this token kind.
   *
   * @return the lexeme, or {@code null} if this token kind has no fixed
   *     source-level representation
   */
  String lexeme();

  /**
   * Indicates whether this token kind has a fixed source-level lexeme.
   *
   * @return {@code true} if a lexeme is defined; {@code false} otherwise
   */
  default boolean hasLexeme() {
    return lexeme() != null;
  }
}