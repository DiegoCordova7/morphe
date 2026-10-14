package dev.morphe.lexer.exception.source;

/**
 * Indicates that a source position contains invalid coordinates.
 */
public final class InvalidSourcePositionException extends SourceException {

  /**
   * Creates an exception for an invalid source coordinate.
   *
   * @param coordinate the name of the invalid coordinate
   * @param value the invalid coordinate value
   */
  public InvalidSourcePositionException(String coordinate, int value) {
    super("Invalid source position: %s cannot be %s", coordinate, value);
  }
}