package dev.morphe.parser.cursor;

import dev.morphe.lexer.kind.ITokenKind;
import dev.morphe.lexer.kind.SpecialTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.exception.argument.InvalidArgumentException;
import dev.morphe.parser.exception.argument.NullArgumentException;

import java.util.List;

/**
 * Provides sequential access to the tokens produced by the Morphe lexer.
 *
 * <p>The cursor maintains the current position within the token stream and
 * provides operations for inspecting and consuming tokens without exposing
 * the underlying collection.</p>
 *
 * <p>The token stream is expected to contain an {@link SpecialTokenKind#EOF}
 * token as its final token. This invariant is guaranteed by the lexer.</p>
 */
public final class TokenCursor {

  private final List<Token> tokens;
  private int position;

  /**
   * Creates a token cursor over the given token stream.
   *
   * @param tokens the tokens to traverse
   * @throws NullArgumentException if {@code tokens} is {@code null}
   */
  public TokenCursor(List<Token> tokens) {
    if (tokens == null) throw new NullArgumentException("tokens");
    if (tokens.isEmpty()) throw new InvalidArgumentException("tokens", "must contain an EOF token");
    if (tokens.get(tokens.size() - 1).kind() != SpecialTokenKind.EOF)
      throw new InvalidArgumentException("tokens", "must end with an EOF token");
    this.tokens = List.copyOf(tokens);
  }

  /**
   * Returns the current token without advancing the cursor.
   *
   * @return the current token
   */
  public Token current() {
    return tokens.get(position);
  }

  /**
   * Returns the token at the specified offset from the current position.
   *
   * <p>If the requested position is beyond the end of the token stream,
   * the final EOF token is returned.</p>
   *
   * @param offset the number of tokens ahead of the current position
   * @return the token at the specified offset, or EOF if the requested
   *     position exceeds the token stream
   * @throws InvalidArgumentException if {@code offset} is negative
   */
  public Token peek(int offset) {
    if (offset < 0) throw new InvalidArgumentException("offset", "must not be negative");
    int index = position + offset;
    return tokens.get(Math.min(index, tokens.size() - 1));
  }

  /**
   * Consumes the current token and advances the cursor.
   *
   * <p>If the cursor is already positioned at EOF, EOF is returned and
   * the cursor remains at EOF.</p>
   *
   * @return the consumed token
   */
  public Token advance() {
    Token token = current();
    if (!isAtEnd()) position++;
    return token;
  }

  /**
   * Determines whether the current token has the specified kind.
   *
   * @param kind the token kind to check
   * @return {@code true} if the current token has the specified kind;
   *     {@code false} otherwise
   * @throws NullArgumentException if {@code kind} is {@code null}
   */
  public boolean check(ITokenKind kind) {
    if (kind == null) throw new NullArgumentException("kind");
    return current().kind().equals(kind);
  }

  /**
   * Consumes the current token if it has the specified kind.
   *
   * @param kind the token kind to match
   * @return {@code true} if the token was consumed;
   *     {@code false} otherwise
   * @throws NullArgumentException if {@code kind} is {@code null}
   */
  public boolean match(ITokenKind kind) {
    if (!check(kind)) return false;
    advance();
    return true;
  }

  /**
   * Determines whether the cursor is positioned at the end of the token
   * stream.
   *
   * @return {@code true} if the current token is EOF;
   *     {@code false} otherwise
   */
  public boolean isAtEnd() {
    return current().kind() == SpecialTokenKind.EOF;
  }

  /**
   * Returns the token immediately preceding the current position.
   *
   * <p>If the cursor is at the beginning of the token stream, the current
   * token is returned.</p>
   *
   * @return the previous token, or the current token if the cursor is at
   *     the beginning of the stream
   */
  public Token previous() {
    return tokens.get(Math.max(0, position - 1));
  }

  /**
   * Returns the current zero-based token position.
   *
   * @return the current token index
   */
  public int position() {
    return position;
  }
}