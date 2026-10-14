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
package dev.morphe.runtime.value.primitive;

import dev.morphe.runtime.kind.ValueKind;
import dev.morphe.runtime.value.IValue;

/**
 * Represents the {@code null} value in the Morphe runtime.
 *
 * <p>
 * A null value represents the absence of a concrete runtime value.
 * Unlike Java {@code null}, {@code NullValue} is an actual Morphe
 * runtime value and can therefore be safely stored in runtime
 * collections and arrays.
 * </p>
 *
 * <p>
 * Null values are immutable and contain no additional state.
 * </p>
 */
public final class NullValue implements IValue {

  /**
   * Creates a new null runtime value.
   */
  public NullValue() {}

  /**
   * Returns the runtime kind of this value.
   *
   * @return {@link ValueKind#NULL}
   */
  @Override
  public ValueKind kind() {
    return ValueKind.NULL;
  }

  /**
   * Compares this value with another value.
   *
   * <p>
   * All instances of {@code NullValue} are considered equal because
   * the null value contains no state.
   * </p>
   *
   * @param object the object to compare
   * @return {@code true} if the object is another {@code NullValue}
   */
  @Override
  public boolean equals(Object object) {
    return object instanceof NullValue;
  }

  /**
   * Returns the hash code of this null value.
   *
   * @return a constant hash code shared by all null values
   */
  @Override
  public int hashCode() {
    return NullValue.class.hashCode();
  }

  /**
   * Returns the string representation of this value.
   *
   * @return {@code "null"}
   */
  @Override
  public String toString() {
    return "null";
  }
}