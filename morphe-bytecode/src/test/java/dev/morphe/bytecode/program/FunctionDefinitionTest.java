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
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.bytecode.type.PrimitiveBytecodeType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FunctionDefinitionTest {

  private static final FunctionBytecodeType TYPE = new FunctionBytecodeType(List.of(), PrimitiveBytecodeType.INTEGER);

  @Test
  void createsDefinitionWithoutCaptures() {
    FunctionDefinition definition = new FunctionDefinition("test", TYPE, 5);
    assertEquals("test", definition.name());
    assertEquals(TYPE, definition.type());
    assertEquals(5, definition.entryPoint());
    assertTrue(definition.captures().isEmpty());
    assertEquals(0, definition.captureCount());
  }

  @Test
  void createsDefinitionWithCaptures() {
    List<CaptureDefinition> captures = List.of(
        new CaptureDefinition(CaptureSourceKind.LOCAL, 0),
        new CaptureDefinition(CaptureSourceKind.CAPTURE, 1)
    );
    FunctionDefinition definition = new FunctionDefinition("test", TYPE, 5, captures);
    assertEquals(captures, definition.captures());
    assertEquals(2, definition.captureCount());
  }

  @Test
  void acceptsZeroAsEntryPoint() {
    FunctionDefinition definition = new FunctionDefinition("test", TYPE, 0);
    assertEquals(0, definition.entryPoint());
  }

  @Test
  void rejectsNullName() {
    assertThrows(NullArgumentException.class, () -> new FunctionDefinition(null, TYPE, 0));
  }

  @Test
  void rejectsNullType() {
    assertThrows(NullArgumentException.class, () -> new FunctionDefinition("test", null, 0));
  }

  @Test
  void rejectsNullCaptures() {
    assertThrows(NullArgumentException.class, () -> new FunctionDefinition("test", TYPE, 0, null));
  }

  @Test
  void rejectsBlankName() {
    assertThrows(InvalidArgumentException.class, () -> new FunctionDefinition("   ", TYPE, 0));
  }

  @Test
  void rejectsEmptyName() {
    assertThrows(InvalidArgumentException.class, () -> new FunctionDefinition("", TYPE, 0));
  }

  @Test
  void rejectsNegativeEntryPoint() {
    assertThrows(InvalidArgumentException.class, () -> new FunctionDefinition("test", TYPE, -1));
  }

  @Test
  void rejectsNullCapture() {
    List<CaptureDefinition> captures = new ArrayList<>();
    captures.add(null);
    assertThrows(NullArgumentException.class, () -> new FunctionDefinition("test", TYPE, 0, captures));
  }

  @Test
  void preservesCaptureOrder() {
    CaptureDefinition first = new CaptureDefinition(CaptureSourceKind.LOCAL, 0);
    CaptureDefinition second = new CaptureDefinition(CaptureSourceKind.CAPTURE, 1);
    FunctionDefinition definition = new FunctionDefinition("test", TYPE, 0, List.of(first, second));
    assertEquals(first, definition.captureAt(0));
    assertEquals(second, definition.captureAt(1));
  }

  @Test
  void captureAtReturnsCaptureByIndex() {
    CaptureDefinition capture = new CaptureDefinition(CaptureSourceKind.LOCAL, 3);
    FunctionDefinition definition = new FunctionDefinition("test", TYPE, 0, List.of(capture));
    assertSame(capture, definition.captureAt(0));
  }

  @Test
  void captureAtRejectsNegativeIndex() {
    FunctionDefinition definition = new FunctionDefinition(
        "test", TYPE, 0, List.of(new CaptureDefinition(CaptureSourceKind.LOCAL, 0))
    );
    assertThrows(InvalidArgumentException.class, () -> definition.captureAt(-1));
  }

  @Test
  void captureAtRejectsIndexEqualToCaptureCount() {
    FunctionDefinition definition = new FunctionDefinition(
        "test", TYPE, 0, List.of(new CaptureDefinition(CaptureSourceKind.LOCAL, 0))
    );
    assertThrows(InvalidArgumentException.class, () -> definition.captureAt(1));
  }

  @Test
  void capturesAreImmutable() {
    FunctionDefinition definition = new FunctionDefinition(
        "test", TYPE, 0, List.of(new CaptureDefinition(CaptureSourceKind.LOCAL, 0))
    );
    assertThrows(
        UnsupportedOperationException.class,
        () -> definition.captures().add(new CaptureDefinition(CaptureSourceKind.LOCAL, 1))
    );
  }

  @Test
  void copiesCapturesDefensively() {
    List<CaptureDefinition> captures = new ArrayList<>();
    captures.add(new CaptureDefinition(CaptureSourceKind.LOCAL, 0));
    FunctionDefinition definition = new FunctionDefinition("test", TYPE, 0, captures);
    captures.add(new CaptureDefinition(CaptureSourceKind.LOCAL, 1));
    assertEquals(1, definition.captureCount());
    assertEquals(CaptureSourceKind.LOCAL, definition.captureAt(0).sourceKind());
  }

  @Test
  void considersDefinitionsWithSameValuesEqual() {
    List<CaptureDefinition> captures = List.of(new CaptureDefinition(CaptureSourceKind.LOCAL, 0));
    FunctionDefinition first = new FunctionDefinition("test", TYPE, 5, captures);
    FunctionDefinition second = new FunctionDefinition("test", TYPE, 5, captures);
    assertEquals(first, second);
  }

  @Test
  void considersDifferentNamesNotEqual() {
    FunctionDefinition first = new FunctionDefinition("first", TYPE, 0);
    FunctionDefinition second = new FunctionDefinition("second", TYPE, 0);
    assertNotEquals(first, second);
  }

  @Test
  void considersDifferentTypesNotEqual() {
    FunctionBytecodeType otherType = new FunctionBytecodeType(List.of(), PrimitiveBytecodeType.DOUBLE);
    FunctionDefinition first = new FunctionDefinition("test", TYPE, 0);
    FunctionDefinition second = new FunctionDefinition("test", otherType, 0);
    assertNotEquals(first, second);
  }

  @Test
  void considersDifferentEntryPointsNotEqual() {
    FunctionDefinition first = new FunctionDefinition("test", TYPE, 0);
    FunctionDefinition second = new FunctionDefinition("test", TYPE, 1);
    assertNotEquals(first, second);
  }

  @Test
  void considersDifferentCapturesNotEqual() {
    FunctionDefinition first = new FunctionDefinition(
        "test", TYPE, 0, List.of(new CaptureDefinition(CaptureSourceKind.LOCAL, 0))
    );
    FunctionDefinition second = new FunctionDefinition(
        "test", TYPE, 0, List.of(new CaptureDefinition(CaptureSourceKind.LOCAL, 1))
    );
    assertNotEquals(first, second);
  }

  @Test
  void considersSameInstanceEqual() {
    FunctionDefinition definition = new FunctionDefinition("test", TYPE, 0);
    assertEquals(definition, definition);
  }

  @Test
  void rejectsNullInEquals() {
    FunctionDefinition definition = new FunctionDefinition("test", TYPE, 0);
    assertNotEquals(definition, null);
  }

  @Test
  void rejectsDifferentTypeInEquals() {
    FunctionDefinition definition = new FunctionDefinition("test", TYPE, 0);
    assertNotEquals(definition, "FunctionDefinition");
  }

  @Test
  void equalDefinitionsHaveSameHashCode() {
    FunctionDefinition first = new FunctionDefinition("test", TYPE, 5);
    FunctionDefinition second = new FunctionDefinition("test", TYPE, 5);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void toStringContainsRelevantInformation() {
    FunctionDefinition definition = new FunctionDefinition(
        "test", TYPE, 5, List.of(
            new CaptureDefinition(CaptureSourceKind.LOCAL, 0), new CaptureDefinition(CaptureSourceKind.CAPTURE, 1)
        )
    );
    String result = definition.toString();
    assertTrue(result.contains("test"));
    assertTrue(result.contains("5"));
    assertTrue(result.contains("captures=2"));
  }
}