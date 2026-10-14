package dev.morphe.parser.expression.precedence;

import dev.morphe.ast.expression.BinaryExpression;
import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.ast.operator.BinaryOperator;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class RelationalParserTest extends AParserTest {

  @Test
  void parsesGreater() {
    BinaryExpression expression = (BinaryExpression) new RelationalParser(
        cursor("a > b")
    ).parse();
    assertEquals(BinaryOperator.GREATER, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }

  @Test
  void parsesGreaterOrEqual() {
    BinaryExpression expression = (BinaryExpression) new RelationalParser(
        cursor("a >= b")
    ).parse();
    assertEquals(BinaryOperator.GREATER_OR_EQUAL, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }

  @Test
  void parsesLess() {
    BinaryExpression expression = (BinaryExpression) new RelationalParser(
        cursor("a < b")
    ).parse();
    assertEquals(BinaryOperator.LESS, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }

  @Test
  void parsesLessOrEqual() {
    BinaryExpression expression = (BinaryExpression) new RelationalParser(
        cursor("a <= b")
    ).parse();
    assertEquals(BinaryOperator.LESS_OR_EQUAL, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }

  @Test
  void parsesMultipleOperatorsLeftAssociative() {
    BinaryExpression expression = (BinaryExpression) new RelationalParser(
        cursor("a > b >= c")
    ).parse();
    assertEquals(BinaryOperator.GREATER_OR_EQUAL, expression.operator());
    assertEquals("c", ((VariableExpression) expression.right()).name());
    BinaryExpression left = assertInstanceOf(BinaryExpression.class, expression.left());
    assertEquals(BinaryOperator.GREATER, left.operator());
    assertEquals("a", ((VariableExpression) left.left()).name());
    assertEquals("b", ((VariableExpression) left.right()).name());
  }

  @Test
  void respectsAdditivePrecedence() {
    BinaryExpression expression = (BinaryExpression) new RelationalParser(
        cursor("a + b > c")
    ).parse();
    assertEquals(BinaryOperator.GREATER, expression.operator());
    assertEquals("c", ((VariableExpression) expression.right()).name());
    BinaryExpression left = assertInstanceOf(BinaryExpression.class, expression.left());
    assertEquals(BinaryOperator.ADD, left.operator());
    assertEquals("a", ((VariableExpression) left.left()).name());
    assertEquals("b", ((VariableExpression) left.right()).name());
  }

  @Test
  void respectsMultiplicativeAndAdditivePrecedence() {
    BinaryExpression expression = (BinaryExpression) new RelationalParser(
        cursor("a + b * c > d")
    ).parse();
    assertEquals(BinaryOperator.GREATER, expression.operator());
    assertEquals("d", ((VariableExpression) expression.right()).name());
    BinaryExpression additive = assertInstanceOf(BinaryExpression.class, expression.left());
    assertEquals(BinaryOperator.ADD, additive.operator());
    assertEquals("a", ((VariableExpression) additive.left()).name());
    BinaryExpression multiplicative = assertInstanceOf(BinaryExpression.class, additive.right());
    assertEquals(BinaryOperator.MULTIPLY, multiplicative.operator());
    assertEquals("b", ((VariableExpression) multiplicative.left()).name());
    assertEquals("c", ((VariableExpression) multiplicative.right()).name());
  }

  @Test
  void stopsBeforeEqualityOperator() {
    BinaryExpression expression = (BinaryExpression) new RelationalParser(
        cursor("a > b == c")
    ).parse();
    assertEquals(BinaryOperator.GREATER, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }
}