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
 * Represents a boolean literal stored in a bytecode constant pool.
 */
public final class BooleanConstant implements IConstant {

  private final boolean value;

  /**
   * Creates a boolean constant.
   *
   * @param value the boolean value
   */
  public BooleanConstant(boolean value) {
    this.value = value;
  }

  /**
   * Returns the boolean value.
   *
   * @return the boolean value
   */
  public boolean value() {
    return value;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof BooleanConstant other)) return false;
    return value == other.value;
  }

  @Override
  public int hashCode() {
    return Boolean.hashCode(value);
  }

  @Override
  public String toString() {
    return Boolean.toString(value);
  }
}