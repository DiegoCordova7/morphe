package dev.morphe.runtime.type.primitive;

import dev.morphe.runtime.kind.TypeKind;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.runtime.value.primitive.NullValue;
import dev.morphe.runtime.value.primitive.StringValue;
import dev.morphe.runtime.value.primitive.numeric.DoubleValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntegerTypeTest {

  private final IntegerType type = IntegerType.INSTANCE;

  @Test
  void reportsCorrectName() {
    assertEquals("Integer", type.name());
  }

  @Test
  void reportsCorrectKind() {
    assertEquals(TypeKind.INTEGER, type.kind());
  }

  @Test
  void acceptsIntegerValue() {
    assertTrue(type.accepts(new IntegerValue(42)));
  }

  @Test
  void rejectsDoubleValue() {
    assertFalse(type.accepts(new DoubleValue(42.0)));
  }

  @Test
  void rejectsBooleanValue() {
    assertFalse(type.accepts(new BooleanValue(true)));
  }

  @Test
  void rejectsStringValue() {
    assertFalse(type.accepts(new StringValue("42")));
  }

  @Test
  void rejectsNullValue() {
    assertFalse(type.accepts(null));
  }

  @Test
  void acceptsNullValue() {
    assertTrue(type.accepts(new NullValue()));
  }
}