package dev.morphe.semantic.visitors.expression;

import dev.morphe.ast.declaration.Parameter;
import dev.morphe.ast.expression.LambdaExpression;
import dev.morphe.ast.type.FunctionType;
import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.symbol.Symbol;
import dev.morphe.semantic.symbol.SymbolKind;
import dev.morphe.semantic.scope.ScopeKind;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

import java.util.List;

public final class LambdaExpressionAnalyzer {

  private final SemanticExpressionVisitor visitor;

  public LambdaExpressionAnalyzer(SemanticExpressionVisitor visitor) {
    if (visitor == null) throw new NullSemanticArgumentException("visitor");
    this.visitor = visitor;
  }

  public IASTType analyze(LambdaExpression expression, SemanticContext context) {
    if (expression == null) throw new NullSemanticArgumentException("expression");
    if (context == null) throw new NullSemanticArgumentException("context");
    List<IASTType> parameterTypes = expression.parameters()
        .stream()
        .map(Parameter::type)
        .toList();
    context.symbols().enterScope(ScopeKind.FUNCTION);
    try {
      for (Parameter parameter : expression.parameters()) {
        context.symbols().define(new Symbol(parameter.name(), SymbolKind.PARAMETER, parameter.type(), false));
      }
      IASTType bodyType = expression.body().accept(visitor, context).type();
      return new FunctionType(parameterTypes, bodyType);
    } finally {
      context.symbols().exitScope();
    }
  }
}