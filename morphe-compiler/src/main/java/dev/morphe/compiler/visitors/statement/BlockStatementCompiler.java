package dev.morphe.compiler.visitors.statement;

import dev.morphe.ast.statement.BlockStatement;
import dev.morphe.ast.statement.ExpressionStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.visitors.CompilerStatementVisitor;

import java.util.List;

public final class BlockStatementCompiler {

  private final CompilerStatementVisitor statementVisitor;

  public BlockStatementCompiler(CompilerStatementVisitor statementVisitor) {
    if (statementVisitor == null) throw new IllegalArgumentException("statementVisitor cannot be null");
    this.statementVisitor = statementVisitor;
  }

  public Void compile(BlockStatement statement, CompilerFunctionContext context) {
    for (IStatement istatement : statement.statements())
      istatement.accept(statementVisitor, context);
    return null;
  }
}