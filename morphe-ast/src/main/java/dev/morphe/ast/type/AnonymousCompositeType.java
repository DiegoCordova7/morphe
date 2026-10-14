package dev.morphe.ast.type;

import dev.morphe.ast.exception.argument.NullArgumentException;

import java.util.List;
import java.util.Objects;

/**
 * Represents an anonymous composite type in the Morphe abstract syntax tree.
 *
 * <p>Anonymous composite types define an inline structural shape without a named declaration,
 * such as {@code { name : String, surface : Double }}.</p>
 */
public final class AnonymousCompositeType implements IASTType {

  private final List<CompositeType.CompositeField> fields;
  private final boolean immutable;

  /**
   * Creates a new mutable anonymous composite type with the specified fields.
   *
   * @param fields the list of fields comprising this anonymous composite type
   */
  public AnonymousCompositeType(List<CompositeType.CompositeField> fields) {
    this(fields, false);
  }

  /**
   * Creates a new anonymous composite type with the specified fields and immutability flag.
   *
   * @param fields the list of fields comprising this anonymous composite type
   * @param immutable true if this type is immutable, false otherwise
   */
  public AnonymousCompositeType(List<CompositeType.CompositeField> fields, boolean immutable) {
    if (fields == null) throw new NullArgumentException("fields");
    this.fields = List.copyOf(fields);
    this.immutable = immutable;
  }

  /**
   * Returns the fields of this anonymous composite type.
   *
   * @return the list of composite fields
   */
  public List<CompositeType.CompositeField> fields() {
    return fields;
  }

  @Override
  public ASTTypeKind kind() {
    return ASTTypeKind.COMPOSITE;
  }

  @Override
  public boolean isImmutable() {
    return immutable;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AnonymousCompositeType that)) return false;
    return immutable == that.immutable && fields.equals(that.fields);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fields, immutable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (immutable) sb.append("#");
    sb.append("{ ");
    for (int i = 0; i < fields.size(); i++) {
      CompositeType.CompositeField field = fields.get(i);
      sb.append(field.type()).append(" ").append(field.name());
      if (i < fields.size() - 1) sb.append("; ");
    }
    sb.append(" }");
    return sb.toString();
  }
}