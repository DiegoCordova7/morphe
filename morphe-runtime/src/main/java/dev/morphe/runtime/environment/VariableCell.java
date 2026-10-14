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

import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.value.IValue;

/**
 * Represents a mutable storage cell for a runtime variable.
 *
 * <p>
 * Variable cells allow local variables and closures to share access to the
 * same value. Updating the value stored in a cell is therefore visible to
 * every closure or scope that references that cell.
 * </p>
 */
public final class VariableCell {

  private IValue value;

  /**
   * Creates a variable cell containing the given value.
   *
   * @param value the initial value
   *
   * @throws NullArgumentException if {@code value} is null
   */
  public VariableCell(IValue value) {
    if (value == null) throw new NullArgumentException("value");
    this.value = value;
  }

  /**
   * Returns the value stored in this cell.
   *
   * @return the stored value
   */
  public IValue get() {
    return value;
  }

  /**
   * Replaces the value stored in this cell.
   *
   * @param value the new value
   *
   * @throws NullArgumentException if {@code value} is null
   */
  public void set(IValue value) {
    if (value == null) throw new NullArgumentException("value");
    this.value = value;
  }
}