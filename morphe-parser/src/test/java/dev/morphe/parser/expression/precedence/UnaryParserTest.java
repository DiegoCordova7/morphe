package dev.morphe.parser.expression.precedence;

import dev.morphe.ast.expression.UnaryExpression;
import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.ast.operator.UnaryOperator;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class UnaryParserTest extends AParserTest {

  @Test
  void parsesMinus() {
    UnaryExpression expression = (UnaryExpression) new UnaryParser(
        cursor("-value")
    ).parse();
    assertEquals(UnaryOperator.MINUS, expression.operator());
    assertInstanceOf(VariableExpression.class, expression.operand());
  }

  @Test
  void parsesNot() {
    UnaryExpression expression = (UnaryExpression) new UnaryParser(
        cursor("!value")
    ).parse();
    assertEquals(UnaryOperator.NOT, expression.operator());
    assertInstanceOf(VariableExpression.class, expression.operand());
  }

  @Test
  void parsesNestedUnaryExpression() {
    UnaryExpression expression = (UnaryExpression) new UnaryParser(
        cursor("-(-value)")
    ).parse();
    assertEquals(UnaryOperator.MINUS, expression.operator());
    UnaryExpression operand = assertInstanceOf(UnaryExpression.class, expression.operand());
    assertEquals(UnaryOperator.MINUS, operand.operator());
    VariableExpression variable = assertInstanceOf(VariableExpression.class, operand.operand());
    assertEquals("value", variable.name());
  }
}