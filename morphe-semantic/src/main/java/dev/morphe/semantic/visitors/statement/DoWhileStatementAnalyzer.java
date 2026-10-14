package dev.morphe.semantic.visitors.statement;

import dev.morphe.ast.statement.DoWhileStatement;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.semantic.exception.expression.InvalidConditionTypeException;
import dev.morphe.semantic.result.ExpressionResult;
import dev.morphe.semantic.result.StatementResult;
import dev.morphe.semantic.scope.ScopeKind;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.SemanticStatementVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

/**
 * Performs semantic analysis of {@link DoWhileStatement} nodes.
 *
 * <p>The analyzer validates the loop body within a loop scope, allowing
 * control-flow statements such as {@code break} and {@code continue} to
 * resolve the enclosing loop context. The loop condition is then analyzed
 * and must evaluate to a boolean value.</p>
 */
public final class DoWhileStatementAnalyzer {

  /**
   * Analyzes a do-while statement.
   *
   * <p>The loop scope is created before analyzing the body and is always
   * removed after analysis, including when semantic analysis fails. The
   * condition is analyzed after the body and must have
   * {@link PrimitiveType#BOOLEAN} type.</p>
   *
   * @param statement the do-while statement to analyze
   * @param context the shared semantic analysis context
   * @param expressionVisitor the visitor used to analyze expressions
   * @param statementVisitor the visitor used to analyze statements
   * @return a statement result indicating whether the statement always returns (always {@code false})   * @throws InvalidConditionTypeException if the loop condition does not
   *     have boolean type
   */
  public StatementResult analyze(DoWhileStatement statement, SemanticContext context,
                      SemanticExpressionVisitor expressionVisitor, SemanticStatementVisitor statementVisitor) {
    validateArguments(statement, "statement", context);
    context.enterScope(ScopeKind.LOOP);
    try {
      statement.body().accept(statementVisitor, context);
    } finally {
      context.exitScope();
    }
    ExpressionResult result = statement.condition().accept(expressionVisitor, context);
    if (result.type() != PrimitiveType.BOOLEAN) throw new InvalidConditionTypeException(result.type());
    return new StatementResult(false);
  }
}