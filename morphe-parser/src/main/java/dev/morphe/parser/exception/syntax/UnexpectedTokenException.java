package dev.morphe.parser.exception.syntax;

import dev.morphe.lexer.kind.ITokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.exception.ParserException;

/**
 * Thrown when the parser encounters a token that is not valid at the
 * current position in the source.
 *
 * <p>This exception indicates a syntax error detected during parsing.
 * The offending token and its source span are included in the exception
 * message to provide a precise diagnostic location.</p>
 */
public final class UnexpectedTokenException extends ParserException {

  /**
   * Creates an exception for an unexpected token.
   *
   * @param token the token that was not valid at the current parser position
   */
  public UnexpectedTokenException(Token token) {
    super(String.format("Unexpected token '%s' at %s", token.lexeme(), token.span()));
  }

  /**
   * Creates an exception for an unexpected token when a specific token
   * kind was expected.
   *
   * @param token the token that was encountered
   * @param expected the token kind that was expected
   */
  public UnexpectedTokenException(Token token, ITokenKind expected) {
    super(String.format("Expected '%s' but found '%s' at %s", expected.lexeme(), token.lexeme(), token.span()));
  }
}