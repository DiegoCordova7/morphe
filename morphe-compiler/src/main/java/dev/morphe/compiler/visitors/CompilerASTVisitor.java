package dev.morphe.compiler.visitors;

import dev.morphe.ast.declaration.*;
import dev.morphe.ast.expression.*;
import dev.morphe.ast.pattern.*;
import dev.morphe.ast.statement.*;
import dev.morphe.ast.visitor.IASTVisitor;
import dev.morphe.compiler.context.*;
import dev.morphe.compiler.type.CompilerTypeResolver;

public final class CompilerASTVisitor implements IASTVisitor<Void, Void, CompilerContext,
    CompilerFunctionContext, CompilerPatternContext> {

  private final CompilerPatternVisitor patterns;
  private final CompilerExpressionVisitor expressions;
  private final CompilerStatementVisitor statements;
  private final CompilerDeclarationVisitor declarations;

  public CompilerASTVisitor(CompilerTypeResolver typeResolver) {
    this.patterns = new CompilerPatternVisitor(typeResolver);
    this.expressions = new CompilerExpressionVisitor(typeResolver);
    this.statements = new CompilerStatementVisitor();
    this.declarations = new CompilerDeclarationVisitor(typeResolver);
  }

  public CompilerPatternVisitor patternVisitor() {
    return patterns;
  }

  public CompilerExpressionVisitor expressionVisitor() {
    return expressions;
  }

  public CompilerStatementVisitor statementVisitor() {
    return statements;
  }

  @Override
  public Void visit(FunctionDeclaration functionDeclaration, CompilerContext context) {
    return declarations.visit(functionDeclaration, context);
  }

  @Override
  public Void visit(CompositeDeclaration compositeDeclaration, CompilerContext context) {
    return declarations.visit(compositeDeclaration, context);
  }

  @Override
  public Void visit(LiteralExpression literalExpression, CompilerFunctionContext context) {
    return expressions.visit(literalExpression, context);
  }

  @Override
  public Void visit(VariableExpression variableExpression, CompilerFunctionContext context) {
    return expressions.visit(variableExpression, context);
  }

  @Override
  public Void visit(BinaryExpression binaryExpression, CompilerFunctionContext context) {
    return expressions.visit(binaryExpression, context);
  }

  @Override
  public Void visit(UnaryExpression unaryExpression, CompilerFunctionContext context) {
    return expressions.visit(unaryExpression, context);
  }

  @Override
  public Void visit(UpdateExpression updateExpression, CompilerFunctionContext context) {
    return expressions.visit(updateExpression, context);
  }

  @Override
  public Void visit(ArrayLiteralExpression arrayLiteralExpression, CompilerFunctionContext context) {
    return expressions.visit(arrayLiteralExpression, context);
  }

  @Override
  public Void visit(ArrayAccessExpression arrayAccessExpression, CompilerFunctionContext context) {
    return expressions.visit(arrayAccessExpression, context);
  }

  @Override
  public Void visit(ArrayCreationExpression arrayCreationExpression, CompilerFunctionContext context) {
    return expressions.visit(arrayCreationExpression, context);
  }

  @Override
  public Void visit(IfExpression ifExpression, CompilerFunctionContext context) {
    return expressions.visit(ifExpression, context);
  }

  @Override
  public Void visit(MatchExpression matchExpression, CompilerFunctionContext context) {
    return expressions.visit(matchExpression, context);
  }

  @Override
  public Void visit(AttemptExpression attemptExpression, CompilerFunctionContext context) {
    return expressions.visit(attemptExpression, context);
  }

  @Override
  public Void visit(FunctionCallExpression functionCallExpression, CompilerFunctionContext context) {
    return expressions.visit(functionCallExpression, context);
  }

  @Override
  public Void visit(LambdaExpression lambdaExpression, CompilerFunctionContext context) {
    return expressions.visit(lambdaExpression, context);
  }

  @Override
  public Void visit(CompositeInstanceExpression compositeInstanceExpression, CompilerFunctionContext context) {
    return expressions.visit(compositeInstanceExpression, context);
  }

  @Override
  public Void visit(FieldAccessExpression fieldAccessExpression, CompilerFunctionContext context) {
    return expressions.visit(fieldAccessExpression, context);
  }

  @Override
  public Void visit(DefaultPattern defaultPattern, CompilerPatternContext context) {
    return patterns.visit(defaultPattern, context);
  }

  @Override
  public Void visit(LiteralPattern literalPattern, CompilerPatternContext context) {
    return patterns.visit(literalPattern, context);
  }

  @Override
  public Void visit(OrPattern orPattern, CompilerPatternContext context) {
    return patterns.visit(orPattern, context);
  }

  @Override
  public Void visit(RangePattern rangePattern, CompilerPatternContext context) {
    return patterns.visit(rangePattern, context);
  }

  @Override
  public Void visit(VariablePattern variablePattern, CompilerPatternContext context) {
    return patterns.visit(variablePattern, context);
  }

  @Override
  public Void visit(ArrayPattern arrayPattern, CompilerPatternContext context) {
    return patterns.visit(arrayPattern, context);
  }

  @Override
  public Void visit(CompositePattern compositePattern, CompilerPatternContext context) {
    return patterns.visit(compositePattern, context);
  }

  @Override
  public Void visit(BlockStatement blockStatement, CompilerFunctionContext context) {
    return statements.visit(blockStatement, context);
  }

  @Override
  public Void visit(ExpressionStatement expressionStatement, CompilerFunctionContext context) {
    return statements.visit(expressionStatement, context);
  }

  @Override
  public Void visit(VariableDeclarationStatement variableDeclarationStatement, CompilerFunctionContext context) {
    return statements.visit(variableDeclarationStatement, context);
  }

  @Override
  public Void visit(AssignmentStatement assignmentStatement, CompilerFunctionContext context) {
    return statements.visit(assignmentStatement, context);
  }

  @Override
  public Void visit(IfStatement ifStatement, CompilerFunctionContext context) {
    return statements.visit(ifStatement, context);
  }

  @Override
  public Void visit(MatchStatement matchStatement, CompilerFunctionContext context) {
    return statements.visit(matchStatement, context);
  }

  @Override
  public Void visit(WhileStatement whileStatement, CompilerFunctionContext context) {
    return statements.visit(whileStatement, context);
  }

  @Override
  public Void visit(DoWhileStatement doWhileStatement, CompilerFunctionContext context) {
    return statements.visit(doWhileStatement, context);
  }

  @Override
  public Void visit(ForStatement forStatement, CompilerFunctionContext context) {
    return statements.visit(forStatement, context);
  }

  @Override
  public Void visit(BreakStatement breakStatement, CompilerFunctionContext context) {
    return statements.visit(breakStatement, context);
  }

  @Override
  public Void visit(ContinueStatement continueStatement, CompilerFunctionContext context) {
    return statements.visit(continueStatement, context);
  }

  @Override
  public Void visit(ReturnStatement returnStatement, CompilerFunctionContext context) {
    return statements.visit(returnStatement, context);
  }
}