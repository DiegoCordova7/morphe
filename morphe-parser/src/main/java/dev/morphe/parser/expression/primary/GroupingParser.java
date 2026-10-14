package dev.morphe.parser.expression.primary;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.expression.IExpressionParser;

/**
 * Parses grouped expressions enclosed in parentheses.
 *
 * <p>Grouping allows an expression to be evaluated as a single primary
 * expression and can be used to explicitly control expression precedence.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * (1 + 2)
 * (value * 2) + 3
 * ((x + y) * z)
 * }</pre>
 *
 * <p>The parentheses themselves are not represented in the AST. The parser
 * returns the expression contained between them.</p>
 */
public final class GroupingParser extends AbstractParser implements IExpressionParser {

  /**
   * Creates a grouping parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public GroupingParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a grouped expression.
   *
   * <p>The parser consumes the opening parenthesis, parses the enclosed
   * expression, and then consumes the closing parenthesis.</p>
   *
   * @return the expression enclosed by the parentheses
   */
  @Override
  public IExpression parse() {
    consume(PunctuationTokenKind.LEFT_PAREN);
    IExpression expression = new ExpressionParser(cursor).parse();
    consume(PunctuationTokenKind.RIGHT_PAREN);
    return expression;
  }
}