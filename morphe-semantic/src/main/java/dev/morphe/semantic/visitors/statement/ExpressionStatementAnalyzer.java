package dev.morphe.semantic.visitors.statement;

import dev.morphe.ast.statement.ExpressionStatement;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.result.StatementResult;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

/**
 * Performs semantic analysis of expression statements in the Morphe AST.
 *
 * <p>An expression statement evaluates an expression for its side effects.
 * The semantic analysis of the contained expression is delegated to the
 * shared {@link SemanticExpressionVisitor}.</p>
 */
public final class ExpressionStatementAnalyzer {

  /**
   * Analyzes an expression statement.
   *
   * @param statement the expression statement being analyzed
   * @param context the shared semantic analysis context
   * @param visitor the shared expression visitor used to analyze the expression
   * @return a statement result indicating whether the statement always returns (always {@code false})
   * @throws NullSemanticArgumentException if {@code statement}, {@code context}, or {@code visitor} is null
   */
  public StatementResult analyze(ExpressionStatement statement, SemanticContext context, SemanticExpressionVisitor visitor) {
    validateArguments(statement, "statement", context);
    statement.expression().accept(visitor, context);
    return new StatementResult(false);
  }
}