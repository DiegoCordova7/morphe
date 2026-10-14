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
import dev.morphe.bytecode.exception.validation.DuplicateFieldException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompositeBytecodeTypeTest {

  @Test
  void rejectsNullName() {
    assertThrows(NullArgumentException.class, () -> new CompositeBytecodeType(null, List.of()));
  }

  @Test
  void rejectsNullFields() {
    assertThrows(NullArgumentException.class, () -> new CompositeBytecodeType("Ciudad", null));
  }

  @Test
  void rejectsDuplicateFields() {
    CompositeBytecodeType.CompositeBytecodeField first = new CompositeBytecodeType.CompositeBytecodeField(
        "nombre", PrimitiveBytecodeType.STRING
    );
    CompositeBytecodeType.CompositeBytecodeField second = new CompositeBytecodeType.CompositeBytecodeField(
        "nombre", PrimitiveBytecodeType.STRING
    );
    assertThrows(DuplicateFieldException.class, () -> new CompositeBytecodeType("Ciudad", List.of(first, second)));
  }

  @Test
  void rejectsNullFieldName() {
    assertThrows(NullArgumentException.class, () -> new CompositeBytecodeType.CompositeBytecodeField(
        null, PrimitiveBytecodeType.STRING)
    );
  }

  @Test
  void rejectsNullFieldType() {
    assertThrows(NullArgumentException.class, () -> new CompositeBytecodeType.CompositeBytecodeField("nombre", null));
  }

  @Test
  void returnsName() {
    CompositeBytecodeType type = new CompositeBytecodeType("Ciudad", List.of());
    assertEquals("Ciudad", type.name());
  }

  @Test
  void returnsDeclaredFields() {
    CompositeBytecodeType.CompositeBytecodeField field = new CompositeBytecodeType.CompositeBytecodeField(
        "nombre", PrimitiveBytecodeType.STRING
    );
    CompositeBytecodeType type = new CompositeBytecodeType("Ciudad", List.of(field));
    assertEquals(List.of(field), type.fields());
  }

  @Test
  void fieldsAreImmutable() {
    CompositeBytecodeType type = new CompositeBytecodeType(
        "Ciudad",
        List.of(new CompositeBytecodeType.CompositeBytecodeField("nombre", PrimitiveBytecodeType.STRING))
    );
    assertThrows(
        UnsupportedOperationException.class,
        () -> type.fields().add(new CompositeBytecodeType.CompositeBytecodeField("codigo", PrimitiveBytecodeType.INTEGER))
    );
  }

  @Test
  void resolvesFieldByName() {
    CompositeBytecodeType.CompositeBytecodeField field = new CompositeBytecodeType.CompositeBytecodeField(
        "nombre", PrimitiveBytecodeType.STRING
    );
    CompositeBytecodeType type = new CompositeBytecodeType("Ciudad", List.of(field));
    assertEquals(field, type.field("nombre"));
  }

  @Test
  void returnsNullForUnknownField() {
    CompositeBytecodeType type = new CompositeBytecodeType("Ciudad", List.of());
    assertNull(type.field("nombre"));
  }

  @Test
  void returnsFieldIndex() {
    CompositeBytecodeType type = new CompositeBytecodeType(
        "Ciudad",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("nombre", PrimitiveBytecodeType.STRING),
            new CompositeBytecodeType.CompositeBytecodeField("codigo", PrimitiveBytecodeType.INTEGER)
        )
    );
    assertEquals(0, type.fieldIndex("nombre"));
    assertEquals(1, type.fieldIndex("codigo"));
  }

  @Test
  void returnsMinusOneForUnknownFieldIndex() {
    CompositeBytecodeType type = new CompositeBytecodeType("Ciudad", List.of());
    assertEquals(-1, type.fieldIndex("nombre"));
  }

  @Test
  void detectsExistingField() {
    CompositeBytecodeType type = new CompositeBytecodeType(
        "Ciudad",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("nombre", PrimitiveBytecodeType.STRING)
        )
    );
    assertTrue(type.hasField("nombre"));
    assertFalse(type.hasField("codigo"));
  }

  @Test
  void rejectsNullFieldNameWhenResolvingField() {
    CompositeBytecodeType type = new CompositeBytecodeType("Ciudad", List.of());
    assertThrows(NullArgumentException.class, () -> type.field(null));
    assertThrows(NullArgumentException.class, () -> type.fieldIndex(null));
    assertThrows(NullArgumentException.class, () -> type.hasField(null));
  }

  @Test
  void hasCompositeKind() {
    CompositeBytecodeType type = new CompositeBytecodeType("Ciudad", List.of());
    assertEquals(BytecodeTypeKind.COMPOSITE, type.kind());
  }

  @Test
  void considersEquivalentStructuresEqual() {
    CompositeBytecodeType first = new CompositeBytecodeType(
        "Ciudad",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField(
                "nombre",
                PrimitiveBytecodeType.STRING
            )
        )
    );
    CompositeBytecodeType second = new CompositeBytecodeType(
        "Ciudad",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("nombre", PrimitiveBytecodeType.STRING)
        )
    );
    assertEquals(first, second);
  }

  @Test
  void ignoresTypeNameDuringStructuralEquality() {
    CompositeBytecodeType ciudad = new CompositeBytecodeType(
        "Ciudad",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("nombre", PrimitiveBytecodeType.STRING
            )
        )
    );
    CompositeBytecodeType localidad = new CompositeBytecodeType(
        "Localidad",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("nombre", PrimitiveBytecodeType.STRING)
        )
    );
    assertEquals(ciudad, localidad);
  }

  @Test
  void ignoresFieldOrderDuringStructuralEquality() {
    CompositeBytecodeType first = new CompositeBytecodeType(
        "First",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("nombre", PrimitiveBytecodeType.STRING),
            new CompositeBytecodeType.CompositeBytecodeField("codigo", PrimitiveBytecodeType.INTEGER)
        )
    );
    CompositeBytecodeType second = new CompositeBytecodeType(
        "Second",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("codigo", PrimitiveBytecodeType.INTEGER),
            new CompositeBytecodeType.CompositeBytecodeField("nombre", PrimitiveBytecodeType.STRING)
        )
    );
    assertEquals(first, second);
  }

  @Test
  void rejectsDifferentFieldTypes() {
    CompositeBytecodeType first = new CompositeBytecodeType(
        "Ciudad",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("codigo", PrimitiveBytecodeType.INTEGER)
        )
    );
    CompositeBytecodeType second = new CompositeBytecodeType(
        "Ciudad",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("codigo", PrimitiveBytecodeType.STRING)
        )
    );
    assertNotEquals(first, second);
  }

  @Test
  void rejectsDifferentFieldCount() {
    CompositeBytecodeType first = new CompositeBytecodeType("Ciudad", List.of());
    CompositeBytecodeType second = new CompositeBytecodeType(
        "Ciudad",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("nombre", PrimitiveBytecodeType.STRING)
        )
    );
    assertNotEquals(first, second);
  }

  @Test
  void rejectsDifferentFieldNames() {
    CompositeBytecodeType first = new CompositeBytecodeType(
        "Ciudad",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("nombre", PrimitiveBytecodeType.STRING)
        )
    );
    CompositeBytecodeType second = new CompositeBytecodeType(
        "Ciudad",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("codigo", PrimitiveBytecodeType.STRING)
        )
    );
    assertNotEquals(first, second);
  }

  @Test
  void equalTypesHaveEqualHashCodes() {
    CompositeBytecodeType first = new CompositeBytecodeType(
        "Ciudad",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("nombre", PrimitiveBytecodeType.STRING),
            new CompositeBytecodeType.CompositeBytecodeField("codigo", PrimitiveBytecodeType.INTEGER)
        )
    );
    CompositeBytecodeType second = new CompositeBytecodeType(
        "Localidad",
        List.of(
            new CompositeBytecodeType.CompositeBytecodeField("codigo", PrimitiveBytecodeType.INTEGER),
            new CompositeBytecodeType.CompositeBytecodeField("nombre", PrimitiveBytecodeType.STRING)
        )
    );
    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void usesTypeNameAsStringRepresentation() {
    CompositeBytecodeType type = new CompositeBytecodeType("Ciudad", List.of());
    assertEquals("Ciudad", type.toString());
  }
}