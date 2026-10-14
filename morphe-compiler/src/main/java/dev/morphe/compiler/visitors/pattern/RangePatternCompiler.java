package dev.morphe.compiler.visitors.pattern;

import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.pattern.LiteralPattern;
import dev.morphe.ast.pattern.RangePattern;
import dev.morphe.ast.pattern.VariablePattern;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.instructions.opcodes.ComparisonOpCode;
import dev.morphe.bytecode.instructions.opcodes.ControlOpCode;
import dev.morphe.bytecode.instructions.opcodes.ScopeOpCode;
import dev.morphe.bytecode.instructions.opcodes.StackOpCode;
import dev.morphe.bytecode.constants.IConstant;
import dev.morphe.compiler.constant.CompilerConstantResolver;
import dev.morphe.compiler.context.CompilerPatternContext;
import dev.morphe.semantic.symbol.Symbol;

public final class RangePatternCompiler implements IPatternCompiler<RangePattern> {

  @Override
  public Void compile(RangePattern pattern, CompilerPatternContext context) {
    BytecodeBuilder builder = context.function().compiler().builder();
    builder.emit(ScopeOpCode.LOAD, context.subjectSlot());
    compileBoundary(pattern.start(), context);
    builder.emit(ComparisonOpCode.GTE);
    builder.emit(ControlOpCode.JMP_IF_FALSE, context.failed());
    builder.emit(ScopeOpCode.LOAD, context.subjectSlot());
    compileBoundary(pattern.end(), context);
    builder.emit(ComparisonOpCode.LTE);
    builder.emit(ControlOpCode.JMP_IF_TRUE, context.matched());
    builder.emit(ControlOpCode.JMP, context.failed());
    return null;
  }

  private void compileBoundary(IPattern boundary, CompilerPatternContext context) {
    BytecodeBuilder builder = context.function().compiler().builder();
    if (boundary instanceof LiteralPattern literal) {
      IConstant constant = CompilerConstantResolver.resolve(literal.value());
      int constantIndex = builder.constant(constant);
      builder.emit(StackOpCode.PUSH, constantIndex);
      return;
    }
    if (boundary instanceof VariablePattern variable) {
      Symbol symbol = context.function().compiler().semantic().symbol(variable);
      if (symbol == null)
        throw new IllegalStateException("Variable pattern has no semantic symbol: " + variable.name());
      int slot = context.slot(symbol);
      builder.emit(ScopeOpCode.LOAD, slot);
      return;
    }
    throw new IllegalStateException("Unsupported range boundary: " + boundary.getClass().getSimpleName());
  }
}