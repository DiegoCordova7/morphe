package dev.morphe.semantic.visitors.statement;

import dev.morphe.ast.statement.ReturnStatement;
import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.statement.InvalidReturnTypeException;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.result.ExpressionResult;
import dev.morphe.semantic.result.StatementResult;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

/**
 * Performs semantic analysis of return statements.
 *
 * <p>The returned expression, when present, must produce a type compatible
 * with the return type expected by the current function.</p>
 *
 * <p>A return statement without an expression is valid only when the current
 * function does not require a return value.</p>
 */
public final class ReturnStatementAnalyzer {

  /**
   * Analyzes a return statement.
   *
   * @param statement the return statement being analyzed
   * @param context the shared semantic analysis context
   * @param expressionVisitor the visitor used to analyze the returned expression
   * @return a statement result indicating that the statement always returns (always {@code true})
   * @throws NullSemanticArgumentException if any required argument is null
   * @throws InvalidReturnTypeException if the returned expression type is
   * incompatible with the expected return type
   */
  public StatementResult analyze(ReturnStatement statement, SemanticContext context, SemanticExpressionVisitor expressionVisitor) {
    validateArguments(statement, "statement", context);
    if (expressionVisitor == null) throw new NullSemanticArgumentException("expressionVisitor");
    IASTType expectedType = context.expectedType();
    if (statement.value() == null) {
      if (expectedType != null) throw new InvalidReturnTypeException(expectedType, null);
      return new StatementResult(true);
    }
    ExpressionResult result = statement.value().accept(expressionVisitor, context);
    if (expectedType != null && !context.checker().areCompatible(expectedType, result.type())) {
      throw new InvalidReturnTypeException(expectedType, result.type());
    }
    return new StatementResult(true);
  }
}