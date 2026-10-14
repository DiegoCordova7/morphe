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
 * Parses relational expressions according to their operator precedence.
 *
 * <p>This parser handles greater-than, greater-than-or-equal-to,
 * less-than, and less-than-or-equal-to comparisons. Additive expressions
 * are parsed first, ensuring that arithmetic operators bind more tightly
 * than relational operators.</p>
 *
 * <p>Relational operators are parsed from left to right.</p>
 */
public final class RelationalParser extends AbstractParser implements IExpressionParser {

  /**
   * Creates a relational expression parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public RelationalParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a relational expression.
   *
   * <p>The expression begins by parsing an additive expression. Subsequent
   * relational operators are consumed and combined with the following
   * additive expression.</p>
   *
   * @return the parsed relational expression
   */
  @Override
  public IExpression parse() {
    IExpression left = new AdditiveParser(cursor).parse();
    while (cursor.check(OperatorTokenKind.GREATER) || cursor.check(OperatorTokenKind.GREATER_EQUAL)
        || cursor.check(OperatorTokenKind.LESS) || cursor.check(OperatorTokenKind.LESS_EQUAL)) {
      BinaryOperator operator;
      if (cursor.check(OperatorTokenKind.GREATER)) operator = BinaryOperator.GREATER;
      else if (cursor.check(OperatorTokenKind.GREATER_EQUAL)) operator = BinaryOperator.GREATER_OR_EQUAL;
      else if (cursor.check(OperatorTokenKind.LESS)) operator = BinaryOperator.LESS;
      else operator = BinaryOperator.LESS_OR_EQUAL;
      cursor.advance();
      IExpression right = new AdditiveParser(cursor).parse();
      SourceRange range = new SourceRange(left.range().start(), right.range().end());
      left = new BinaryExpression(range, left, operator, right);
    }
    return left;
  }
}