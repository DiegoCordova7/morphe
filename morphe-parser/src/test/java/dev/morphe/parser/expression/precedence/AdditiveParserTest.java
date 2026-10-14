package dev.morphe.parser.expression.precedence;

import dev.morphe.ast.expression.BinaryExpression;
import dev.morphe.ast.expression.UnaryExpression;
import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.ast.operator.BinaryOperator;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class AdditiveParserTest extends AParserTest {

  @Test
  void parsesAddition() {
    BinaryExpression expression = (BinaryExpression) new AdditiveParser(
        cursor("a + b")
    ).parse();
    assertEquals(BinaryOperator.ADD, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }

  @Test
  void parsesSubtraction() {
    BinaryExpression expression = (BinaryExpression) new AdditiveParser(
        cursor("a - b")
    ).parse();
    assertEquals(BinaryOperator.SUBTRACT, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }

  @Test
  void parsesMultipleOperatorsLeftAssociative() {
    BinaryExpression expression = (BinaryExpression) new AdditiveParser(
        cursor("a + b - c")
    ).parse();
    assertEquals(BinaryOperator.SUBTRACT, expression.operator());
    assertEquals("c", ((VariableExpression) expression.right()).name());
    BinaryExpression left = assertInstanceOf(BinaryExpression.class, expression.left());
    assertEquals(BinaryOperator.ADD, left.operator());
    assertEquals("a", ((VariableExpression) left.left()).name());
    assertEquals("b", ((VariableExpression) left.right()).name());
  }

  @Test
  void respectsMultiplicativePrecedence() {
    BinaryExpression expression = (BinaryExpression) new AdditiveParser(
        cursor("a + b * c")
    ).parse();
    assertEquals(BinaryOperator.ADD, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    BinaryExpression right = assertInstanceOf(BinaryExpression.class, expression.right());
    assertEquals(BinaryOperator.MULTIPLY, right.operator());
    assertEquals("b", ((VariableExpression) right.left()).name());
    assertEquals("c", ((VariableExpression) right.right()).name());
  }

  @Test
  void respectsMultiplicativePrecedenceOnLeftOperand() {
    BinaryExpression expression = (BinaryExpression) new AdditiveParser(
        cursor("a * b + c")
    ).parse();
    assertEquals(BinaryOperator.ADD, expression.operator());
    assertEquals("c", ((VariableExpression) expression.right()).name());
    BinaryExpression left = assertInstanceOf(BinaryExpression.class, expression.left());
    assertEquals(BinaryOperator.MULTIPLY, left.operator());
    assertEquals("a", ((VariableExpression) left.left()).name());
    assertEquals("b", ((VariableExpression) left.right()).name());
  }

  @Test
  void parsesUnaryExpression() {
    BinaryExpression expression = (BinaryExpression) new AdditiveParser(
        cursor("-a + b")
    ).parse();
    assertEquals(BinaryOperator.ADD, expression.operator());
    assertInstanceOf(UnaryExpression.class, expression.left());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }

  @Test
  void stopsBeforeLowerPrecedenceOperator() {
    BinaryExpression expression = (BinaryExpression) new AdditiveParser(
        cursor("a + b == c")
    ).parse();
    assertEquals(BinaryOperator.ADD, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }
}