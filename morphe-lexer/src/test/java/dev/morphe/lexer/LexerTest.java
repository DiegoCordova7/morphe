package dev.morphe.lexer;

import dev.morphe.lexer.exception.argument.NullArgumentException;
import dev.morphe.lexer.kind.*;
import dev.morphe.lexer.source.SourcePosition;
import dev.morphe.lexer.source.SourceSpan;
import dev.morphe.lexer.token.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

  @Test
  void acceptsEmptySource() {
    assertDoesNotThrow(() -> new Lexer(""));
  }

  @Test
  void acceptsNonEmptySource() {
    assertDoesNotThrow(() -> new Lexer("Integer x = 10;"));
  }

  @Test
  void rejectsNullSource() {
    assertThrows(NullArgumentException.class, () -> new Lexer(null));
  }

  @Test
  void tokenizesEmptySourceAsEof() {
    Lexer lexer = new Lexer("");
    List<Token> tokens = lexer.tokenize();
    assertEquals(1, tokens.size());
    Token eof = tokens.get(0);
    assertEquals(SpecialTokenKind.EOF, eof.kind());
    assertEquals("", eof.lexeme());
    assertEquals(
        new SourceSpan(
            new SourcePosition(0, 1, 1),
            new SourcePosition(0, 1, 1)
        ),
        eof.span()
    );
  }

  @Test
  void tokenizesWhitespaceOnlySourceAsEof() {
    Lexer lexer = new Lexer(" \t\r\n");
    List<Token> tokens = lexer.tokenize();
    assertEquals(1, tokens.size());
    Token eof = tokens.get(0);
    assertEquals(SpecialTokenKind.EOF, eof.kind());
    assertEquals("", eof.lexeme());
    assertEquals(
        new SourceSpan(
            new SourcePosition(4, 2, 1),
            new SourcePosition(4, 2, 1)
        ),
        eof.span()
    );
  }

  @Test
  void scansIdentifier() {
    Lexer lexer = new Lexer("counter");
    List<Token> tokens = lexer.tokenize();
    assertEquals(2, tokens.size());
    Token identifier = tokens.get(0);
    Token eof = tokens.get(1);
    assertEquals(IdentifierTokenKind.IDENTIFIER, identifier.kind());
    assertEquals("counter", identifier.lexeme());
    assertEquals(SpecialTokenKind.EOF, eof.kind());
  }

  @Test
  void scansIdentifierWithDigits() {
    Lexer lexer = new Lexer("value42");
    List<Token> tokens = lexer.tokenize();
    assertEquals(IdentifierTokenKind.IDENTIFIER, tokens.get(0).kind());
    assertEquals("value42", tokens.get(0).lexeme());
    assertEquals(SpecialTokenKind.EOF, tokens.get(1).kind());
  }

  @Test
  void scansIdentifierWithUnderscores() {
    Lexer lexer = new Lexer("my_value_2");
    List<Token> tokens = lexer.tokenize();
    assertEquals(IdentifierTokenKind.IDENTIFIER, tokens.get(0).kind());
    assertEquals("my_value_2", tokens.get(0).lexeme());
    assertEquals(SpecialTokenKind.EOF, tokens.get(1).kind());
  }

  @ParameterizedTest
  @MethodSource("knownLexemes")
  void scansKnownLexemes(String source, ITokenKind expectedKind) {
    Lexer lexer = new Lexer(source);
    List<Token> tokens = lexer.tokenize();
    assertEquals(2, tokens.size());
    Token token = tokens.get(0);
    assertEquals(expectedKind, token.kind());
    assertEquals(source, token.lexeme());
    assertEquals(SpecialTokenKind.EOF, tokens.get(1).kind());
  }

  static Stream<Arguments> knownLexemes() {
    return Stream.of(
        // Types
        Arguments.of("Integer", TypeTokenKind.INTEGER),
        Arguments.of("Double", TypeTokenKind.DOUBLE),
        Arguments.of("Boolean", TypeTokenKind.BOOLEAN),
        Arguments.of("String", TypeTokenKind.STRING),
        Arguments.of("Void", TypeTokenKind.VOID_TYPE),

        // Keywords
        Arguments.of("if", KeywordTokenKind.IF),
        Arguments.of("else", KeywordTokenKind.ELSE),
        Arguments.of("match", KeywordTokenKind.MATCH),
        Arguments.of("do", KeywordTokenKind.DO),
        Arguments.of("while", KeywordTokenKind.WHILE),
        Arguments.of("for", KeywordTokenKind.FOR),
        Arguments.of("func", KeywordTokenKind.FUNC),
        Arguments.of("break", KeywordTokenKind.BREAK),
        Arguments.of("continue", KeywordTokenKind.CONTINUE),
        Arguments.of("return", KeywordTokenKind.RETURN),
        Arguments.of("attempt", KeywordTokenKind.ATTEMPT),
        Arguments.of("onError", KeywordTokenKind.ON_ERROR),
        Arguments.of("type", KeywordTokenKind.TYPE),
        Arguments.of("new", KeywordTokenKind.NEW),

        // Literals with fixed lexemes
        Arguments.of("null", LiteralTokenKind.NULL),

        // Match
        Arguments.of("|", MatchTokenKind.OR),
        Arguments.of("=>", MatchTokenKind.ARROW),
        Arguments.of("..", MatchTokenKind.RANGE),
        Arguments.of("...", MatchTokenKind.ELLIPSIS),
        Arguments.of("_", MatchTokenKind.WILDCARD),

        // Operators
        Arguments.of("+", OperatorTokenKind.PLUS),
        Arguments.of("-", OperatorTokenKind.MINUS),
        Arguments.of("*", OperatorTokenKind.MULTIPLY),
        Arguments.of("/", OperatorTokenKind.DIVIDE),
        Arguments.of("%", OperatorTokenKind.MODULO),
        Arguments.of("=", OperatorTokenKind.ASSIGN),
        Arguments.of("+=", OperatorTokenKind.PLUS_ASSIGN),
        Arguments.of("-=", OperatorTokenKind.MINUS_ASSIGN),
        Arguments.of("*=", OperatorTokenKind.MULTIPLY_ASSIGN),
        Arguments.of("/=", OperatorTokenKind.DIVIDE_ASSIGN),
        Arguments.of("++", OperatorTokenKind.INCREMENT),
        Arguments.of("--", OperatorTokenKind.DECREMENT),
        Arguments.of("==", OperatorTokenKind.EQUAL),
        Arguments.of("!=", OperatorTokenKind.NOT_EQUAL),
        Arguments.of("~=", OperatorTokenKind.STRUCT_MATCH),
        Arguments.of(">", OperatorTokenKind.GREATER),
        Arguments.of("<", OperatorTokenKind.LESS),
        Arguments.of(">=", OperatorTokenKind.GREATER_EQUAL),
        Arguments.of("<=", OperatorTokenKind.LESS_EQUAL),
        Arguments.of("&&", OperatorTokenKind.AND),
        Arguments.of("||", OperatorTokenKind.OR),
        Arguments.of("!", OperatorTokenKind.NOT),
        Arguments.of("|>", OperatorTokenKind.PIPELINE),
        Arguments.of("->", OperatorTokenKind.ARROW),
        Arguments.of("#", OperatorTokenKind.IMMUTABLE),

        // Punctuation
        Arguments.of("(", PunctuationTokenKind.LEFT_PAREN),
        Arguments.of(")", PunctuationTokenKind.RIGHT_PAREN),
        Arguments.of("{", PunctuationTokenKind.LEFT_BRACE),
        Arguments.of("}", PunctuationTokenKind.RIGHT_BRACE),
        Arguments.of("[", PunctuationTokenKind.LEFT_BRACKET),
        Arguments.of("]", PunctuationTokenKind.RIGHT_BRACKET),
        Arguments.of(";", PunctuationTokenKind.SEMICOLON),
        Arguments.of(",", PunctuationTokenKind.COMMA)
    );
  }

  @Test
  void scansMultipleIdentifiers() {
    Lexer lexer = new Lexer("foo bar");
    List<Token> tokens = lexer.tokenize();
    assertEquals(3, tokens.size());
    Token first = tokens.get(0);
    Token second = tokens.get(1);
    Token eof = tokens.get(2);
    assertEquals(IdentifierTokenKind.IDENTIFIER, first.kind());
    assertEquals("foo", first.lexeme());
    assertEquals(IdentifierTokenKind.IDENTIFIER, second.kind());
    assertEquals("bar", second.lexeme());
    assertEquals(SpecialTokenKind.EOF, eof.kind());
  }

  @Test
  void scansIdentifiersSeparatedByNewline() {
    Lexer lexer = new Lexer("foo\nbar");
    List<Token> tokens = lexer.tokenize();
    assertEquals(3, tokens.size());
    Token first = tokens.get(0);
    Token second = tokens.get(1);
    Token eof = tokens.get(2);
    assertEquals(IdentifierTokenKind.IDENTIFIER, first.kind());
    assertEquals("foo", first.lexeme());
    assertEquals(IdentifierTokenKind.IDENTIFIER, second.kind());
    assertEquals("bar", second.lexeme());
    assertEquals(SpecialTokenKind.EOF, eof.kind());
  }

  @Test
  void scansIdentifierWithDigitsAndUnderscores() {
    Lexer lexer = new Lexer("value_123_test");
    List<Token> tokens = lexer.tokenize();
    assertEquals(IdentifierTokenKind.IDENTIFIER, tokens.get(0).kind());
    assertEquals("value_123_test", tokens.get(0).lexeme());
    assertEquals(SpecialTokenKind.EOF, tokens.get(1).kind());
  }

  @Test
  void preservesIdentifierSpan() {
    Lexer lexer = new Lexer("counter");
    List<Token> tokens = lexer.tokenize();
    Token token = tokens.get(0);
    assertEquals(0, token.span().start().offset());
    assertEquals(1, token.span().start().line());
    assertEquals(1, token.span().start().column());
    assertEquals(7, token.span().end().offset());
    assertEquals(1, token.span().end().line());
    assertEquals(8, token.span().end().column());
  }

  @Test
  void preservesSpanAfterWhitespace() {
    Lexer lexer = new Lexer(" counter");
    List<Token> tokens = lexer.tokenize();
    Token token = tokens.get(0);
    assertEquals(1, token.span().start().offset());
    assertEquals(1, token.span().start().line());
    assertEquals(2, token.span().start().column());
    assertEquals(8, token.span().end().offset());
    assertEquals(1, token.span().end().line());
    assertEquals(9, token.span().end().column());
  }

  @Test
  void preservesSpanAfterNewline() {
    Lexer lexer = new Lexer("\n\ncounter");
    List<Token> tokens = lexer.tokenize();
    Token token = tokens.get(0);
    assertEquals(2, token.span().start().offset());
    assertEquals(3, token.span().start().line());
    assertEquals(1, token.span().start().column());
    assertEquals(9, token.span().end().offset());
    assertEquals(3, token.span().end().line());
    assertEquals(8, token.span().end().column());
  }

  @Test
  void preservesSpanAcrossMixedWhitespace() {
    Lexer lexer = new Lexer(" \t\n counter");
    List<Token> tokens = lexer.tokenize();
    Token token = tokens.get(0);
    assertEquals(4, token.span().start().offset());
    assertEquals(2, token.span().start().line());
    assertEquals(2, token.span().start().column());
    assertEquals(11, token.span().end().offset());
    assertEquals(2, token.span().end().line());
    assertEquals(9, token.span().end().column());
  }
}