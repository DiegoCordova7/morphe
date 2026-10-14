package dev.morphe.compiler.visitors.expression;

import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.constants.*;
import dev.morphe.bytecode.instructions.opcodes.StackOpCode;
import dev.morphe.compiler.constant.CompilerConstantResolver;
import dev.morphe.compiler.context.CompilerFunctionContext;

public final class LiteralExpressionCompiler {

  public Void compile(LiteralExpression expression, CompilerFunctionContext context) {
    BytecodeBuilder builder = context.compiler().builder();
    IConstant constant = CompilerConstantResolver.resolve(expression.value());
    int index = builder.constant(constant);
    builder.emit(StackOpCode.PUSH, index);
    return null;
  }
}