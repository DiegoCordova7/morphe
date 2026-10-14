package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.*;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrPatternParserTest extends AParserTest {

  @Test
  void returnsSinglePatternWhenNoAlternativeOperatorIsPresent() {
    IPattern pattern = new OrPatternParser(
        cursor("42")
    ).parse();
    assertInstanceOf(LiteralPattern.class, pattern);
    assertEquals(42, ((LiteralPattern) pattern).value());
  }

  @Test
  void parsesTwoAlternatives() {
    IPattern pattern = new OrPatternParser(
        cursor("1 | 2")
    ).parse();
    assertInstanceOf(OrPattern.class, pattern);
    OrPattern or = (OrPattern) pattern;
    assertEquals(2, or.patterns().size());
    assertInstanceOf(LiteralPattern.class, or.patterns().get(0));
    assertEquals(1, ((LiteralPattern) or.patterns().get(0)).value());
    assertInstanceOf(LiteralPattern.class, or.patterns().get(1));
    assertEquals(2, ((LiteralPattern) or.patterns().get(1)).value());
  }

  @Test
  void parsesMultipleAlternatives() {
    IPattern pattern = new OrPatternParser(
        cursor("1 | 2 | 3")
    ).parse();
    assertInstanceOf(OrPattern.class, pattern);
    OrPattern or = (OrPattern) pattern;
    assertEquals(3, or.patterns().size());
    assertEquals(1, ((LiteralPattern) or.patterns().get(0)).value());
    assertEquals(2, ((LiteralPattern) or.patterns().get(1)).value());
    assertEquals(3, ((LiteralPattern) or.patterns().get(2)).value());
  }

  @Test
  void parsesDifferentPatternTypes() {
    IPattern pattern = new OrPatternParser(
        cursor("1 | _")
    ).parse();
    assertInstanceOf(OrPattern.class, pattern);
    OrPattern or = (OrPattern) pattern;
    assertEquals(2, or.patterns().size());
    assertInstanceOf(LiteralPattern.class, or.patterns().get(0));
    assertInstanceOf(DefaultPattern.class, or.patterns().get(1));
  }

  @Test
  void preservesSourceRange() {
    IPattern pattern = new OrPatternParser(
        cursor("1 | 2 | 3")
    ).parse();
    SourceRange range = pattern.range();
    assertEquals(0, range.start().offset());
    assertEquals(9, range.end().offset());
  }

  @Test
  void parsesRangeAsAlternative() {
    IPattern pattern = new OrPatternParser(
        cursor("1..10 | 20")
    ).parse();
    assertInstanceOf(OrPattern.class, pattern);
    OrPattern or = (OrPattern) pattern;
    assertEquals(2, or.patterns().size());
    assertInstanceOf(RangePattern.class, or.patterns().get(0));
    RangePattern range = (RangePattern) or.patterns().get(0);
    assertInstanceOf(LiteralPattern.class, range.start());
    assertEquals(1, ((LiteralPattern) range.start()).value());
    assertInstanceOf(LiteralPattern.class, range.end());
    assertEquals(10, ((LiteralPattern) range.end()).value());
    assertInstanceOf(LiteralPattern.class, or.patterns().get(1));
    assertEquals(20, ((LiteralPattern) or.patterns().get(1)).value());
  }

  @Test
  void parsesMultipleRangesAsAlternatives() {
    IPattern pattern = new OrPatternParser(
        cursor("1..10 | 20..30")
    ).parse();
    assertInstanceOf(OrPattern.class, pattern);
    OrPattern or = (OrPattern) pattern;
    assertEquals(2, or.patterns().size());
    assertInstanceOf(RangePattern.class, or.patterns().get(0));
    assertInstanceOf(RangePattern.class, or.patterns().get(1));
  }
}