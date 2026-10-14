package dev.morphe.ast.source;

import dev.morphe.ast.exception.argument.InvalidArgumentException;

/**
 * Represents a position within Morphe source code.
 *
 * <p>
 * Source positions are immutable and identify a single location using
 * a zero-based character offset and one-based line and column numbers.
 * </p>
 *
 * @param offset zero-based character offset within the source
 * @param line one-based line number
 * @param column one-based column number
 */
public record SourcePosition(int offset, int line, int column) {

  /**
   * Creates a source position.
   *
   * @throws InvalidArgumentException if {@code offset} is negative,
   * {@code line} is less than one, or {@code column} is less than one
   */
  public SourcePosition {
    if (offset < 0) throw new InvalidArgumentException("offset", "must be non-negative");
    if (line < 1) throw new InvalidArgumentException("line", "must be positive");
    if (column < 1) throw new InvalidArgumentException("column", "must be positive");
  }
}