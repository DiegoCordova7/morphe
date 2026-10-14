package dev.morphe.lexer.source;

import dev.morphe.lexer.exception.argument.NullArgumentException;
import dev.morphe.lexer.exception.source.InvalidSourceSpanException;

/**
 * Represents a range within a Morphe source file.
 *
 * <p>A source span is defined by a starting position and an ending
 * position. Both positions belong to the same source and the ending
 * position must not precede the starting position.</p>
 *
 * @param start the starting position of the span
 * @param end the ending position of the span
 */
public record SourceSpan(SourcePosition start, SourcePosition end) {

  /**
   * Creates a source span.
   *
   * @param start the starting position of the span
   * @param end the ending position of the span
   * @throws NullArgumentException if {@code start} or {@code end} is null
   * @throws InvalidSourceSpanException if {@code end} precedes {@code start}
   *     by source offset
   */
  public SourceSpan {
    if (start == null) throw new NullArgumentException("start");
    if (end == null) throw new NullArgumentException("end");
    if (end.offset() < start.offset()) throw new InvalidSourceSpanException(start, end);
  }

  /**
   * Returns a human-readable representation of this source span.
   *
   * @return a formatted representation of the span
   */
  @Override
  public String toString() {
    if (start.equals(end)) return start.toString();
    if (start.line() == end.line()) {
      if (end.column() == start.column() + 1) return start.toString();
      return String.format("line %d, columns %d-%d", start.line(), start.column(), end.column() - 1);
    }
    return String.format("%s to %s", start, end);
  }
}