package dev.morphe.parser.expression.postfix;

import dev.morphe.ast.expression.FieldAccessExpression;
import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import dev.morphe.parser.expression.primary.PrimaryParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FieldAccessParserTest extends AParserTest {

  @Test
  void parsesFieldAccess() {
    TokenCursor cursor = cursor("person.name");
    VariableExpression target = (VariableExpression) new PrimaryParser(cursor).parse();
    cursor.advance(); // .
    FieldAccessExpression expression = (FieldAccessExpression) new FieldAccessParser(cursor).parse(target);
    assertEquals("person", target.name());
    assertEquals("name", expression.field());
    assertEquals(target, expression.target());
  }

  @Test
  void storesSourceRange() {
    TokenCursor cursor = cursor("person.name");
    VariableExpression target = (VariableExpression) new PrimaryParser(cursor).parse();
    cursor.advance(); // .
    FieldAccessExpression expression = (FieldAccessExpression) new FieldAccessParser(cursor).parse(target);
    assertEquals(0, expression.range().start().offset());
    assertEquals(11, expression.range().end().offset());
  }

  @Test
  void rejectsMissingFieldName() {
    TokenCursor cursor = cursor("person.");
    VariableExpression target = (VariableExpression) new PrimaryParser(cursor).parse();
    cursor.advance(); // .
    assertThrows(UnexpectedTokenException.class, () -> new FieldAccessParser(cursor).parse(target));
  }
}