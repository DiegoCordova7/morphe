package dev.morphe.semantic.exception.statement;

import dev.morphe.semantic.exception.SemanticException;

/**
 * Indicates that a constant declaration does not provide an initializer.
 *
 * <p>Constants in Morphe must be initialized at the point of declaration.
 * This exception is raised when a constant declaration omits its initializer.</p>
 */
public final class MissingInitializerException extends SemanticException {

  /**
   * Creates an exception for a constant declaration without an initializer.
   *
   * @param name the name of the constant
   */
  public MissingInitializerException(String name) {
    super("Constant " + name + " requires an initializer");
  }
}