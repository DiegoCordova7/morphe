package dev.morphe.compiler.visitors.statement;

import dev.morphe.ast.statement.IfStatement;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.builder.Label;
import dev.morphe.bytecode.instructions.opcodes.ControlOpCode;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.visitors.CompilerDispatch;
import dev.morphe.compiler.visitors.CompilerStatementVisitor;

public final class IfStatementCompiler {

  private final CompilerStatementVisitor statementVisitor;

  public IfStatementCompiler(CompilerStatementVisitor statementVisitor) {
    if (statementVisitor == null) throw new IllegalArgumentException("statementVisitor cannot be null");
    this.statementVisitor = statementVisitor;
  }

  public Void compile(IfStatement statement, CompilerFunctionContext context) {
    CompilerDispatch dispatch = context.compiler().dispatch();
    BytecodeBuilder builder = context.compiler().builder();
    Label elseLabel = builder.createLabel();
    Label endLabel = builder.createLabel();
    statement.condition().accept(dispatch.expressions(), context);
    builder.emit(ControlOpCode.JMP_IF_FALSE, elseLabel);
    statement.thenBranch().accept(statementVisitor, context);
    if (statement.elseBranch() != null) {
      builder.emit(ControlOpCode.JMP, endLabel);
      builder.mark(elseLabel);
      statement.elseBranch().accept(statementVisitor, context);
      builder.mark(endLabel);
    } else {
      builder.mark(elseLabel);
    }
    return null;
  }
}