package dev.morphe.ast.expression;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IExpressionVisitor;

/**
 * Represents an expression that references a variable by name.
 *
 * <p>A variable expression only represents the syntactic use of a variable.
 * It does not resolve the variable, determine its type, or associate it with
 * a runtime storage location. Those responsibilities belong to later stages
 * of the Morphe pipeline.</p>
 */
public final class VariableExpression implements IExpression {

  private final SourceRange range;
  private final String name;

  /**
   * Creates a variable expression.
   *
   * @param range the source range occupied by the variable reference
   * @param name the name of the referenced variable
   * @throws NullArgumentException if {@code range} or {@code name} is null
   */
  public VariableExpression(SourceRange range, String name) {
    if (range == null) throw new NullArgumentException("range");
    if (name == null) throw new NullArgumentException("name");
    this.range = range;
    this.name = name;
  }

  /**
   * Returns the source range occupied by this expression.
   *
   * @return the source range of this expression
   */
  @Override
  public SourceRange range() {
    return range;
  }

  /**
   * Returns the name of the referenced variable.
   *
   * @return the variable name
   */
  public String name() {
    return name;
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