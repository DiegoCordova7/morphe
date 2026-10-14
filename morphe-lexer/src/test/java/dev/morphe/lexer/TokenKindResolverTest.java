package dev.morphe.lexer;

import dev.morphe.lexer.kind.IdentifierTokenKind;
import dev.morphe.lexer.kind.KeywordTokenKind;
import dev.morphe.lexer.source.LexerCursor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenKindResolverTest {

  @Test
  void resolvesKeyword() {
    TokenKindResolver resolver = new TokenKindResolver();
    assertEquals(KeywordTokenKind.IF, resolver.resolve("if"));
  }

  @Test
  void resolvesUnknownLexemeAsIdentifier() {
    TokenKindResolver resolver = new TokenKindResolver();
    assertEquals(IdentifierTokenKind.IDENTIFIER, resolver.resolve("hello"));
  }

  @Test
  void recognizesSymbolStart() {
    TokenKindResolver resolver = new TokenKindResolver();
    assertTrue(resolver.canStartSymbol('+'));
    assertTrue(resolver.canStartSymbol('('));
    assertTrue(resolver.canStartSymbol('|'));
  }

  @Test
  void resolvesLongestSymbolMatch() {
    TokenKindResolver resolver = new TokenKindResolver();
    LexerCursor cursor = new LexerCursor(">=");
    assertEquals(">=", resolver.resolveSymbol(cursor));
  }

  @Test
  void prefersLongestSymbolMatch() {
    TokenKindResolver resolver = new TokenKindResolver();
    LexerCursor cursor = new LexerCursor("==");
    assertEquals("==", resolver.resolveSymbol(cursor));
  }

  @Test
  void resolvesMatchSymbols() {
    TokenKindResolver resolver = new TokenKindResolver();
    LexerCursor cursor = new LexerCursor("=>");
    assertEquals("=>", resolver.resolveSymbol(cursor));
  }
}