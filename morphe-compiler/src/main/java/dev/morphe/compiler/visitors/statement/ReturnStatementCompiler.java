package dev.morphe.compiler.visitors.statement;

import dev.morphe.ast.statement.ReturnStatement;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.instructions.opcodes.CallOpCode;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.visitors.CompilerDispatch;

public final class ReturnStatementCompiler {

  public Void compile(ReturnStatement statement, CompilerFunctionContext context) {
    CompilerDispatch dispatch = context.compiler().dispatch();
    BytecodeBuilder builder = context.compiler().builder();
    if (statement.value() != null) statement.value().accept(dispatch.expressions(), context);
    builder.emit(CallOpCode.RETURN);
    return null;
  }
}