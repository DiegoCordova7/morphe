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
 * Represents a Double value in the Morphe runtime.
 *
 * <p>Double values participate in numeric value-based equality.
 * A double is considered equal to another numeric value when both
 * represent the same numeric value according to Morphe's numeric
 * equality semantics.</p>
 */
public final class DoubleValue implements INumericValue {

  private final double value;

  /**
   * Creates a Double value.
   *
   * @param value the double value
   */
  public DoubleValue(double value) {
    this.value = value;
  }

  /**
   * Returns the underlying double value.
   *
   * @return the double value
   */
  public double value() {
    return value;
  }

  /**
   * Returns whether this value uses an integer representation.
   *
   * @return always {@code false}
   */
  @Override
  public boolean isInteger() {
    return false;
  }

  /**
   * Returns this value as a double.
   *
   * @return the double value
   */
  @Override
  public double doubleValue() {
    return value;
  }

  /**
   * Returns the runtime kind of this value.
   *
   * @return {@link ValueKind#DOUBLE}
   */
  @Override
  public ValueKind kind() {
    return ValueKind.DOUBLE;
  }

  /**
   * Compares this value with another numeric value.
   *
   * <p>Numeric equality is based on the numeric value rather than
   * the concrete numeric representation. Therefore, a
   * {@code DoubleValue} containing {@code 10.0} is equal to an
   * {@code IntegerValue} containing {@code 10}.</p>
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
    return Double.hashCode(value);
  }

  /**
   * Returns the string representation of the underlying double value.
   *
   * @return the double as a string
   */
  @Override
  public String toString() {
    return Double.toString(value);
  }
}