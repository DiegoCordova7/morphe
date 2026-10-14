package dev.morphe.lexer.token;

import dev.morphe.lexer.exception.argument.NullArgumentException;
import dev.morphe.lexer.kind.ITokenKind;
import dev.morphe.lexer.source.SourceSpan;

/**
 * Represents a lexical token produced by the Morphe lexer.
 *
 * <p>A token contains its lexical category, the exact lexeme extracted
 * from the source, and the source range occupied by that lexeme.</p>
 */
public final class Token {

  private final ITokenKind kind;
  private final String lexeme;
  private final SourceSpan span;

  /**
   * Creates a lexical token.
   *
   * @param kind the token kind
   * @param lexeme the exact source text represented by this token
   * @param span the source range occupied by this token
   * @throws NullArgumentException if {@code kind}, {@code lexeme}, or
   * {@code span} is null
   */
  public Token(ITokenKind kind, String lexeme, SourceSpan span) {
    if (kind == null) throw new NullArgumentException("kind");
    if (lexeme == null) throw new NullArgumentException("lexeme");
    if (span == null) throw new NullArgumentException("span");
    this.kind = kind;
    this.lexeme = lexeme;
    this.span = span;
  }

  /**
   * Returns the kind of this token.
   *
   * @return the token kind
   */
  public ITokenKind kind() {
    return kind;
  }

  /**
   * Returns the exact source text represented by this token.
   *
   * @return the token lexeme
   */
  public String lexeme() {
    return lexeme;
  }

  /**
   * Returns the source range occupied by this token.
   *
   * @return the token source span
   */
  public SourceSpan span() {
    return span;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof Token other)) return false;
    return kind.equals(other.kind) && lexeme.equals(other.lexeme) && span.equals(other.span);
  }

  @Override
  public int hashCode() {
    int result = kind.hashCode();
    result = 31 * result + lexeme.hashCode();
    result = 31 * result + span.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Token[" +
        "kind=" + kind +
        ", lexeme='" + lexeme + '\'' +
        ", span=" + span +
        ']';
  }
}