package dev.morphe.compiler.visitors.expression;

import dev.morphe.ast.expression.BinaryExpression;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.instructions.opcodes.IOpCode;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.operator.BinaryOpCodeResolver;
import dev.morphe.compiler.visitors.CompilerExpressionVisitor;

public final class BinaryExpressionCompiler {

  private final CompilerExpressionVisitor expressionVisitor;

  public BinaryExpressionCompiler(CompilerExpressionVisitor expressionVisitor) {
    if (expressionVisitor == null) throw new IllegalArgumentException("expressionVisitor cannot be null");
    this.expressionVisitor = expressionVisitor;
  }

  public Void compile(BinaryExpression expression, CompilerFunctionContext context) {
    BytecodeBuilder builder = context.compiler().builder();
    expression.left().accept(expressionVisitor, context);
    expression.right().accept(expressionVisitor, context);
    IOpCode opcode = BinaryOpCodeResolver.resolve(expression.operator());
    builder.emit(opcode);
    return null;
  }
}