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
 * Parses equality expressions according to their operator precedence.
 *
 * <p>This parser handles equality and inequality operators. Relational
 * expressions are parsed first, ensuring that relational operators bind
 * more tightly than equality operators.</p>
 *
 * <p>Equality operators are parsed from left to right.</p>
 */
public final class EqualityParser extends AbstractParser implements IExpressionParser {

  /**
   * Creates an equality expression parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public EqualityParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses an equality expression.
   *
   * <p>The expression begins by parsing a relational expression.
   * Subsequent equality or inequality operators are then consumed and
   * combined with the following relational expression.</p>
   *
   * @return the parsed equality expression
   */
  @Override
  public IExpression parse() {
    IExpression left = new RelationalParser(cursor).parse();
    while (cursor.check(OperatorTokenKind.EQUAL)
        || cursor.check(OperatorTokenKind.NOT_EQUAL)
        || cursor.check(OperatorTokenKind.STRUCT_MATCH)) {
      BinaryOperator operator;
      if (cursor.check(OperatorTokenKind.EQUAL)) {
        operator = BinaryOperator.EQUAL;
      } else if (cursor.check(OperatorTokenKind.NOT_EQUAL)) {
        operator = BinaryOperator.NOT_EQUAL;
      } else {
        operator = BinaryOperator.STRUCT_MATCH;
      }
      cursor.advance();
      IExpression right = new RelationalParser(cursor).parse();
      SourceRange range = new SourceRange(left.range().start(), right.range().end());
      left = new BinaryExpression(range, left, operator, right);
    }
    return left;
  }
}