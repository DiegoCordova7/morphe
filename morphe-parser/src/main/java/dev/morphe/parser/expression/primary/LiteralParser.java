package dev.morphe.parser.expression.primary;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.kind.LiteralTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import dev.morphe.parser.expression.IExpressionParser;
import dev.morphe.parser.source.SourceConverter;

/**
 * Parses literal expressions into {@link LiteralExpression} nodes.
 *
 * <p>Supported literal kinds are integers, doubles, strings, and booleans.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * 42
 * 3.14
 * "hello"
 * true
 * }</pre>
 */
public final class LiteralParser extends AbstractParser implements IExpressionParser {

  /**
   * Creates a literal parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public LiteralParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses the literal at the current token.
   *
   * <p>The token lexeme is converted to its corresponding Java value before
   * constructing the {@link LiteralExpression}. String delimiters are removed
   * during parsing.</p>
   *
   * @return the parsed literal expression
   * @throws UnexpectedTokenException if the current token is not a supported
   * literal token
   */
  @Override
  public IExpression parse() {
    Token token = cursor.current();
    if (!(token.kind() instanceof LiteralTokenKind kind)) throw new UnexpectedTokenException(token);
    Object value = switch (kind) {
      case INTEGER -> Integer.parseInt(token.lexeme());
      case DOUBLE -> Double.parseDouble(token.lexeme());
      case STRING -> parseString(token.lexeme());
      case BOOLEAN -> Boolean.parseBoolean(token.lexeme());
      case NULL -> token.lexeme();
    };
    cursor.advance();
    SourceRange range = SourceConverter.toASTRange(token, token);
    return new LiteralExpression(range, value);
  }

  /**
   * Removes the surrounding quotation marks from a string literal lexeme.
   *
   * @param lexeme the complete string literal lexeme
   * @return the string contents without its surrounding quotation marks
   */
  private String parseString(String lexeme) {
    return lexeme.substring(1, lexeme.length() - 1);
  }
}