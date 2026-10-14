package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.pattern.VariablePattern;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VariablePatternParserTest extends AParserTest {

  @Test
  void parsesVariablePattern() {
    IPattern pattern = new VariablePatternParser(
        cursor("value")
    ).parse();
    assertInstanceOf(VariablePattern.class, pattern);
    assertEquals("value", ((VariablePattern) pattern).name());
  }

  @Test
  void preservesSourceRange() {
    IPattern pattern = new VariablePatternParser(
        cursor("value")
    ).parse();
    SourceRange range = pattern.range();
    assertEquals(0, range.start().offset());
    assertEquals(5, range.end().offset());
  }
}