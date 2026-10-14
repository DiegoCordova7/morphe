package dev.morphe.ast.pattern;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IPatternVisitor;

import java.util.List;
import java.util.Objects;

/**
 * Represents a composite pattern in the Morphe abstract syntax tree.
 *
 * <p>A composite pattern matches a composite value by its type name and
 * applies individual patterns to its named fields.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * Person {
 *     name: "Diego",
 *     age: age
 * }
 * }</pre>
 *
 * <p>Field patterns may themselves contain other patterns, allowing
 * composite patterns to be nested recursively.</p>
 *
 * <p>The resolution of the type name to an actual {@code CompositeType}
 * and the validity of the referenced fields are determined during
 * semantic analysis.</p>
 */
public final class CompositePattern implements IPattern {

  private final SourceRange range;
  private final String typeName;
  private final List<FieldPattern> fields;

  /**
   * Creates a composite pattern.
   *
   * @param range the source range occupied by the pattern
   * @param typeName the name of the composite type being matched
   * @param fields the field patterns applied to the composite value
   * @throws NullArgumentException if {@code range}, {@code typeName}, or
   *     {@code fields} is null, or if any field pattern is null
   */
  public CompositePattern(SourceRange range, String typeName, List<FieldPattern> fields) {
    if (range == null) throw new NullArgumentException("range");
    if (typeName == null) throw new NullArgumentException("typeName");
    if (fields == null) throw new NullArgumentException("fields");
    if (fields.stream().anyMatch(Objects::isNull)) throw new NullArgumentException("field");
    this.range = range;
    this.typeName = typeName;
    this.fields = List.copyOf(fields);
  }

  /**
   * Returns the source range occupied by this pattern.
   *
   * @return the pattern source range
   */
  @Override
  public SourceRange range() {
    return range;
  }

  /**
   * Returns the name of the composite type being matched.
   *
   * @return the composite type name
   */
  public String typeName() {
    return typeName;
  }

  /**
   * Returns the field patterns applied to the composite value.
   *
   * @return an immutable list of field patterns
   */
  public List<FieldPattern> fields() {
    return fields;
  }

  /**
   * Accepts a pattern visitor and dispatches this pattern to the
   * appropriate visitor operation.
   *
   * @param visitor the visitor processing this pattern
   * @param <C> the context type passed to the visitor
   * @return the result produced by the visitor
   * @throws NullArgumentException if {@code visitor} is null
   */
  @Override
  public <C> Void accept(IPatternVisitor<C> visitor, C context) {
    if (visitor == null) throw new NullArgumentException("visitor");
    return visitor.visit(this, context);
  }

  /**
   * Represents a field pattern within a composite pattern in the Morphe
   * abstract syntax tree.
   *
   * <p>A field pattern associates a composite field name with a pattern
   * that is applied to the value of that field.</p>
   *
   * <p>For example:</p>
   *
   * <pre>{@code
   * Person {
   *     name: "Diego",
   *     age: age
   * }
   * }</pre>
   *
   * <p>Each field pattern may contain any other pattern, allowing
   * composite patterns to be recursively composed.</p>
   */
  public record FieldPattern(String name, IPattern pattern) {

    /**
     * Creates a field pattern.
     *
     * @param name the name of the composite field being matched
     * @param pattern the pattern applied to the field value
     * @throws NullArgumentException if {@code name} or {@code pattern}
     *     is null
     */
    public FieldPattern {
      if (name == null) throw new NullArgumentException("name");
      if (pattern == null) throw new NullArgumentException("pattern");
    }
  }
}