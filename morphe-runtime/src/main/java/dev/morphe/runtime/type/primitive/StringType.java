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
import dev.morphe.runtime.value.primitive.StringValue;

/**
 * Represents the String primitive type in the Morphe runtime.
 *
 * <p>The String type accepts {@link StringValue} instances and
 * {@link NullValue} instances according to the runtime's nullability rules.</p>
 */
public final class StringType implements IPrimitiveType {

  /**
   * Singleton instance of the String runtime type.
   */
  public static final StringType INSTANCE = new StringType();

  /**
   * Creates the String runtime type.
   *
   * <p>The type is exposed through {@link #INSTANCE} and cannot be
   * instantiated externally.</p>
   */
  private StringType() {}

  /**
   * {@inheritDoc}
   */
  @Override
  public String name() {
    return "String";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TypeKind kind() {
    return TypeKind.STRING;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean accepts(IValue value) {
    return value instanceof NullValue || value instanceof StringValue;
  }
}