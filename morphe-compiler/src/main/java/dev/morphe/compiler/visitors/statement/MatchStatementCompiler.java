package dev.morphe.compiler.visitors.statement;

import dev.morphe.ast.statement.MatchStatement;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.builder.Label;
import dev.morphe.bytecode.instructions.opcodes.ControlOpCode;
import dev.morphe.bytecode.instructions.opcodes.ScopeOpCode;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.context.CompilerPatternContext;
import dev.morphe.compiler.visitors.CompilerDispatch;
import dev.morphe.compiler.visitors.CompilerStatementVisitor;

public final class MatchStatementCompiler {

  private final CompilerStatementVisitor statementVisitor;

  public MatchStatementCompiler(CompilerStatementVisitor statementVisitor) {
    if (statementVisitor == null) throw new IllegalArgumentException("statementVisitor cannot be null");
    this.statementVisitor = statementVisitor;
  }

  public Void compile(MatchStatement statement, CompilerFunctionContext context) {
    BytecodeBuilder builder = context.compiler().builder();
    CompilerDispatch dispatch = context.compiler().dispatch();
    int subjectSlot = context.allocateTemporarySlot();
    builder.emit(ScopeOpCode.DECLARE, subjectSlot);
    statement.value().accept(dispatch.expressions(), context);
    builder.emit(ScopeOpCode.STORE, subjectSlot);
    Label end = builder.createLabel();
    for (MatchStatement.MatchCase matchCase : statement.cases()) {
      Label matched = builder.createLabel();
      Label failed = builder.createLabel();
      CompilerPatternContext patternContext = new CompilerPatternContext(context, subjectSlot, matched, failed);
      matchCase.pattern().accept(dispatch.patterns(), patternContext);
      builder.mark(matched);
      matchCase.body().accept(statementVisitor, context);
      builder.emit(ControlOpCode.JMP, end);
      builder.mark(failed);
    }
    builder.mark(end);
    return null;
  }
}