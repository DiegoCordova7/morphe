package dev.morphe.compiler.visitors;

import dev.morphe.ast.expression.*;
import dev.morphe.ast.visitor.IExpressionVisitor;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.type.CompilerTypeResolver;
import dev.morphe.compiler.visitors.expression.*;

public final class CompilerExpressionVisitor implements IExpressionVisitor<Void, CompilerFunctionContext> {

  private final LiteralExpressionCompiler literalExpression;
  private final VariableExpressionCompiler variableExpression;
  private final BinaryExpressionCompiler binaryExpression;
  private final UnaryExpressionCompiler unaryExpression;
  private final UpdateExpressionCompiler updateExpression;
  private final ArrayLiteralExpressionCompiler arrayLiteralExpression;
  private final ArrayCreationExpressionCompiler arrayCreationExpression;
  private final ArrayAccessExpressionCompiler arrayAccessExpression;
  private final CompositeInstanceExpressionCompiler compositeInstanceExpression;
  private final FieldAccessExpressionCompiler fieldAccessExpression;
  private final FunctionCallExpressionCompiler functionCallExpression;
  private final LambdaExpressionCompiler lambdaExpression;
  private final IfExpressionCompiler ifExpression;
  private final MatchExpressionCompiler matchExpression;
  private final AttemptExpressionCompiler attemptExpression;

  public CompilerExpressionVisitor(CompilerTypeResolver typeResolver) {
    if (typeResolver == null) throw new IllegalArgumentException("typeResolver cannot be null");
    this.literalExpression = new LiteralExpressionCompiler();
    this.variableExpression = new VariableExpressionCompiler();
    this.binaryExpression = new BinaryExpressionCompiler(this);
    this.unaryExpression = new UnaryExpressionCompiler(this);
    this.updateExpression = new UpdateExpressionCompiler();
    this.arrayLiteralExpression = new ArrayLiteralExpressionCompiler(this, typeResolver);
    this.arrayCreationExpression = new ArrayCreationExpressionCompiler(this, typeResolver);
    this.arrayAccessExpression = new ArrayAccessExpressionCompiler(this);
    this.compositeInstanceExpression = new CompositeInstanceExpressionCompiler(this, typeResolver);
    this.fieldAccessExpression = new FieldAccessExpressionCompiler(this);
    this.functionCallExpression = new FunctionCallExpressionCompiler(this);
    this.lambdaExpression = new LambdaExpressionCompiler(typeResolver, this);
    this.ifExpression = new IfExpressionCompiler(this);
    matchExpression = new MatchExpressionCompiler(this);
    this.attemptExpression = new AttemptExpressionCompiler();
  }

  @Override
  public Void visit(LiteralExpression expression, CompilerFunctionContext context) {
    return literalExpression.compile(expression, context);
  }

  @Override
  public Void visit(VariableExpression expression, CompilerFunctionContext context) {
    return variableExpression.compile(expression, context);
  }

  @Override
  public Void visit(BinaryExpression expression, CompilerFunctionContext context) {
    return binaryExpression.compile(expression, context);
  }

  @Override
  public Void visit(UnaryExpression expression, CompilerFunctionContext context) {
    return unaryExpression.compile(expression, context);
  }

  @Override
  public Void visit(UpdateExpression expression, CompilerFunctionContext context) {
    return updateExpression.compile(expression, context);
  }

  @Override
  public Void visit(ArrayLiteralExpression expression, CompilerFunctionContext context) {
    return arrayLiteralExpression.compile(expression, context);
  }

  @Override
  public Void visit(ArrayAccessExpression expression, CompilerFunctionContext context) {
    return arrayAccessExpression.compile(expression, context);
  }

  @Override
  public Void visit(ArrayCreationExpression expression, CompilerFunctionContext context) {
    return arrayCreationExpression.compile(expression, context);
  }

  @Override
  public Void visit(IfExpression expression, CompilerFunctionContext context) {
    return ifExpression.compile(expression, context);
  }

  @Override
  public Void visit(MatchExpression expression, CompilerFunctionContext context) {
    return matchExpression.compile(expression, context);
  }

  @Override
  public Void visit(AttemptExpression expression, CompilerFunctionContext context) {
    return attemptExpression.compile(expression, context);
  }

  @Override
  public Void visit(FunctionCallExpression expression, CompilerFunctionContext context) {
    return functionCallExpression.compile(expression, context);
  }

  @Override
  public Void visit(LambdaExpression expression, CompilerFunctionContext context) {
    return lambdaExpression.compile(expression, context);
  }

  @Override
  public Void visit(CompositeInstanceExpression expression, CompilerFunctionContext context) {
    return compositeInstanceExpression.compile(expression, context);
  }

  @Override
  public Void visit(FieldAccessExpression expression, CompilerFunctionContext context) {
    return fieldAccessExpression.compile(expression, context);
  }
}