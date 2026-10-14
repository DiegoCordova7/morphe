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
package dev.morphe.runtime.type;

import dev.morphe.runtime.kind.TypeKind;
import dev.morphe.runtime.value.IValue;

/**
 * Represents a type in the Morphe runtime.
 *
 * <p>A type defines a name, a runtime kind, and the values
 * accepted by that type.</p>
 */
public interface IType {

  /**
   * Returns the name of this type.
   *
   * @return the type name
   */
  String name();

  /**
   * Returns the runtime kind of this type.
   *
   * @return the type kind
   */
  TypeKind kind();

  /**
   * Determines whether the given value is accepted by this type.
   *
   * <p>The result may depend on the type's nullability rules.</p>
   *
   * @param value the value to validate against this type
   * @return {@code true} if the value is accepted by this type;
   *     {@code false} otherwise
   */
  boolean accepts(IValue value);
}