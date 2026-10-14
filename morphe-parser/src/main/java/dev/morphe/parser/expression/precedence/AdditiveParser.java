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
 * Parses additive expressions according to their operator precedence.
 *
 * <p>This parser handles addition and subtraction operators. Multiplicative
 * expressions are parsed first, ensuring that multiplication, division, and
 * modulo bind more tightly than additive operators.</p>
 *
 * <p>Additive operators are parsed from left to right, making them
 * left-associative. For example, {@code a - b + c} is parsed as
 * {@code (a - b) + c}.</p>
 */
public final class AdditiveParser extends AbstractParser implements IExpressionParser {

  /**
   * Creates an additive expression parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public AdditiveParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses an additive expression.
   *
   * <p>The expression begins by parsing a multiplicative expression.
   * Subsequent addition or subtraction operators are then consumed and
   * combined with the following multiplicative expression.</p>
   *
   * @return the parsed additive expression
   */
  @Override
  public IExpression parse() {
    IExpression left = new MultiplicativeParser(cursor).parse();
    while (true) {
      Token operatorToken = cursor.current();
      if (!(operatorToken.kind() instanceof OperatorTokenKind kind)) return left;
      BinaryOperator operator = switch (kind) {
        case PLUS -> BinaryOperator.ADD;
        case MINUS -> BinaryOperator.SUBTRACT;
        default -> null;
      };
      if (operator == null) return left;
      cursor.advance();
      IExpression right = new MultiplicativeParser(cursor).parse();
      SourceRange range = new SourceRange(left.range().start(), right.range().end());
      left = new BinaryExpression(range, left, operator, right);
    }
  }
}