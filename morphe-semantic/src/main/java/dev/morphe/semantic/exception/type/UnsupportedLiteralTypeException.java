package dev.morphe.semantic.exception.type;

/**
 * Thrown when a literal value cannot be mapped to a supported Morphe AST type.
 */
public final class UnsupportedLiteralTypeException extends TypeResolutionException {

  public UnsupportedLiteralTypeException(Object value) {
    super("Unsupported literal type: " + value.getClass().getName());
  }
}
