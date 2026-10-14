package dev.morphe.parser.expression.precedence;

import dev.morphe.ast.expression.BinaryExpression;
import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.ast.expression.UnaryExpression;
import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.ast.operator.BinaryOperator;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class MultiplicativeParserTest extends AParserTest {

  @Test
  void parsesMultiplication() {
    BinaryExpression expression = (BinaryExpression) new MultiplicativeParser(
        cursor("a * b")
    ).parse();
    assertEquals(BinaryOperator.MULTIPLY, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }

  @Test
  void parsesDivision() {
    BinaryExpression expression = (BinaryExpression) new MultiplicativeParser(
        cursor("a / b")
    ).parse();
    assertEquals(BinaryOperator.DIVIDE, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }

  @Test
  void parsesModulo() {
    BinaryExpression expression = (BinaryExpression) new MultiplicativeParser(
        cursor("a % b")
    ).parse();
    assertEquals(BinaryOperator.MODULO, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }

  @Test
  void parsesMultipleOperatorsLeftAssociative() {
    BinaryExpression expression = (BinaryExpression) new MultiplicativeParser(
        cursor("a * b / c")
    ).parse();
    assertEquals(BinaryOperator.DIVIDE, expression.operator());
    BinaryExpression left = assertInstanceOf(BinaryExpression.class, expression.left());
    assertEquals(BinaryOperator.MULTIPLY, left.operator());
    assertEquals("a", ((VariableExpression) left.left()).name());
    assertEquals("b", ((VariableExpression) left.right()).name());
    assertEquals("c", ((VariableExpression) expression.right()).name());
  }

  @Test
  void parsesUnaryOperand() {
    BinaryExpression expression = (BinaryExpression) new MultiplicativeParser(
        cursor("-a * b")
    ).parse();
    assertEquals(BinaryOperator.MULTIPLY, expression.operator());
    assertInstanceOf(UnaryExpression.class, expression.left());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }

  @Test
  void parsesLiteralOperands() {
    BinaryExpression expression = (BinaryExpression) new MultiplicativeParser(
        cursor("10 * 2.5")
    ).parse();

    LiteralExpression left = assertInstanceOf(LiteralExpression.class, expression.left());
    LiteralExpression right = assertInstanceOf(LiteralExpression.class, expression.right());
    assertEquals(10, left.value());
    assertEquals(2.5, right.value());
  }

  @Test
  void stopsBeforeLowerPrecedenceOperator() {
    BinaryExpression expression = (BinaryExpression) new MultiplicativeParser(
        cursor("a * b + c")
    ).parse();
    assertEquals(BinaryOperator.MULTIPLY, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }

  @Test
  void parsesMixedOperatorsLeftAssociative() {
    BinaryExpression expression = (BinaryExpression) new MultiplicativeParser(
        cursor("a * b % c")
    ).parse();
    assertEquals(BinaryOperator.MODULO, expression.operator());
    BinaryExpression left = assertInstanceOf(BinaryExpression.class, expression.left());
    assertEquals(BinaryOperator.MULTIPLY, left.operator());
    assertEquals("a", ((VariableExpression) left.left()).name());
    assertEquals("b", ((VariableExpression) left.right()).name());
    assertEquals("c", ((VariableExpression) expression.right()).name());
  }
}