package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.pattern.LiteralPattern;
import dev.morphe.ast.pattern.RangePattern;
import dev.morphe.ast.pattern.VariablePattern;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RangePatternParserTest extends AParserTest {

  @Test
  void parsesLiteralRange() {
    IPattern pattern = new RangePatternParser(
        cursor("1..10")
    ).parse();
    assertInstanceOf(RangePattern.class, pattern);
    RangePattern range = (RangePattern) pattern;
    assertInstanceOf(LiteralPattern.class, range.start());
    assertEquals(1, ((LiteralPattern) range.start()).value());
    assertInstanceOf(LiteralPattern.class, range.end());
    assertEquals(10, ((LiteralPattern) range.end()).value());
  }

  @Test
  void parsesVariableRange() {
    IPattern pattern = new RangePatternParser(
        cursor("start..end")
    ).parse();
    assertInstanceOf(RangePattern.class, pattern);
    RangePattern range = (RangePattern) pattern;
    assertInstanceOf(VariablePattern.class, range.start());
    assertEquals("start", ((VariablePattern) range.start()).name());
    assertInstanceOf(VariablePattern.class, range.end());
    assertEquals("end", ((VariablePattern) range.end()).name());
  }

  @Test
  void parsesMixedPatternRange() {
    IPattern pattern = new RangePatternParser(
        cursor("1..end")
    ).parse();
    assertInstanceOf(RangePattern.class, pattern);
    RangePattern range = (RangePattern) pattern;
    assertInstanceOf(LiteralPattern.class, range.start());
    assertEquals(1, ((LiteralPattern) range.start()).value());
    assertInstanceOf(VariablePattern.class, range.end());
    assertEquals("end", ((VariablePattern) range.end()).name());
  }

  @Test
  void preservesSourceRange() {
    IPattern pattern = new RangePatternParser(
        cursor("1..10")
    ).parse();
    SourceRange range = pattern.range();
    assertEquals(0, range.start().offset());
    assertEquals(5, range.end().offset());
  }

  @Test
  void returnsPrimaryPatternWhenRangeOperatorIsAbsent() {
    IPattern pattern = new RangePatternParser(
        cursor("42")
    ).parse();
    assertInstanceOf(LiteralPattern.class, pattern);
    assertEquals(42, ((LiteralPattern) pattern).value());
  }

  @Test
  void rejectsMissingRangeEnd() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new RangePatternParser(
            cursor("1..")
        ).parse()
    );
  }
}