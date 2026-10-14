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
package dev.morphe.runtime.type.composite;

import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.type.IType;

/**
 * Defines the expected type of a field in a composite type.
 *
 * <p>A field type associates a field name with the {@link IType} expected
 * for values stored in that field.</p>
 */
public final class FieldType {

  private final String name;
  private final IType type;

  /**
   * Creates a field type definition.
   *
   * @param name the name of the field
   * @param type the type expected by the field
   * @throws NullArgumentException if {@code name} or {@code type} is
   *     {@code null}
   */
  public FieldType(String name, IType type) {
    if (name == null) throw new NullArgumentException("name");
    if (type == null) throw new NullArgumentException("type");
    this.name = name;
    this.type = type;
  }

  /**
   * Returns the name of this field.
   *
   * @return the field name
   */
  public String name() {
    return name;
  }

  /**
   * Returns the type expected by this field.
   *
   * @return the field type
   */
  public IType type() {
    return type;
  }

  /**
   * Determines whether this field type is equal to another object.
   *
   * <p>Two field types are equal when they have the same field name and
   * the same associated type.</p>
   *
   * @param object the object to compare with this field type
   * @return {@code true} if the given object represents the same field
   *     type; {@code false} otherwise
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof FieldType other)) return false;
    return name.equals(other.name) && type.equals(other.type);
  }

  /**
   * Returns a hash code for this field type.
   *
   * <p>The hash code is derived from the field name and associated type,
   * consistent with {@link #equals(Object)}.</p>
   *
   * @return the field type hash code
   */
  @Override
  public int hashCode() {
    return 31 * name.hashCode() + type.hashCode();
  }
}