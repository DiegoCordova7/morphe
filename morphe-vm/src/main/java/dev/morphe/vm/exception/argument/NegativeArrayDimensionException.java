package dev.morphe.vm.exception.argument;

/**
 * Thrown when an array dimension has a negative size.
 */
public final class NegativeArrayDimensionException extends ArgumentException {

  /**
   * Creates an exception for a negative array dimension.
   *
   * @param dimension the invalid dimension size
   */
  public NegativeArrayDimensionException(int dimension) {
    super("Array dimension cannot be negative: %d", dimension);
  }
}