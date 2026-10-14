package dev.morphe.ast.type;

/**
 * Represents a type in the Morphe abstract syntax tree.
 *
 * <p>AST types describe the type information expressed by the source
 * program. They are independent from the bytecode and runtime type
 * systems.</p>
 */
public interface IASTType {

  /**
   * Returns the general kind of this AST type.
   *
   * @return the type kind
   */
  ASTTypeKind kind();

  /**
   * Indicates whether this type is marked as immutable (constant).
   *
   * @return true if the type is immutable, false otherwise
   */
  boolean isImmutable();

  /**
   * Returns a human-readable string representation of this type,
   * used primarily for error messages and diagnostics.
   *
   * @return the string representation of the type
   */
  @Override
  String toString();
}