package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.pattern.LiteralPattern;
import dev.morphe.ast.pattern.RangePattern;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatternParserTest extends AParserTest {

  @Test
  void parsesLiteralPattern() {
    IPattern pattern = new PatternParser(
        cursor("42")
    ).parse();
    assertInstanceOf(LiteralPattern.class, pattern);
  }

  @Test
  void parsesRangePattern() {
    IPattern pattern = new PatternParser(
        cursor("1..10")
    ).parse();
    assertInstanceOf(RangePattern.class, pattern);
  }

  @Test
  void parsesOrPattern() {
    IPattern pattern = new PatternParser(
        cursor("1 | 2")
    ).parse();
    assertNotNull(pattern);
  }

  @Test
  void parsesComplexPattern() {
    IPattern pattern = new PatternParser(
        cursor("1..10 | 20 | 30..40")
    ).parse();
    assertNotNull(pattern);
  }
}