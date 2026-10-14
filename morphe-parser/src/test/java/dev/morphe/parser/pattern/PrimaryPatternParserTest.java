package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.*;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimaryPatternParserTest extends AParserTest {

  @Test
  void parsesLiteralPattern() {
    IPattern pattern = new PrimaryPatternParser(
        cursor("42")
    ).parse();
    assertInstanceOf(LiteralPattern.class, pattern);
    assertEquals(42, ((LiteralPattern) pattern).value());
  }

  @Test
  void parsesDefaultPattern() {
    IPattern pattern = new PrimaryPatternParser(
        cursor("_")
    ).parse();
    assertInstanceOf(DefaultPattern.class, pattern);
  }

  @Test
  void parsesVariablePattern() {
    IPattern pattern = new PrimaryPatternParser(
        cursor("value")
    ).parse();
    assertInstanceOf(VariablePattern.class, pattern);
    assertEquals("value", ((VariablePattern) pattern).name());
  }

  @Test
  void parsesArrayPattern() {
    IPattern pattern = new PrimaryPatternParser(
        cursor("[1, 2]")
    ).parse();
    assertInstanceOf(ArrayPattern.class, pattern);
    ArrayPattern array = (ArrayPattern) pattern;
    assertEquals(2, array.elements().size());
  }

  @Test
  void parsesCompositePattern() {
    IPattern pattern = new PrimaryPatternParser(
        cursor("Person {}")
    ).parse();
    assertInstanceOf(CompositePattern.class, pattern);
    CompositePattern composite = (CompositePattern) pattern;
    assertEquals("Person", composite.typeName());
  }

  @Test
  void rejectsUnsupportedToken() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new PrimaryPatternParser(
            cursor("=>")
        ).parse()
    );
  }
}