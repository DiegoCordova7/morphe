package dev.morphe.ast.type;

/**
 * Represents a primitive type in the Morphe type system, such as integer,
 * double, boolean, string, or void.
 */
public enum PrimitiveType implements IASTType {
  INTEGER,
  DOUBLE,
  BOOLEAN,
  STRING,
  VOID;

  @Override
  public ASTTypeKind kind() {
    return ASTTypeKind.PRIMITIVE;
  }

  /**
   * Indicates whether this primitive type is marked as immutable (constant).
   *
   * @return false by default, as primitive enum constants represent the base types
   */
  @Override
  public boolean isImmutable() {
    return false;
  }

  @Override
  public String toString() {
    String name = name();
    return name.charAt(0) + name.substring(1).toLowerCase();
  }
}