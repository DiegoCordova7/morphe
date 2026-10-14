package dev.morphe.ast.declaration;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.type.IASTType;

/**
 * Represents a parameter declared by a function or lambda.
 *
 * <p>A parameter associates an identifier with its declared AST type.</p>
 */
public final class Parameter {

  private final String name;
  private final IASTType type;

  /**
   * Creates a parameter.
   *
   * @param name the parameter identifier
   * @param type the declared parameter type
   * @throws NullArgumentException if {@code name} or {@code type} is null
   */
  public Parameter(String name, IASTType type) {
    if (name == null) throw new NullArgumentException("name");
    if (type == null) throw new NullArgumentException("type");
    this.name = name;
    this.type = type;
  }

  /**
   * Returns the parameter identifier.
   *
   * @return the parameter name
   */
  public String name() {
    return name;
  }

  /**
   * Returns the declared parameter type.
   *
   * @return the parameter type
   */
  public IASTType type() {
    return type;
  }
}