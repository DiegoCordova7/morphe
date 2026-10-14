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
 * Parses multiplicative expressions according to their operator precedence.
 *
 * <p>This parser handles multiplication, division, and modulo operations.
 * Unary expressions are parsed first, ensuring that unary operators bind
 * more tightly than multiplicative operators.</p>
 *
 * <p>Multiplicative operators are parsed from left to right.</p>
 */
public final class MultiplicativeParser extends AbstractParser implements IExpressionParser {

  /**
   * Creates a multiplicative expression parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public MultiplicativeParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a multiplicative expression.
   *
   * <p>The expression begins by parsing a unary expression. Subsequent
   * multiplicative operators are consumed and combined with the following
   * unary expression.</p>
   *
   * @return the parsed multiplicative expression
   */
  @Override
  public IExpression parse() {
    IExpression left = new UnaryParser(cursor).parse();
    while (true) {
      Token operatorToken = cursor.current();
      if (!(operatorToken.kind() instanceof OperatorTokenKind kind)) return left;
      BinaryOperator operator = switch (kind) {
        case MULTIPLY -> BinaryOperator.MULTIPLY;
        case DIVIDE -> BinaryOperator.DIVIDE;
        case MODULO -> BinaryOperator.MODULO;
        default -> null;
      };
      if (operator == null) return left;
      cursor.advance();
      IExpression right = new UnaryParser(cursor).parse();
      SourceRange range = new SourceRange(left.range().start(), right.range().end());
      left = new BinaryExpression(range, left, operator, right);
    }
  }
}