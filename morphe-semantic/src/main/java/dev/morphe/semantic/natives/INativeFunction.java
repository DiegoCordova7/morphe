package dev.morphe.semantic.natives;

import dev.morphe.ast.type.FunctionType;

/**
 * Represents a native function available to Morphe programs.
 *
 * <p>Native functions are implemented outside the Morphe language runtime
 * and provide an interface between semantic analysis and the execution
 * environment.</p>
 */
public interface INativeFunction {

  /**
   * Returns the name by which the function is exposed to Morphe programs.
   *
   * @return the native function name
   */
  String name();

  /**
   * Returns the type signature of the native function.
   *
   * @return the native function type
   */
  FunctionType type();
}