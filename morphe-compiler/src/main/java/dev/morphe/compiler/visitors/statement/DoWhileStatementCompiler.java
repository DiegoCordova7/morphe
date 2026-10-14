package dev.morphe.compiler.visitors.statement;

import dev.morphe.ast.statement.DoWhileStatement;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.builder.Label;
import dev.morphe.bytecode.instructions.opcodes.ControlOpCode;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.visitors.CompilerDispatch;
import dev.morphe.compiler.visitors.CompilerStatementVisitor;

public final class DoWhileStatementCompiler {

  private final CompilerStatementVisitor statementVisitor;

  public DoWhileStatementCompiler(CompilerStatementVisitor statementVisitor) {
    if (statementVisitor == null) throw new IllegalArgumentException("statementVisitor cannot be null");
    this.statementVisitor = statementVisitor;
  }

  public Void compile(DoWhileStatement statement, CompilerFunctionContext context) {
    CompilerDispatch dispatch = context.compiler().dispatch();
    BytecodeBuilder builder = context.compiler().builder();
    Label bodyLabel = builder.createLabel();
    Label conditionLabel = builder.createLabel();
    Label endLabel = builder.createLabel();
    builder.mark(bodyLabel);
    context.enterLoop(endLabel, conditionLabel);
    statement.body().accept(statementVisitor, context);
    context.exitLoop();
    builder.mark(conditionLabel);
    statement.condition().accept(dispatch.expressions(), context);
    builder.emit(ControlOpCode.JMP_IF_TRUE, bodyLabel);
    builder.mark(endLabel);
    return null;
  }
}
