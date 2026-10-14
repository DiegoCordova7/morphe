package dev.morphe.compiler.visitors.statement;

import dev.morphe.bytecode.instructions.opcodes.ControlOpCode;
import dev.morphe.compiler.context.CompilerFunctionContext;

public final class BreakStatementCompiler {

  public BreakStatementCompiler() {}

  public Void compile(CompilerFunctionContext context) {
    context.compiler()
        .builder()
        .emit(ControlOpCode.JMP, context.breakLabel());
    return null;
  }
}