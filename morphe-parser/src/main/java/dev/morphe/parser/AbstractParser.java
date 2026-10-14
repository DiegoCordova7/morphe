package dev.morphe.parser;

import dev.morphe.lexer.kind.ITokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.exception.argument.NullArgumentException;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;

/**
 * Base class for parser components that consume tokens through a
 * {@link TokenCursor}.
 *
 * <p>This class provides shared access to the token cursor and common
 * operations used by specialized parsers. Concrete parsers extend this
 * class when they need to consume and validate tokens directly.</p>
 *
 * <p>The class does not define any parsing grammar itself. Instead, it
 * provides the common infrastructure used by parsers for declarations,
 * statements, expressions, patterns, and other syntactic constructs.</p>
 */
public abstract class AbstractParser {

  /**
   * Token cursor used by this parser to inspect and consume tokens.
   */
  protected final TokenCursor cursor;

  /**
   * Creates a parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the token stream
   * @throws NullArgumentException if {@code cursor} is {@code null}
   */
  protected AbstractParser(TokenCursor cursor) {
    if (cursor == null) throw new NullArgumentException("cursor");
    this.cursor = cursor;
  }

  /**
   * Consumes the current token if it has the expected kind.
   *
   * <p>If the current token does not have the specified kind, parsing
   * fails immediately with an {@link UnexpectedTokenException}. When
   * the expected token is present, it is consumed and returned.</p>
   *
   * @param kind the expected token kind
   * @return the consumed token
   * @throws NullArgumentException if {@code kind} is {@code null}
   * @throws UnexpectedTokenException if the current token does not have
   *     the expected kind
   */
  protected Token consume(ITokenKind kind) {
    if (!cursor.check(kind)) throw new UnexpectedTokenException(cursor.current(), kind);
    return cursor.advance();
  }
}