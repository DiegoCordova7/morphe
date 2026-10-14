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
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FunctionBytecodeTypeTest {

  @Test
  void rejectsNullParameterTypes() {
    assertThrows(NullArgumentException.class, () -> new FunctionBytecodeType(null, VoidBytecodeType.INSTANCE));
  }

  @Test
  void rejectsNullReturnType() {
    assertThrows(NullArgumentException.class, () -> new FunctionBytecodeType(List.of(), null));
  }

  @Test
  void returnsParameterTypes() {
    List<IBytecodeType> parameters = List.of(PrimitiveBytecodeType.INTEGER, PrimitiveBytecodeType.STRING);
    FunctionBytecodeType type = new FunctionBytecodeType(parameters, VoidBytecodeType.INSTANCE);
    assertEquals(parameters, type.parameterTypes());
  }

  @Test
  void parameterTypesAreImmutable() {
    FunctionBytecodeType type = new FunctionBytecodeType(
        List.of(PrimitiveBytecodeType.INTEGER), VoidBytecodeType.INSTANCE
    );
    assertThrows(
        UnsupportedOperationException.class, () -> type.parameterTypes().add(PrimitiveBytecodeType.STRING)
    );
  }

  @Test
  void returnsReturnType() {
    FunctionBytecodeType type = new FunctionBytecodeType(List.of(), PrimitiveBytecodeType.INTEGER);
    assertSame(PrimitiveBytecodeType.INTEGER, type.returnType());
  }

  @Test
  void hasFunctionKind() {
    FunctionBytecodeType type = new FunctionBytecodeType(List.of(), VoidBytecodeType.INSTANCE);
    assertEquals(BytecodeTypeKind.FUNCTION, type.kind());
  }

  @Test
  void considersEquivalentSignaturesEqual() {
    FunctionBytecodeType first = new FunctionBytecodeType(
        List.of(
            PrimitiveBytecodeType.INTEGER,
            PrimitiveBytecodeType.STRING
        ),
        VoidBytecodeType.INSTANCE
    );
    FunctionBytecodeType second = new FunctionBytecodeType(
        List.of(
            PrimitiveBytecodeType.INTEGER,
            PrimitiveBytecodeType.STRING
        ),
        VoidBytecodeType.INSTANCE
    );
    assertEquals(first, second);
  }

  @Test
  void rejectsDifferentReturnTypes() {
    FunctionBytecodeType first = new FunctionBytecodeType(
        List.of(PrimitiveBytecodeType.INTEGER),
        VoidBytecodeType.INSTANCE
    );
    FunctionBytecodeType second = new FunctionBytecodeType(
        List.of(PrimitiveBytecodeType.INTEGER),
        PrimitiveBytecodeType.STRING
    );
    assertNotEquals(first, second);
  }

  @Test
  void rejectsDifferentParameterTypes() {
    FunctionBytecodeType first = new FunctionBytecodeType(
        List.of(PrimitiveBytecodeType.INTEGER),
        VoidBytecodeType.INSTANCE
    );
    FunctionBytecodeType second = new FunctionBytecodeType(
        List.of(PrimitiveBytecodeType.STRING),
        VoidBytecodeType.INSTANCE
    );
    assertNotEquals(first, second);
  }

  @Test
  void parameterOrderAffectsEquality() {
    FunctionBytecodeType first = new FunctionBytecodeType(
        List.of(
            PrimitiveBytecodeType.INTEGER,
            PrimitiveBytecodeType.STRING
        ),
        VoidBytecodeType.INSTANCE
    );
    FunctionBytecodeType second = new FunctionBytecodeType(
        List.of(
            PrimitiveBytecodeType.STRING,
            PrimitiveBytecodeType.INTEGER
        ),
        VoidBytecodeType.INSTANCE
    );
    assertNotEquals(first, second);
  }

  @Test
  void rejectsDifferentParameterCount() {
    FunctionBytecodeType first = new FunctionBytecodeType(
        List.of(),
        VoidBytecodeType.INSTANCE
    );
    FunctionBytecodeType second = new FunctionBytecodeType(
        List.of(PrimitiveBytecodeType.INTEGER),
        VoidBytecodeType.INSTANCE
    );
    assertNotEquals(first, second);
  }

  @Test
  void equalSignaturesHaveEqualHashCodes() {
    FunctionBytecodeType first = new FunctionBytecodeType(
        List.of(
            PrimitiveBytecodeType.INTEGER,
            PrimitiveBytecodeType.STRING
        ),
        VoidBytecodeType.INSTANCE
    );
    FunctionBytecodeType second = new FunctionBytecodeType(
        List.of(
            PrimitiveBytecodeType.INTEGER,
            PrimitiveBytecodeType.STRING
        ),
        VoidBytecodeType.INSTANCE
    );
    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void formatsFunctionSignature() {
    FunctionBytecodeType type = new FunctionBytecodeType(
        List.of(
            PrimitiveBytecodeType.INTEGER,
            PrimitiveBytecodeType.STRING
        ),
        VoidBytecodeType.INSTANCE
    );
    assertEquals("[INTEGER, STRING] -> VOID", type.toString());
  }

  @Test
  void formatsFunctionWithoutParameters() {
    FunctionBytecodeType type = new FunctionBytecodeType(
        List.of(),
        VoidBytecodeType.INSTANCE
    );
    assertEquals("[] -> VOID", type.toString());
  }
}