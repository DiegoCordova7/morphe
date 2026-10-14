package dev.morphe.semantic.visitors.statement;

import dev.morphe.ast.statement.WhileStatement;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.semantic.exception.expression.InvalidConditionTypeException;
import dev.morphe.semantic.result.StatementResult;
import dev.morphe.semantic.scope.ScopeKind;
import dev.morphe.semantic.result.ExpressionResult;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.SemanticStatementVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

/**
 * Performs semantic analysis of {@link WhileStatement} nodes.
 *
 * <p>The analyzer validates that the loop condition evaluates to a boolean
 * value and analyzes the loop body inside a loop scope.</p>
 *
 * <p>The loop scope allows nested {@code break} and {@code continue}
 * statements to resolve a valid enclosing loop context.</p>
 */
public final class WhileStatementAnalyzer {

  /**
   * Analyzes a while statement.
   *
   * @param statement the while statement to analyze
   * @param context the shared semantic analysis context
   * @param expressionVisitor the expression visitor used to analyze the
   *     loop condition
   * @param statementVisitor the statement visitor used to analyze the body
   * @return a statement result indicating whether the statement always returns (always {@code false})
   * @throws InvalidConditionTypeException if the condition does not have
   * {@link PrimitiveType#BOOLEAN} type
   */
  public StatementResult analyze(WhileStatement statement, SemanticContext context,
                                 SemanticExpressionVisitor expressionVisitor, SemanticStatementVisitor statementVisitor) {
    validateArguments(statement, "statement", context);
    ExpressionResult condition = statement.condition().accept(expressionVisitor, context);
    if (condition.type() != PrimitiveType.BOOLEAN) throw new InvalidConditionTypeException(condition.type());
    context.enterScope(ScopeKind.LOOP);
    try {
      statement.body().accept(statementVisitor, context);
    } finally {
      context.exitScope();
    }
    return new StatementResult(false);
  }
}