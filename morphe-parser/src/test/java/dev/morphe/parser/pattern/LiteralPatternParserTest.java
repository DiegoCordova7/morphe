package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.pattern.LiteralPattern;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LiteralPatternParserTest extends AParserTest {

  @Test
  void parsesIntegerLiteral() {
    IPattern pattern = new LiteralPatternParser(
        cursor("42")
    ).parse();
    assertInstanceOf(LiteralPattern.class, pattern);
    assertEquals(42, ((LiteralPattern) pattern).value());
  }

  @Test
  void parsesDoubleLiteral() {
    IPattern pattern = new LiteralPatternParser(
        cursor("3.14")
    ).parse();
    assertInstanceOf(LiteralPattern.class, pattern);
    assertEquals(3.14, ((LiteralPattern) pattern).value());
  }

  @Test
  void parsesStringLiteral() {
    IPattern pattern = new LiteralPatternParser(
        cursor("\"hello\"")
    ).parse();
    assertInstanceOf(LiteralPattern.class, pattern);
    assertEquals("hello", ((LiteralPattern) pattern).value());
  }

  @Test
  void parsesBooleanLiteral() {
    IPattern pattern = new LiteralPatternParser(
        cursor("true")
    ).parse();
    assertInstanceOf(LiteralPattern.class, pattern);
    assertEquals(true, ((LiteralPattern) pattern).value());
  }

  @Test
  void preservesSourceRange() {
    IPattern pattern = new LiteralPatternParser(
        cursor("42")
    ).parse();
    assertEquals(0, pattern.range().start().offset());
    assertEquals(2, pattern.range().end().offset());
  }
}