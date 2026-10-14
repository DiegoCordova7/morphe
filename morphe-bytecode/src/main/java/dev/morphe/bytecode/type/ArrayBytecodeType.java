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
package dev.morphe.bytecode.type;

import dev.morphe.bytecode.exception.argument.NullArgumentException;

/**
 * Represents an array type in the Morphe bytecode type system.
 *
 * <p>
 * An array type is defined by the type of the elements it contains.
 * The element type may itself be another {@link ArrayBytecodeType},
 * allowing nested array types to represent multidimensional arrays.
 * </p>
 *
 * <p>
 * For example, an {@code Integer[][]} type can be represented as:
 * </p>
 *
 * <pre>
 * new ArrayBytecodeType(
 *     new ArrayBytecodeType(
 *         PrimitiveBytecodeType.INTEGER
 *     )
 * )
 * </pre>
 *
 * <p>
 * This class represents bytecode-level type information and does not
 * depend on the Morphe runtime type system.
 * </p>
 */
public final class ArrayBytecodeType implements IBytecodeType {

  private final IBytecodeType elementType;

  /**
   * Creates an array bytecode type containing values of the specified type.
   *
   * @param elementType the type of the elements stored in the array
   * @throws NullArgumentException if {@code elementType} is {@code null}
   */
  public ArrayBytecodeType(IBytecodeType elementType) {
    if (elementType == null) throw new NullArgumentException("elementType");
    this.elementType = elementType;
  }

  /**
   * Returns the type of the values contained in the array.
   *
   * <p>
   * If the returned type is another {@link ArrayBytecodeType}, the
   * represented type is multidimensional.
   * </p>
   *
   * @return the array element type
   */
  public IBytecodeType elementType() {
    return elementType;
  }

  /**
   * Returns the general kind of this bytecode type.
   *
   * @return {@link BytecodeTypeKind#ARRAY}
   */
  @Override
  public BytecodeTypeKind kind() {
    return BytecodeTypeKind.ARRAY;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof ArrayBytecodeType other)) return false;
    return elementType.equals(other.elementType);
  }

  @Override
  public int hashCode() {
    return elementType.hashCode();
  }

  @Override
  public String toString() {
    return elementType + "[]";
  }
}