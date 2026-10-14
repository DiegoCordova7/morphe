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
import dev.morphe.runtime.value.primitive.numeric.DoubleValue;

/**
 * Represents the Double primitive type in the Morphe runtime.
 *
 * <p>The Double type accepts {@link DoubleValue} instances and
 * {@link NullValue} instances according to the runtime's nullability rules.</p>
 */
public final class DoubleType implements IPrimitiveType {

  /**
   * Singleton instance of the Double runtime type.
   */
  public static final DoubleType INSTANCE = new DoubleType();

  /**
   * Creates the Double runtime type.
   *
   * <p>The type is exposed through {@link #INSTANCE} and cannot be
   * instantiated externally.</p>
   */
  private DoubleType() {}

  /**
   * {@inheritDoc}
   */
  @Override
  public String name() {
    return "Double";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TypeKind kind() {
    return TypeKind.DOUBLE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean accepts(IValue value) {
    return value instanceof NullValue || value instanceof DoubleValue;
  }
}