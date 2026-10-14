package dev.morphe.parser;

import dev.morphe.lexer.Lexer;
import dev.morphe.parser.cursor.TokenCursor;

public abstract class AParserTest {

  protected TokenCursor cursor(String source) {
    return new TokenCursor(new Lexer(source).tokenize());
  }
}