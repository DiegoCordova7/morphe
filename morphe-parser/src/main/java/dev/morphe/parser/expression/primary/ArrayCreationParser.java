package dev.morphe.parser.expression.primary;

import dev.morphe.ast.expression.ArrayCreationExpression;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.type.IASTType;
import dev.morphe.lexer.kind.KeywordTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.expression.IExpressionParser;
import dev.morphe.parser.source.SourceConverter;
import dev.morphe.parser.type.TypeParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses array creation expressions.
 *
 * <p>An array creation expression consists of the {@code new} keyword,
 * an element type, and one or more dimensions. Each dimension contains
 * an expression that determines the size of that dimension.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * new Integer[10]
 * new String[size]
 * new Integer[rows][columns]
 * }</pre>
 *
 * <p>The element type is parsed as a base type, while the dimensions are
 * parsed as expressions. This allows array sizes to be determined by
 * arbitrary expressions.</p>
 */
public final class ArrayCreationParser extends AbstractParser implements IExpressionParser {

  /**
   * Creates an array creation parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public ArrayCreationParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses an array creation expression.
   *
   * <p>The parser consumes the {@code new} keyword, parses the element
   * type, and then parses one or more bracketed dimension expressions.</p>
   *
   * @return the parsed array creation expression
   */
  @Override
  public IExpression parse() {
    Token start = consume(KeywordTokenKind.NEW);
    IASTType elementType = new TypeParser(cursor).parseBaseType();
    List<IExpression> dimensions = new ArrayList<>();
    do {
      consume(PunctuationTokenKind.LEFT_BRACKET);
      IExpression dimension = new ExpressionParser(cursor).parse();
      dimensions.add(dimension);
      consume(PunctuationTokenKind.RIGHT_BRACKET);
    } while (cursor.check(PunctuationTokenKind.LEFT_BRACKET));
    Token end = cursor.previous();
    SourceRange range = SourceConverter.toASTRange(start, end);
    return new ArrayCreationExpression(range, elementType, dimensions);
  }
}