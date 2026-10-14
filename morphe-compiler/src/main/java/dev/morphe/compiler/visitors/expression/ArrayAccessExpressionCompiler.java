package dev.morphe.compiler.visitors.expression;

import dev.morphe.ast.expression.ArrayAccessExpression;
import dev.morphe.bytecode.instructions.opcodes.ArrayOpCode;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.visitors.CompilerExpressionVisitor;

public final class ArrayAccessExpressionCompiler {

  private final CompilerExpressionVisitor expressionVisitor;

  public ArrayAccessExpressionCompiler(CompilerExpressionVisitor expressionVisitor) {
    if (expressionVisitor == null) throw new IllegalArgumentException("expressionVisitor cannot be null");
    this.expressionVisitor = expressionVisitor;
  }

  public Void compile(ArrayAccessExpression expression, CompilerFunctionContext context) {
    expression.array().accept(expressionVisitor, context);
    expression.index().accept(expressionVisitor, context);
    context.compiler()
        .builder()
        .emit(ArrayOpCode.ARRAY_GET);
    return null;
  }
}
