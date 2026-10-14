package dev.morphe.ast.type;

import dev.morphe.ast.exception.argument.NullArgumentException;

import java.util.List;
import java.util.Objects;

/**
 * Represents a function type in the Morphe abstract syntax tree.
 *
 * <p>A function type describes the types of the parameters accepted by
 * a function and the type of value produced by it.</p>
 *
 * <p>Function types are independent from the bytecode and runtime type
 * systems.</p>
 *
 * <p>For example, a function accepting two integers and returning an
 * integer is represented by the type
 * {@code (Integer, Integer) -> Integer}.</p>
 */
public final class FunctionType implements IASTType {

  private final List<IASTType> parameterTypes;
  private final IASTType returnType;
  private final boolean immutable;

  /**
   * Creates a mutable function type.
   *
   * @param parameterTypes the types of the parameters accepted by the function
   * @param returnType the type of value produced by the function
   * @throws NullArgumentException if {@code parameterTypes} or
   * {@code returnType} is null, or if any parameter type is null
   */
  public FunctionType(List<IASTType> parameterTypes, IASTType returnType) {
    this(parameterTypes, returnType, false);
  }

  /**
   * Creates a function type with the specified immutability flag.
   *
   * @param parameterTypes the types of the parameters accepted by the function
   * @param returnType the type of value produced by the function
   * @param immutable true if this function type is immutable, false otherwise
   * @throws NullArgumentException if {@code parameterTypes} or
   * {@code returnType} is null, or if any parameter type is null
   */
  public FunctionType(List<IASTType> parameterTypes, IASTType returnType, boolean immutable) {
    if (parameterTypes == null) throw new NullArgumentException("parameterTypes");
    if (parameterTypes.stream().anyMatch(Objects::isNull)) throw new NullArgumentException("parameterType");
    if (returnType == null) throw new NullArgumentException("returnType");
    this.parameterTypes = List.copyOf(parameterTypes);
    this.returnType = returnType;
    this.immutable = immutable;
  }

  /**
   * Returns the general kind of this AST type.
   *
   * @return {@link ASTTypeKind#FUNCTION}
   */
  @Override
  public ASTTypeKind kind() {
    return ASTTypeKind.FUNCTION;
  }

  /**
   * Indicates whether this function type is marked as immutable (constant).
   *
   * @return true if the function type is immutable, false otherwise
   */
  @Override
  public boolean isImmutable() {
    return immutable;
  }

  /**
   * Returns the types of the parameters accepted by the function.
   *
   * @return an immutable list of parameter types
   */
  public List<IASTType> parameterTypes() {
    return parameterTypes;
  }

  /**
   * Returns the type of value produced by the function.
   *
   * @return the function return type
   */
  public IASTType returnType() {
    return returnType;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof FunctionType other)) return false;
    return immutable == other.immutable && parameterTypes.equals(other.parameterTypes) && returnType.equals(other.returnType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(parameterTypes, returnType, immutable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (immutable) sb.append("#");
    sb.append("(");
    for (int i = 0; i < parameterTypes.size(); i++) {
      sb.append(parameterTypes.get(i));
      if (i < parameterTypes.size() - 1) sb.append(", ");
    }
    sb.append(") -> ").append(returnType);
    return sb.toString();
  }
}