package dev.morphe.ast.type;

import dev.morphe.ast.exception.argument.NullArgumentException;

import java.util.List;
import java.util.Objects;

/**
 * Represents a composite type in the Morphe abstract syntax tree.
 *
 * <p>A composite type describes a named collection of typed fields. Composite
 * types are part of the AST type system and are independent from the bytecode
 * and runtime type systems.</p>
 *
 * <p>For example, a composite type representing a person may contain fields
 * such as {@code name} and {@code age}, each associated with an AST type.</p>
 */
public final class CompositeType implements IASTType {

  private final String name;
  private final List<CompositeField> fields;
  private final boolean immutable;

  /**
   * Creates a mutable composite type.
   *
   * @param name the name of the composite type
   * @param fields the fields declared by the composite type
   * @throws NullArgumentException if {@code name} or {@code fields} is null,
   * or if any field is null
   */
  public CompositeType(String name, List<CompositeField> fields) {
    this(name, fields, false);
  }

  /**
   * Creates a composite type with the specified immutability flag.
   *
   * @param name the name of the composite type
   * @param fields the fields declared by the composite type
   * @param immutable true if this composite type is immutable, false otherwise
   * @throws NullArgumentException if {@code name} or {@code fields} is null,
   * or if any field is null
   */
  public CompositeType(String name, List<CompositeField> fields, boolean immutable) {
    if (name == null) throw new NullArgumentException("name");
    if (fields == null) throw new NullArgumentException("fields");
    if (fields.stream().anyMatch(Objects::isNull)) throw new NullArgumentException("field");
    this.name = name;
    this.fields = List.copyOf(fields);
    this.immutable = immutable;
  }

  /**
   * Returns the name of this composite type.
   *
   * @return the composite type name
   */
  public String name() {
    return name;
  }

  /**
   * Returns the fields declared by this composite type.
   *
   * @return an immutable list of composite fields
   */
  public List<CompositeField> fields() {
    return fields;
  }

  /**
   * Returns the general kind of this AST type.
   *
   * @return {@link ASTTypeKind#COMPOSITE}
   */
  @Override
  public ASTTypeKind kind() {
    return ASTTypeKind.COMPOSITE;
  }

  /**
   * Indicates whether this composite type is marked as immutable (constant).
   *
   * @return true if the composite type is immutable, false otherwise
   */
  @Override
  public boolean isImmutable() {
    return immutable;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CompositeType that)) return false;
    return immutable == that.immutable && name.equals(that.name) && fields.equals(that.fields);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, fields, immutable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (immutable) sb.append("#");
    sb.append(name).append(" { ").append(String.join("; ", fields.stream().map(Object::toString).toList())).append(" }");
    return sb.toString();
  }

  /**
   * Represents a typed field belonging to a composite type.
   *
   * <p>A composite field associates a name with an AST type. Field-specific
   * semantic rules, such as duplicate names or valid identifiers, are
   * validated by later compiler phases.</p>
   *
   * @param name the field name
   * @param type the field AST type
   */
  public record CompositeField(String name, IASTType type) {

    /**
     * Creates a composite field.
     *
     * @param name the field name
     * @param type the field AST type
     * @throws NullArgumentException if {@code name} or {@code type} is null
     */
    public CompositeField {
      if (name == null) throw new NullArgumentException("name");
      if (type == null) throw new NullArgumentException("type");
    }

    @Override
    public String toString() {
      return name + " : " + type;
    }
  }
}