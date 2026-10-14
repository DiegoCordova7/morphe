/*
 * Copyright 2026 DiegoCordova
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.morphe.runtime.value.callable;

import dev.morphe.runtime.environment.ClosureEnvironment;
import dev.morphe.runtime.exception.argument.InvalidFunctionIdException;
import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.kind.ValueKind;

/**
 * Represents a function as a runtime value.
 *
 * <p>
 * A function value identifies a function through a non-negative function
 * identifier and carries the lexical environment captured by that function.
 * The environment may be empty when the function does not capture any
 * variables.
 * </p>
 *
 * <p>
 * The function identifier is used by the virtual machine to resolve the
 * corresponding function definition within the active bytecode program.
 * The closure environment stores the variables captured by the function
 * and allows those variables to remain accessible after their original
 * scope has been exited.
 * </p>
 *
 * <p>
 * Function definitions and their associated bytecode are owned by the
 * bytecode program, while the virtual machine is responsible for resolving
 * the function identifier and executing it using its associated closure
 * environment.
 * </p>
 *
 * <p>
 * Function values are first-class values and can therefore be stored,
 * passed, or assigned like other runtime values.
 * </p>
 */
public final class FunctionValue implements ICallableValue {

  private final int functionId;
  private final ClosureEnvironment environment;

  /**
   * Creates a function value associated with the specified function identifier
   * and closure environment.
   *
   * @param functionId the identifier of the function represented by this value
   * @param environment the lexical environment captured by the function
   *
   * @throws InvalidFunctionIdException if {@code functionId} is negative
   * @throws NullArgumentException if {@code environment} is null
   */
  public FunctionValue(int functionId, ClosureEnvironment environment) {
    if (functionId < 0) throw new InvalidFunctionIdException(functionId);
    if (environment == null) throw new NullArgumentException("environment");
    this.functionId = functionId;
    this.environment = environment;
  }

  /**
   * Returns the identifier of the function represented by this value.
   *
   * @return the function identifier
   */
  public int functionId() {
    return functionId;
  }

  /**
   * Returns the lexical environment captured by this function.
   *
   * <p>
   * The returned environment contains the variable cells captured by the
   * function. The environment may be empty when the function does not capture
   * any variables.
   * </p>
   *
   * @return the closure environment
   */
  public ClosureEnvironment environment() {
    return environment;
  }

  /**
   * Returns the runtime kind of this value.
   *
   * @return {@link ValueKind#FUNCTION}
   */
  @Override
  public ValueKind kind() {
    return ValueKind.FUNCTION;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof FunctionValue other)) return false;
    return functionId == other.functionId;
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(functionId);
  }

  @Override
  public String toString() {
    return "Function(" + functionId + ")";
  }
}