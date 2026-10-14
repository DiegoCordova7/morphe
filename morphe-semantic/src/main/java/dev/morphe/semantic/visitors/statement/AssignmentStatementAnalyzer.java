package dev.morphe.semantic.visitors.statement;

import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.ast.operator.AssignmentOperator;
import dev.morphe.ast.operator.BinaryOperator;
import dev.morphe.ast.statement.AssignmentStatement;
import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.type.IncompatibleTypesException;
import dev.morphe.semantic.exception.statement.InvalidAssignmentTargetException;
import dev.morphe.semantic.result.ExpressionResult;
import dev.morphe.semantic.result.StatementResult;
import dev.morphe.semantic.type.BinaryOperationResolver;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

/**
 * Performs semantic analysis of assignment statements.
 *
 * <p>An assignment analyzes both its target and assigned value and verifies
 * that the target represents an assignable expression and that the assigned
 * value is compatible with the target type.</p>
 *
 * <p>Compound assignment operators are validated using the type of the
 * target and the type of the assigned value.</p>
 */
public final class AssignmentStatementAnalyzer {

  private final BinaryOperationResolver operationResolver = new BinaryOperationResolver();

  /**
   * Analyzes an assignment statement.
   *
   * @param statement the assignment being analyzed
   * @param context the shared semantic analysis context
   * @param visitor the shared expression visitor used to analyze the target
   *     and assigned value
   * @return a statement result indicating whether the statement always returns (always {@code false})   * @throws NullSemanticArgumentException if {@code statement},
   *     {@code context}, or {@code visitor} is null
   * @throws InvalidAssignmentTargetException if the target is not assignable
   * @throws IncompatibleTypesException if the assigned value is incompatible
   *     with the target type
   */
  public StatementResult analyze(AssignmentStatement statement, SemanticContext context, SemanticExpressionVisitor visitor) {
    validateArguments(statement, "statement", context);
    ExpressionResult target = statement.target().accept(visitor, context);
    if (!target.assignable()) throw new InvalidAssignmentTargetException(statement.target());
    if (statement.target() instanceof VariableExpression) context.symbol(statement, context.symbol(statement.target()));
    IASTType targetType = target.type();
    IASTType valueType = statement.value().accept(visitor, context).type();
    if (statement.operator() == AssignmentOperator.ASSIGN) {
      validateAssignment(targetType, valueType, context);
      return new StatementResult(false);
    }
    validateCompoundAssignment(statement.operator(), targetType, valueType, context);
    return new StatementResult(false);
  }

  private void validateAssignment(IASTType targetType, IASTType valueType, SemanticContext context) {
    if (!context.checker().areCompatible(valueType, targetType))
      throw new IncompatibleTypesException(targetType, valueType);
  }

  private void validateCompoundAssignment(AssignmentOperator operator, IASTType targetType,
                                          IASTType valueType, SemanticContext context) {
    BinaryOperator binaryOperator = switch (operator) {
      case ADD -> BinaryOperator.ADD;
      case SUBTRACT -> BinaryOperator.SUBTRACT;
      case MULTIPLY -> BinaryOperator.MULTIPLY;
      case DIVIDE -> BinaryOperator.DIVIDE;
      case MODULO -> BinaryOperator.MODULO;
      case ASSIGN -> null;
    };
    assert binaryOperator != null;
    IASTType resultType = operationResolver.resolve(binaryOperator, targetType, valueType, context.checker());
    if (!context.checker().areCompatible(valueType, targetType))
      throw new IncompatibleTypesException(targetType, resultType);
  }
}