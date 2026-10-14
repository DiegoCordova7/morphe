package dev.morphe.ast.source;

import dev.morphe.ast.exception.argument.InvalidArgumentException;
import dev.morphe.ast.exception.argument.NullArgumentException;

/**
 * Represents a contiguous range within Morphe source code.
 *
 * <p>
 * The start position is inclusive and the end position is exclusive.
 * A range may be empty when both positions have the same offset.
 * </p>
 *
 * @param start inclusive start position
 * @param end exclusive end position
 */
public record SourceRange(SourcePosition start, SourcePosition end) {

  /**
   * Creates a source range.
   *
   * @throws NullArgumentException if {@code start} or {@code end}
   * is {@code null}
   * @throws InvalidArgumentException if the start position occurs
   * after the end position
   */
  public SourceRange {
    if (start == null) throw new NullArgumentException("start");
    if (end == null) throw new NullArgumentException("end");
    if (start.offset() > end.offset()) throw new InvalidArgumentException("start", "cannot occur after end");
  }

  /**
   * Returns the number of source characters covered by this range.
   *
   * @return the range length
   */
  public int length() {
    return end.offset() - start.offset();
  }

  /**
   * Returns whether this range contains no source characters.
   *
   * @return {@code true} if the start and end offsets are equal;
   * {@code false} otherwise
   */
  public boolean isEmpty() {
    return start.offset() == end.offset();
  }
}