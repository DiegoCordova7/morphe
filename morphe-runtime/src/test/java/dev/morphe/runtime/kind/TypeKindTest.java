package dev.morphe.runtime.kind;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class TypeKindTest {

  @Test
  void containsExpectedKinds() {
    assertArrayEquals(
        new TypeKind[]{
            TypeKind.INTEGER,
            TypeKind.DOUBLE,
            TypeKind.BOOLEAN,
            TypeKind.STRING,
            TypeKind.ARRAY,
            TypeKind.COMPOSITE,
            TypeKind.NULL,
            TypeKind.FUNCTION
        },
        TypeKind.values()
    );
  }
}