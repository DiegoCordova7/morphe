package dev.morphe.semantic.exception.pattern;

/**
 * Indicates that a range pattern has invalid bounds.
 *
 * <p>A range is invalid when its start value is greater than its end value.
 * For example, {@code 10 .. 5} is considered an invalid range.</p>
 */
public final class InvalidRangeException extends PatternException {

  /**
   * Creates an exception for a range whose start exceeds its end.
   *
   * @param start the start value of the invalid range
   * @param end the end value of the invalid range
   */
  public InvalidRangeException(Number start, Number end) {
    super("Range start cannot be greater than range end: " + start + " .. " + end);
  }
}