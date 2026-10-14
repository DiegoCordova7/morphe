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
 * Parses logical OR expressions according to their operator precedence.
 *
 * <p>This parser handles the logical OR operator. Logical AND expressions
 * are parsed first, ensuring that logical AND binds more tightly than
 * logical OR.</p>
 *
 * <p>Logical OR operators are parsed from left to right.</p>
 */
public final class LogicalOrParser extends AbstractParser implements IExpressionParser {

  /**
   * Creates a logical OR expression parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public LogicalOrParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a logical OR expression.
   *
   * <p>The expression begins by parsing a logical AND expression.
   * Subsequent logical OR operators are then consumed and combined
   * with the following logical AND expression.</p>
   *
   * @return the parsed logical OR expression
   */
  @Override
  public IExpression parse() {
    IExpression left = new LogicalAndParser(cursor).parse();
    while (cursor.check(OperatorTokenKind.OR)) {
      cursor.advance();
      IExpression right = new LogicalAndParser(cursor).parse();
      SourceRange range = new SourceRange(left.range().start(), right.range().end());
      left = new BinaryExpression(range, left, BinaryOperator.OR, right);
    }
    return left;
  }
}