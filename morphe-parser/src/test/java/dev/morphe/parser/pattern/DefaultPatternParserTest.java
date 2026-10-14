package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.DefaultPattern;
import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultPatternParserTest extends AParserTest {

  @Test
  void parsesDefaultPattern() {
    IPattern pattern = new DefaultPatternParser(
        cursor("_")
    ).parse();
    assertInstanceOf(DefaultPattern.class, pattern);
  }

  @Test
  void preservesSourceRange() {
    IPattern pattern = new DefaultPatternParser(
        cursor("_")
    ).parse();
    SourceRange range = pattern.range();
    assertEquals(0, range.start().offset());
    assertEquals(1, range.end().offset());
  }

  @Test
  void rejectsNonWildcardToken() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new DefaultPatternParser(
            cursor("42")
        ).parse()
    );
  }
}