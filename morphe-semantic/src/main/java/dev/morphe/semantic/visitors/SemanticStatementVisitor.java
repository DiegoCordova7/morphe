package dev.morphe.semantic.visitors;

import dev.morphe.ast.statement.*;
import dev.morphe.ast.visitor.IStatementVisitor;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.result.StatementResult;
import dev.morphe.semantic.visitors.context.SemanticContext;
import dev.morphe.semantic.visitors.statement.*;
import dev.morphe.semantic.visitors.validator.SemanticArgumentValidator;

/**
 * Coordinates semantic analysis of statement nodes in the Morphe AST.
 *
 * <p>This visitor dispatches each statement to the semantic analyzer
 * responsible for its specific statement kind.</p>
 *
 * <p>The visitor itself does not perform statement-specific semantic
 * validation. That responsibility belongs to the corresponding statement
 * analyzers.</p>
 *
 * <p>All analyzers operate using the shared {@link SemanticContext}.</p>
 */
public final class SemanticStatementVisitor implements IStatementVisitor<StatementResult, SemanticContext> {

  private final SemanticExpressionVisitor expressionVisitor;

  private final ExpressionStatementAnalyzer expressionStatement = new ExpressionStatementAnalyzer();
  private final VariableDeclarationStatementAnalyzer variableDeclarationStatement = new VariableDeclarationStatementAnalyzer();
  private final AssignmentStatementAnalyzer assignmentStatement = new AssignmentStatementAnalyzer();
  private final BlockStatementAnalyzer blockStatement = new BlockStatementAnalyzer();
  private final ContinueStatementAnalyzer continueStatement = new ContinueStatementAnalyzer();
  private final BreakStatementAnalyzer breakStatement = new BreakStatementAnalyzer();
  private final ReturnStatementAnalyzer returnStatement = new ReturnStatementAnalyzer();
  private final IfStatementAnalyzer ifStatement = new IfStatementAnalyzer();
  private final MatchStatementAnalyzer matchStatement = new MatchStatementAnalyzer();
  private final WhileStatementAnalyzer whileStatement = new WhileStatementAnalyzer();
  private final DoWhileStatementAnalyzer doWhileStatement = new DoWhileStatementAnalyzer();
  private final ForStatementAnalyzer forStatement = new ForStatementAnalyzer();

  public SemanticStatementVisitor(SemanticExpressionVisitor expressionVisitor) {
    if (expressionVisitor == null) throw new NullSemanticArgumentException("expressionVisitor");
    this.expressionVisitor = expressionVisitor;
  }

  /**
   * Performs semantic analysis of a statement using the appropriate
   * statement-specific analyzer.
   *
   * @param statement the statement being analyzed
   * @param context the shared semantic analysis context
   * @return the semantic result produced for the statement
   */
  public StatementResult analyze(IStatement statement, SemanticContext context) {
    SemanticArgumentValidator.validateArguments(statement, "statement", context);
    return statement.accept(this, context);
  }

  /**
   * Visits a block statement.
   *
   * @param statement the block statement being visited
   * @param context   the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  @Override
  public StatementResult visit(BlockStatement statement, SemanticContext context) {
    return blockStatement.analyze(statement, context, this);
  }

  /**
   * Visits an expression statement.
   *
   * @param statement the expression statement being visited
   * @param context   the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  @Override
  public StatementResult visit(ExpressionStatement statement, SemanticContext context) {
    return expressionStatement.analyze(statement, context, expressionVisitor);
  }

  /**
   * Visits a variable declaration statement.
   *
   * @param statement the variable declaration statement being visited
   * @param context   the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  @Override
  public StatementResult visit(VariableDeclarationStatement statement, SemanticContext context) {
    return variableDeclarationStatement.analyze(statement, context, expressionVisitor);
  }

  /**
   * Visits an assignment statement.
   *
   * @param statement the assignment statement being visited
   * @param context   the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  @Override
  public StatementResult visit(AssignmentStatement statement, SemanticContext context) {
    return assignmentStatement.analyze(statement, context, expressionVisitor);
  }

  /**
   * Visits an if statement.
   *
   * @param statement the if statement being visited
   * @param context   the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  @Override
  public StatementResult visit(IfStatement statement, SemanticContext context) {
    return ifStatement.analyze(statement, context, expressionVisitor, this);
  }

  /**
   * Visits a match statement.
   *
   * @param statement the match statement being visited
   * @param context   the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  @Override
  public StatementResult visit(MatchStatement statement, SemanticContext context) {
    return matchStatement.analyze(statement, context, expressionVisitor, context.patternVisitor(), this);
  }

  /**
   * Visits a while statement.
   *
   * @param statement the while statement being visited
   * @param context   the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  @Override
  public StatementResult visit(WhileStatement statement, SemanticContext context) {
    return whileStatement.analyze(statement, context, expressionVisitor, this);
  }

  /**
   * Visits a do-while statement.
   *
   * @param statement the do-while statement being visited
   * @param context   the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  @Override
  public StatementResult visit(DoWhileStatement statement, SemanticContext context) {
    return doWhileStatement.analyze(statement, context, expressionVisitor, this);
  }

  /**
   * Visits a for statement.
   *
   * @param statement the for statement being visited
   * @param context   the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  @Override
  public StatementResult visit(ForStatement statement, SemanticContext context) {
    return forStatement.analyze(statement, context, expressionVisitor, this);
  }

  /**
   * Visits a break statement.
   *
   * @param statement the break statement being visited
   * @param context   the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  @Override
  public StatementResult visit(BreakStatement statement, SemanticContext context) {
    return breakStatement.analyze(statement, context);
  }

  /**
   * Visits a continue statement.
   *
   * @param statement the continue statement being visited
   * @param context   the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  @Override
  public StatementResult visit(ContinueStatement statement, SemanticContext context) {
    return continueStatement.analyze(statement, context);
  }

  /**
   * Visits a return statement.
   *
   * @param statement the return statement being visited
   * @param context   the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  @Override
  public StatementResult visit(ReturnStatement statement, SemanticContext context) {
    return returnStatement.analyze(statement, context, expressionVisitor);
  }
}