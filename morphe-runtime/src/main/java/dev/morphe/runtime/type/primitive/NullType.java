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
package dev.morphe.runtime.type.primitive;

import dev.morphe.runtime.kind.TypeKind;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.NullValue;

/**
 * Represents the {@code null} type in the Morphe runtime.
 *
 * <p>The null type represents the absence of a concrete runtime value.
 * It is primarily used as the default value for uninitialized
 * reference-compatible values such as array elements.</p>
 */
public final class NullType implements IPrimitiveType {

  /**
   * Singleton instance of the null runtime type.
   */
  public static final NullType INSTANCE = new NullType();

  /**
   * Creates a new null type.
   */
  private NullType() {}

  /**
   * Returns the name of this type.
   *
   * @return {@code "null"}
   */
  @Override
  public String name() {
    return "null";
  }

  /**
   * Returns the runtime kind of this type.
   *
   * @return {@link TypeKind#NULL}
   */
  @Override
  public TypeKind kind() {
    return TypeKind.NULL;
  }

  /**
   * Determines whether the given value represents the null value.
   *
   * @param value the value to validate
   * @return {@code true} if the value is a {@link NullValue};
   *     {@code false} otherwise
   */
  @Override
  public boolean accepts(IValue value) {
    return value instanceof NullValue;
  }
}