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

import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.kind.ValueKind;

/**
 * Represents an immutable String value in the Morphe runtime.
 *
 * <p>String values use value-based equality, meaning that two
 * {@code StringValue} instances are equal when they contain the same
 * sequence of characters.</p>
 */
public final class StringValue implements IPrimitiveValue {

  private final String value;

  /**
   * Creates a String value.
   *
   * @param value the string value
   * @throws NullArgumentException if {@code value} is {@code null}
   */
  public StringValue(String value) {
    if (value == null) throw new NullArgumentException("value");
    this.value = value;
  }

  /**
   * Returns the underlying string value.
   *
   * @return the string value
   */
  public String value() {
    return value;
  }

  /**
   * Returns the runtime kind of this value.
   *
   * @return {@link ValueKind#STRING}
   */
  @Override
  public ValueKind kind() {
    return ValueKind.STRING;
  }

  /**
   * Compares this value with another String value.
   *
   * <p>Equality is based on string content rather than object identity.</p>
   *
   * @param obj the object to compare
   * @return {@code true} if both values contain the same string
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof StringValue other)) return false;
    return value.equals(other.value);
  }

  /**
   * Returns the hash code of the underlying string value.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return value.hashCode();
  }

  /**
   * Returns the string representation of the underlying string value.
   *
   * @return the string value
   */
  @Override
  public String toString() {
    return value;
  }
}