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

class CaptureDefinitionTest {

  @Test
  void createsDefinitionWithLocalSource() {
    CaptureDefinition definition = new CaptureDefinition(CaptureSourceKind.LOCAL, 2);
    assertEquals(CaptureSourceKind.LOCAL, definition.sourceKind());
    assertEquals(2, definition.index());
  }

  @Test
  void createsDefinitionWithCaptureSource() {
    CaptureDefinition definition = new CaptureDefinition(CaptureSourceKind.CAPTURE, 3);
    assertEquals(CaptureSourceKind.CAPTURE, definition.sourceKind());
    assertEquals(3, definition.index());
  }

  @Test
  void acceptsZeroAsIndex() {
    CaptureDefinition definition = new CaptureDefinition(CaptureSourceKind.LOCAL, 0);
    assertEquals(0, definition.index());
  }

  @Test
  void rejectsNullSourceKind() {
    assertThrows(NullArgumentException.class, () -> new CaptureDefinition(null, 0));
  }

  @Test
  void rejectsNegativeIndex() {
    assertThrows(InvalidArgumentException.class, () -> new CaptureDefinition(CaptureSourceKind.LOCAL, -1));
  }

  @Test
  void considersDefinitionsWithSameValuesEqual() {
    CaptureDefinition first = new CaptureDefinition(CaptureSourceKind.LOCAL, 2);
    CaptureDefinition second = new CaptureDefinition(CaptureSourceKind.LOCAL, 2);
    assertEquals(first, second);
  }

  @Test
  void considersDifferentSourceKindsNotEqual() {
    CaptureDefinition local = new CaptureDefinition(CaptureSourceKind.LOCAL, 2);
    CaptureDefinition capture = new CaptureDefinition(CaptureSourceKind.CAPTURE, 2);
    assertNotEquals(local, capture);
  }

  @Test
  void considersDifferentIndicesNotEqual() {
    CaptureDefinition first = new CaptureDefinition(CaptureSourceKind.LOCAL, 1);
    CaptureDefinition second = new CaptureDefinition(CaptureSourceKind.LOCAL, 2);
    assertNotEquals(first, second);
  }

  @Test
  void considersSameInstanceEqual() {
    CaptureDefinition definition = new CaptureDefinition(CaptureSourceKind.LOCAL, 2);
    assertEquals(definition, definition);
  }

  @Test
  void rejectsNullInEquals() {
    CaptureDefinition definition = new CaptureDefinition(CaptureSourceKind.LOCAL, 2);
    assertNotEquals(null, definition);
  }

  @Test
  void rejectsDifferentTypeInEquals() {
    CaptureDefinition definition = new CaptureDefinition(CaptureSourceKind.LOCAL, 2);
    assertNotEquals(definition, "CaptureDefinition");
  }

  @Test
  void equalDefinitionsHaveSameHashCode() {
    CaptureDefinition first = new CaptureDefinition(CaptureSourceKind.LOCAL, 2);
    CaptureDefinition second = new CaptureDefinition(CaptureSourceKind.LOCAL, 2);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void toStringContainsSourceKindAndIndex() {
    CaptureDefinition definition = new CaptureDefinition(CaptureSourceKind.CAPTURE, 4);
    String result = definition.toString();
    assertTrue(result.contains("CAPTURE"));
    assertTrue(result.contains("4"));
  }
}