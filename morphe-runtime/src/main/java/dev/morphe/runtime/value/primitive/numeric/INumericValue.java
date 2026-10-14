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

import dev.morphe.runtime.value.primitive.IPrimitiveValue;

/**
 * Represents a numeric primitive runtime value.
 *
 * <p>Numeric values expose a common double representation that can be
 * used by arithmetic operations regardless of their concrete type.</p>
 */
public interface INumericValue extends IPrimitiveValue {

  /**
   * Returns this numeric value as a double.
   *
   * @return the numeric value converted to double
   */
  double doubleValue();

  /**
   * Returns whether this value uses an integer representation.
   *
   * @return {@code true} if this value uses an integer representation;
   *         {@code false} otherwise
   */
  boolean isInteger();

  @Override
  boolean equals(Object obj);

  @Override
  int hashCode();

}