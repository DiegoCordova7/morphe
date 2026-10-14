package dev.morphe.runtime.kind;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ValueKindTest {

  @Test
  void containsExpectedKinds() {
    assertArrayEquals(
        new ValueKind[]{
            ValueKind.INTEGER,
            ValueKind.DOUBLE,
            ValueKind.BOOLEAN,
            ValueKind.STRING,
            ValueKind.ARRAY,
            ValueKind.COMPOSITE,
            ValueKind.REFERENCE,
            ValueKind.NULL,
            ValueKind.FUNCTION
        },
        ValueKind.values()
    );
  }
}