package dev.morphe.vm.exception.internal.scope;

/**
 * Thrown when a variable slot cannot be resolved in the current scope
 * or any of its enclosing scopes.
 *
 * <p>
 * This exception is raised when the virtual machine attempts to access
 * a variable slot that has not been declared in the active scope
 * hierarchy.
 * </p>
 */
public final class UndefinedVariableException extends ScopeException {

  /**
   * Creates an exception indicating that the specified variable slot
   * could not be resolved.
   *
   * @param slot the unresolved variable slot
   */
  public UndefinedVariableException(int slot) {
    super("Undefined variable slot: %d", slot);
  }
}