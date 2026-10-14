package dev.morphe.parser.expression.precedence;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.expression.UnaryExpression;
import dev.morphe.ast.operator.UnaryOperator;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.kind.OperatorTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.IExpressionParser;
import dev.morphe.parser.expression.postfix.PostfixParser;
import dev.morphe.parser.expression.primary.PrimaryParser;
import dev.morphe.parser.source.SourceConverter;

/**
 * Parses unary expressions according to their operator precedence.
 *
 * <p>This parser handles the unary minus, and logical NOT
 * operators. Unary operators are parsed recursively, allowing multiple
 * unary operators to be chained.</p>
 *
 * <p>When no unary operator is present, parsing continues with a primary
 * expression followed by any applicable postfix operations.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * -value
 * !condition
 * -!value
 * object.field
 * array[index]
 * function(argument)
 * }</pre>
 */
public final class UnaryParser extends AbstractParser implements IExpressionParser {

  /**
   * Creates a unary expression parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public UnaryParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a unary expression.
   *
   * <p>If the current token represents a supported unary operator, the
   * operator is consumed and the operand is parsed recursively. Otherwise,
   * a primary expression is parsed followed by its postfix operations.</p>
   *
   * @return the parsed unary or primary expression
   */
  @Override
  public IExpression parse() {
    Token operatorToken = cursor.current();
    if (!(operatorToken.kind() instanceof OperatorTokenKind kind)) {
      IExpression expression = new PrimaryParser(cursor).parse();
      return new PostfixParser(cursor).parse(expression);
    }
    UnaryOperator operator = switch (kind) {
      case MINUS -> UnaryOperator.MINUS;
      case NOT -> UnaryOperator.NOT;
      default -> null;
    };
    if (operator == null) {
      IExpression expression = new PrimaryParser(cursor).parse();
      return new PostfixParser(cursor).parse(expression);
    }
    cursor.advance();
    IExpression operand = parse();
    SourceRange range = new SourceRange(
        SourceConverter.toASTPosition(operatorToken.span().start()),
        operand.range().end()
    );
    return new UnaryExpression(range, operator, operand);
  }
}