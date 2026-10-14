package dev.morphe.lexer.exception.source;

import dev.morphe.lexer.source.SourcePosition;

/**
 * Thrown when the lexer reaches the end of the source before
 * a string literal is properly terminated.
 */
public final class UnterminatedStringException extends SourceException {

  /**
   * Creates an exception for an unterminated string literal.
   *
   * @param position the source position where the unterminated string
   *     was detected
   */
  public UnterminatedStringException(SourcePosition position) {
    super(String.format("Unterminated string at %s", position));
  }
}