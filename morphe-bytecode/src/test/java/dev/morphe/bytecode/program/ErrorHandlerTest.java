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
package dev.morphe.bytecode.program;

import dev.morphe.bytecode.exception.argument.InvalidArgumentException;
import dev.morphe.bytecode.exception.argument.NullArgumentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class ErrorHandlerTest {

  @Test
  void createsHandlerWithValidArguments() {
    BytecodeErrorKind errorKind = BytecodeErrorKind.values()[0];
    ErrorHandler handler = new ErrorHandler(errorKind, 5);
    assertEquals(errorKind, handler.errorKind());
    assertEquals(5, handler.entryPoint());
  }

  @Test
  void acceptsZeroAsEntryPoint() {
    BytecodeErrorKind errorKind = BytecodeErrorKind.values()[0];
    ErrorHandler handler = new ErrorHandler(errorKind, 0);
    assertEquals(0, handler.entryPoint());
  }

  @Test
  void rejectsNullErrorKind() {
    assertThrows(NullArgumentException.class, () -> new ErrorHandler(null, 0));
  }

  @Test
  void rejectsNegativeEntryPoint() {
    BytecodeErrorKind errorKind = BytecodeErrorKind.values()[0];
    assertThrows(InvalidArgumentException.class, () -> new ErrorHandler(errorKind, -1));
  }

  @Test
  void considersHandlersWithSameValuesEqual() {
    BytecodeErrorKind errorKind = BytecodeErrorKind.values()[0];
    ErrorHandler first = new ErrorHandler(errorKind, 5);
    ErrorHandler second = new ErrorHandler(errorKind, 5);
    assertEquals(first, second);
  }

  @Test
  void considersDifferentErrorKindsNotEqual() {
    BytecodeErrorKind[] kinds = BytecodeErrorKind.values();
    assumeTrue(kinds.length > 1);
    ErrorHandler first = new ErrorHandler(kinds[0], 5);
    ErrorHandler second = new ErrorHandler(kinds[1], 5);
    assertNotEquals(first, second);
  }

  @Test
  void considersDifferentEntryPointsNotEqual() {
    BytecodeErrorKind errorKind = BytecodeErrorKind.values()[0];
    ErrorHandler first = new ErrorHandler(errorKind, 5);
    ErrorHandler second = new ErrorHandler(errorKind, 6);
    assertNotEquals(first, second);
  }

  @Test
  void considersSameInstanceEqual() {
    BytecodeErrorKind errorKind = BytecodeErrorKind.values()[0];
    ErrorHandler handler = new ErrorHandler(errorKind, 5);
    assertEquals(handler, handler);
  }

  @Test
  void rejectsNullInEquals() {
    BytecodeErrorKind errorKind = BytecodeErrorKind.values()[0];
    ErrorHandler handler = new ErrorHandler(errorKind, 5);
    assertNotEquals(handler, null);
  }

  @Test
  void rejectsDifferentTypeInEquals() {
    BytecodeErrorKind errorKind = BytecodeErrorKind.values()[0];
    ErrorHandler handler = new ErrorHandler(errorKind, 5);
    assertNotEquals(handler, "ErrorHandler");
  }

  @Test
  void equalHandlersHaveSameHashCode() {
    BytecodeErrorKind errorKind = BytecodeErrorKind.values()[0];
    ErrorHandler first = new ErrorHandler(errorKind, 5);
    ErrorHandler second = new ErrorHandler(errorKind, 5);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void toStringContainsErrorKindAndEntryPoint() {
    BytecodeErrorKind errorKind = BytecodeErrorKind.values()[0];
    ErrorHandler handler = new ErrorHandler(errorKind, 5);
    String result = handler.toString();
    assertTrue(result.contains(errorKind.toString()));
    assertTrue(result.contains("5"));
  }
}