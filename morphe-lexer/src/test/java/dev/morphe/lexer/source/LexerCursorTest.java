package dev.morphe.lexer.source;

import dev.morphe.lexer.exception.argument.NullArgumentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LexerCursorTest {

  @Test
  void startsAtBeginning() {
    LexerCursor cursor = new LexerCursor("abc");
    assertEquals(new SourcePosition(0, 1, 1), cursor.position());
  }

  @Test
  void reportsCurrentCharacter() {
    LexerCursor cursor = new LexerCursor("abc");
    assertEquals('a', cursor.current());
  }

  @Test
  void advancesPosition() {
    LexerCursor cursor = new LexerCursor("abc");
    cursor.advance();
    assertEquals(new SourcePosition(1, 1, 2), cursor.position());
    assertEquals('b', cursor.current());
  }

  @Test
  void advancesToNextLine() {
    LexerCursor cursor = new LexerCursor("a\nb");
    cursor.advance();
    cursor.advance();
    assertEquals(new SourcePosition(2, 2, 1), cursor.position());
    assertEquals('b', cursor.current());
  }

  @Test
  void reportsEndOfSource() {
    LexerCursor cursor = new LexerCursor("a");
    assertFalse(cursor.isAtEnd());
    cursor.advance();
    assertTrue(cursor.isAtEnd());
  }

  @Test
  void reportsRemainingCharacters() {
    LexerCursor cursor = new LexerCursor("abc");
    assertEquals(3, cursor.remaining());
    cursor.advance();
    assertEquals(2, cursor.remaining());
  }

  @Test
  void startsWithMatchingText() {
    LexerCursor cursor = new LexerCursor("== value");
    assertTrue(cursor.startsWith("=="));
    assertTrue(cursor.startsWith("="));
  }

  @Test
  void startsWithNonMatchingText() {
    LexerCursor cursor = new LexerCursor("== value");
    assertFalse(cursor.startsWith(">"));
  }

  @Test
  void extractsSubstring() {
    LexerCursor cursor = new LexerCursor("hello");
    assertEquals("ell", cursor.substring(1, 4));
  }

  @Test
  void skipsWhitespace() {
    LexerCursor cursor = new LexerCursor(" \t\n  abc");
    cursor.skipWhitespace();
    assertEquals('a', cursor.current());
    assertEquals(new SourcePosition(5, 2, 3), cursor.position());
  }

  @Test
  void rejectsNullSource() {
    assertThrows(NullArgumentException.class, () -> new LexerCursor(null));
  }
}