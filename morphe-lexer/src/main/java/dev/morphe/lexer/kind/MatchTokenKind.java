package dev.morphe.lexer.kind;

/**
 * Represents lexical tokens specific to Morphe pattern matching.
 *
 * <p>These tokens are only meaningful within {@code match} expressions
 * and their associated patterns.</p>
 */
public enum MatchTokenKind implements ITokenKind {

  /**
   * Pattern alternation operator.
   *
   * <p>Matches when any of the patterns on either side matches.</p>
   *
   * <pre>
   * 1 | 2 | 3
   * </pre>
   */
  OR("|"),

  /**
   * Match arm separator.
   *
   * <p>Associates a pattern with the expression produced when that
   * pattern matches.</p>
   *
   * <pre>
   * 1 => "one"
   * </pre>
   */
  ARROW("=>"),

  /**
   * Inclusive range pattern operator.
   *
   * <p>Both endpoints are included in the matched range.</p>
   *
   * <pre>
   * 1 .. 10
   * </pre>
   */
  RANGE(".."),

  /**
   * Rest pattern operator.
   *
   * <p>Introduces a pattern that matches the remaining elements of a
   * sequence.</p>
   *
   * <pre>{@code
   * [first, ...rest]
   * }</pre>
   */
  ELLIPSIS("..."),

  /**
   * Wildcard pattern.
   *
   * <p>Matches any value and is commonly used as the fallback pattern.</p>
   *
   * <pre>
   * _ => "other"
   * </pre>
   */
  WILDCARD("_");

  private final String lexeme;

  MatchTokenKind(String lexeme) {
    this.lexeme = lexeme;
  }

  /**
   * Returns the source-level representation of this token.
   *
   * @return the token lexeme
   */
  public String lexeme() {
    return lexeme;
  }
}