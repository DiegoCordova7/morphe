package dev.morphe.ast.declaration;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.type.CompositeType;
import dev.morphe.ast.visitor.IDeclarationVisitor;

import java.util.List;
import java.util.Objects;

/**
 * Represents a composite type declaration in the Morphe abstract syntax tree.
 *
 * <p>A composite declaration introduces a named composite type and defines
 * the fields that make up its structure.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * composite Person {
 *   String name;
 *   Integer age;
 * }
 * }</pre>
 *
 * <p>The declaration describes how the composite type is introduced in the
 * program, while {@link CompositeType} represents the corresponding AST
 * type.</p>
 */
public final class CompositeDeclaration implements IDeclaration {

  private final SourceRange range;
  private final String name;
  private final List<CompositeType.CompositeField> fields;

  /**
   * Creates a composite declaration.
   *
   * @param range the source range occupied by the declaration
   * @param name the name introduced by the declaration
   * @param fields the fields declared by the composite
   * @throws NullArgumentException if {@code range}, {@code name}, or
   * {@code fields} is null, or if any field is null
   */
  public CompositeDeclaration(SourceRange range, String name, List<CompositeType.CompositeField> fields) {
    if (range == null) throw new NullArgumentException("range");
    if (name == null) throw new NullArgumentException("name");
    if (fields == null) throw new NullArgumentException("fields");
    if (fields.stream().anyMatch(Objects::isNull)) throw new NullArgumentException("field");
    this.range = range;
    this.name = name;
    this.fields = List.copyOf(fields);
  }

  /**
   * Returns the source range occupied by this declaration.
   *
   * @return the declaration source range
   */
  @Override
  public SourceRange range() {
    return range;
  }

  /**
   * Returns the name introduced by this declaration.
   *
   * @return the composite name
   */
  public String name() {
    return name;
  }

  /**
   * Returns the fields declared by this composite.
   *
   * @return an immutable list of composite fields
   */
  public List<CompositeType.CompositeField> fields() {
    return fields;
  }

  /**
   * Accepts a declaration visitor and dispatches this declaration to the
   * appropriate visitor operation.
   *
   * @param visitor the visitor processing this declaration
   * @param context the operational context passed through the traversal
   * @return always {@code null}
   * @throws NullArgumentException if {@code visitor} is null
   */
  @Override
  public <C> Void accept(IDeclarationVisitor<C> visitor, C context) {
    if (visitor == null) throw new NullArgumentException("visitor");
    return visitor.visit(this, context);
  }
}