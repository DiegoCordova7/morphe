package dev.morphe.ast.expression;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IExpressionVisitor;

import java.util.List;
import java.util.Objects;

/**
 * Represents an expression that creates an instance of a composite type
 * in the Morphe abstract syntax tree.
 *
 * <p>A composite instance expression identifies the composite type by name
 * and provides expressions for its field initializers.</p>
 *
 * <p>For example:</p>
 *
 * <pre>
 * Person {
 *     name = "Diego";
 *     age = 25;
 * }
 * </pre>
 *
 * <p>The type name is represented as source-level information. Resolution
 * of the referenced composite type and validation of its fields are
 * performed during semantic analysis.</p>
 */
public final class CompositeInstanceExpression implements IExpression {

  private final SourceRange range;
  private final String typeName;
  private final List<FieldInitializer> fields;

  /**
   * Creates a composite instance expression.
   *
   * @param range the source range occupied by the expression
   * @param typeName the name of the composite type being instantiated
   * @param fields the field initializers provided for the instance
   * @throws NullArgumentException if {@code range}, {@code typeName}, or
   * {@code fields} is null, or if any field initializer is null
   */
  public CompositeInstanceExpression(SourceRange range, String typeName, List<FieldInitializer> fields) {
    if (range == null) throw new NullArgumentException("range");
    if (typeName == null) throw new NullArgumentException("typeName");
    if (fields == null) throw new NullArgumentException("fields");
    if (fields.stream().anyMatch(Objects::isNull)) throw new NullArgumentException("field");
    this.range = range;
    this.typeName = typeName;
    this.fields = List.copyOf(fields);
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
   * Returns the name of the composite type being instantiated.
   *
   * @return the composite type name
   */
  public String typeName() {
    return typeName;
  }

  /**
   * Returns the field initializers provided for the instance.
   *
   * @return an immutable list of field initializers
   */
  public List<FieldInitializer> fields() {
    return fields;
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
   * Represents a value assigned to a field during composite instance
   * creation.
   *
   * @param name the name of the field being initialized
   * @param value the expression producing the field value
   */
  public record FieldInitializer(String name, IExpression value) {

    /**
     * Creates a field initializer.
     *
     * @param name the field name
     * @param value the expression producing the field value
     * @throws NullArgumentException if {@code name} or {@code value} is null
     */
    public FieldInitializer {
      if (name == null) throw new NullArgumentException("name");
      if (value == null) throw new NullArgumentException("value");
    }
  }
}