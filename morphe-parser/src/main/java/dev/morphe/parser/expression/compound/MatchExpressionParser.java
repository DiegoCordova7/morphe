package dev.morphe.parser.expression.compound;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.expression.MatchExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.kind.KeywordTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.expression.IExpressionParser;
import dev.morphe.parser.source.SourceConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses match expressions.
 *
 * <p>A match expression evaluates a value and selects the result
 * expression associated with the first matching pattern.</p>
 *
 * <p>The general syntax is:</p>
 *
 * <pre>{@code
 * match (value) {
 *     pattern => expression
 *     pattern => expression
 * }
 * }</pre>
 *
 * <p>Each case is parsed independently by
 * {@link MatchExpressionCaseParser}. The semantic analysis phase is
 * responsible for validating pattern compatibility, exhaustiveness,
 * and the types of the resulting expressions.</p>
 */
public final class MatchExpressionParser
    extends AbstractParser
    implements IExpressionParser {

  /**
   * Creates a match expression parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public MatchExpressionParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a match expression from the current parser position.
   *
   * <p>The matched value is enclosed in parentheses for consistency
   * with the other conditional constructs in the language.</p>
   *
   * @return the parsed match expression
   * @throws UnexpectedTokenException if the match expression reaches
   *         the end of input before its closing brace
   */
  @Override
  public IExpression parse() {
    Token match = consume(KeywordTokenKind.MATCH);
    consume(PunctuationTokenKind.LEFT_PAREN);
    IExpression value = new ExpressionParser(cursor).parse();
    consume(PunctuationTokenKind.RIGHT_PAREN);
    consume(PunctuationTokenKind.LEFT_BRACE);
    List<MatchExpression.MatchCase> cases = new ArrayList<>();
    while (!cursor.check(PunctuationTokenKind.RIGHT_BRACE)) {
      if (cursor.isAtEnd()) throw new UnexpectedTokenException(cursor.current());
      cases.add(new MatchExpressionCaseParser(cursor).parse());
    }
    Token rightBrace = consume(PunctuationTokenKind.RIGHT_BRACE);
    SourceRange range = SourceConverter.toASTRange(match, rightBrace);
    return new MatchExpression(range, value, cases);
  }
}