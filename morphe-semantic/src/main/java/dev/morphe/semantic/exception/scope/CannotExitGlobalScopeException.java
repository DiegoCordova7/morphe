package dev.morphe.semantic.exception.scope;

/**
 * Thrown when an attempt is made to exit the global semantic scope.
 *
 * <p>The global scope is the root of the scope hierarchy and therefore
 * cannot have its parent scope exited to.</p>
 */
public final class CannotExitGlobalScopeException extends InvalidScopeOperationException {

  /**
   * Creates an exception indicating that the global scope cannot be exited.
   */
  public CannotExitGlobalScopeException() {
    super("Cannot exit global scope");
  }
}