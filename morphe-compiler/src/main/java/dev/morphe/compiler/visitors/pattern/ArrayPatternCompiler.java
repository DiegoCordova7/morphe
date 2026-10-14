package dev.morphe.compiler.visitors.pattern;

import dev.morphe.ast.pattern.ArrayPattern;
import dev.morphe.ast.pattern.IPattern;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.builder.Label;
import dev.morphe.bytecode.constants.IntegerConstant;
import dev.morphe.bytecode.instructions.opcodes.ArrayOpCode;
import dev.morphe.bytecode.instructions.opcodes.ComparisonOpCode;
import dev.morphe.bytecode.instructions.opcodes.ControlOpCode;
import dev.morphe.bytecode.instructions.opcodes.ScopeOpCode;
import dev.morphe.bytecode.instructions.opcodes.StackOpCode;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.context.CompilerPatternContext;
import dev.morphe.compiler.visitors.CompilerPatternVisitor;

public final class ArrayPatternCompiler {

  public Void compile(ArrayPattern pattern, CompilerPatternContext context) {
    BytecodeBuilder builder = context.function().compiler().builder();
    CompilerFunctionContext function = context.function();
    CompilerPatternVisitor visitor = function.compiler().dispatch().patterns();
    Label sizeMatched = builder.createLabel();
    builder.emit(ScopeOpCode.LOAD, context.subjectSlot());
    builder.emit(ArrayOpCode.ARRAY_LENGTH);
    int constant = builder.constant(new IntegerConstant(pattern.elements().size()));
    builder.emit(StackOpCode.PUSH, constant);
    builder.emit(ComparisonOpCode.EQ);
    builder.emit(ControlOpCode.JMP_IF_TRUE, sizeMatched);
    builder.emit(ControlOpCode.JMP, context.failed());
    builder.mark(sizeMatched);
    for (int i = 0; i < pattern.elements().size(); i++) {
      IPattern element = pattern.elements().get(i);
      Label elementMatched = builder.createLabel();
      int elementSlot = function.allocateTemporarySlot();
      builder.emit(ScopeOpCode.DECLARE, elementSlot);
      builder.emit(ScopeOpCode.LOAD, context.subjectSlot());
      int indexConstant = builder.constant(new IntegerConstant(i));
      builder.emit(StackOpCode.PUSH, indexConstant);
      builder.emit(ArrayOpCode.ARRAY_GET);
      builder.emit(ScopeOpCode.STORE, elementSlot);
      CompilerPatternContext elementContext =
          new CompilerPatternContext(function, elementSlot, elementMatched, context.failed());
      element.accept(visitor, elementContext);
      builder.mark(elementMatched);
    }
    builder.emit(ControlOpCode.JMP, context.matched());
    return null;
  }
}