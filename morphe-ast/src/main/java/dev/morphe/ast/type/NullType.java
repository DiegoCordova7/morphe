package dev.morphe.ast.type;

/**
 * Represents the null type in the Morphe type system.
 *
 * <p>The null type represents the absence of a value. Unlike primitive
 * types, it does not represent a concrete value category such as an integer,
 * string, or boolean.</p>
 *
 * <p>{@link #NULL} is the only value of the null type and is used by the
 * semantic type system when analyzing the {@code null} literal.</p>
 */
public enum NullType implements IASTType {

  /**
   * Represents the null type.
   */
  NULL;

  /**
   * Returns the category of this AST type.
   *
   * @return {@link ASTTypeKind#NULL}
   */
  @Override
  public ASTTypeKind kind() {
    return ASTTypeKind.NULL;
  }

  /**
   * Indicates whether this type is marked as immutable (constant).
   *
   * @return false, as the null type is mutable by default
   */
  @Override
  public boolean isImmutable() {
    return false;
  }
}