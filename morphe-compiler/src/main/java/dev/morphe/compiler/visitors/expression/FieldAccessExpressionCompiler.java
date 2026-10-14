package dev.morphe.compiler.visitors.expression;

import dev.morphe.ast.expression.FieldAccessExpression;
import dev.morphe.bytecode.constants.StringConstant;
import dev.morphe.bytecode.instructions.opcodes.ArrayOpCode;
import dev.morphe.bytecode.instructions.opcodes.CompositeOpCode;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.visitors.CompilerExpressionVisitor;

public final class FieldAccessExpressionCompiler {

  private final CompilerExpressionVisitor expressionVisitor;

  public FieldAccessExpressionCompiler(CompilerExpressionVisitor expressionVisitor) {
    if (expressionVisitor == null) throw new IllegalArgumentException("expressionVisitor cannot be null");
    this.expressionVisitor = expressionVisitor;
  }

  public Void compile(FieldAccessExpression expression, CompilerFunctionContext context) {
    expression.target().accept(expressionVisitor, context);
    if (expression.field().equals("length")) {
      context.compiler()
          .builder()
          .emit(ArrayOpCode.ARRAY_LENGTH);
      return null;
    }
    int fieldConstant = context.compiler()
        .builder()
        .constant(new StringConstant(expression.field()));
    context.compiler()
        .builder()
        .emit(CompositeOpCode.GET_FIELD, fieldConstant);
    return null;
  }
}