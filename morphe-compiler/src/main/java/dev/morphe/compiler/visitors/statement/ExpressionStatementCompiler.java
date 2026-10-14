package dev.morphe.compiler.visitors.statement;

import dev.morphe.ast.statement.ExpressionStatement;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.bytecode.instructions.opcodes.StackOpCode;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.visitors.CompilerDispatch;
import dev.morphe.semantic.result.ExpressionResult;

public final class ExpressionStatementCompiler {

  public Void compile(ExpressionStatement statement, CompilerFunctionContext context) {
    CompilerDispatch dispatch = context.compiler().dispatch();
    statement.expression().accept(dispatch.expressions(), context);
    ExpressionResult result = context.compiler()
        .semantic()
        .expressionResult(statement.expression());
    if (result.type() != PrimitiveType.VOID) context.compiler().builder().emit(StackOpCode.POP);
    return null;
  }
}