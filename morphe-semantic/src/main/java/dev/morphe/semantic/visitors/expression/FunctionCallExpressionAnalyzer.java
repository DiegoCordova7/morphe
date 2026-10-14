package dev.morphe.semantic.visitors.expression;

import dev.morphe.ast.expression.FunctionCallExpression;
import dev.morphe.ast.type.FunctionType;
import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.expression.IncompatibleArgumentException;
import dev.morphe.semantic.exception.expression.InvalidArgumentCountException;
import dev.morphe.semantic.exception.expression.InvalidFunctionCallException;
import dev.morphe.semantic.symbol.Symbol;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

public final class FunctionCallExpressionAnalyzer {

  private final SemanticExpressionVisitor visitor;

  public FunctionCallExpressionAnalyzer(SemanticExpressionVisitor visitor) {
    if (visitor == null) throw new NullSemanticArgumentException("visitor");
    this.visitor = visitor;
  }

  public IASTType analyze(FunctionCallExpression expression, SemanticContext context) {
    if (expression == null) throw new NullSemanticArgumentException("expression");
    if (context == null) throw new NullSemanticArgumentException("context");
    expression.function().accept(visitor, context);
    Symbol symbol = context.symbol(expression.function());
    IASTType functionType = symbol.type();
    if (!(functionType instanceof FunctionType function)) throw new InvalidFunctionCallException(functionType);
    if (function.parameterTypes().size() != expression.arguments().size())
      throw new InvalidArgumentCountException(function.parameterTypes().size(), expression.arguments().size());
    for (int i = 0; i < expression.arguments().size(); i++) {
      IASTType argumentType = expression.arguments().get(i).accept(visitor, context).type();
      IASTType parameterType = function.parameterTypes().get(i);
      if (!context.checker().areCompatible(argumentType, parameterType))
        throw new IncompatibleArgumentException(i, parameterType, argumentType);
    }
    return function.returnType();
  }
}