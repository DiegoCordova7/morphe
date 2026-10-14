package dev.morphe.ast.expression;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IExpressionVisitor;

import java.util.List;
import java.util.Objects;

/**
 * Represents a function call expression in the Morphe abstract syntax tree.
 *
 * <p>A function call evaluates a function expression and supplies it with
 * zero or more argument expressions. The function expression is not limited
 * to a named function and may evaluate to any value of a function type.</p>
 *
 * <p>This allows both direct calls to named functions and calls through
 * function values, including lambdas.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * add(10, 20)
 * }</pre>
 *
 * <p>and:</p>
 *
 * <pre>{@code
 * apply(10, 20, (Integer a, Integer b) => a + b)
 * }</pre>
 */
public final class FunctionCallExpression implements IExpression {

  private final SourceRange range;
  private final IExpression function;
  private final List<IExpression> arguments;

  /**
   * Creates a function call expression.
   *
   * @param range the source range occupied by the expression
   * @param function the expression evaluating to the function being called
   * @param arguments the expressions providing the function arguments
   * @throws NullArgumentException if {@code range}, {@code function},
   * or {@code arguments} is null, or if any argument is null
   */
  public FunctionCallExpression(SourceRange range, IExpression function, List<IExpression> arguments) {
    if (range == null) throw new NullArgumentException("range");
    if (function == null) throw new NullArgumentException("function");
    if (arguments == null) throw new NullArgumentException("arguments");
    if (arguments.stream().anyMatch(Objects::isNull)) throw new NullArgumentException("argument");
    this.range = range;
    this.function = function;
    this.arguments = List.copyOf(arguments);
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
   * Returns the expression evaluating to the function being called.
   *
   * @return the function expression
   */
  public IExpression function() {
    return function;
  }

  /**
   * Returns the expressions providing the function arguments.
   *
   * @return an immutable list of argument expressions
   */
  public List<IExpression> arguments() {
    return arguments;
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
}