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

/**
 * Represents a Boolean value in the Morphe runtime.
 *
 * <p>Canonical instances are available through {@link #TRUE} and
 * {@link #FALSE}. The {@link #of(boolean)} factory method returns
 * the corresponding canonical instance.</p>
 *
 * <p>Boolean values use value-based equality, meaning that two
 * {@code BooleanValue} instances are equal when they represent
 * the same boolean value, regardless of whether they are the same
 * object instance.</p>
 */
public final class BooleanValue implements IPrimitiveValue {

  /**
   * The canonical Boolean value representing {@code true}.
   */
  public static final BooleanValue TRUE = new BooleanValue(true);

  /**
   * The canonical Boolean value representing {@code false}.
   */
  public static final BooleanValue FALSE = new BooleanValue(false);

  private final boolean value;

  /**
   * Creates a Boolean value.
   *
   * @param value the boolean value
   */
  public BooleanValue(boolean value) {
    this.value = value;
  }

  /**
   * Returns the canonical Boolean instance for the specified value.
   *
   * @param value the boolean value
   * @return {@link #TRUE} if the value is {@code true};
   * {@link #FALSE} otherwise
   */
  public static BooleanValue of(boolean value) {
    return value ? TRUE : FALSE;
  }

  /**
   * Returns the underlying boolean value.
   *
   * @return the boolean value
   */
  public boolean value() {
    return value;
  }

  /**
   * Returns the runtime kind of this value.
   *
   * @return {@link ValueKind#BOOLEAN}
   */
  @Override
  public ValueKind kind() {
    return ValueKind.BOOLEAN;
  }

  /**
   * Compares this value with another Boolean value.
   *
   * <p>Equality is based on the underlying boolean value rather than
   * object identity.</p>
   *
   * @param obj the object to compare
   * @return {@code true} if both values contain the same boolean value
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof BooleanValue other)) return false;
    return value == other.value;
  }

  /**
   * Returns the hash code of the underlying boolean value.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Boolean.hashCode(value);
  }

  /**
   * Returns the string representation of the underlying boolean value.
   *
   * @return the boolean value as a string
   */
  @Override
  public String toString() {
    return Boolean.toString(value);
  }
}