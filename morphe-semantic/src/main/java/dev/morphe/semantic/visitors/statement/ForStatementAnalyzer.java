package dev.morphe.semantic.visitors.statement;

import dev.morphe.ast.statement.ForStatement;
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
 * Performs semantic analysis of {@link ForStatement} nodes.
 *
 * <p>The analyzer evaluates the optional initialization, condition, body,
 * and update components within a loop scope. This allows control-flow
 * statements such as {@code break} and {@code continue} to resolve the
 * enclosing loop context.</p>
 *
 * <p>If a condition is present, it must evaluate to a boolean value.</p>
 */
public final class ForStatementAnalyzer {

  /**
   * Analyzes a for statement.
   *
   * <p>The loop scope is created before analyzing any loop component and is
   * always removed after analysis. Optional initialization, condition, and
   * update components are analyzed only when present.</p>
   *
   * @param statement the for statement to analyze
   * @param context the shared semantic analysis context
   * @param expressionVisitor the visitor used to analyze expressions
   * @param statementVisitor the visitor used to analyze statements
   * @return a statement result indicating whether the statement always returns (always {@code false})
   * @throws InvalidConditionTypeException if the condition is present and
   * does not have boolean type
   */
  public StatementResult analyze(ForStatement statement, SemanticContext context,
                                 SemanticExpressionVisitor expressionVisitor,
                                 SemanticStatementVisitor statementVisitor) {
    validateArguments(statement, "statement", context);
    context.enterScope(ScopeKind.LOOP);
    try {
      if (statement.initialization() != null) statement.initialization().accept(statementVisitor, context);
      if (statement.condition() != null) {
        ExpressionResult result = statement.condition().accept(expressionVisitor, context);
        if (result.type() != PrimitiveType.BOOLEAN) throw new InvalidConditionTypeException(result.type());
      }
      statement.body().accept(statementVisitor, context);
      if (statement.update() != null) statement.update().accept(statementVisitor, context);
    } finally {
      context.exitScope();
    }
    return new StatementResult(false);  }
}