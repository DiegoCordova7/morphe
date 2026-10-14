package dev.morphe.semantic.exception.pattern;

/**
 * Thrown when a composite pattern references a field that does not exist
 * in the matched composite type.
 */
public final class UnknownCompositeFieldException extends PatternException {

  /**
   * Creates an exception for an unknown composite field.
   *
   * @param compositeName the name of the composite type
   * @param fieldName the name of the unknown field
   */
  public UnknownCompositeFieldException(String compositeName, String fieldName) {
    super("Unknown field '" + fieldName + "' in composite type '" + compositeName + "'");
  }
}