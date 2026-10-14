package dev.morphe.ast.type;

import dev.morphe.ast.exception.argument.NullArgumentException;

import java.util.Objects;

/**
 * Represents an array type in the Morphe abstract syntax tree.
 *
 * <p>An array type is defined by the type of the values it contains.
 * The element type may itself be another {@link ArrayType}, allowing
 * nested array types to represent multidimensional arrays.</p>
 *
 * <p>For example, an {@code Integer[][]} type can be represented as:</p>
 * <pre>
 *  new ArrayType(
 *     new ArrayType(
 *         PrimitiveType.INTEGER
 *     )
 * )
 * </pre>
 *
 * <p>This class represents type information at the AST level and does
 * not depend on the runtime or bytecode type systems.</p>
 */
public final class ArrayType implements IASTType {

  private final IASTType elementType;
  private final boolean immutable;

  /**
   * Creates a mutable array type containing values of the specified type.
   *
   * @param elementType the type of the elements stored in the array
   * @throws NullArgumentException if {@code elementType} is {@code null}
   */
  public ArrayType(IASTType elementType) {
    this(elementType, false);
  }

  /**
   * Creates an array type containing values of the specified type with the given immutability flag.
   *
   * @param elementType the type of the elements stored in the array
   * @param immutable true if this array type is immutable, false otherwise
   * @throws NullArgumentException if {@code elementType} is {@code null}
   */
  public ArrayType(IASTType elementType, boolean immutable) {
    if (elementType == null) throw new NullArgumentException("elementType");
    this.elementType = elementType;
    this.immutable = immutable;
  }

  /**
   * Returns the type of the values contained in the array.
   *
   * <p>If the returned type is another {@link ArrayType}, the represented
   * type is multidimensional.</p>
   *
   * @return the array element type
   */
  public IASTType elementType() {
    return elementType;
  }

  /**
   * Returns the general kind of this AST type.
   *
   * @return {@link ASTTypeKind#ARRAY}
   */
  @Override
  public ASTTypeKind kind() {
    return ASTTypeKind.ARRAY;
  }

  /**
   * Indicates whether this array type is marked as immutable (constant).
   *
   * @return true if the array type is immutable, false otherwise
   */
  @Override
  public boolean isImmutable() {
    return immutable;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof ArrayType other)) return false;
    return immutable == other.immutable && elementType.equals(other.elementType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(elementType, immutable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (immutable) sb.append("#");
    sb.append(elementType).append("[]");
    return sb.toString();
  }
}