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
package dev.morphe.runtime.value.collection;

import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.exception.value.NullElementValueException;
import dev.morphe.runtime.kind.ValueKind;
import dev.morphe.runtime.type.collection.ArrayType;
import dev.morphe.runtime.value.IValue;

import java.util.List;

/**
 * Represents an immutable array runtime value defined by an {@link ArrayType}.
 *
 * <p>An array value contains an ordered sequence of runtime values whose
 * types must be accepted by the element type declared by the associated
 * {@link ArrayType}.</p>
 *
 * <p>Array values are immutable. The provided element list is defensively
 * copied during construction, and null elements are not permitted.</p>
 *
 * <p>Array equality is structural. Two arrays are considered equal when they
 * have equal array types and their elements are equal in the same order.</p>
 */
public final class ArrayValue implements ICollectionValue {

  private final ArrayType type;
  private final List<IValue> values;

  /**
   * Creates an array value with the specified type and elements.
   *
   * <p>All elements must be non-null. The provided list is defensively
   * copied, so subsequent modifications to the original list do not
   * affect this array value.</p>
   *
   * @param type the array type associated with this value
   * @param values the ordered elements contained in the array
   *
   * @throws NullArgumentException if {@code type} or {@code values} is null
   * @throws NullElementValueException if any element is null
   */
  public ArrayValue(ArrayType type, List<IValue> values) {
    if (type == null) throw new NullArgumentException("type");
    if (values == null) throw new NullArgumentException("values");
    this.type = type;
    validate(values);
    this.values = List.copyOf(values);
  }

  /**
   * Returns the array type associated with this value.
   *
   * @return the array type
   */
  public ArrayType type() {
    return type;
  }

  /**
   * Returns the immutable ordered list of elements contained in this array.
   *
   * @return the array elements
   */
  public List<IValue> values() {
    return values;
  }

  private void validate(List<IValue> values) {
    for (IValue value : values) {
      if (value == null) throw new NullElementValueException(type);
    }
  }

  /**
   * Returns the runtime kind of this value.
   *
   * @return {@link ValueKind#ARRAY}
   */
  @Override
  public ValueKind kind() {
    return ValueKind.ARRAY;
  }

  /**
   * Returns the number of elements contained in this array.
   *
   * @return the array size
   */
  @Override
  public int size() {
    return values.size();
  }

  /**
   * Returns whether this array contains no elements.
   *
   * @return {@code true} if the array is empty; {@code false} otherwise
   */
  @Override
  public boolean isEmpty() {
    return values.isEmpty();
  }

  /**
   * Compares this array with another array value.
   *
   * <p>Array equality is structural. Both arrays must have equal array types
   * and equal elements in the same order.</p>
   *
   * @param object the object to compare
   * @return {@code true} if both arrays have equal types and elements
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof ArrayValue other)) return false;
    return type.equals(other.type) && values.equals(other.values);
  }

  /**
   * Returns the hash code of this array value.
   *
   * <p>The hash code is derived from both the array type and its elements.</p>
   *
   * @return the array hash code
   */
  @Override
  public int hashCode() {
    return 31 * type.hashCode() + values.hashCode();
  }

  /**
   * Returns the string representation of this array value.
   *
   * @return the array elements as a list representation
   */
  @Override
  public String toString() {
    return values.toString();
  }
}