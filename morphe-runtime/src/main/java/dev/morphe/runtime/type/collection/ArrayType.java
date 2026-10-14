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
package dev.morphe.runtime.type.collection;

import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.kind.TypeKind;
import dev.morphe.runtime.type.IType;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.collection.ArrayValue;
import dev.morphe.runtime.value.primitive.NullValue;

/**
 * Represents an array type whose elements are defined by a declared
 * element type.
 *
 * <p>
 * An array type accepts array values whose declared type is equal to
 * this type and whose elements are accepted by the declared element type.
 * </p>
 *
 * <p>
 * {@link NullValue} is accepted by every array type, allowing arrays
 * to contain uninitialized or null elements.
 * </p>
 */
public final class ArrayType implements ICollectionType {

  private final IType elementType;

  /**
   * Creates an array type.
   *
   * @param elementType the type expected for every array element
   *
   * @throws NullArgumentException if {@code elementType} is null
   */
  public ArrayType(IType elementType) {
    if (elementType == null) throw new NullArgumentException("elementType");
    this.elementType = elementType;
  }

  /**
   * Returns the type expected for each array element.
   *
   * @return the element type
   */
  public IType elementType() {
    return elementType;
  }

  /**
   * Returns the name of this type.
   *
   * @return the type name
   */
  @Override
  public String name() {
    return elementType.name() + "[]";
  }

  /**
   * Returns the runtime kind of this type.
   *
   * @return {@link TypeKind#ARRAY}
   */
  @Override
  public TypeKind kind() {
    return TypeKind.ARRAY;
  }

  /**
   * Determines whether the given value is accepted by this type.
   *
   * <p>
   * A {@link NullValue} is accepted by every array type. Otherwise,
   * the value must be an {@link ArrayValue} whose declared type matches
   * this type and whose elements are accepted by the declared element type.
   * </p>
   *
   * @param value the value to validate
   * @return {@code true} if the value is accepted; {@code false} otherwise
   */
  @Override
  public boolean accepts(IValue value) {
    if (value instanceof NullValue) return true;
    if (!(value instanceof ArrayValue arrayValue)) return false;
    if (!equals(arrayValue.type())) return false;
    return arrayValue.values().stream().allMatch(elementType::accepts);
  }

  /**
   * Compares this array type with another object.
   *
   * <p>
   * Array types are structurally equal when their element types
   * are equal.</p>
   *
   * @param object the object to compare
   * @return {@code true} if both array types have equal element types
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof ArrayType other)) return false;
    return elementType.equals(other.elementType);
  }

  /**
   * Returns the hash code of this array type.
   *
   * @return the array type hash code
   */
  @Override
  public int hashCode() {
    return elementType.hashCode();
  }

  /**
   * Returns the string representation of this array type.
   *
   * @return the array type name
   */
  @Override
  public String toString() {
    return name();
  }
}