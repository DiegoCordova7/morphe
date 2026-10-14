package dev.morphe.semantic.visitors;

import dev.morphe.ast.expression.*;
import dev.morphe.ast.type.IASTType;
import dev.morphe.ast.visitor.IExpressionVisitor;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.result.ExpressionResult;
import dev.morphe.semantic.result.VariableResult;
import dev.morphe.semantic.symbol.SymbolKind;
import dev.morphe.semantic.visitors.context.SemanticContext;
import dev.morphe.semantic.visitors.expression.*;

/**
 * Coordinates semantic analysis of expression nodes in the Morphe AST.
 *
 * <p>This visitor dispatches each expression to its corresponding semantic
 * analyzer and wraps the resulting type information in an
 * {@link ExpressionResult}.</p>
 *
 * <p>The result also identifies whether the analyzed expression represents
 * an assignable location. Currently variables, array accesses, and field
 * accesses are considered assignable expressions.</p>
 */
public final class SemanticExpressionVisitor implements IExpressionVisitor<ExpressionResult, SemanticContext> {

  private final LiteralExpressionAnalyzer literalExpression = new LiteralExpressionAnalyzer();
  private final VariableExpressionAnalyzer variableExpression = new VariableExpressionAnalyzer();
  private final BinaryExpressionAnalyzer binaryExpression = new BinaryExpressionAnalyzer(this);
  private final UnaryExpressionAnalyzer unaryExpression = new UnaryExpressionAnalyzer(this);
  private final UpdateExpressionAnalyzer updateExpression = new UpdateExpressionAnalyzer();
  private final ArrayLiteralExpressionAnalyzer arrayLiteralExpression = new ArrayLiteralExpressionAnalyzer(this);
  private final ArrayAccessExpressionAnalyzer arrayAccessExpression = new ArrayAccessExpressionAnalyzer(this);
  private final ArrayCreationExpressionAnalyzer arrayCreationExpression = new ArrayCreationExpressionAnalyzer(this);
  private final FieldAccessExpressionAnalyzer fieldAccessExpression = new FieldAccessExpressionAnalyzer(this);
  private final CompositeInstanceExpressionAnalyzer compositeInstanceExpression = new CompositeInstanceExpressionAnalyzer(this);
  private final FunctionCallExpressionAnalyzer functionCallExpression = new FunctionCallExpressionAnalyzer(this);
  private final LambdaExpressionAnalyzer lambdaExpression = new LambdaExpressionAnalyzer(this);
  private final IfExpressionAnalyzer ifExpression = new IfExpressionAnalyzer(this);
  private final MatchExpressionAnalyzer matchExpression = new MatchExpressionAnalyzer(this);
  private final AttemptExpressionAnalyzer attemptExpression = new AttemptExpressionAnalyzer();

  public SemanticExpressionVisitor() {}

  /**
   * Creates and stores the semantic result of an expression.
   *
   * <p>The result is stored in the semantic context so that later compiler
   * stages can retrieve semantic information for the exact AST node.</p>
   */
  private ExpressionResult result(IExpression expression, SemanticContext context, IASTType type,
                                  boolean assignable, boolean mutable) {
    ExpressionResult result = new ExpressionResult(type, assignable, mutable);
    context.expressionResult(expression, result);
    return result;
  }

  /**
   * Analyzes any expression using the appropriate semantic analyzer.
   *
   * <p>Array literal expressions use the specialized array literal analysis
   * entry point because their analysis may depend on contextual type
   * information.</p>
   *
   * @param expression the expression being analyzed
   * @param context the shared semantic analysis context
   * @return the semantic result of the expression
   */
  public ExpressionResult analyze(IExpression expression, SemanticContext context) {
    if (expression == null) throw new NullSemanticArgumentException("expression");
    if (context == null) throw new NullSemanticArgumentException("context");
    if (expression instanceof ArrayLiteralExpression arrayLiteral) {
      return result(arrayLiteral, context, arrayLiteralExpression.analyze(arrayLiteral, context), false, false);
    }
    return expression.accept(this, context);
  }

  /**
   * Visits a literal expression.
   *
   * @param expression the literal expression being visited
   * @param context the shared semantic analysis context
   * @return the semantic result of the literal
   */
  @Override
  public ExpressionResult visit(LiteralExpression expression, SemanticContext context) {
    return result(expression, context, literalExpression.analyze(expression), false, false);
  }

  /**
   * Visits a variable expression.
   *
   * @param expression the variable expression being visited
   * @param context the shared semantic analysis context
   * @return the semantic result of the variable
   */
  @Override
  public ExpressionResult visit(VariableExpression expression, SemanticContext context) {
    VariableResult variable = variableExpression.analyze(expression, context);
    SymbolKind kind = variable.kind();
    boolean assignable = kind == SymbolKind.VARIABLE || kind == SymbolKind.CONSTANT || kind == SymbolKind.PARAMETER;
    boolean mutable = kind == SymbolKind.VARIABLE || kind == SymbolKind.PARAMETER;
    return result(expression, context, variable.type(), assignable, mutable);
  }

  /**
   * Visits a binary expression.
   *
   * @param expression the binary expression being visited
   * @param context the shared semantic analysis context
   * @return the semantic result of the binary expression
   */
  @Override
  public ExpressionResult visit(BinaryExpression expression, SemanticContext context) {
    return result(expression, context, binaryExpression.analyze(expression, context), false, false);
  }

  /**
   * Visits a unary expression.
   *
   * @param expression the unary expression being visited
   * @param context the shared semantic analysis context
   * @return the semantic result of the unary expression
   */
  @Override
  public ExpressionResult visit(UnaryExpression expression, SemanticContext context) {
    return result(expression, context, unaryExpression.analyze(expression, context), false, false);
  }

  /**
   * Visits an update expression.
   *
   * @param expression the update expression being visited
   * @param context the shared semantic analysis context
   * @return the semantic result of the update expression
   */
  @Override
  public ExpressionResult visit(UpdateExpression expression, SemanticContext context) {
    return result(expression, context, updateExpression.analyze(expression, context), false, false);
  }

  /**
   * Visits an array literal expression.
   *
   * @param expression the array literal expression being visited
   * @param context the shared semantic analysis context
   * @return the semantic result of the array literal
   */
  @Override
  public ExpressionResult visit(ArrayLiteralExpression expression, SemanticContext context) {
    return result(expression, context, arrayLiteralExpression.analyze(expression, context), false, false);
  }

  /**
   * Visits an array access expression.
   *
   * @param expression the array access expression being visited
   * @param context the shared semantic analysis context
   * @return the semantic result of the array access
   */
  @Override
  public ExpressionResult visit(ArrayAccessExpression expression, SemanticContext context) {
    return result(expression, context, arrayAccessExpression.analyze(expression, context), true, true);
  }

  /**
   * Visits an array creation expression.
   *
   * @param expression the array creation expression being visited
   * @param context the shared semantic analysis context
   * @return the semantic result of the array creation
   */
  @Override
  public ExpressionResult visit(ArrayCreationExpression expression, SemanticContext context) {
    return result(expression, context, arrayCreationExpression.analyze(expression, context), false, false);
  }

  /**
   * Visits an if expression.
   *
   * @param expression the if expression being visited
   * @param context the shared semantic analysis context
   * @return the semantic result of the if expression
   */
  @Override
  public ExpressionResult visit(IfExpression expression, SemanticContext context) {
    return result(expression, context, ifExpression.analyze(expression, context), false, false);
  }

  /**
   * Visits a match expression.
   *
   * @param expression the match expression being visited
   * @param context the shared semantic analysis context
   * @return the semantic result of the match expression
   */
  @Override
  public ExpressionResult visit(MatchExpression expression, SemanticContext context) {
    IASTType type = matchExpression.analyze(expression, context);
    return result(expression, context, type, false, false);
  }

  /**
   * Visits an attempt expression.
   *
   * @param expression the attempt expression being visited
   * @param context the shared semantic analysis context
   * @return the semantic result of the attempt expression
   */
  @Override
  public ExpressionResult visit(AttemptExpression expression, SemanticContext context) {
    return attemptExpression.analyze(expression, context);
  }

  /**
   * Visits a function call expression.
   *
   * @param expression the function call expression being visited
   * @param context the shared semantic analysis context
   * @return the semantic result of the function call
   */
  @Override
  public ExpressionResult visit(FunctionCallExpression expression, SemanticContext context) {
    return result(expression, context, functionCallExpression.analyze(expression, context), false, false);
  }

  /**
   * Visits a lambda expression.
   *
   * @param expression the lambda expression being visited
   * @param context the shared semantic analysis context
   * @return the semantic result of the lambda expression
   */
  @Override
  public ExpressionResult visit(LambdaExpression expression, SemanticContext context) {
    return result(expression, context, lambdaExpression.analyze(expression, context), false, false);
  }

  /**
   * Visits a composite instance expression.
   *
   * @param expression the composite instance expression being visited
   * @param context the shared semantic analysis context
   * @return the semantic result of the composite instance
   */
  @Override
  public ExpressionResult visit(CompositeInstanceExpression expression, SemanticContext context) {
    return result(expression, context, compositeInstanceExpression.analyze(expression, context), false, false);
  }

  /**
   * Visits a field access expression.
   *
   * @param expression the field access expression being visited
   * @param context the shared semantic analysis context
   * @return the semantic result of the field access
   */
  @Override
  public ExpressionResult visit(FieldAccessExpression expression, SemanticContext context) {
    return result(expression, context, fieldAccessExpression.analyze(expression, context), true, true);
  }
}