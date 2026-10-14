package dev.morphe.parser.expression;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.precedence.LogicalOrParser;

/**
 * Entry point for parsing expressions.
 *
 * <p>Expression parsing is delegated to the highest-level expression
 * parser in the precedence hierarchy. The delegated parser then processes
 * lower-precedence and higher-precedence operators through the expression
 * parsing chain.</p>
 *
 * <p>This parser provides a single entry point for all expression forms,
 * allowing statements, declarations, and compound expressions to parse
 * expressions without depending on a specific precedence level.</p>
 */
public final class ExpressionParser extends AbstractParser {

  /**
   * Creates an expression parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public ExpressionParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses an expression from the current parser position.
   *
   * @return the parsed expression
   */
  public IExpression parse() {
    return new LogicalOrParser(cursor).parse();
  }
}