package dev.morphe.compiler.visitors.statement;

import dev.morphe.ast.statement.ForStatement;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.builder.Label;
import dev.morphe.bytecode.instructions.opcodes.ControlOpCode;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.visitors.CompilerDispatch;
import dev.morphe.compiler.visitors.CompilerStatementVisitor;

public final class ForStatementCompiler {

  private final CompilerStatementVisitor statementVisitor;

  public ForStatementCompiler(CompilerStatementVisitor statementVisitor) {
    if (statementVisitor == null) throw new IllegalArgumentException("statementVisitor cannot be null");
    this.statementVisitor = statementVisitor;
  }

  public Void compile(ForStatement statement, CompilerFunctionContext context) {
    CompilerDispatch dispatch = context.compiler().dispatch();
    BytecodeBuilder builder = context.compiler().builder();
    if (statement.initialization() != null) statement.initialization().accept(statementVisitor, context);
    Label condition = builder.createLabel();
    Label update = builder.createLabel();
    Label end = builder.createLabel();
    builder.mark(condition);
    statement.condition().accept(dispatch.expressions(), context);
    builder.emit(ControlOpCode.JMP_IF_FALSE, end);
    context.enterLoop(end, update);
    statement.body().accept(statementVisitor, context);
    builder.mark(update);
    if (statement.update() != null) statement.update().accept(statementVisitor, context);
    builder.emit(ControlOpCode.JMP, condition);
    context.exitLoop();
    builder.mark(end);
    return null;
  }
}