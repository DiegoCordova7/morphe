package dev.morphe.parser.expression.postfix;

import dev.morphe.ast.expression.ArrayAccessExpression;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.source.SourceConverter;

/**
 * Parses an array access expression.
 *
 * <p>An array access consists of an already parsed target expression
 * followed by an index expression enclosed in square brackets.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * values[index]
 * matrix[row][column]
 * getValues()[index]
 * }</pre>
 *
 * <p>The opening bracket is consumed by the surrounding postfix parser.
 * This parser receives the target expression and parses only the index
 * and the closing bracket.</p>
 */
public final class ArrayAccessParser extends AbstractParser {

  /**
   * Creates an array access parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public ArrayAccessParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses an array access using the specified target expression.
   *
   * <p>The index is parsed as a complete expression, allowing array
   * accesses such as {@code values[i + 1]} or
   * {@code matrix[row * width + column]}.</p>
   *
   * <p>The resulting source range begins at the start of the target
   * expression and ends at the closing bracket.</p>
   *
   * @param target the expression being indexed
   * @return the parsed array access expression
   */
  public IExpression parse(IExpression target) {
    IExpression index = new ExpressionParser(cursor).parse();
    Token end = consume(PunctuationTokenKind.RIGHT_BRACKET);
    SourceRange range = new SourceRange(
        target.range().start(),
        SourceConverter.toASTPosition(end.span().end())
    );
    return new ArrayAccessExpression(range, target, index);
  }
}