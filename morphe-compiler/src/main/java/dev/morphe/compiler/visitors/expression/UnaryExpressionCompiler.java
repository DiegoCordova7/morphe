package dev.morphe.compiler.visitors.expression;

import dev.morphe.ast.expression.UnaryExpression;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.operator.UnaryOpCodeResolver;
import dev.morphe.compiler.visitors.CompilerExpressionVisitor;

public final class UnaryExpressionCompiler {

  private final CompilerExpressionVisitor expressionVisitor;

  public UnaryExpressionCompiler(CompilerExpressionVisitor expressionVisitor) {
    if (expressionVisitor == null) throw new IllegalArgumentException("expressionVisitor cannot be null");
    this.expressionVisitor = expressionVisitor;
  }

  public Void compile(UnaryExpression expression, CompilerFunctionContext context) {
    BytecodeBuilder builder = context.compiler().builder();
    expression.operand().accept(expressionVisitor, context);
    builder.emit(UnaryOpCodeResolver.resolve(expression.operator()));
    return null;
  }
}
