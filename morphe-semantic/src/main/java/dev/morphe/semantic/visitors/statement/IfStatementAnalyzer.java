package dev.morphe.semantic.visitors.statement;

import dev.morphe.ast.statement.IfStatement;
import dev.morphe.ast.type.IASTType;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.expression.InvalidConditionTypeException;
import dev.morphe.semantic.result.StatementResult;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.SemanticStatementVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

/**
 * Performs semantic analysis of {@link IfStatement} nodes.
 *
 * <p>The condition of an if statement must evaluate to a boolean value.
 * The then branch and optional else branch are analyzed through the
 * shared statement visitor.</p>
 *
 * <p>Branch scopes are managed by the corresponding block analyzers when
 * the branches are represented by {@code BlockStatement} nodes.</p>
 */
public final class IfStatementAnalyzer {

  /**
   * Analyzes an if statement.
   *
   * @param statement the if statement to analyze
   * @param context the shared semantic analysis context
   * @param expressionVisitor the visitor used to analyze the condition
   * @param statementVisitor the visitor used to analyze the branches
   * @return a statement result indicating whether both branches always return
   * @throws NullSemanticArgumentException if an argument is null
   * @throws InvalidConditionTypeException if the condition is not boolean
   */
  public StatementResult analyze(IfStatement statement, SemanticContext context,
                                 SemanticExpressionVisitor expressionVisitor,
                                 SemanticStatementVisitor statementVisitor) {
    validateArguments(statement, "statement", context);
    IASTType conditionType = statement.condition().accept(expressionVisitor, context).type();
    if (conditionType != PrimitiveType.BOOLEAN) throw new InvalidConditionTypeException(conditionType);
    StatementResult thenResult = statement.thenBranch().accept(statementVisitor, context);
    boolean alwaysReturns = false;
    if (statement.elseBranch() != null) {
      StatementResult elseResult = statement.elseBranch().accept(statementVisitor, context);
      if (thenResult != null && elseResult != null && thenResult.alwaysReturns() && elseResult.alwaysReturns()) {
        alwaysReturns = true;
      }
    }
    return new StatementResult(alwaysReturns);
  }
}