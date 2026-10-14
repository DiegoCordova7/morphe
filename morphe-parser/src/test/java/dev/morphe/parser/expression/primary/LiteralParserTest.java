package dev.morphe.parser.expression.primary;

import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LiteralParserTest  extends AParserTest {

  @Test
  void parsesIntegerLiteral() {
    LiteralExpression expression = (LiteralExpression) new LiteralParser(
        cursor("42")
    ).parse();
    assertEquals(42, expression.value());
  }

  @Test
  void parsesDoubleLiteral() {
    LiteralExpression expression = (LiteralExpression) new LiteralParser(
        cursor("3.14")
    ).parse();
    assertEquals(3.14, expression.value());
  }

  @Test
  void parsesStringLiteral() {
    LiteralExpression expression = (LiteralExpression) new LiteralParser(
        cursor("\"Diego\"")
    ).parse();
    assertEquals("Diego", expression.value());
  }

  @Test
  void parsesBooleanLiteral() {
    LiteralExpression expression = (LiteralExpression) new LiteralParser(
        cursor("true")
    ).parse();
    assertEquals(true, expression.value());
  }

  @Test
  void parsesNullLiteral() {
    LiteralExpression expression = (LiteralExpression) new LiteralParser(
        cursor("null")
    ).parse();
    assertEquals("null", expression.value());
  }

  @Test
  void storesSourceRange() {
    LiteralExpression expression = (LiteralExpression) new LiteralParser(
        cursor("42")
    ).parse();
    assertEquals(0, expression.range().start().offset());
    assertEquals(2, expression.range().end().offset());
  }

  @Test
  void rejectsUnsupportedToken() {
    assertThrows(UnexpectedTokenException.class, () -> new LiteralParser(cursor("nul")).parse());
  }
}