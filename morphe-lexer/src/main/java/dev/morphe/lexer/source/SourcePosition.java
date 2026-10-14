package dev.morphe.lexer.source;

import dev.morphe.lexer.exception.source.InvalidSourcePositionException;

/**
 * Represents a position within a Morphe source file.
 *
 * <p>The position consists of a zero-based character offset and
 * one-based line and column numbers.</p>
 *
 * @param offset the zero-based character offset within the source
 * @param line the one-based line number within the source
 * @param column the one-based column number within the line
 */
public record SourcePosition(int offset, int line, int column) {

  /**
   * Creates a source position.
   *
   * @param offset the zero-based character offset within the source
   * @param line the one-based line number within the source
   * @param column the one-based column number within the line
   * @throws InvalidSourcePositionException if {@code offset} is negative,
   *     or if {@code line} or {@code column} is less than one
   */
  public SourcePosition {
    if (offset < 0) throw new InvalidSourcePositionException("offset", offset);
    if (line < 1) throw new InvalidSourcePositionException("line", line);
    if (column < 1) throw new InvalidSourcePositionException("column", column);
  }

  /**
   * Returns a human-readable representation of this source position.
   *
   * @return the line and column represented by this position
   */
  @Override
  public String toString() {
    return String.format("line %d, column %d", line, column);
  }
}