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
package dev.morphe.runtime.environment;

import dev.morphe.runtime.exception.argument.InvalidClosureEnvironmentIndexException;
import dev.morphe.runtime.exception.argument.InvalidFunctionIdException;
import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.value.IValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the lexical environment captured by a function value.
 *
 * <p>
 * Captured variables are stored as {@link VariableCell} instances so that
 * multiple scopes or closures can share access to the same mutable variable.
 * </p>
 *
 * <p>
 * Captured variables are identified by their position inside the environment.
 * The compiler and virtual machine are responsible for assigning and resolving
 * those positions.
 * </p>
 */
public final class ClosureEnvironment {

  private final List<VariableCell> variables;

  /**
   * Represents an immutable snapshot of the captured values in this
   * environment.
   *
   * <p>
   * The snapshot stores the value contained in each captured variable at
   * the moment the snapshot is created. It does not store the
   * {@link VariableCell} instances themselves, allowing the original cells
   * to remain shared with other scopes or closure environments.
   * </p>
   *
   * @param values the captured values in their original environment order
   */
  public record Snapshot(List<IValue> values) {

    /**
     * Creates a snapshot of the captured values.
     *
     * @param values the captured values in their original environment order
     *
     * @throws NullArgumentException if {@code values} is {@code null}
     */
    public Snapshot {
      if (values == null) throw new NullArgumentException("values");
      values = List.copyOf(values);
    }
  }

  /**
   * Creates an empty closure environment.
   */
  public ClosureEnvironment() {
    variables = new ArrayList<>();
  }

  /**
   * Adds a captured variable to this environment.
   *
   * @param cell the captured variable cell
   *
   * @return the index assigned to the captured variable
   *
   * @throws NullArgumentException if {@code cell} is null
   */
  public int capture(VariableCell cell) {
    if (cell == null) throw new NullArgumentException("cell");
    variables.add(cell);
    return variables.size() - 1;
  }

  /**
   * Returns a captured variable by its index.
   *
   * @param index the captured variable index
   *
   * @return the captured variable cell
   *
   * @throws InvalidFunctionIdException if {@code index} is outside the
   * environment bounds
   */
  public VariableCell get(int index) {
    validateIndex(index);
    return variables.get(index);
  }

  /**
   * Returns the number of captured variables.
   *
   * @return the number of captured variables
   */
  public int size() {
    return variables.size();
  }

  /**
   * Validates a captured variable index.
   *
   * @param index the index to validate
   *
   * @throws InvalidClosureEnvironmentIndexException if {@code index} is outside the
   * environment bounds
   */
  private void validateIndex(int index) {
    if (index < 0 || index >= variables.size()) throw new InvalidClosureEnvironmentIndexException(index);
  }

  /**
   * Captures the current values of all variables in this closure environment.
   *
   * <p>
   * The returned snapshot preserves the current value of every captured
   * variable while leaving the original {@link VariableCell} instances
   * untouched.
   * </p>
   *
   * @return an immutable snapshot of the current closure environment
   */
  public Snapshot snapshot() {
    List<IValue> values = new ArrayList<>(variables.size());
    for (VariableCell variable : variables) values.add(variable.get());
    return new Snapshot(values);
  }

  /**
   * Restores the values of this closure environment from a snapshot.
   *
   * <p>
   * The existing {@link VariableCell} instances are preserved and their
   * contents are replaced with the values stored in the snapshot. This is
   * important because variable cells may be shared by multiple closures or
   * scopes.
   * </p>
   *
   * @param snapshot the snapshot from which to restore the environment
   *
   * @throws NullArgumentException if {@code snapshot} is {@code null}
   * @throws IllegalArgumentException if the snapshot does not contain the
   * same number of captured variables as this environment
   */
  public void restore(Snapshot snapshot) {
    if (snapshot == null) throw new NullArgumentException("snapshot");
    if (variables.size() != snapshot.values().size())
      throw new IllegalArgumentException("snapshot variable count does not match closure environment");
    for (int i = 0; i < variables.size(); i++) variables.get(i).set(snapshot.values().get(i));
  }
}