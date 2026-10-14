package dev.morphe.ast.type;

/**
 * Represents the universal type in the Morphe type system.
 *
 * <p>The any type represents a value whose concrete type is not restricted.
 * It can be used by operations that accept values of different types.</p>
 *
 * <p>{@link #ANY} is the only value of the any type.</p>
 */
public enum AnyType implements IASTType {

  /**
   * Represents the universal type.
   */
  ANY;

  /**
   * Returns the category of this AST type.
   *
   * @return {@link ASTTypeKind#ANY}
   */
  @Override
  public ASTTypeKind kind() {
    return ASTTypeKind.ANY;
  }

  /**
   * Indicates whether this type is marked as immutable (constant).
   *
   * @return false, as the universal any type is mutable by default
   */
  @Override
  public boolean isImmutable() {
    return false;
  }
}