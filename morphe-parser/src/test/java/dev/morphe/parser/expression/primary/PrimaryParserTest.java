package dev.morphe.parser.expression.primary;

import dev.morphe.ast.expression.*;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrimaryParserTest extends AParserTest {

  @Test
  void parsesIntegerLiteral() {
    LiteralExpression expression = (LiteralExpression) new PrimaryParser(
        cursor("42")
    ).parse();

    assertEquals(42, expression.value());
  }

  @Test
  void parsesDoubleLiteral() {
    LiteralExpression expression = (LiteralExpression) new PrimaryParser(
        cursor("3.14")
    ).parse();

    assertEquals(3.14, expression.value());
  }

  @Test
  void parsesStringLiteral() {
    LiteralExpression expression = (LiteralExpression) new PrimaryParser(
        cursor("\"hello\"")
    ).parse();

    assertEquals("hello", expression.value());
  }

  @Test
  void parsesBooleanLiteral() {
    LiteralExpression expression = (LiteralExpression) new PrimaryParser(
        cursor("true")
    ).parse();

    assertEquals(true, expression.value());
  }

  @Test
  void parsesVariable() {
    VariableExpression expression = (VariableExpression) new PrimaryParser(
        cursor("value")
    ).parse();

    assertEquals("value", expression.name());
  }

  @Test
  void parsesArray() {
    ArrayLiteralExpression expression = (ArrayLiteralExpression) new PrimaryParser(
        cursor("[1, 2]")
    ).parse();

    assertEquals(2, expression.elements().size());
  }

  @Test
  void parsesGroupedExpression() {
    LiteralExpression expression = (LiteralExpression) new PrimaryParser(
        cursor("(42)")
    ).parse();

    assertEquals(42, expression.value());
  }

  @Test
  void parsesNewExpression() {
    assertInstanceOf(
        CompositeInstanceExpression.class,
        new PrimaryParser(cursor("new Person {}")).parse()
    );
  }

  @Test
  void parsesIfExpression() {
    assertInstanceOf(
        IfExpression.class,
        new PrimaryParser(
            cursor("if (true) 1 else 2")
        ).parse()
    );
  }

  @Test
  void parsesMatchExpression() {
    assertInstanceOf(
        MatchExpression.class,
        new PrimaryParser(
            cursor("match (value) { _ => 1 }")
        ).parse()
    );
  }

  @Test
  void parsesAttemptExpression() {
    assertInstanceOf(
        AttemptExpression.class,
        new PrimaryParser(
            cursor("attempt { 1; } => 0")
        ).parse()
    );
  }

  @Test
  void parsesLambdaExpression() {
    LambdaExpression expression = (LambdaExpression) new PrimaryParser(
        cursor("(Integer x) -> x")
    ).parse();
    assertInstanceOf(LambdaExpression.class, expression);
    assertEquals(1, expression.parameters().size());
    assertEquals("x", expression.parameters().get(0).name());
  }

  @Test
  void rejectsInvalidPrimaryExpression() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new PrimaryParser(cursor(";")).parse()
    );
  }
}