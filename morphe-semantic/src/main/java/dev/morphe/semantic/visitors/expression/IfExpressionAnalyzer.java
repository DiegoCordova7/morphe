package dev.morphe.semantic.visitors.expression;

import dev.morphe.ast.expression.IfExpression;
import dev.morphe.ast.type.IASTType;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.expression.IncompatibleIfBranchesException;
import dev.morphe.semantic.exception.expression.InvalidIfConditionException;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

public final class IfExpressionAnalyzer {

  private final SemanticExpressionVisitor visitor;

  public IfExpressionAnalyzer(SemanticExpressionVisitor visitor) {
    if (visitor == null) throw new NullSemanticArgumentException("visitor");
    this.visitor = visitor;
  }

  public IASTType analyze(IfExpression expression, SemanticContext context) {
    if (expression == null) throw new NullSemanticArgumentException("expression");
    if (context == null) throw new NullSemanticArgumentException("context");
    IASTType conditionType = expression.condition().accept(visitor, context).type();
    if (conditionType != PrimitiveType.BOOLEAN) throw new InvalidIfConditionException(conditionType);
    IASTType thenType = expression.thenBranch().accept(visitor, context).type();
    IASTType elseType = expression.elseBranch().accept(visitor, context).type();
    if (!context.checker().areCompatible(thenType, elseType))
      throw new IncompatibleIfBranchesException(thenType, elseType);
    return thenType;
  }
}