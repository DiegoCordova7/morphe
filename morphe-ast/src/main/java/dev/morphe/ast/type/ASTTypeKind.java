package dev.morphe.ast.type;

/**
 * Represents the general categories of types available in the
 * Morphe abstract syntax tree.
 */
public enum ASTTypeKind {
  PRIMITIVE,
  ARRAY,
  FUNCTION,
  NAMED,
  COMPOSITE,
  ERROR,
  ANY,
  NULL
}