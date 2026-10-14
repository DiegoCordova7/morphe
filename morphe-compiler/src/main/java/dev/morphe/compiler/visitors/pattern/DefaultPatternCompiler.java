package dev.morphe.compiler.visitors.pattern;

import dev.morphe.ast.pattern.DefaultPattern;
import dev.morphe.bytecode.instructions.opcodes.ControlOpCode;
import dev.morphe.compiler.context.CompilerPatternContext;

public final class DefaultPatternCompiler implements IPatternCompiler<DefaultPattern> {

  @Override
  public Void compile(DefaultPattern pattern, CompilerPatternContext context) {
    context.function()
        .compiler()
        .builder()
        .emit(ControlOpCode.JMP, context.matched());
    return null;
  }
}