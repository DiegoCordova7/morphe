package dev.morphe.parser.expression.precedence;

import dev.morphe.ast.expression.BinaryExpression;
import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.ast.operator.BinaryOperator;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class EqualityParserTest extends AParserTest {

  @Test
  void parsesEquality() {
    BinaryExpression expression = (BinaryExpression) new EqualityParser(
        cursor("a == b")
    ).parse();
    assertEquals(BinaryOperator.EQUAL, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }

  @Test
  void parsesInequality() {
    BinaryExpression expression = (BinaryExpression) new EqualityParser(
        cursor("a != b")
    ).parse();
    assertEquals(BinaryOperator.NOT_EQUAL, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }

  @Test
  void parsesMultipleOperatorsLeftAssociative() {
    BinaryExpression expression = (BinaryExpression) new EqualityParser(
        cursor("a == b != c")
    ).parse();
    assertEquals(BinaryOperator.NOT_EQUAL, expression.operator());
    assertEquals("c", ((VariableExpression) expression.right()).name());
    BinaryExpression left = assertInstanceOf(BinaryExpression.class, expression.left());
    assertEquals(BinaryOperator.EQUAL, left.operator());
    assertEquals("a", ((VariableExpression) left.left()).name());
    assertEquals("b", ((VariableExpression) left.right()).name());
  }

  @Test
  void respectsRelationalPrecedence() {
    BinaryExpression expression = (BinaryExpression) new EqualityParser(
        cursor("a > b == c")
    ).parse();
    assertEquals(BinaryOperator.EQUAL, expression.operator());
    assertEquals("c", ((VariableExpression) expression.right()).name());
    BinaryExpression left = assertInstanceOf(BinaryExpression.class, expression.left());
    assertEquals(BinaryOperator.GREATER, left.operator());
    assertEquals("a", ((VariableExpression) left.left()).name());
    assertEquals("b", ((VariableExpression) left.right()).name());
  }

  @Test
  void respectsAdditiveAndRelationalPrecedence() {
    BinaryExpression expression = (BinaryExpression) new EqualityParser(
        cursor("a + b > c == d")
    ).parse();
    assertEquals(BinaryOperator.EQUAL, expression.operator());
    assertEquals("d", ((VariableExpression) expression.right()).name());
    BinaryExpression relational = assertInstanceOf(BinaryExpression.class, expression.left());
    assertEquals(BinaryOperator.GREATER, relational.operator());
    assertEquals("c", ((VariableExpression) relational.right()).name());
    BinaryExpression additive = assertInstanceOf(BinaryExpression.class, relational.left());
    assertEquals(BinaryOperator.ADD, additive.operator());
    assertEquals("a", ((VariableExpression) additive.left()).name());
    assertEquals("b", ((VariableExpression) additive.right()).name());
  }

  @Test
  void stopsBeforeLogicalAnd() {
    BinaryExpression expression = (BinaryExpression) new EqualityParser(
        cursor("a == b && c")
    ).parse();
    assertEquals(BinaryOperator.EQUAL, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }
}