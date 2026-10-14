package dev.morphe.parser.expression.primary;

import dev.morphe.ast.expression.ArrayLiteralExpression;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.expression.IExpressionParser;
import dev.morphe.parser.source.SourceConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses array literal expressions.
 *
 * <p>An array literal consists of a comma-separated sequence of expressions
 * enclosed in square brackets. Elements may themselves be arbitrary
 * expressions, including nested array literals.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * []
 * [1, 2, 3]
 * [1, x + 2, function()]
 * [[1, 2], [3, 4]]
 * }</pre>
 *
 * <p>The resulting expression is represented by an
 * {@link ArrayLiteralExpression} containing the parsed elements.</p>
 */
public final class ArrayParser extends AbstractParser implements IExpressionParser {

  /**
   * Creates an array parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public ArrayParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses an array literal expression from the current token.
   *
   * <p>The parser consumes the opening bracket, parses each element as a
   * complete expression separated by commas, and finally consumes the
   * closing bracket.</p>
   *
   * @return the parsed array literal expression
   */
  @Override
  public IExpression parse() {
    Token start = consume(PunctuationTokenKind.LEFT_BRACKET);
    List<IExpression> elements = new ArrayList<>();
    if (!cursor.check(PunctuationTokenKind.RIGHT_BRACKET)) {
      do elements.add(new ExpressionParser(cursor).parse());
      while (cursor.match(PunctuationTokenKind.COMMA));
    }
    Token end = consume(PunctuationTokenKind.RIGHT_BRACKET);
    SourceRange range = SourceConverter.toASTRange(start, end);
    return new ArrayLiteralExpression(range, elements);
  }
}