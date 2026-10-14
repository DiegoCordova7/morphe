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

import dev.morphe.bytecode.exception.argument.NullArgumentException;

/**
 * Represents a string literal stored in a bytecode constant pool.
 */
public final class StringConstant implements IConstant {

  private final String value;

  /**
   * Creates a string constant.
   *
   * @param value the string value
   * @throws NullArgumentException if {@code value} is {@code null}
   */
  public StringConstant(String value) {
    if (value == null) throw new NullArgumentException("value");
    this.value = value;
  }

  /**
   * Returns the string value.
   *
   * @return the string value
   */
  public String value() {
    return value;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof StringConstant other)) return false;
    return value.equals(other.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public String toString() {
    return value;
  }
}