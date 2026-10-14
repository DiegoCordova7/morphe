package dev.morphe.parser.cursor;

import dev.morphe.lexer.kind.IdentifierTokenKind;
import dev.morphe.lexer.kind.SpecialTokenKind;
import dev.morphe.lexer.source.*;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.exception.argument.InvalidArgumentException;
import dev.morphe.parser.exception.argument.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TokenCursorTest {

  private Token first;
  private Token second;
  private Token eof;
  private TokenCursor cursor;

  @BeforeEach
  void setUp() {
    SourcePosition start = new SourcePosition(0, 1, 1);
    SourcePosition end = new SourcePosition(5, 1, 6);
    SourceSpan span = new SourceSpan(start, end);
    first = new Token(IdentifierTokenKind.IDENTIFIER, "first", span);
    second = new Token(IdentifierTokenKind.IDENTIFIER, "second", span);
    eof = new Token(SpecialTokenKind.EOF, "", span);
    cursor = new TokenCursor(List.of(first, second, eof));
  }

  @Test
  void constructorRejectsNullTokens() {
    assertThrows(NullArgumentException.class, () -> new TokenCursor(null));
  }

  @Test
  void constructorRejectsEmptyTokens() {
    InvalidArgumentException exception = assertThrows(InvalidArgumentException.class, () -> new TokenCursor(List.of()));
    assertEquals("Invalid argument 'tokens': must contain an EOF token", exception.getMessage());
  }

  @Test
  void constructorRejectsTokensWithoutEOF() {
    InvalidArgumentException exception = assertThrows(
        InvalidArgumentException.class,
        () -> new TokenCursor(List.of(first, second))
    );
    assertEquals("Invalid argument 'tokens': must end with an EOF token", exception.getMessage());
  }

  @Test
  void currentReturnsCurrentToken() {
    assertSame(first, cursor.current());
  }

  @Test
  void previousReturnsCurrentTokenAtBeginning() {
    assertSame(first, cursor.previous());
  }

  @Test
  void previousReturnsPreviouslyConsumedToken() {
    cursor.advance();
    assertSame(first, cursor.previous());
  }

  @Test
  void previousReturnsTokenBeforeEOF() {
    cursor.advance();
    cursor.advance();
    assertSame(second, cursor.previous());
  }

  @Test
  void constructorCopiesTokenList() {
    List<Token> tokens = new ArrayList<>(
        List.of(first, second, eof)
    );
    TokenCursor cursor = new TokenCursor(tokens);
    tokens.clear();
    assertSame(first, cursor.current());
    assertSame(second, cursor.peek(1));
    assertSame(eof, cursor.peek(2));
  }

  @Test
  void peekReturnsTokenAtOffsetWithoutAdvancing() {
    assertSame(first, cursor.peek(0));
    assertSame(second, cursor.peek(1));
    assertSame(eof, cursor.peek(2));
    assertEquals(0, cursor.position());
  }

  @Test
  void peekBeyondEndReturnsEOF() {
    assertSame(eof, cursor.peek(100));
    assertEquals(0, cursor.position());
  }

  @Test
  void peekRejectsNegativeOffset() {
    InvalidArgumentException exception = assertThrows(InvalidArgumentException.class, () -> cursor.peek(-1));
    assertEquals("Invalid argument 'offset': must not be negative", exception.getMessage());
  }

  @Test
  void advanceReturnsCurrentTokenAndMovesCursor() {
    assertSame(first, cursor.advance());
    assertEquals(1, cursor.position());
    assertSame(second, cursor.current());
  }

  @Test
  void advanceDoesNotMovePastEOF() {
    cursor.advance();
    cursor.advance();
    assertSame(eof, cursor.advance());
    assertEquals(2, cursor.position());
    assertTrue(cursor.isAtEnd());
  }

  @Test
  void checkReturnsTrueWhenKindMatches() {
    assertTrue(cursor.check(IdentifierTokenKind.IDENTIFIER));
  }

  @Test
  void checkReturnsFalseWhenKindDoesNotMatch() {
    assertFalse(cursor.check(SpecialTokenKind.EOF));
  }

  @Test
  void checkRejectsNullKind() {
    assertThrows(NullArgumentException.class, () -> cursor.check(null));
  }

  @Test
  void matchConsumesMatchingToken() {
    assertTrue(cursor.match(IdentifierTokenKind.IDENTIFIER));
    assertEquals(1, cursor.position());
    assertSame(second, cursor.current());
  }

  @Test
  void matchDoesNotConsumeNonMatchingToken() {
    assertFalse(cursor.match(SpecialTokenKind.EOF));
    assertEquals(0, cursor.position());
    assertSame(first, cursor.current());
  }

  @Test
  void matchRejectsNullKind() {
    assertThrows(NullArgumentException.class, () -> cursor.match(null));
  }

  @Test
  void isAtEndReturnsFalseBeforeEOF() {
    assertFalse(cursor.isAtEnd());
  }

  @Test
  void isAtEndReturnsTrueAtEOF() {
    cursor.advance();
    cursor.advance();
    assertTrue(cursor.isAtEnd());
  }

  @Test
  void positionReturnsCurrentTokenIndex() {
    assertEquals(0, cursor.position());
    cursor.advance();
    assertEquals(1, cursor.position());
    cursor.advance();
    assertEquals(2, cursor.position());
  }
}