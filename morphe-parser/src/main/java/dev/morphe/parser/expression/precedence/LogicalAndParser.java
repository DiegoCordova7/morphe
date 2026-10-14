package dev.morphe.parser.expression.precedence;

import dev.morphe.ast.expression.BinaryExpression;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.operator.BinaryOperator;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.kind.OperatorTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.IExpressionParser;

/**
 * Parses logical AND expressions according to their operator precedence.
 *
 * <p>This parser handles the logical AND operator. Equality expressions
 * are parsed first, ensuring that equality and relational operators bind
 * more tightly than logical AND.</p>
 *
 * <p>Logical AND operators are parsed from left to right.</p>
 */
public final class LogicalAndParser extends AbstractParser implements IExpressionParser {

  /**
   * Creates a logical AND expression parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public LogicalAndParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a logical AND expression.
   *
   * <p>The expression begins by parsing an equality expression.
   * Subsequent logical AND operators are then consumed and combined
   * with the following equality expression.</p>
   *
   * @return the parsed logical AND expression
   */
  @Override
  public IExpression parse() {
    IExpression left = new EqualityParser(cursor).parse();
    while (cursor.check(OperatorTokenKind.AND)) {
      cursor.advance();
      IExpression right = new EqualityParser(cursor).parse();
      SourceRange range = new SourceRange(left.range().start(), right.range().end());
      left = new BinaryExpression(range, left, BinaryOperator.AND, right);
    }
    return left;
  }
}