package dev.morphe.api.exception;

/**
 * Thrown when the execution of a Morphe program exceeds a safety limit
 * (output lines, available inputs, etc).
 */
public final class MorpheExecutionLimitException extends RuntimeException {
  public MorpheExecutionLimitException(String message) {
    super(message);
  }
}