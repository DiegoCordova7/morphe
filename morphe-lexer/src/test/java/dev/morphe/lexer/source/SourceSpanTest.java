package dev.morphe.lexer.source;

import dev.morphe.lexer.exception.argument.NullArgumentException;
import dev.morphe.lexer.exception.source.InvalidSourceSpanException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SourceSpanTest {

  @Test
  void storesPositions() {
    SourcePosition start = new SourcePosition(5, 1, 6);
    SourcePosition end = new SourcePosition(12, 1, 13);
    SourceSpan span = new SourceSpan(start, end);
    assertSame(start, span.start());
    assertSame(end, span.end());
  }

  @Test
  void acceptsEmptySpan() {
    SourcePosition position = new SourcePosition(5, 1, 6);
    SourceSpan span = new SourceSpan(position, position);
    assertSame(position, span.start());
    assertSame(position, span.end());
  }

  @Test
  void acceptsSpanWithDifferentPositions() {
    SourcePosition start = new SourcePosition(5, 1, 6);
    SourcePosition end = new SourcePosition(10, 1, 11);
    SourceSpan span = new SourceSpan(start, end);
    assertEquals(start, span.start());
    assertEquals(end, span.end());
  }

  @Test
  void rejectsNullStart() {
    SourcePosition end = new SourcePosition(10, 1, 11);
    assertThrows(NullArgumentException.class, () -> new SourceSpan(null, end));
  }

  @Test
  void rejectsNullEnd() {
    SourcePosition start = new SourcePosition(5, 1, 6);
    assertThrows(NullArgumentException.class, () -> new SourceSpan(start, null));
  }

  @Test
  void rejectsEndBeforeStart() {
    SourcePosition start = new SourcePosition(10, 1, 11);
    SourcePosition end = new SourcePosition(5, 1, 6);
    assertThrows(InvalidSourceSpanException.class, () -> new SourceSpan(start, end));
  }

  @Test
  void acceptsEndWithSameOffset() {
    SourcePosition start = new SourcePosition(5, 1, 6);
    SourcePosition end = new SourcePosition(5, 1, 6);
    SourceSpan span = new SourceSpan(start, end);
    assertEquals(start, span.start());
    assertEquals(end, span.end());
  }

  @Test
  void equalSpansAreEqual() {
    SourceSpan first = new SourceSpan(new SourcePosition(5, 1, 6), new SourcePosition(10, 1, 11));
    SourceSpan second = new SourceSpan(new SourcePosition(5, 1, 6), new SourcePosition(10, 1, 11));
    assertEquals(first, second);
  }

  @Test
  void equalSpansHaveEqualHashCodes() {
    SourceSpan first = new SourceSpan(new SourcePosition(5, 1, 6), new SourcePosition(10, 1, 11));
    SourceSpan second = new SourceSpan(new SourcePosition(5, 1, 6), new SourcePosition(10, 1, 11));
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void spansWithDifferentStartPositionsAreNotEqual() {
    SourceSpan first = new SourceSpan(new SourcePosition(5, 1, 6), new SourcePosition(10, 1, 11));
    SourceSpan second = new SourceSpan(new SourcePosition(6, 1, 7), new SourcePosition(10, 1, 11));
    assertNotEquals(first, second);
  }

  @Test
  void spansWithDifferentEndPositionsAreNotEqual() {
    SourceSpan first = new SourceSpan(new SourcePosition(5, 1, 6), new SourcePosition(10, 1, 11));
    SourceSpan second = new SourceSpan(new SourcePosition(5, 1, 6), new SourcePosition(11, 1, 12));
    assertNotEquals(first, second);
  }
}
