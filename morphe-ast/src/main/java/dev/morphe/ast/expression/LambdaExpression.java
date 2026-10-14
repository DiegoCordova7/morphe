package dev.morphe.ast.expression;

import dev.morphe.ast.declaration.Parameter;
import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IExpressionVisitor;

import java.util.List;
import java.util.Objects;

/**
 * Represents a lambda expression in the Morphe abstract syntax tree.
 *
 * <p>A lambda expression defines an anonymous function with explicitly
 * typed parameters and an expression body.</p>
 *
 * <p>A lambda evaluates to a function value. Its function type is
 * determined during semantic analysis from its parameter types and
 * the type produced by its body.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * (Integer a, Integer b) -> a + b
 * }</pre>
 *
 * <p>represents a function accepting two integers and producing an
 * integer value.</p>
 */
public final class LambdaExpression implements IExpression {

  private final SourceRange range;
  private final List<Parameter> parameters;
  private final IExpression body;

  /**
   * Creates a lambda expression.
   *
   * @param range the source range occupied by the expression
   * @param parameters the parameters declared by the lambda
   * @param body the expression evaluated by the lambda
   * @throws NullArgumentException if {@code range}, {@code parameters},
   * or {@code body} is null, or if any parameter is null
   */
  public LambdaExpression(SourceRange range, List<Parameter> parameters, IExpression body) {
    if (range == null) throw new NullArgumentException("range");
    if (parameters == null) throw new NullArgumentException("parameters");
    if (parameters.stream().anyMatch(Objects::isNull)) throw new NullArgumentException("parameter");
    if (body == null) throw new NullArgumentException("body");
    this.range = range;
    this.parameters = List.copyOf(parameters);
    this.body = body;
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
   * Returns the parameters declared by the lambda.
   *
   * @return an immutable list of lambda parameters
   */
  public List<Parameter> parameters() {
    return parameters;
  }

  /**
   * Returns the expression evaluated when the lambda is invoked.
   *
   * @return the lambda body
   */
  public IExpression body() {
    return body;
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