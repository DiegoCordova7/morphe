package dev.morphe.semantic;

import dev.morphe.ast.declaration.*;
import dev.morphe.ast.expression.*;
import dev.morphe.ast.pattern.*;
import dev.morphe.ast.statement.*;
import dev.morphe.ast.visitor.IASTVisitor;
import dev.morphe.semantic.result.ExpressionResult;
import dev.morphe.semantic.result.StatementResult;
import dev.morphe.semantic.visitors.*;
import dev.morphe.semantic.visitors.context.SemanticContext;

/**
 * Coordinates semantic analysis of the Morphe abstract syntax tree.
 *
 * <p>This visitor acts as the entry point for semantic analysis and delegates
 * each AST node to the specialized visitor responsible for its node family.</p>
 *
 * <p>Declaration analysis is delegated to {@link SemanticDeclarationVisitor},
 * pattern analysis to {@link SemanticPatternVisitor}, and expression and
 * statement analysis are delegated to their corresponding visitors as their
 * implementations are introduced.</p>
 *
 * <p>This class is responsible only for coordinating semantic analysis.
 * Node-specific semantic rules are implemented by the specialized analyzers.</p>
 */
public final class SemanticAnalyzer
    implements IASTVisitor<ExpressionResult, StatementResult, SemanticContext, SemanticContext, SemanticContext> {

  private final SemanticPatternVisitor patternVisitor = new SemanticPatternVisitor();
  private final SemanticExpressionVisitor expressionVisitor = new SemanticExpressionVisitor();
  private final SemanticStatementVisitor statementVisitor = new SemanticStatementVisitor(expressionVisitor);
  private final SemanticDeclarationVisitor declarationVisitor = new SemanticDeclarationVisitor(statementVisitor);

  /**
   * Delegates function declaration analysis to the declaration visitor.
   */
  @Override
  public Void visit(FunctionDeclaration declaration, SemanticContext context) {
    return declarationVisitor.visit(declaration, context);
  }

  /**
   * Delegates composite declaration analysis to the declaration visitor.
   */
  @Override
  public Void visit(CompositeDeclaration declaration, SemanticContext context) {
    return declarationVisitor.visit(declaration, context);
  }

  /**
   * Performs semantic analysis of a literal expression.
   */
  @Override
  public ExpressionResult visit(LiteralExpression expression, SemanticContext context) {
    return expressionVisitor.visit(expression, context);
  }

  /**
   * Performs semantic analysis of a variable expression.
   */
  @Override
  public ExpressionResult visit(VariableExpression expression, SemanticContext context) {
    return expressionVisitor.visit(expression, context);
  }

  /**
   * Performs semantic analysis of a binary expression.
   */
  @Override
  public ExpressionResult visit(BinaryExpression expression, SemanticContext context) {
    return expressionVisitor.visit(expression, context);
  }

  /**
   * Performs semantic analysis of a unary expression.
   */
  @Override
  public ExpressionResult visit(UnaryExpression expression, SemanticContext context) {
    return expressionVisitor.visit(expression, context);
  }

  /**
   * Performs semantic analysis of an update expression.
   */
  @Override
  public ExpressionResult visit(UpdateExpression expression, SemanticContext context) {
    return expressionVisitor.visit(expression, context);
  }

  /**
   * Performs semantic analysis of an array literal expression.
   */
  @Override
  public ExpressionResult visit(ArrayLiteralExpression expression, SemanticContext context) {
    return expressionVisitor.visit(expression, context);
  }

  /**
   * Performs semantic analysis of an array access expression.
   */
  @Override
  public ExpressionResult visit(ArrayAccessExpression expression, SemanticContext context) {
    return expressionVisitor.visit(expression, context);
  }

  /**
   * Performs semantic analysis of an array creation expression.
   */
  @Override
  public ExpressionResult visit(ArrayCreationExpression expression, SemanticContext context) {
    return expressionVisitor.visit(expression, context);
  }

  /**
   * Performs semantic analysis of an if expression.
   */
  @Override
  public ExpressionResult visit(IfExpression expression, SemanticContext context) {
    return expressionVisitor.visit(expression, context);
  }

  /**
   * Performs semantic analysis of a match expression.
   */
  @Override
  public ExpressionResult visit(MatchExpression expression, SemanticContext context) {
    return expressionVisitor.visit(expression, context);
  }

  /**
   * Performs semantic analysis of an attempt expression.
   */
  @Override
  public ExpressionResult visit(AttemptExpression expression, SemanticContext context) {
    return expressionVisitor.visit(expression, context);
  }

  /**
   * Performs semantic analysis of a function call expression.
   */
  @Override
  public ExpressionResult visit(FunctionCallExpression expression, SemanticContext context) {
    return expressionVisitor.visit(expression, context);
  }

  /**
   * Performs semantic analysis of a lambda expression.
   */
  @Override
  public ExpressionResult visit(LambdaExpression expression, SemanticContext context) {
    return expressionVisitor.visit(expression, context);
  }

  /**
   * Performs semantic analysis of a composite instance expression.
   */
  @Override
  public ExpressionResult visit(CompositeInstanceExpression expression, SemanticContext context) {
    return expressionVisitor.visit(expression, context);
  }

  /**
   * Performs semantic analysis of a field access expression.
   */
  @Override
  public ExpressionResult visit(FieldAccessExpression expression, SemanticContext context) {
    return expressionVisitor.visit(expression, context);
  }

  /**
   * Delegates default pattern analysis to the pattern visitor.
   */
  @Override
  public Void visit(DefaultPattern pattern, SemanticContext context) {
    return patternVisitor.visit(pattern, context);
  }

  /**
   * Delegates literal pattern analysis to the pattern visitor.
   */
  @Override
  public Void visit(LiteralPattern pattern, SemanticContext context) {
    return patternVisitor.visit(pattern, context);
  }

  /**
   * Delegates or-pattern analysis to the pattern visitor.
   */
  @Override
  public Void visit(OrPattern pattern, SemanticContext context) {
    return patternVisitor.visit(pattern, context);
  }

  /**
   * Delegates range pattern analysis to the pattern visitor.
   */
  @Override
  public Void visit(RangePattern pattern, SemanticContext context) {
    return patternVisitor.visit(pattern, context);
  }

  /**
   * Delegates variable pattern analysis to the pattern visitor.
   */
  @Override
  public Void visit(VariablePattern pattern, SemanticContext context) {
    return patternVisitor.visit(pattern, context);
  }

  /**
   * Delegates array pattern analysis to the pattern visitor.
   */
  @Override
  public Void visit(ArrayPattern pattern, SemanticContext context) {
    return patternVisitor.visit(pattern, context);
  }

  /**
   * Delegates composite pattern analysis to the pattern visitor.
   */
  @Override
  public Void visit(CompositePattern pattern, SemanticContext context) {
    return patternVisitor.visit(pattern, context);
  }

  /**
   * Performs semantic analysis of a block statement.
   */
  @Override
  public StatementResult visit(BlockStatement statement, SemanticContext context) {
    return statementVisitor.visit(statement, context);
  }

  /**
   * Performs semantic analysis of an expression statement.
   */
  @Override
  public StatementResult visit(ExpressionStatement statement, SemanticContext context) {
    return statementVisitor.visit(statement, context);
  }

  /**
   * Performs semantic analysis of a variable declaration statement.
   */
  @Override
  public StatementResult visit(VariableDeclarationStatement statement, SemanticContext context) {
    return statementVisitor.visit(statement, context);
  }

  /**
   * Performs semantic analysis of an assignment statement.
   */
  @Override
  public StatementResult visit(AssignmentStatement statement, SemanticContext context) {
    return statementVisitor.visit(statement, context);
  }

  /**
   * Performs semantic analysis of an if statement.
   */
  @Override
  public StatementResult visit(IfStatement statement, SemanticContext context) {
    return statementVisitor.visit(statement, context);
  }

  /**
   * Performs semantic analysis of a match statement.
   */
  @Override
  public StatementResult visit(MatchStatement statement, SemanticContext context) {
    return statementVisitor.visit(statement, context);
  }

  /**
   * Performs semantic analysis of a while statement.
   */
  @Override
  public StatementResult visit(WhileStatement statement, SemanticContext context) {
    return statementVisitor.visit(statement, context);
  }

  /**
   * Performs semantic analysis of a do-while statement.
   */
  @Override
  public StatementResult visit(DoWhileStatement statement, SemanticContext context) {
    return statementVisitor.visit(statement, context);
  }

  /**
   * Performs semantic analysis of a for statement.
   */
  @Override
  public StatementResult visit(ForStatement statement, SemanticContext context) {
    return statementVisitor.visit(statement, context);
  }

  /**
   * Performs semantic analysis of a break statement.
   */
  @Override
  public StatementResult visit(BreakStatement statement, SemanticContext context) {
    return statementVisitor.visit(statement, context);
  }

  /**
   * Performs semantic analysis of a continue statement.
   */
  @Override
  public StatementResult visit(ContinueStatement statement, SemanticContext context) {
    return statementVisitor.visit(statement, context);
  }

  /**
   * Performs semantic analysis of a return statement.
   */
  @Override
  public StatementResult visit(ReturnStatement statement, SemanticContext context) {
    return statementVisitor.visit(statement, context);
  }

  public SemanticExpressionVisitor expressionVisitor() {
    return expressionVisitor;
  }

  public SemanticStatementVisitor statementVisitor() {
    return statementVisitor;
  }

  public SemanticPatternVisitor patternVisitor() {
    return patternVisitor;
  }
}