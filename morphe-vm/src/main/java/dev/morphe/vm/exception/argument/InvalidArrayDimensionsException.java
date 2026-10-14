package dev.morphe.vm.exception.argument;

/**
 * Thrown when an array is created with an invalid number of dimensions.
 *
 * <p>An array must specify at least one dimension. An empty set of
 * dimensions is therefore invalid.</p>
 */
public final class InvalidArrayDimensionsException extends ArgumentException {

  /**
   * Creates an exception for an array with no dimensions.
   */
  public InvalidArrayDimensionsException() {
    super("Array dimensions cannot be empty");
  }
}