package dev.morphe.parser.expression.postfix;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.expression.UpdateExpression;
import dev.morphe.ast.operator.UpdateOperator;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.kind.OperatorTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.source.SourceConverter;

/**
 * Parses postfix operations applied to an expression.
 *
 * <p>Postfix operations extend an already parsed expression and may be
 * chained arbitrarily. Supported postfix operations include:</p>
 *
 * <ul>
 *   <li>field access using {@code .}</li>
 *   <li>array access using {@code []}</li>
 *   <li>function calls using {@code ()}</li>
 *   <li>postfix increment using {@code ++}</li>
 *   <li>postfix decrement using {@code --}</li>
 * </ul>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * object.field
 * array[index]
 * function(argument)
 * value++
 * value--
 * object.method()[index].field
 * }</pre>
 *
 * <p>Because postfix operations may be chained, the parser repeatedly
 * applies the next available postfix operation until none remains.</p>
 */
public final class PostfixParser extends AbstractParser {

  /**
   * Creates a postfix parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public PostfixParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses all postfix operations following the specified expression.
   *
   * <p>The operations are applied from left to right. Each parsed
   * operation becomes the new expression to which subsequent postfix
   * operations are applied.</p>
   *
   * @param expression the expression to which postfix operations are applied
   * @return the resulting expression after all postfix operations have
   *     been parsed
   */
  public IExpression parse(IExpression expression) {
    IExpression result = expression;
    while (true) {
      if (cursor.match(PunctuationTokenKind.DOT)) {
        result = new FieldAccessParser(cursor).parse(result);
        continue;
      }
      if (cursor.match(PunctuationTokenKind.LEFT_BRACKET)) {
        result = new ArrayAccessParser(cursor).parse(result);
        continue;
      }
      if (cursor.match(PunctuationTokenKind.LEFT_PAREN)) {
        result = new FunctionCallParser(cursor).parse(result);
        continue;
      }
      if (cursor.match(OperatorTokenKind.INCREMENT)) {
        result = parseUpdate(result, UpdateOperator.INCREMENT);
        continue;
      }
      if (cursor.match(OperatorTokenKind.DECREMENT)) {
        result = parseUpdate(result, UpdateOperator.DECREMENT);
        continue;
      }
      return result;
    }
  }

  /**
   * Creates a postfix update expression from the specified expression.
   *
   * <p>The update operator is applied after the expression has been
   * evaluated. The source range of the resulting expression extends from
   * the beginning of the original expression to the end of the update
   * operator.</p>
   *
   * @param expression the expression being updated
   * @param operator the postfix update operator
   * @return the resulting postfix update expression
   */
  private IExpression parseUpdate(IExpression expression, UpdateOperator operator) {
    Token token = cursor.previous();
    SourceRange range = new SourceRange(
        expression.range().start(),
        SourceConverter.toASTPosition(token.span().end())
    );
    return new UpdateExpression(range, expression, operator);
  }
}