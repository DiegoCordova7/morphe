package dev.morphe.vm.exception.internal.scope;

import dev.morphe.vm.exception.internal.VMInternalException;

/**
 * Base exception for errors related to virtual machine scope operations.
 *
 * <p>
 * This exception represents failures that occur while resolving, accessing,
 * or manipulating variables within Morphe VM scopes.
 * </p>
 */
public abstract class ScopeException extends VMInternalException {

  /**
   * Creates a new scope exception with the specified message.
   *
   * @param message the exception message
   */
  public ScopeException(String message) {
    super(message);
  }

  /**
   * Creates a new scope exception using a formatted message.
   *
   * @param messageFormat the message format
   * @param arguments the arguments used to format the message
   */
  public ScopeException(String messageFormat, Object... arguments) {
    super(messageFormat, arguments);
  }
}
