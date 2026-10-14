package dev.morphe.parser;

import dev.morphe.ast.CompilationUnit;
import dev.morphe.ast.declaration.FunctionDeclaration;
import dev.morphe.parser.exception.argument.NullArgumentException;
import dev.morphe.lexer.kind.*;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

  private Token eof;

  @BeforeEach
  void setUp() {
    eof = token(SpecialTokenKind.EOF, "", 0, 1, 1, 1, 1, 1);
  }

  @Test
  void constructorRejectsNullTokens() {
    assertThrows(NullArgumentException.class, () -> new Parser(null));
  }

  @Test
  void parseReturnsEmptyCompilationUnitForEOF() {
    CompilationUnit unit = new Parser(List.of(eof)).parse();
    assertNotNull(unit);
    assertTrue(unit.declarations().isEmpty());
    assertNotNull(unit.range());
  }

  @Test
  void parseReturnsSingleFunctionDeclaration() {
    Token func = token(KeywordTokenKind.FUNC, "func", 0, 1, 1, 4, 1, 5);
    Token name = token(IdentifierTokenKind.IDENTIFIER, "greet", 5, 1, 6, 10, 1, 11);
    Token leftParen = token(PunctuationTokenKind.LEFT_PAREN, "(", 10, 1, 11, 11, 1, 12);
    Token rightParen = token(PunctuationTokenKind.RIGHT_PAREN, ")", 11, 1, 12, 12, 1, 13);
    Token colon = token(PunctuationTokenKind.COLON, ":", 12, 1, 13, 13, 1, 14);
    Token voidType = token(IdentifierTokenKind.IDENTIFIER, "Void", 14, 1, 15, 18, 1, 19);
    Token leftBrace = token(PunctuationTokenKind.LEFT_BRACE, "{", 19, 1, 20, 20, 1, 21);
    Token rightBrace = token(PunctuationTokenKind.RIGHT_BRACE, "}", 20, 1, 21, 21, 1, 22);
    CompilationUnit unit = new Parser(
        List.of(func, name, leftParen, rightParen, colon, voidType, leftBrace, rightBrace, eof)
    ).parse();
    assertEquals(1, unit.declarations().size());
    assertInstanceOf(FunctionDeclaration.class, unit.declarations().get(0));
    FunctionDeclaration declaration = (FunctionDeclaration) unit.declarations().get(0);
    assertEquals("greet", declaration.name());
    assertTrue(declaration.parameters().isEmpty());
    assertNotNull(declaration.returnType());
    assertNotNull(declaration.body());
  }

  @Test
  void parsePreservesDeclarationOrder() {
    Token firstFunc = token(KeywordTokenKind.FUNC, "func", 0, 1, 1, 4, 1, 5);
    Token firstName = token(IdentifierTokenKind.IDENTIFIER, "first", 5, 1, 6, 10, 1, 11);
    Token secondFunc = token(KeywordTokenKind.FUNC, "func", 21, 1, 22, 25, 1, 26);
    Token secondName = token(IdentifierTokenKind.IDENTIFIER, "second", 26, 1, 27, 32, 1, 33);
    List<Token> tokens = List.of(
        firstFunc,
        firstName,
        token(PunctuationTokenKind.LEFT_PAREN, "(", 10, 1, 11, 11, 1, 12),
        token(PunctuationTokenKind.RIGHT_PAREN, ")", 11, 1, 12, 12, 1, 13),
        token(PunctuationTokenKind.COLON, ":", 12, 1, 13, 13, 1, 14),
        token(IdentifierTokenKind.IDENTIFIER, "Void", 14, 1, 15, 18, 1, 19),
        token(PunctuationTokenKind.LEFT_BRACE, "{", 19, 1, 20, 20, 1, 21),
        token(PunctuationTokenKind.RIGHT_BRACE, "}", 20, 1, 21, 21, 1, 22),
        secondFunc,
        secondName,
        token(PunctuationTokenKind.LEFT_PAREN, "(", 33, 1, 34, 34, 1, 35),
        token(PunctuationTokenKind.RIGHT_PAREN, ")", 34, 1, 35, 35, 1, 36),
        token(PunctuationTokenKind.COLON, ":", 35, 1, 36, 36, 1, 37),
        token(IdentifierTokenKind.IDENTIFIER, "Void", 37, 1, 38, 41, 1, 42),
        token(PunctuationTokenKind.LEFT_BRACE, "{", 42, 1, 43, 43, 1, 44),
        token(PunctuationTokenKind.RIGHT_BRACE, "}", 43, 1, 44, 44, 1, 45),
        eof
    );
    CompilationUnit unit = new Parser(tokens).parse();
    assertEquals(2, unit.declarations().size());
    FunctionDeclaration first = (FunctionDeclaration) unit.declarations().get(0);
    FunctionDeclaration second = (FunctionDeclaration) unit.declarations().get(1);
    assertEquals("first", first.name());
    assertEquals("second", second.name());
  }

  @Test
  void parseRejectsUnsupportedTopLevelToken() {
    Token identifier = token(IdentifierTokenKind.IDENTIFIER, "invalid", 0, 1, 1, 7, 1, 8);
    UnexpectedTokenException exception = assertThrows(
        UnexpectedTokenException.class, () -> new Parser(List.of(identifier, eof)).parse()
    );
    assertNotNull(exception);
  }

  @Test
  void parseUsesSourceRangeFromFirstToLastToken() {
    Token func = token(KeywordTokenKind.FUNC, "func", 0, 1, 1, 4, 1, 5);
    Token name = token(IdentifierTokenKind.IDENTIFIER, "greet", 5, 1, 6, 10, 1, 11);
    Token leftParen = token(PunctuationTokenKind.LEFT_PAREN, "(", 10, 1, 11, 11, 1, 12);
    Token rightParen = token(PunctuationTokenKind.RIGHT_PAREN, ")", 11, 1, 12, 12, 1, 13);
    Token colon = token(PunctuationTokenKind.COLON, ":", 12, 1, 13, 13, 1, 14);
    Token voidType = token(IdentifierTokenKind.IDENTIFIER, "Void", 14, 1, 15, 18, 1, 19);
    Token leftBrace = token(PunctuationTokenKind.LEFT_BRACE, "{", 19, 1, 20, 20, 1, 21);
    Token rightBrace = token(PunctuationTokenKind.RIGHT_BRACE, "}", 20, 1, 21, 21, 1, 22);
    CompilationUnit unit = new Parser(
        List.of(
            func, name, leftParen, rightParen, colon, voidType, leftBrace, rightBrace, eof
        )
    ).parse();
    assertEquals(0, unit.range().start().offset());
    assertEquals(1, unit.range().start().line());
    assertEquals(1, unit.range().start().column());
    assertEquals(21, unit.range().end().offset());
    assertEquals(1, unit.range().end().line());
    assertEquals(22, unit.range().end().column());
  }
  private static Token token(
      ITokenKind kind, String lexeme, int startOffset, int startLine,
      int startColumn, int endOffset, int endLine, int endColumn
  ) {
    dev.morphe.lexer.source.SourcePosition start =
        new dev.morphe.lexer.source.SourcePosition(startOffset, startLine, startColumn);
    dev.morphe.lexer.source.SourcePosition end =
        new dev.morphe.lexer.source.SourcePosition(endOffset, endLine, endColumn);
    return new Token(kind, lexeme, new dev.morphe.lexer.source.SourceSpan(start, end));
  }
}