package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.CompositePattern.FieldPattern;
import dev.morphe.ast.pattern.LiteralPattern;
import dev.morphe.ast.pattern.VariablePattern;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldPatternParserTest extends AParserTest {

  @Test
  void parsesLiteralField() {
    FieldPattern field = new FieldPatternParser(
        cursor("name: \"Diego\"")
    ).parse();
    assertEquals("name", field.name());
    assertInstanceOf(LiteralPattern.class, field.pattern());
    assertEquals("Diego", ((LiteralPattern) field.pattern()).value());
  }

  @Test
  void parsesVariableField() {
    FieldPattern field = new FieldPatternParser(
        cursor("age: value")
    ).parse();
    assertEquals("age", field.name());
    assertInstanceOf(VariablePattern.class, field.pattern());
    assertEquals("value", ((VariablePattern) field.pattern()).name());
  }

  @Test
  void parsesNestedPattern() {
    FieldPattern field = new FieldPatternParser(
        cursor("value: 1 | 2")
    ).parse();
    assertEquals("value", field.name());
    assertNotNull(field.pattern());
  }

  @Test
  void rejectsMissingColon() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new FieldPatternParser(
            cursor("name \"Diego\"")
        ).parse()
    );
  }

  @Test
  void rejectsMissingFieldName() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new FieldPatternParser(
            cursor(": \"Diego\"")
        ).parse()
    );
  }

  @Test
  void rejectsMissingPattern() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new FieldPatternParser(
            cursor("name:")
        ).parse()
    );
  }
}