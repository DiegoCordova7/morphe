package dev.morphe.ast.type;

import dev.morphe.ast.exception.argument.NullArgumentException;

import java.util.Objects;

/**
 * Represents an error type in the Morphe type system.
 *
 * <p>Error types identify recoverable execution errors that may be
 * handled by constructs such as {@code attempt}.</p>
 *
 * <p>Each error type is identified by an {@link ErrorKind}, which provides
 * the language-level representation of a corresponding runtime execution
 * error.</p>
 *
 * <p>Error types are independent of the concrete exception classes used
 * by the runtime or virtual machine.</p>
 */
public final class ErrorType implements IASTType {

  private final ErrorKind errorKind;
  private final boolean immutable;

  /**
   * Creates a mutable error type.
   *
   * @param errorKind the kind identifying this error type
   * @throws NullArgumentException if {@code errorKind} is null
   */
  public ErrorType(ErrorKind errorKind) {
    this(errorKind, false);
  }

  /**
   * Creates an error type with the specified immutability flag.
   *
   * @param errorKind the kind identifying this error type
   * @param immutable true if this error type is immutable, false otherwise
   * @throws NullArgumentException if {@code errorKind} is null
   */
  public ErrorType(ErrorKind errorKind, boolean immutable) {
    if (errorKind == null) throw new NullArgumentException("errorKind");
    this.errorKind = errorKind;
    this.immutable = immutable;
  }

  /**
   * Returns the kind identifying this error type.
   *
   * @return the error kind
   */
  public ErrorKind errorKind() {
    return errorKind;
  }

  /**
   * Returns the kind of this AST type.
   *
   * @return {@link ASTTypeKind#ERROR}
   */
  @Override
  public ASTTypeKind kind() {
    return ASTTypeKind.ERROR;
  }

  /**
   * Indicates whether this error type is marked as immutable (constant).
   *
   * @return true if the error type is immutable, false otherwise
   */
  @Override
  public boolean isImmutable() {
    return immutable;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ErrorType that)) return false;
    return immutable == that.immutable && errorKind == that.errorKind;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (immutable) sb.append("#");
    sb.append(errorKind.name());
    return sb.toString();
  }
}