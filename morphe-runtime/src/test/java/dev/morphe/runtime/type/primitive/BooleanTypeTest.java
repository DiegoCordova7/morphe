package dev.morphe.runtime.type.primitive;

import dev.morphe.runtime.kind.TypeKind;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.runtime.value.primitive.NullValue;
import dev.morphe.runtime.value.primitive.StringValue;
import dev.morphe.runtime.value.primitive.numeric.DoubleValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BooleanTypeTest {

  private final BooleanType type = BooleanType.INSTANCE;

  @Test
  void reportsCorrectName() {
    assertEquals("Boolean", type.name());
  }

  @Test
  void reportsCorrectKind() {
    assertEquals(TypeKind.BOOLEAN, type.kind());
  }

  @Test
  void acceptsBooleanValue() {
    assertTrue(type.accepts(BooleanValue.TRUE));
  }

  @Test
  void acceptsNullValue() {
    assertTrue(type.accepts(new NullValue()));
  }

  @Test
  void rejectsIntegerValue() {
    assertFalse(type.accepts(new IntegerValue(42)));
  }

  @Test
  void rejectsDoubleValue() {
    assertFalse(type.accepts(new DoubleValue(42.0)));
  }

  @Test
  void rejectsStringValue() {
    assertFalse(type.accepts(new StringValue("true")));
  }

  @Test
  void rejectsNullReference() {
    assertFalse(type.accepts(null));
  }
}