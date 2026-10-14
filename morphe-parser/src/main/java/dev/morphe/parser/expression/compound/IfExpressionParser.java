package dev.morphe.parser.expression.compound;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.expression.IfExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.kind.KeywordTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.expression.IExpressionParser;
import dev.morphe.parser.source.SourceConverter;

/**
 * Parses conditional if expressions.
 *
 * <p>An if expression evaluates a condition and produces the value of
 * either its then branch or its else branch.</p>
 *
 * <p>The general syntax is:</p>
 *
 * <pre>{@code
 * if (condition) thenExpression else elseExpression
 * }</pre>
 *
 * <p>Unlike an {@code if} statement, an if expression requires both
 * branches and produces a value. The semantic compatibility of the
 * condition and branch expressions is determined during semantic
 * analysis.</p>
 */
public final class IfExpressionParser extends AbstractParser implements IExpressionParser {

  /**
   * Creates an if expression parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public IfExpressionParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses an if expression from the current parser position.
   *
   * <p>The parser consumes the condition enclosed in parentheses, the
   * then branch, and the required else branch.</p>
   *
   * @return the parsed if expression
   */
  @Override
  public IExpression parse() {
    Token start = consume(KeywordTokenKind.IF);
    consume(PunctuationTokenKind.LEFT_PAREN);
    IExpression condition = new ExpressionParser(cursor).parse();
    consume(PunctuationTokenKind.RIGHT_PAREN);
    IExpression thenBranch = new ExpressionParser(cursor).parse();
    consume(KeywordTokenKind.ELSE);
    IExpression elseBranch = new ExpressionParser(cursor).parse();
    Token end = cursor.previous();
    SourceRange range = SourceConverter.toASTRange(start, end);
    return new IfExpression(range, condition, thenBranch, elseBranch);
  }
}