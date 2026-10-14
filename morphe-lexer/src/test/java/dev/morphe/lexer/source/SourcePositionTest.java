package dev.morphe.lexer.source;

import dev.morphe.lexer.exception.source.InvalidSourcePositionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SourcePositionTest {

  @Test
  void storesPositionValues() {
    SourcePosition position = new SourcePosition(42, 3, 7);
    assertEquals(42, position.offset());
    assertEquals(3, position.line());
    assertEquals(7, position.column());
  }

  @Test
  void acceptsFirstSourcePosition() {
    SourcePosition position = new SourcePosition(0, 1, 1);
    assertEquals(0, position.offset());
    assertEquals(1, position.line());
    assertEquals(1, position.column());
  }

  @Test
  void rejectsNegativeOffset() {
    InvalidSourcePositionException exception = assertThrows(
        InvalidSourcePositionException.class, () -> new SourcePosition(-1, 1, 1)
    );
  }

  @Test
  void rejectsZeroLine() {
    InvalidSourcePositionException exception = assertThrows(
        InvalidSourcePositionException.class, () -> new SourcePosition(0, 0, 1)
    );
  }

  @Test
  void rejectsNegativeLine() {
    InvalidSourcePositionException exception = assertThrows(
        InvalidSourcePositionException.class, () -> new SourcePosition(0, -1, 1)
    );
  }

  @Test
  void rejectsZeroColumn() {
    InvalidSourcePositionException exception = assertThrows(
        InvalidSourcePositionException.class, () -> new SourcePosition(0, 1, 0)
    );
  }

  @Test
  void rejectsNegativeColumn() {
    InvalidSourcePositionException exception = assertThrows(
        InvalidSourcePositionException.class, () -> new SourcePosition(0, 1, -1)
    );
  }

  @Test
  void equalPositionsAreEqual() {
    SourcePosition first = new SourcePosition(10, 2, 5);
    SourcePosition second = new SourcePosition(10, 2, 5);
    assertEquals(first, second);
  }

  @Test
  void equalPositionsHaveEqualHashCodes() {
    SourcePosition first = new SourcePosition(10, 2, 5);
    SourcePosition second = new SourcePosition(10, 2, 5);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void differentOffsetsAreNotEqual() {
    SourcePosition first = new SourcePosition(10, 2, 5);
    SourcePosition second = new SourcePosition(11, 2, 5);
    assertNotEquals(first, second);
  }

  @Test
  void differentLinesAreNotEqual() {
    SourcePosition first = new SourcePosition(10, 2, 5);
    SourcePosition second = new SourcePosition(10, 3, 5);
    assertNotEquals(first, second);
  }

  @Test
  void differentColumnsAreNotEqual() {
    SourcePosition first = new SourcePosition(10, 2, 5);
    SourcePosition second = new SourcePosition(10, 2, 6);
    assertNotEquals(first, second);
  }
}