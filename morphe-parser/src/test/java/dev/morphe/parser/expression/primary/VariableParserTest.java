package dev.morphe.parser.expression.primary;

import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VariableParserTest extends AParserTest {

  @Test
  void parsesVariableExpression() {
    VariableExpression expression = (VariableExpression) new VariableParser(
        cursor("person")
    ).parse();
    assertEquals("person", expression.name());
  }

  @Test
  void storesSourceRange() {
    VariableExpression expression = (VariableExpression) new VariableParser(
        cursor("person")
    ).parse();
    assertEquals(0, expression.range().start().offset());
    assertEquals(6, expression.range().end().offset());
  }

  @Test
  void rejectsNonIdentifierToken() {
    assertThrows(UnexpectedTokenException.class, () -> new VariableParser(cursor("42")).parse());
  }
}