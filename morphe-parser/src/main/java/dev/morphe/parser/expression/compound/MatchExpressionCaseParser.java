package dev.morphe.parser.expression.compound;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.expression.MatchExpression;
import dev.morphe.ast.pattern.IPattern;
import dev.morphe.lexer.kind.MatchTokenKind;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.pattern.PatternParser;

/**
 * Parses a single case of a match expression.
 *
 * <p>A match expression case associates a pattern with an expression
 * that is evaluated when the pattern matches the value being examined.</p>
 *
 * <p>The general syntax is:</p>
 *
 * <pre>{@code
 * pattern => expression
 * }</pre>
 */
public final class MatchExpressionCaseParser extends AbstractParser {

  /**
   * Creates a match expression case parser using the specified token
   * cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public MatchExpressionCaseParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a single match expression case.
   *
   * @return the parsed match case
   */
  public MatchExpression.MatchCase parse() {
    IPattern pattern = new PatternParser(cursor).parse();
    consume(MatchTokenKind.ARROW);
    IExpression result = new ExpressionParser(cursor).parse();
    return new MatchExpression.MatchCase(pattern, result);
  }
}