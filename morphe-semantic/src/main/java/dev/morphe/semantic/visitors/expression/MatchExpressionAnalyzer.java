package dev.morphe.semantic.visitors.expression;

import dev.morphe.ast.expression.MatchExpression;
import dev.morphe.ast.pattern.DefaultPattern;
import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.expression.IncompatibleMatchResultException;
import dev.morphe.semantic.exception.expression.MissingDefaultCaseException;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.SemanticPatternVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

/**
 * Performs semantic analysis of match expressions.
 *
 * <p>The value expression is analyzed first to determine the type against
 * which all match patterns must be validated. Each pattern is then analyzed
 * using that type as its expected type.</p>
 *
 * <p>The result expression of every case must produce a type compatible
 * with the result type established by the first case.</p>
 *
 * <p>A match expression must contain a default case so that the expression
 * always produces a value.</p>
 */
public final class MatchExpressionAnalyzer {

  private final SemanticExpressionVisitor expressionVisitor;

  /**
   * Creates a match expression analyzer.
   *
   * @param expressionVisitor the shared semantic expression visitor
   * @throws NullSemanticArgumentException if either visitor is null
   */
  public MatchExpressionAnalyzer(SemanticExpressionVisitor expressionVisitor) {
    if (expressionVisitor == null) throw new NullSemanticArgumentException("expressionVisitor");
    this.expressionVisitor = expressionVisitor;
  }

  /**
   * Analyzes a match expression.
   *
   * @param expression the match expression being analyzed
   * @param context the semantic analysis context
   * @return the type produced by the match expression
   * @throws NullSemanticArgumentException if {@code expression} or
   *     {@code context} is null
   * @throws MissingDefaultCaseException if the match contains no default case
   * @throws IncompatibleMatchResultException if case result types are
   *     incompatible
   */
  public IASTType analyze(MatchExpression expression, SemanticContext context) {
    validateArguments(expression, "expression", context);
    SemanticPatternVisitor patternVisitor = context.patternVisitor();
    IASTType valueType = expression.value().accept(expressionVisitor, context).type();
    IASTType previousExpectedType = context.expectedType();
    context.expectedType(valueType);
    try {
      IASTType resultType = null;
      boolean hasDefaultCase = false;
      for (MatchExpression.MatchCase matchCase : expression.cases()) {
        patternVisitor.analyze(matchCase.pattern(), context);
        IASTType caseType = matchCase.result().accept(expressionVisitor, context).type();
        if (resultType == null) resultType = caseType;
        else if (!context.checker().areCompatible(resultType, caseType))
          throw new IncompatibleMatchResultException(resultType, caseType);
        if (matchCase.pattern() instanceof DefaultPattern) hasDefaultCase = true;
      }
      if (!hasDefaultCase) throw new MissingDefaultCaseException();
      return resultType;
    } finally {
      context.expectedType(previousExpectedType);
    }
  }
}