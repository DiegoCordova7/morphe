package dev.morphe.semantic.natives;

import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Registry of native functions available to Morphe programs.
 */
public final class NativeFunctionRegistry {

  private final Map<String, INativeFunction> functions = new HashMap<>();

  /**
   * Registers a native function.
   *
   * @param function the native function
   * @throws NullSemanticArgumentException if {@code function} is null
   * @throws IllegalArgumentException if another function with the same name
   *     is already registered
   */
  public void register(INativeFunction function) {
    if (function == null) throw new NullSemanticArgumentException("function");
    if (functions.putIfAbsent(function.name(), function) != null)
      throw new IllegalArgumentException("Native function already registered: " + function.name());
  }

  public static NativeFunctionRegistry defaults() {
    NativeFunctionRegistry registry = new NativeFunctionRegistry();
    registry.register(new PrintNativeFunction());
    registry.register(new ReadNativeFunction("readString", PrimitiveType.STRING));
    registry.register(new ReadNativeFunction("readInteger", PrimitiveType.INTEGER));
    registry.register(new ReadNativeFunction("readDouble", PrimitiveType.DOUBLE));
    registry.register(new ReadNativeFunction("readBoolean", PrimitiveType.BOOLEAN));
    return registry;
  }

  /**
   * Returns the native function with the specified name.
   *
   * @param name the native function name
   * @return the registered native function
   * @throws IllegalArgumentException if no function is registered with
   *     the specified name
   */
  public INativeFunction resolve(String name) {
    INativeFunction function = functions.get(name);
    if (function == null) throw new IllegalArgumentException("Unknown native function: " + name);
    return function;
  }

  /**
   * Determines whether a native function with the specified name exists.
   *
   * @param name the native function name
   * @return {@code true} if the function is registered
   */
  public boolean contains(String name) {
    return functions.containsKey(name);
  }

  public Collection<INativeFunction> functions() {
    return Collections.unmodifiableCollection(functions.values());
  }
}
