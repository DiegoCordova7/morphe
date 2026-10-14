package dev.morphe.semantic.result;

/**
 * Represents the semantic information produced by analyzing a statement.
 *
 * <p>The result describes whether the statement always completes by returning
 * from the current function.</p>
 *
 * <p>A statement that always returns cannot complete normally. This information
 * can be used by subsequent semantic analysis and compiler phases to reason
 * about control flow.</p>
 */
public final class StatementResult {

  private final boolean alwaysReturns;

  /**
   * Creates a statement analysis result.
   *
   * @param alwaysReturns whether the statement always returns from the
   *                      current function
   */
  public StatementResult(boolean alwaysReturns) {
    this.alwaysReturns = alwaysReturns;
  }

  /**
   * Returns whether the statement always returns from the current function.
   *
   * @return {@code true} if the statement always returns
   */
  public boolean alwaysReturns() {
    return alwaysReturns;
  }
}