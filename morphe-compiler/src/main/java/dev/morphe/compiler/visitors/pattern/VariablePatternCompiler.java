package dev.morphe.compiler.visitors.pattern;

import dev.morphe.ast.pattern.VariablePattern;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.instructions.opcodes.ComparisonOpCode;
import dev.morphe.bytecode.instructions.opcodes.ControlOpCode;
import dev.morphe.bytecode.instructions.opcodes.ScopeOpCode;
import dev.morphe.compiler.context.CompilerPatternContext;
import dev.morphe.semantic.symbol.Symbol;

public final class VariablePatternCompiler implements IPatternCompiler<VariablePattern> {

  @Override
  public Void compile(VariablePattern pattern, CompilerPatternContext context) {
    BytecodeBuilder builder = context.function().compiler().builder();
    Symbol symbol = context.function().compiler().semantic().symbol(pattern);
    if (symbol == null) throw new IllegalStateException("Variable pattern has no semantic symbol: " + pattern.name());
    int slot = context.slot(symbol);
    builder.emit(ScopeOpCode.LOAD, context.subjectSlot());
    builder.emit(ScopeOpCode.LOAD, slot);
    builder.emit(ComparisonOpCode.EQ);
    builder.emit(ControlOpCode.JMP_IF_TRUE, context.matched());
    builder.emit(ControlOpCode.JMP, context.failed());
    return null;
  }
}