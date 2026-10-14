package dev.morphe.engine;

/**
 * Represents the result of an Engine operation.
 *
 * @param <T> the type of value produced by the operation
 */
public final class EngineResult<T> {

  private final T value;
  private final EnginePhase phase;
  private final Throwable error;

  private EngineResult(T value, EnginePhase phase, Throwable error) {
    this.value = value;
    this.phase = phase;
    this.error = error;
  }

  /**
   * Creates a successful result.
   *
   * @param value the produced value
   * @param <T> the result value type
   * @return a successful result
   */
  public static <T> EngineResult<T> success(T value) {
    return new EngineResult<>(value, null, null);
  }

  /**
   * Creates a failed result.
   *
   * @param phase the pipeline phase where the error occurred
   * @param error the error that caused the failure
   * @param <T> the expected result value type
   * @return a failed result
   */
  public static <T> EngineResult<T> error(EnginePhase phase, Throwable error) {
    return new EngineResult<>(null, phase, error);
  }

  /**
   * Returns whether the operation completed successfully.
   *
   * @return {@code true} if successful
   */
  public boolean isSuccess() {
    return error == null;
  }

  /**
   * Returns the produced value.
   *
   * @return the result value, or {@code null} if the operation failed
   */
  public T value() {
    return value;
  }

  /**
   * Returns the phase where the operation failed.
   *
   * @return the error phase, or {@code null} if successful
   */
  public EnginePhase phase() {
    return phase;
  }

  /**
   * Returns the error that caused the operation to fail.
   *
   * @return the error, or {@code null} if successful
   */
  public Throwable error() {
    return error;
  }
}