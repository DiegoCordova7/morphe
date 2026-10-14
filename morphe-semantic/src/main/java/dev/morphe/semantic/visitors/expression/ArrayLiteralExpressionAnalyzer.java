package dev.morphe.semantic.visitors.expression;

import dev.morphe.ast.expression.ArrayLiteralExpression;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.type.ArrayType;
import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.expression.InvalidArrayElementTypeException;
import dev.morphe.semantic.type.TypeChecker;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

public final class ArrayLiteralExpressionAnalyzer {

  private final SemanticExpressionVisitor visitor;

  public ArrayLiteralExpressionAnalyzer(SemanticExpressionVisitor visitor) {
    if (visitor == null) throw new NullSemanticArgumentException("visitor");
    this.visitor = visitor;
  }

  public IASTType analyze(ArrayLiteralExpression expression, SemanticContext context) {
    if (expression == null) throw new NullSemanticArgumentException("expression");
    if (context == null) throw new NullSemanticArgumentException("context");
    IASTType expectedType = context.expectedType();
    if (!(expectedType instanceof ArrayType arrayType)) throw new InvalidArrayElementTypeException(null, expectedType);
    IASTType elementType = arrayType.elementType();
    for (IExpression element : expression.elements()) {
      IASTType actualType = element.accept(visitor, context).type();
      if (!isCompatible(actualType, elementType, context.checker()))
        throw new InvalidArrayElementTypeException(elementType, actualType);
    }
    return expectedType;
  }

  private boolean isCompatible(IASTType actualType, IASTType expectedType, TypeChecker checker) {
    return checker.areCompatible(actualType, expectedType) || checker.areCompatible(expectedType, actualType);
  }
}