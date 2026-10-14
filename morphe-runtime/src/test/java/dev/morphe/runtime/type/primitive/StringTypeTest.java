package dev.morphe.runtime.type.primitive;

import dev.morphe.runtime.kind.TypeKind;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.runtime.value.primitive.NullValue;
import dev.morphe.runtime.value.primitive.StringValue;
import dev.morphe.runtime.value.primitive.numeric.DoubleValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringTypeTest {

  private final StringType type = StringType.INSTANCE;

  @Test
  void reportsCorrectName() {
    assertEquals("String", type.name());
  }

  @Test
  void reportsCorrectKind() {
    assertEquals(TypeKind.STRING, type.kind());
  }

  @Test
  void acceptsStringValue() {
    assertTrue(type.accepts(new StringValue("Hello")));
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
  void rejectsBooleanValue() {
    assertFalse(type.accepts(BooleanValue.TRUE));
  }

  @Test
  void rejectsNullReference() {
    assertFalse(type.accepts(null));
  }
}