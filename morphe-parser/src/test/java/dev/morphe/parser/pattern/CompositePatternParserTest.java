package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.CompositePattern;
import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.pattern.LiteralPattern;
import dev.morphe.ast.pattern.VariablePattern;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompositePatternParserTest extends AParserTest {

  @Test
  void parsesEmptyComposite() {
    IPattern pattern = new CompositePatternParser(
        cursor("Person {}")
    ).parse();
    assertInstanceOf(CompositePattern.class, pattern);
    CompositePattern composite = (CompositePattern) pattern;
    assertEquals("Person", composite.typeName());
    assertTrue(composite.fields().isEmpty());
  }

  @Test
  void parsesSingleField() {
    IPattern pattern = new CompositePatternParser(
        cursor("Person { name : \"Diego\" }")
    ).parse();
    CompositePattern composite = (CompositePattern) pattern;
    assertEquals("Person", composite.typeName());
    assertEquals(1, composite.fields().size());
    CompositePattern.FieldPattern field = composite.fields().get(0);
    assertEquals("name", field.name());
    assertInstanceOf(LiteralPattern.class, field.pattern());
    assertEquals("Diego", ((LiteralPattern) field.pattern()).value());
  }

  @Test
  void parsesMultipleFields() {
    IPattern pattern = new CompositePatternParser(
        cursor("Person { name : \"Diego\", age : age }")
    ).parse();
    CompositePattern composite = (CompositePattern) pattern;
    assertEquals("Person", composite.typeName());
    assertEquals(2, composite.fields().size());
    CompositePattern.FieldPattern name = composite.fields().get(0);
    CompositePattern.FieldPattern age = composite.fields().get(1);
    assertEquals("name", name.name());
    assertInstanceOf(LiteralPattern.class, name.pattern());
    assertEquals("age", age.name());
    assertInstanceOf(VariablePattern.class, age.pattern());
    assertEquals("age", ((VariablePattern) age.pattern()).name());
  }

  @Test
  void preservesSourceRange() {
    IPattern pattern = new CompositePatternParser(
        cursor("Person { name : \"Diego\" }")
    ).parse();
    SourceRange range = pattern.range();
    assertEquals(0, range.start().offset());
    assertEquals(25, range.end().offset());
  }

  @Test
  void parsesNestedPattern() {
    IPattern pattern = new CompositePatternParser(
        cursor("Person { value : 1 | 2 }")
    ).parse();
    CompositePattern composite = (CompositePattern) pattern;
    assertEquals(1, composite.fields().size());
    assertNotNull(composite.fields().get(0).pattern());
  }

  @Test
  void rejectsMissingClosingBrace() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new CompositePatternParser(
            cursor("Person { name: \"Diego\"")
        ).parse()
    );
  }

  @Test
  void rejectsTrailingComma() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new CompositePatternParser(
            cursor("Person { name: \"Diego\", }")
        ).parse()
    );
  }

  @Test
  void rejectsMissingFieldBetweenCommas() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new CompositePatternParser(
            cursor("Person { name: \"Diego\", , age: 25 }")
        ).parse()
    );
  }

  @Test
  void rejectsMissingClosingBraceForEmptyComposite() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new CompositePatternParser(
            cursor("Person {")
        ).parse()
    );
  }
}