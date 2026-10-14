package dev.morphe.compiler.visitors.pattern;

import dev.morphe.ast.pattern.LiteralPattern;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.instructions.opcodes.ComparisonOpCode;
import dev.morphe.bytecode.instructions.opcodes.ControlOpCode;
import dev.morphe.bytecode.instructions.opcodes.ScopeOpCode;
import dev.morphe.bytecode.instructions.opcodes.StackOpCode;
import dev.morphe.compiler.constant.CompilerConstantResolver;
import dev.morphe.compiler.context.CompilerPatternContext;

public final class LiteralPatternCompiler implements IPatternCompiler<LiteralPattern> {

  @Override
  public Void compile(LiteralPattern pattern, CompilerPatternContext context) {
    BytecodeBuilder builder = context.function().compiler().builder();
    builder.emit(ScopeOpCode.LOAD, context.subjectSlot());
    int constantIndex = builder.constant(CompilerConstantResolver.resolve(pattern.value()));
    builder.emit(StackOpCode.PUSH, constantIndex);
    builder.emit(ComparisonOpCode.EQ);
    builder.emit(ControlOpCode.JMP_IF_TRUE, context.matched());
    builder.emit(ControlOpCode.JMP, context.failed());
    return null;
  }
}