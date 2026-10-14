package dev.morphe.compiler.visitors.expression;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.expression.MatchExpression;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.builder.Label;
import dev.morphe.bytecode.instructions.opcodes.ControlOpCode;
import dev.morphe.bytecode.instructions.opcodes.ScopeOpCode;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.context.CompilerPatternContext;
import dev.morphe.compiler.visitors.CompilerDispatch;
import dev.morphe.compiler.visitors.CompilerExpressionVisitor;
import dev.morphe.compiler.visitors.CompilerPatternVisitor;

public final class MatchExpressionCompiler {

  private final CompilerExpressionVisitor expressionVisitor;

  public MatchExpressionCompiler(CompilerExpressionVisitor expressionVisitor) {
    if (expressionVisitor == null) throw new IllegalArgumentException("expressionVisitor cannot be null");
    this.expressionVisitor = expressionVisitor;
  }

  public Void compile(MatchExpression expression, CompilerFunctionContext context) {
    BytecodeBuilder builder = context.compiler().builder();
    CompilerDispatch dispatch = context.compiler().dispatch();
    CompilerPatternVisitor patternVisitor = dispatch.patterns();
    int subjectSlot = context.allocateTemporarySlot();
    builder.emit(ScopeOpCode.DECLARE, subjectSlot);
    expression.value().accept(expressionVisitor, context);
    builder.emit(ScopeOpCode.STORE, subjectSlot);
    expression.value().accept(expressionVisitor, context);
    builder.emit(ScopeOpCode.STORE, subjectSlot);
    Label end = builder.createLabel();
    for (MatchExpression.MatchCase matchCase : expression.cases()) {
      Label matched = builder.createLabel();
      Label failed = builder.createLabel();
      CompilerPatternContext patternContext = new CompilerPatternContext(context, subjectSlot, matched, failed);
      matchCase.pattern().accept(patternVisitor, patternContext);
      builder.mark(matched);
      IExpression result = matchCase.result();
      result.accept(expressionVisitor, context);
      builder.emit(ControlOpCode.JMP, end);
      builder.mark(failed);
    }
    builder.mark(end);
    return null;
  }
}