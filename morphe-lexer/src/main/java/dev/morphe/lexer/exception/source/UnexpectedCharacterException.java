package dev.morphe.lexer.exception.source;

import dev.morphe.lexer.source.SourcePosition;

/**
 * Thrown when the lexer encounters a character that is not valid
 * in the current lexical context.
 */
public final class UnexpectedCharacterException extends SourceException {

  /**
   * Creates an exception for an unexpected source character.
   *
   * @param character the unexpected character encountered by the lexer
   * @param position the source position where the character was encountered
   */
  public UnexpectedCharacterException(char character, SourcePosition position) {
    super(String.format("Unexpected character '%s' at %s", character, position));
  }
}