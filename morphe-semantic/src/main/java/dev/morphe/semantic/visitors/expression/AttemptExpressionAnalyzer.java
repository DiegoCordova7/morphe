package dev.morphe.semantic.visitors.expression;

import dev.morphe.ast.expression.AttemptExpression;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.expression.IncompatibleAttemptResultException;
import dev.morphe.semantic.result.ExpressionResult;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.SemanticStatementVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;
import dev.morphe.semantic.visitors.statement.BlockStatementAnalyzer;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

/**
 * Performs semantic analysis of attempt expressions.
 *
 * <p>An attempt expression evaluates a block that produces a value and
 * provides a fallback expression to be used when the attempted operation
 * cannot complete successfully at runtime.</p>
 *
 * <p>The result type of the attempted body and the fallback expression
 * must be compatible because either value may become the resulting value
 * of the attempt expression.</p>
 *
 * <p>The attempted body is analyzed through {@link BlockStatementAnalyzer},
 * which obtains the result produced by the final expression statement of
 * the block. Error handler actions are analyzed as statements through
 * {@link SemanticStatementVisitor}.</p>
 *
 * <p>This analyzer coordinates the analysis of the attempt expression and
 * delegates expression and statement-specific semantic rules to their
 * corresponding visitors and analyzers.</p>
 */
public final class AttemptExpressionAnalyzer {

  private final BlockStatementAnalyzer blockStatement;

  /**
   * Creates an attempt expression analyzer.
   *
   */
  public AttemptExpressionAnalyzer() {
    this.blockStatement = new BlockStatementAnalyzer();
  }

  /**
   * Analyzes an attempt expression.
   *
   * <p>The attempted body is analyzed first to determine its resulting
   * type. The fallback expression is then analyzed and its type is checked
   * for compatibility with the body result type. Finally, all error handler
   * actions are analyzed as statements.</p>
   *
   * @param expression the attempt expression being analyzed
   * @param context the shared semantic analysis context
   * @return the semantic result produced by the attempted body
   * @throws NullSemanticArgumentException if {@code expression} or
   * {@code context} is null
   * @throws IncompatibleAttemptResultException if the attempted body and
   * fallback expression produce incompatible types
   */
  public ExpressionResult analyze(AttemptExpression expression, SemanticContext context) {
    validateArguments(expression, "expression", context);
    SemanticExpressionVisitor expressionVisitor = context.expressionVisitor();
    SemanticStatementVisitor statementVisitor = context.statementVisitor();
    ExpressionResult bodyResult = blockStatement.analyzeResult(expression.body(), context, expressionVisitor, statementVisitor);
    ExpressionResult fallbackResult = expression.fallback().accept(expressionVisitor, context);
    if (!context.checker().areCompatible(bodyResult.type(), fallbackResult.type())) {
      throw new IncompatibleAttemptResultException(bodyResult.type(), fallbackResult.type());
    }
    for (AttemptExpression.ErrorHandler handler : expression.handlers()) {
      handler.action().accept(statementVisitor, context);
    }
    return bodyResult;
  }
}