package dev.morphe.semantic.visitors.statement;

import dev.morphe.ast.statement.MatchStatement;
import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.result.ExpressionResult;
import dev.morphe.semantic.result.StatementResult;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.SemanticPatternVisitor;
import dev.morphe.semantic.visitors.SemanticStatementVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

/**
 * Performs semantic analysis of {@link MatchStatement} nodes.
 *
 * <p>The value expression is analyzed first to determine the type against
 * which all match patterns must be validated. Each pattern is then analyzed
 * using that type as its expected type.</p>
 *
 * <p>The body of each match case is delegated to the shared statement
 * visitor.</p>
 */
public final class MatchStatementAnalyzer {

  /**
   * Analyzes a match statement.
   *
   * <p>The previous expected type is restored after analysis so that the
   * match statement does not leak its expected type into the surrounding
   * semantic context.</p>
   *
   * @param statement the match statement being analyzed
   * @param context the semantic analysis context
   * @param expressionVisitor the shared semantic expression visitor
   * @param patternVisitor the shared semantic pattern visitor
   * @param statementVisitor the shared semantic statement visitor
   * @return a statement result indicating whether all match cases always return
   */
  public StatementResult analyze(MatchStatement statement, SemanticContext context, SemanticExpressionVisitor expressionVisitor,
                                 SemanticPatternVisitor patternVisitor, SemanticStatementVisitor statementVisitor) {
    validateArguments(statement, "statement", context);
    ExpressionResult valueResult = statement.value().accept(expressionVisitor, context);
    IASTType previousExpectedType = context.expectedType();
    context.expectedType(valueResult.type());
    boolean alwaysReturns = !statement.cases().isEmpty();
    try {
      for (MatchStatement.MatchCase matchCase : statement.cases()) {
        patternVisitor.analyze(matchCase.pattern(), context);
        StatementResult caseResult = matchCase.body().accept(statementVisitor, context);
        if (caseResult == null || !caseResult.alwaysReturns()) {
          alwaysReturns = false;
        }
      }
    } finally {
      context.expectedType(previousExpectedType);
    }
    return new StatementResult(alwaysReturns);
  }
}