package dev.morphe.ast.statement;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.operator.AssignmentOperator;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IStatementVisitor;

/**
 * Represents an assignment statement in the Morphe abstract syntax tree.
 *
 * <p>An assignment consists of a target expression, an assignment operator,
 * and a value expression. The target identifies the expression whose value
 * is being replaced or modified.</p>
 *
 * <p>Assignment targets are represented by expressions so that different
 * kinds of assignable locations can share the same assignment structure.
 * For example:</p>
 *
 * <pre>
 * value = 10;
 * value += 5;
 *
 * values[index] = 10;
 * values[index] += 5;
 *
 * person.name = "Diego";
 * person.age += 1;
 *
 * person.address.city = "Nogales";
 * </pre>
 *
 * <p>{@link AssignmentOperator} distinguishes simple assignment from
 * compound assignment operations.</p>
 *
 * <p>Whether the target is assignable, whether it is mutable, and whether
 * the assigned value is compatible with the target are determined during
 * semantic analysis.</p>
 */
public final class AssignmentStatement implements IStatement {

  private final SourceRange range;
  private final IExpression target;
  private final AssignmentOperator operator;
  private final IExpression value;

  /**
   * Creates an assignment statement.
   *
   * @param range the source range occupied by the statement
   * @param target the expression identifying the assignment target
   * @param operator the assignment operator
   * @param value the expression providing the assigned value
   * @throws NullArgumentException if any argument is null
   */
  public AssignmentStatement(SourceRange range, IExpression target, AssignmentOperator operator, IExpression value) {
    if (range == null) throw new NullArgumentException("range");
    if (target == null) throw new NullArgumentException("target");
    if (operator == null) throw new NullArgumentException("operator");
    if (value == null) throw new NullArgumentException("value");
    this.range = range;
    this.target = target;
    this.operator = operator;
    this.value = value;
  }

  /**
   * Returns the source range occupied by this statement.
   *
   * @return the statement source range
   */
  @Override
  public SourceRange range() {
    return range;
  }

  /**
   * Returns the expression identifying the assignment target.
   *
   * <p>The target may represent different assignable locations, such as
   * a variable, an array element, or a composite field.</p>
   *
   * @return the assignment target expression
   */
  public IExpression target() {
    return target;
  }

  /**
   * Returns the operator used by the assignment.
   *
   * @return the assignment operator
   */
  public AssignmentOperator operator() {
    return operator;
  }

  /**
   * Returns the expression providing the assigned value.
   *
   * @return the assigned value expression
   */
  public IExpression value() {
    return value;
  }

  /**
   * Accepts a statement visitor and dispatches this statement to the
   * appropriate visitor operation.
   *
   * @param visitor the visitor processing this statement
   * @param <S> the result type produced by the visitor
   * @param <C> the context type passed to the visitor
   * @return the result produced by the visitor
   * @throws NullArgumentException if {@code visitor} is null
   */
  @Override
  public <S, C> S accept(IStatementVisitor<S, C> visitor, C context) {
    if (visitor == null) throw new NullArgumentException("visitor");
    return visitor.visit(this, context);
  }
}