package dev.morphe.lexer.exception.source;

import dev.morphe.lexer.source.SourcePosition;

/**
 * Thrown when the lexer reaches the end of the source before
 * a block comment is properly terminated.
 */
public final class UnterminatedCommentException extends SourceException {

  /**
   * Creates an exception for an unterminated block comment.
   *
   * @param position the source position where the unterminated comment
   *     was detected
   */
  public UnterminatedCommentException(SourcePosition position) {
    super(String.format("Unterminated comment at %s", position));
  }
}