package dev.morphe.vm.exception.execution;

/**
 * Thrown when an array access uses an index outside the valid bounds
 * of the array.
 */
public final class InvalidArrayIndexException extends VMExecutionException {

  /**
   * Creates an exception for an invalid array index.
   *
   * @param index the index used to access the array
   * @param size the size of the array
   */
  public InvalidArrayIndexException(int index, int size) {
    super("Invalid array index: %s (array size: %s)", index, size);
  }
}