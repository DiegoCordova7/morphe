package dev.morphe.semantic.type;

import dev.morphe.ast.type.IASTType;
import dev.morphe.ast.type.NullType;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.semantic.exception.type.UnsupportedLiteralTypeException;

/**
 * Resolves Java literal values into their corresponding Morphe AST types.
 *
 * <p>This resolver is used during semantic analysis to determine the AST
 * type represented by a literal value stored in an AST node.</p>
 *
 * <p>The supported literal mappings are:</p>
 *
 * <ul>
 *   <li>{@link Integer} to {@link PrimitiveType#INTEGER}</li>
 *   <li>{@link Double} to {@link PrimitiveType#DOUBLE}</li>
 *   <li>{@link String} to {@link PrimitiveType#STRING}</li>
 *   <li>{@link Boolean} to {@link PrimitiveType#BOOLEAN}</li>
 * </ul>
 *
 * <p>The Java runtime type of the literal is used only as an intermediate
 * representation. Semantic compatibility between the resulting AST types
 * is handled separately by {@link TypeChecker}.</p>
 */
public final class LiteralTypeResolver {

  /**
   * Resolves the AST type corresponding to a literal value.
   *
   * @param value the Java value representing the literal
   * @return the corresponding Morphe AST type
   * @throws UnsupportedLiteralTypeException if the value does not represent
   *     a supported Morphe literal
   */
  public IASTType resolve(Object value) {
    if (value == null) return NullType.NULL;
    if (value instanceof Integer) return PrimitiveType.INTEGER;
    if (value instanceof Double) return PrimitiveType.DOUBLE;
    if (value instanceof String) return PrimitiveType.STRING;
    if (value instanceof Boolean) return PrimitiveType.BOOLEAN;
    throw new UnsupportedLiteralTypeException(value);
  }
}