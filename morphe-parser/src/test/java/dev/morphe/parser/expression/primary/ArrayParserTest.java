package dev.morphe.parser.expression.primary;

import dev.morphe.ast.expression.ArrayLiteralExpression;
import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArrayParserTest extends AParserTest {

  @Test
  void parsesEmptyArray() {
    ArrayLiteralExpression expression = (ArrayLiteralExpression) new ArrayParser(
        cursor("[]")
    ).parse();
    assertTrue(expression.elements().isEmpty());
  }

  @Test
  void parsesArrayOfLiterals() {
    ArrayLiteralExpression expression = (ArrayLiteralExpression) new ArrayParser(
        cursor("[1, 2, 3]")
    ).parse();
    assertEquals(3, expression.elements().size());
    assertEquals(1, ((LiteralExpression) expression.elements().get(0)).value());
    assertEquals(2, ((LiteralExpression) expression.elements().get(1)).value());
    assertEquals(3, ((LiteralExpression) expression.elements().get(2)).value());
  }

  @Test
  void parsesNestedArrays() {
    ArrayLiteralExpression expression = (ArrayLiteralExpression) new ArrayParser(
        cursor("[[1, 2], [3, 4]]")
    ).parse();
    assertEquals(2, expression.elements().size());
    ArrayLiteralExpression first = (ArrayLiteralExpression) expression.elements().get(0);
    ArrayLiteralExpression second = (ArrayLiteralExpression) expression.elements().get(1);
    assertEquals(2, first.elements().size());
    assertEquals(2, second.elements().size());
  }
}