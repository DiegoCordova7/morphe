package dev.morphe.vm.exception.internal.scope;

/**
 * Thrown when an attempt is made to exit the root scope of the
 * virtual machine.
 *
 * <p>
 * The root scope is the outermost scope in the scope hierarchy and
 * does not have a parent scope. It must remain active for the lifetime
 * of the corresponding execution context and therefore cannot be exited.
 * </p>
 */
public final class RootScopeExitException extends ScopeException {

  /**
   * Creates an exception indicating that an attempt was made to exit
   * the root scope.
   */
  public RootScopeExitException() {
    super("Cannot exit the root scope");
  }
}