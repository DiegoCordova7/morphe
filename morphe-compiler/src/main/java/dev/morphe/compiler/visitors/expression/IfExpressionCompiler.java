package dev.morphe.compiler.visitors.expression;

import dev.morphe.ast.expression.IfExpression;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.builder.Label;
import dev.morphe.bytecode.instructions.opcodes.ControlOpCode;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.visitors.CompilerExpressionVisitor;

public final class IfExpressionCompiler {

  private final CompilerExpressionVisitor expressionVisitor;

  public IfExpressionCompiler(CompilerExpressionVisitor expressionVisitor) {
    if (expressionVisitor == null) throw new IllegalArgumentException("expressionVisitor cannot be null");
    this.expressionVisitor = expressionVisitor;
  }

  public Void compile(IfExpression expression, CompilerFunctionContext context) {
    BytecodeBuilder builder = context.compiler().builder();
    Label elseLabel = builder.createLabel();
    Label endLabel = builder.createLabel();
    expression.condition().accept(expressionVisitor, context);
    builder.emit(ControlOpCode.JMP_IF_FALSE, elseLabel);
    expression.thenBranch().accept(expressionVisitor, context);
    builder.emit(ControlOpCode.JMP, endLabel);
    builder.mark(elseLabel);
    expression.elseBranch().accept(expressionVisitor, context);
    builder.mark(endLabel);
    return null;
  }
}