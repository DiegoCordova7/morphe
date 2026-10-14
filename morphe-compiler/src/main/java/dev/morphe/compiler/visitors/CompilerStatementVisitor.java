package dev.morphe.compiler.visitors;

import dev.morphe.ast.statement.*;
import dev.morphe.ast.visitor.IStatementVisitor;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.visitors.statement.*;

public final class CompilerStatementVisitor implements IStatementVisitor<Void, CompilerFunctionContext> {

  private final BlockStatementCompiler blockStatement;
  private final ExpressionStatementCompiler expressionStatement;
  private final VariableDeclarationStatementCompiler variableDeclarationStatement;
  private final AssignmentStatementCompiler assignmentStatement;
  private final IfStatementCompiler ifStatement;
  private final MatchStatementCompiler matchStatement;
  private final WhileStatementCompiler whileStatement;
  private final DoWhileStatementCompiler doWhileStatement;
  private final ForStatementCompiler forStatement;
  private final BreakStatementCompiler breakStatement;
  private final ContinueStatementCompiler continueStatement;
  private final ReturnStatementCompiler returnStatement;

  public CompilerStatementVisitor() {
    this.blockStatement = new BlockStatementCompiler(this);
    this.expressionStatement = new ExpressionStatementCompiler();
    this.variableDeclarationStatement = new VariableDeclarationStatementCompiler();
    this.assignmentStatement = new AssignmentStatementCompiler();
    this.ifStatement = new IfStatementCompiler(this);
    this.matchStatement = new MatchStatementCompiler(this);
    this.whileStatement = new WhileStatementCompiler(this);
    this.doWhileStatement = new DoWhileStatementCompiler(this);
    this.forStatement = new ForStatementCompiler(this);
    this.breakStatement = new BreakStatementCompiler();
    this.continueStatement = new ContinueStatementCompiler();
    this.returnStatement = new ReturnStatementCompiler();
  }

  @Override
  public Void visit(BlockStatement statement, CompilerFunctionContext context) {
    return blockStatement.compile(statement, context);
  }

  @Override
  public Void visit(ExpressionStatement statement, CompilerFunctionContext context) {
    return expressionStatement.compile(statement, context);
  }

  @Override
  public Void visit(VariableDeclarationStatement statement, CompilerFunctionContext context) {
    return variableDeclarationStatement.compile(statement, context);
  }

  @Override
  public Void visit(AssignmentStatement statement, CompilerFunctionContext context) {
    return assignmentStatement.compile(statement, context);
  }

  @Override
  public Void visit(IfStatement statement, CompilerFunctionContext context) {
    return ifStatement.compile(statement, context);
  }

  @Override
  public Void visit(MatchStatement statement, CompilerFunctionContext context) {
    return matchStatement.compile(statement, context);
  }

  @Override
  public Void visit(WhileStatement statement, CompilerFunctionContext context) {
    return whileStatement.compile(statement, context);
  }

  @Override
  public Void visit(DoWhileStatement statement, CompilerFunctionContext context) {
    return doWhileStatement.compile(statement, context);
  }

  @Override
  public Void visit(ForStatement statement, CompilerFunctionContext context) {
    return forStatement.compile(statement, context);
  }

  @Override
  public Void visit(BreakStatement statement, CompilerFunctionContext context) {
    return breakStatement.compile(context);
  }

  @Override
  public Void visit(ContinueStatement statement, CompilerFunctionContext context) {
    return continueStatement.compile(context);
  }

  @Override
  public Void visit(ReturnStatement statement, CompilerFunctionContext context) {
    return returnStatement.compile(statement, context);
  }
}