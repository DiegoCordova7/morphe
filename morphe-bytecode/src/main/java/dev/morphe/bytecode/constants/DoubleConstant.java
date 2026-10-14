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
package dev.morphe.bytecode.constants;

/**
 * Represents a double-precision floating-point literal
 * stored in a bytecode constant pool.
 */
public final class DoubleConstant implements IConstant {

  private final double value;

  /**
   * Creates a double constant.
   *
   * @param value the double value
   */
  public DoubleConstant(double value) {
    this.value = value;
  }

  /**
   * Returns the double value.
   *
   * @return the double value
   */
  public double value() {
    return value;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof DoubleConstant other)) return false;
    return Double.compare(value, other.value) == 0;
  }

  @Override
  public int hashCode() {
    return Double.hashCode(value);
  }

  @Override
  public String toString() {
    return Double.toString(value);
  }
}