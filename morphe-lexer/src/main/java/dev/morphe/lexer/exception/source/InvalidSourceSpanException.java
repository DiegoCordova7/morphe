package dev.morphe.lexer.exception.source;

import dev.morphe.lexer.source.SourcePosition;

/**
 * Indicates that a source span does not represent a valid source range.
 *
 * <p>A source span must have an end position that does not precede
 * its start position.</p>
 */
public final class InvalidSourceSpanException extends SourceException {

  /**
   * Creates an exception for an invalid source span.
   *
   * @param start the start position of the span
   * @param end the end position of the span
   */
  public InvalidSourceSpanException(SourcePosition start, SourcePosition end) {
    super("Invalid source span: end position %s precedes start position %s", end, start);
  }
}