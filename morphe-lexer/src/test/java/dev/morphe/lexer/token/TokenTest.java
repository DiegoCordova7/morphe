package dev.morphe.lexer.token;

import dev.morphe.lexer.exception.argument.NullArgumentException;
import dev.morphe.lexer.kind.ITokenKind;
import dev.morphe.lexer.source.SourcePosition;
import dev.morphe.lexer.source.SourceSpan;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenTest {

  private static final ITokenKind KIND = TestTokenKind.IDENTIFIER;

  private static final SourceSpan SPAN = new SourceSpan(
      new SourcePosition(0, 1, 1),
      new SourcePosition(4, 1, 5)
  );

  @Test
  void storesTokenValues() {
    Token token = new Token(KIND, "name", SPAN);
    assertSame(KIND, token.kind());
    assertEquals("name", token.lexeme());
    assertSame(SPAN, token.span());
  }

  @Test
  void rejectsNullKind() {
    assertThrows(NullArgumentException.class, () -> new Token(null, "name", SPAN));
  }

  @Test
  void rejectsNullLexeme() {
    assertThrows(NullArgumentException.class, () -> new Token(KIND, null, SPAN));
  }

  @Test
  void rejectsNullSpan() {
    assertThrows(NullArgumentException.class, () -> new Token(KIND, "name", null));
  }

  @Test
  void equalTokensAreEqual() {
    Token first = new Token(
        KIND,
        "name",
        new SourceSpan(
            new SourcePosition(0, 1, 1),
            new SourcePosition(4, 1, 5)
        )
    );
    Token second = new Token(
        KIND,
        "name",
        new SourceSpan(
            new SourcePosition(0, 1, 1),
            new SourcePosition(4, 1, 5)
        )
    );
    assertEquals(first, second);
  }

  @Test
  void equalTokensHaveEqualHashCodes() {
    Token first = new Token(
        KIND,
        "name",
        new SourceSpan(
            new SourcePosition(0, 1, 1),
            new SourcePosition(4, 1, 5)
        )
    );
    Token second = new Token(
        KIND,
        "name",
        new SourceSpan(
            new SourcePosition(0, 1, 1),
            new SourcePosition(4, 1, 5)
        )
    );
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void tokensWithDifferentKindsAreNotEqual() {
    Token first = new Token(
        TestTokenKind.IDENTIFIER,
        "name",
        SPAN
    );
    Token second = new Token(
        TestTokenKind.KEYWORD,
        "name",
        SPAN
    );
    assertNotEquals(first, second);
  }

  @Test
  void tokensWithDifferentLexemesAreNotEqual() {
    Token first = new Token(KIND, "name", SPAN);
    Token second = new Token(KIND, "other", SPAN);
    assertNotEquals(first, second);
  }

  @Test
  void tokensWithDifferentSpansAreNotEqual() {
    Token first = new Token(
        KIND,
        "name",
        new SourceSpan(
            new SourcePosition(0, 1, 1),
            new SourcePosition(4, 1, 5)
        )
    );
    Token second = new Token(
        KIND,
        "name",
        new SourceSpan(
            new SourcePosition(5, 1, 6),
            new SourcePosition(9, 1, 10)
        )
    );

    assertNotEquals(first, second);
  }

  @Test
  void toStringContainsTokenInformation() {
    Token token = new Token(KIND, "name", SPAN);
    String result = token.toString();
    assertTrue(result.contains("kind="));
    assertTrue(result.contains("lexeme='name'"));
    assertTrue(result.contains("span="));
  }

  private enum TestTokenKind implements ITokenKind {

    IDENTIFIER("name"),
    KEYWORD("if");

    private final String lexeme;

    TestTokenKind(String lexeme) {
      this.lexeme = lexeme;
    }

    @Override
    public String lexeme() {
      return lexeme;
    }
  }
}
