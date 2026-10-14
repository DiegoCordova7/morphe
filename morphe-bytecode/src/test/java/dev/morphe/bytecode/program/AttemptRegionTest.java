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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.morphe.bytecode.exception.argument.InvalidArgumentException;
import dev.morphe.bytecode.exception.argument.NullArgumentException;
import java.util.List;
import org.junit.jupiter.api.Test;

class AttemptRegionTest {

  private static ErrorHandler handler(BytecodeErrorKind kind) {
    return new ErrorHandler(kind, 20);
  }

  @Test
  void rejectsNegativeStart() {
    assertThrows(InvalidArgumentException.class, () -> new AttemptRegion(-1, 10, 20, List.of()));
  }

  @Test
  void rejectsEndBeforeStart() {
    assertThrows(InvalidArgumentException.class, () -> new AttemptRegion(10, 9, 20, List.of()));
  }

  @Test
  void rejectsNegativeFallback() {
    assertThrows(InvalidArgumentException.class, () -> new AttemptRegion(0, 10, -1, List.of()));
  }

  @Test
  void rejectsNullHandlers() {
    assertThrows(NullArgumentException.class, () -> new AttemptRegion(0, 10, 20, null));
  }

  @Test
  void returnsStart() {
    AttemptRegion region = new AttemptRegion(5, 10, 20, List.of());
    assertEquals(5, region.start());
  }

  @Test
  void returnsEnd() {
    AttemptRegion region = new AttemptRegion(5, 10, 20, List.of());
    assertEquals(10, region.end());
  }

  @Test
  void returnsFallback() {
    AttemptRegion region = new AttemptRegion(5, 10, 20, List.of());
    assertEquals(20, region.fallback());
  }

  @Test
  void returnsHandlers() {
    ErrorHandler handler = handler(BytecodeErrorKind.DIVISION_BY_ZERO);
    AttemptRegion region = new AttemptRegion(5, 10, 20, List.of(handler));
    assertEquals(List.of(handler), region.handlers());
  }

  @Test
  void handlersAreImmutable() {
    AttemptRegion region = new AttemptRegion(5, 10, 20, List.of(handler(BytecodeErrorKind.DIVISION_BY_ZERO)));
    assertThrows(UnsupportedOperationException.class, () -> region.handlers().clear());
  }

  @Test
  void rejectsNegativeHandlerIndex() {
    AttemptRegion region = new AttemptRegion(5, 10, 20, List.of());
    assertThrows(InvalidArgumentException.class, () -> region.handlerAt(-1));
  }

  @Test
  void rejectsHandlerIndexAfterLastHandler() {
    AttemptRegion region = new AttemptRegion(5, 10, 20, List.of());
    assertThrows(InvalidArgumentException.class, () -> region.handlerAt(0));
  }

  @Test
  void containsStartPosition() {
    AttemptRegion region = new AttemptRegion(5, 10, 20, List.of());
    assertTrue(region.contains(5));
  }

  @Test
  void containsPositionBeforeEnd() {
    AttemptRegion region = new AttemptRegion(5, 10, 20, List.of());
    assertTrue(region.contains(9));
  }

  @Test
  void excludesEndPosition() {
    AttemptRegion region = new AttemptRegion(5, 10, 20, List.of());
    assertFalse(region.contains(10));
  }

  @Test
  void excludesPositionBeforeStart() {
    AttemptRegion region = new AttemptRegion(5, 10, 20, List.of());
    assertFalse(region.contains(4));
  }

  @Test
  void emptyRegionContainsNoPosition() {
    AttemptRegion region = new AttemptRegion(5, 5, 20, List.of());
    assertFalse(region.contains(5));
  }

  @Test
  void considersEquivalentRegionsEqual() {
    ErrorHandler handler = handler(BytecodeErrorKind.DIVISION_BY_ZERO);
    AttemptRegion first = new AttemptRegion(5, 10, 20, List.of(handler));
    AttemptRegion second = new AttemptRegion(5, 10, 20, List.of(handler));
    assertEquals(first, second);
  }

  @Test
  void rejectsDifferentStart() {
    AttemptRegion first = new AttemptRegion(5, 10, 20, List.of());
    AttemptRegion second = new AttemptRegion(6, 10, 20, List.of());
    assertNotEquals(first, second);
  }

  @Test
  void rejectsDifferentEnd() {
    AttemptRegion first = new AttemptRegion(5, 10, 20, List.of());
    AttemptRegion second = new AttemptRegion(5, 11, 20, List.of());
    assertNotEquals(first, second);
  }

  @Test
  void rejectsDifferentFallback() {
    AttemptRegion first = new AttemptRegion(5, 10, 20, List.of());
    AttemptRegion second = new AttemptRegion(5, 10, 21, List.of());
    assertNotEquals(first, second);
  }

  @Test
  void equalRegionsHaveEqualHashCodes() {
    ErrorHandler handler = handler(BytecodeErrorKind.DIVISION_BY_ZERO);
    AttemptRegion first = new AttemptRegion(5, 10, 20, List.of(handler));
    AttemptRegion second = new AttemptRegion(5, 10, 20, List.of(handler));
    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void toStringContainsRegionMetadata() {
    AttemptRegion region = new AttemptRegion(5, 10, 20, List.of());
    String representation = region.toString();
    assertTrue(representation.contains("start=5"));
    assertTrue(representation.contains("end=10"));
    assertTrue(representation.contains("fallback=20"));
    assertTrue(representation.contains("handlers=[]"));
  }
}