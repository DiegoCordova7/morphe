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
package dev.morphe.runtime.value.primitive.numeric;

import dev.morphe.runtime.kind.ValueKind;

/**
 * Represents an Integer value in the Morphe runtime.
 *
 * <p>Integer values participate in numeric value-based equality.
 * An integer is considered equal to another numeric value when both
 * represent the same numeric value according to Morphe's numeric
 * equality semantics.</p>
 */
public final class IntegerValue implements INumericValue {

  private final int value;

  /**
   * Creates an Integer value.
   *
   * @param value the integer value
   */
  public IntegerValue(int value) {
    this.value = value;
  }

  /**
   * Returns the underlying integer value.
   *
   * @return the integer value
   */
  public int value() {
    return value;
  }

  /**
   * Returns whether this value uses an integer representation.
   *
   * @return always {@code true}
   */
  @Override
  public boolean isInteger() {
    return true;
  }

  /**
   * Returns this value as a double.
   *
   * @return the integer value converted to {@code double}
   */
  @Override
  public double doubleValue() {
    return value;
  }

  /**
   * Returns the runtime kind of this value.
   *
   * @return {@link ValueKind#INTEGER}
   */
  @Override
  public ValueKind kind() {
    return ValueKind.INTEGER;
  }

  /**
   * Compares this value with another numeric value.
   *
   * <p>Numeric equality is based on the numeric value rather than
   * the concrete numeric representation. Therefore, an
   * {@code IntegerValue} containing {@code 10} is equal to a
   * {@code DoubleValue} containing {@code 10.0}.</p>
   *
   * @param obj the object to compare
   * @return {@code true} if both values represent the same numeric value
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof INumericValue other)) return false;
    return Double.compare(doubleValue(), other.doubleValue()) == 0;
  }

  /**
   * Returns the hash code associated with the numeric value.
   *
   * <p>The hash code is based on the common double representation so that
   * numerically equal integer and double values produce the same hash code.</p>
   *
   * @return the numeric value hash code
   */
  @Override
  public int hashCode() {
    return Double.hashCode(doubleValue());
  }

  /**
   * Returns the string representation of the underlying integer value.
   *
   * @return the integer as a string
   */
  @Override
  public String toString() {
    return Integer.toString(value);
  }
}