package dev.morphe.parser.expression;

import dev.morphe.ast.expression.IExpression;

/**
 * Defines the contract for parsers that produce expression AST nodes.
 *
 * <p>Implementations parse an expression from the current position of a
 * {@code TokenCursor} and return the corresponding AST expression.</p>
 */
@FunctionalInterface
public interface IExpressionParser {

  /**
   * Parses an expression from the current parser position.
   *
   * @return the parsed expression
   */
  IExpression parse();
}