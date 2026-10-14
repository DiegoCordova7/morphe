package dev.morphe.runtime.type.composite;

import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.type.IType;
import dev.morphe.runtime.type.primitive.IntegerType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldTypeTest {

  @Test
  void storesFieldName() {
    FieldType field = new FieldType("age", IntegerType.INSTANCE);
    assertEquals("age", field.name());
  }

  @Test
  void storesFieldType() {
    IType type = IntegerType.INSTANCE;
    FieldType field = new FieldType("age", type);
    assertEquals(type, field.type());
  }

  @Test
  void rejectsNullFieldName() {
    assertThrows(NullArgumentException.class, () -> new FieldType(null, IntegerType.INSTANCE));
  }

  @Test
  void rejectsNullFieldType() {
    assertThrows(NullArgumentException.class, () -> new FieldType("age", null));
  }

  @Test
  void considersFieldsWithSameNameAndTypeEqual() {
    FieldType first = new FieldType("age", IntegerType.INSTANCE);
    FieldType second = new FieldType("age", IntegerType.INSTANCE);
    assertEquals(first, second);
  }

  @Test
  void considersFieldsWithDifferentNamesNotEqual() {
    FieldType first = new FieldType("age", IntegerType.INSTANCE);
    FieldType second = new FieldType("years", IntegerType.INSTANCE);
    assertNotEquals(first, second);
  }
}