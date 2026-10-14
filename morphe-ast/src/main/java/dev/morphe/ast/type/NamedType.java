package dev.morphe.ast.type;

import dev.morphe.ast.exception.argument.NullArgumentException;

import java.util.Objects;

/**
 * Represents a reference to a named type in the Morphe abstract syntax tree.
 *
 * <p>A named type refers to a type declared elsewhere in the program,
 * such as a composite type. The referenced declaration is resolved during
 * semantic analysis.</p>
 *
 * <p>For example, a reference to {@code Person} is represented as:</p>
 * <pre>
 *  new NamedType("Person")
 * </pre>
 *
 * <p>Named types are independent from the bytecode and runtime type
 * systems.</p>
 */
public final class NamedType implements IASTType {

  private final String name;
  private final boolean immutable;

  /**
   * Creates a mutable named type reference.
   *
   * @param name the name of the referenced type
   * @throws NullArgumentException if {@code name} is {@code null}
   */
  public NamedType(String name) {
    this(name, false);
  }

  /**
   * Creates a named type reference with the specified immutability flag.
   *
   * @param name the name of the referenced type
   * @param immutable true if this named type is immutable, false otherwise
   * @throws NullArgumentException if {@code name} is {@code null}
   */
  public NamedType(String name, boolean immutable) {
    if (name == null) throw new NullArgumentException("name");
    this.name = name;
    this.immutable = immutable;
  }

  /**
   * Returns the name of the referenced type.
   *
   * @return the referenced type name
   */
  public String name() {
    return name;
  }

  /**
   * Returns the general kind of this AST type.
   *
   * @return {@link ASTTypeKind#NAMED}
   */
  @Override
  public ASTTypeKind kind() {
    return ASTTypeKind.NAMED;
  }

  /**
   * Indicates whether this named type is marked as immutable (constant).
   *
   * @return true if the named type is immutable, false otherwise
   */
  @Override
  public boolean isImmutable() {
    return immutable;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof NamedType other)) return false;
    return immutable == other.immutable && name.equals(other.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, immutable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (immutable) sb.append("#");
    sb.append(name);
    return sb.toString();
  }
}