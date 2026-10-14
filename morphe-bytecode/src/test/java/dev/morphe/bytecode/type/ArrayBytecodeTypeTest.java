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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.morphe.bytecode.exception.argument.NullArgumentException;
import org.junit.jupiter.api.Test;

class ArrayBytecodeTypeTest {

  @Test
  void rejectsNullElementType() {
    assertThrows(NullArgumentException.class, () -> new ArrayBytecodeType(null));
  }

  @Test
  void returnsElementType() {
    IBytecodeType elementType = PrimitiveBytecodeType.INTEGER;
    ArrayBytecodeType type = new ArrayBytecodeType(elementType);
    assertSame(elementType, type.elementType());
  }

  @Test
  void hasArrayKind() {
    ArrayBytecodeType type = new ArrayBytecodeType(PrimitiveBytecodeType.INTEGER);
    assertEquals(BytecodeTypeKind.ARRAY, type.kind());
  }

  @Test
  void considersEquivalentArrayTypesEqual() {
    ArrayBytecodeType first = new ArrayBytecodeType(PrimitiveBytecodeType.INTEGER);
    ArrayBytecodeType second = new ArrayBytecodeType(PrimitiveBytecodeType.INTEGER);
    assertEquals(first, second);
  }

  @Test
  void rejectsDifferentElementTypes() {
    ArrayBytecodeType integerArray = new ArrayBytecodeType(PrimitiveBytecodeType.INTEGER);
    ArrayBytecodeType doubleArray = new ArrayBytecodeType(PrimitiveBytecodeType.DOUBLE);
    assertNotEquals(integerArray, doubleArray);
  }

  @Test
  void supportsMultidimensionalTypes() {
    ArrayBytecodeType integerArray = new ArrayBytecodeType(PrimitiveBytecodeType.INTEGER);
    ArrayBytecodeType integerMatrix = new ArrayBytecodeType(integerArray);
    assertEquals(integerArray, integerMatrix.elementType());
  }

  @Test
  void formatsArrayType() {
    ArrayBytecodeType type = new ArrayBytecodeType(PrimitiveBytecodeType.INTEGER);
    assertEquals("INTEGER[]", type.toString());
  }

  @Test
  void formatsMultidimensionalArrayType() {
    ArrayBytecodeType type =
        new ArrayBytecodeType(
            new ArrayBytecodeType(
                PrimitiveBytecodeType.INTEGER
            )
        );
    assertEquals("INTEGER[][]", type.toString());
  }

  @Test
  void equalArrayTypesHaveEqualHashCodes() {
    ArrayBytecodeType first = new ArrayBytecodeType(PrimitiveBytecodeType.INTEGER);
    ArrayBytecodeType second = new ArrayBytecodeType(PrimitiveBytecodeType.INTEGER);
    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
  }
}