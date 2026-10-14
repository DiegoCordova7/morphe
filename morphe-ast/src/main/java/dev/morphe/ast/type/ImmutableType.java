package dev.morphe.ast.type;

import dev.morphe.ast.exception.argument.NullArgumentException;
import java.util.Objects;

/**
 * Represents an immutable wrapper around any AST type in Morphe.
 *
 * <p>For example, {@code #Integer} is represented as an {@code ImmutableType}
 * wrapping {@link PrimitiveType#INTEGER}.</p>
 */
public final class ImmutableType implements IASTType {

  private final IASTType innerType;

  /**
   * Creates an immutable wrapper for the specified type.
   *
   * @param innerType the underlying type to make immutable
   * @throws NullArgumentException if {@code innerType} is null
   */
  public ImmutableType(IASTType innerType) {
    if (innerType == null) throw new NullArgumentException("innerType");
    this.innerType = innerType;
  }

  /**
   * Returns the underlying base type being wrapped.
   *
   * @return the inner AST type
   */
  public IASTType innerType() {
    return innerType;
  }

  @Override
  public ASTTypeKind kind() {
    return innerType.kind();
  }

  @Override
  public boolean isImmutable() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ImmutableType that)) return false;
    return innerType.equals(that.innerType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(innerType, true);
  }

  @Override
  public String toString() {
    return "#" + innerType.toString();
  }
}