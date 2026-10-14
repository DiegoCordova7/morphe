package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.*;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayPatternParserTest extends AParserTest {

  @Test
  void parsesEmptyArray() {
    IPattern pattern = new ArrayPatternParser(
        cursor("[]")
    ).parse();
    assertInstanceOf(ArrayPattern.class, pattern);
    ArrayPattern array = (ArrayPattern) pattern;
    assertTrue(array.elements().isEmpty());
    assertNull(array.rest());
  }

  @Test
  void parsesSingleElement() {
    IPattern pattern = new ArrayPatternParser(
        cursor("[42]")
    ).parse();
    assertInstanceOf(ArrayPattern.class, pattern);
    ArrayPattern array = (ArrayPattern) pattern;
    assertEquals(1, array.elements().size());
    assertInstanceOf(LiteralPattern.class, array.elements().get(0));
    assertEquals(42, ((LiteralPattern) array.elements().get(0)).value());
    assertNull(array.rest());
  }

  @Test
  void parsesMultipleElements() {
    IPattern pattern = new ArrayPatternParser(
        cursor("[1, 2, 3]")
    ).parse();
    assertInstanceOf(ArrayPattern.class, pattern);
    ArrayPattern array = (ArrayPattern) pattern;
    assertEquals(3, array.elements().size());
    assertEquals(1, ((LiteralPattern) array.elements().get(0)).value());
    assertEquals(2, ((LiteralPattern) array.elements().get(1)).value());
    assertEquals(3, ((LiteralPattern) array.elements().get(2)).value());
    assertNull(array.rest());
  }

  @Test
  void parsesDifferentPatternTypes() {
    IPattern pattern = new ArrayPatternParser(
        cursor("[1, _, value]")
    ).parse();
    assertInstanceOf(ArrayPattern.class, pattern);
    ArrayPattern array = (ArrayPattern) pattern;
    assertEquals(3, array.elements().size());
    assertInstanceOf(LiteralPattern.class, array.elements().get(0));
    assertInstanceOf(DefaultPattern.class, array.elements().get(1));
    assertInstanceOf(VariablePattern.class, array.elements().get(2));
  }

  @Test
  void parsesRestPattern() {
    IPattern pattern = new ArrayPatternParser(
        cursor("[1, 2, ...rest]")
    ).parse();
    assertInstanceOf(ArrayPattern.class, pattern);
    ArrayPattern array = (ArrayPattern) pattern;
    assertEquals(2, array.elements().size());
    assertEquals(1, ((LiteralPattern) array.elements().get(0)).value());
    assertEquals(2, ((LiteralPattern) array.elements().get(1)).value());
    assertInstanceOf(VariablePattern.class, array.rest());
    assertEquals("rest", ((VariablePattern) array.rest()).name());
  }

  @Test
  void parsesOnlyRestPattern() {
    IPattern pattern = new ArrayPatternParser(
        cursor("[...rest]")
    ).parse();
    assertInstanceOf(ArrayPattern.class, pattern);
    ArrayPattern array = (ArrayPattern) pattern;
    assertTrue(array.elements().isEmpty());
    assertInstanceOf(VariablePattern.class, array.rest());
    assertEquals("rest", ((VariablePattern) array.rest()).name());
  }

  @Test
  void preservesSourceRange() {
    IPattern pattern = new ArrayPatternParser(
        cursor("[1, 2, 3]")
    ).parse();
    SourceRange range = pattern.range();
    assertEquals(0, range.start().offset());
    assertEquals(9, range.end().offset());
  }

  @Test
  void parsesNestedArrayPattern() {
    IPattern pattern = new ArrayPatternParser(
        cursor("[[1, 2], [3, 4]]")
    ).parse();
    assertInstanceOf(ArrayPattern.class, pattern);
    ArrayPattern outer = (ArrayPattern) pattern;
    assertEquals(2, outer.elements().size());
    assertInstanceOf(ArrayPattern.class, outer.elements().get(0));
    assertInstanceOf(ArrayPattern.class, outer.elements().get(1));
    ArrayPattern first = (ArrayPattern) outer.elements().get(0);
    assertEquals(2, first.elements().size());
    assertEquals(1, ((LiteralPattern) first.elements().get(0)).value());
    assertEquals(2, ((LiteralPattern) first.elements().get(1)).value());
  }

  @Test
  void rejectsTrailingComma() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new ArrayPatternParser(
            cursor("[1, 2,]")
        ).parse()
    );
  }

  @Test
  void rejectsMissingClosingBracket() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new ArrayPatternParser(
            cursor("[1, 2")
        ).parse()
    );
  }

  @Test
  void rejectsRestWithoutPattern() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new ArrayPatternParser(
            cursor("[1, ...]")
        ).parse()
    );
  }

  @Test
  void rejectsMissingElementBetweenCommas() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new ArrayPatternParser(
            cursor("[1,,2]")
        ).parse()
    );
  }
}