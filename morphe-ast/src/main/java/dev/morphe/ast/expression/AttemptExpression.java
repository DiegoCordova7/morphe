package dev.morphe.ast.expression;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.BlockStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.ast.type.ErrorType;
import dev.morphe.ast.visitor.IExpressionVisitor;

import java.util.List;
import java.util.Objects;

/**
 * Represents an attempt expression in the Morphe abstract syntax tree.
 *
 * <p>An attempt expression evaluates a block that may produce a recoverable
 * execution error. If the block completes successfully, its resulting value
 * is used. If a recoverable error occurs, the fallback expression provides
 * the resulting value.</p>
 *
 * <p>Optional error handlers can perform additional actions when a specific
 * recoverable error type is produced by the attempted block. Error handlers
 * do not determine the resulting value of the attempt expression; the
 * fallback expression provides that value when recovery is required.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * Integer division = attempt {
 *   10 / x
 * } => -1
 * onError(DivisionByZero) => print("Division by zero");
 * }</pre>
 */
public final class AttemptExpression implements IExpression {

  private final SourceRange range;
  private final BlockStatement body;
  private final IExpression fallback;
  private final List<ErrorHandler> handlers;

  /**
   * Creates an attempt expression.
   *
   * @param range the source range occupied by the expression
   * @param body the block whose execution is attempted
   * @param fallback the expression providing the result when recovery is required
   * @param handlers the optional error handlers associated with the attempt
   * @throws NullArgumentException if {@code range}, {@code body},
   * {@code fallback}, or {@code handlers} is null, or if any handler is null
   */
  public AttemptExpression(SourceRange range, BlockStatement body, IExpression fallback, List<ErrorHandler> handlers) {
    if (range == null) throw new NullArgumentException("range");
    if (body == null) throw new NullArgumentException("body");
    if (fallback == null) throw new NullArgumentException("fallback");
    if (handlers == null) throw new NullArgumentException("handlers");
    if (handlers.stream().anyMatch(Objects::isNull)) throw new NullArgumentException("handler");
    this.range = range;
    this.body = body;
    this.fallback = fallback;
    this.handlers = List.copyOf(handlers);
  }

  /**
   * Returns the source range occupied by this expression.
   *
   * @return the expression source range
   */
  @Override
  public SourceRange range() {
    return range;
  }

  /**
   * Returns the block whose execution is attempted.
   *
   * @return the attempted block
   */
  public BlockStatement body() {
    return body;
  }

  /**
   * Returns the expression providing the result when recovery is required.
   *
   * @return the fallback expression
   */
  public IExpression fallback() {
    return fallback;
  }

  /**
   * Returns the error handlers associated with this attempt expression.
   *
   * @return an immutable list of error handlers
   */
  public List<ErrorHandler> handlers() {
    return handlers;
  }

  /**
   * Accepts an expression visitor and dispatches this node to it.
   *
   * @param visitor the visitor processing this expression
   * @param context the operational context passed through the traversal
   * @param <R> the result type produced by the visitor
   * @param <C> the context type passed to the visitor
   * @return the result produced by the visitor
   * @throws NullArgumentException if {@code visitor} is null
   */
  @Override
  public <R, C> R accept(IExpressionVisitor<R, C> visitor, C context) {
    if (visitor == null) throw new NullArgumentException("visitor");
    return visitor.visit(this, context);
  }

  /**
   * Represents an error handler associated with an attempt expression.
   *
   * <p>An error handler associates a recoverable error type with a statement
   * that is executed when the attempted block produces that error.</p>
   *
   * <p>The action performed by the handler is independent of the fallback
   * value produced by the enclosing attempt expression.</p>
   */
  public static final class ErrorHandler {

    private final ErrorType errorType;
    private final IStatement action;

    /**
     * Creates an error handler.
     *
     * @param errorType the type of recoverable error handled by this handler
     * @param action the statement executed when the error is handled
     * @throws NullArgumentException if {@code errorType} or {@code action}
     * is null
     */
    public ErrorHandler(ErrorType errorType, IStatement action) {
      if (errorType == null) throw new NullArgumentException("errorType");
      if (action == null) throw new NullArgumentException("action");
      this.errorType = errorType;
      this.action = action;
    }

    /**
     * Returns the type of recoverable error handled by this handler.
     *
     * @return the handled error type
     */
    public ErrorType errorType() {
      return errorType;
    }

    /**
     * Returns the statement executed when the handled error occurs.
     *
     * @return the handler action
     */
    public IStatement action() {
      return action;
    }
  }
}