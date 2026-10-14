package dev.morphe.parser.expression.precedence;

import dev.morphe.ast.expression.BinaryExpression;
import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.ast.operator.BinaryOperator;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class LogicalAndParserTest extends AParserTest {

  @Test
  void parsesLogicalAnd() {
    BinaryExpression expression = (BinaryExpression) new LogicalAndParser(
        cursor("a && b")
    ).parse();
    assertEquals(BinaryOperator.AND, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }

  @Test
  void parsesMultipleOperatorsLeftAssociative() {
    BinaryExpression expression = (BinaryExpression) new LogicalAndParser(
        cursor("a && b && c")
    ).parse();
    assertEquals(BinaryOperator.AND, expression.operator());
    assertEquals("c", ((VariableExpression) expression.right()).name());
    BinaryExpression left = assertInstanceOf(BinaryExpression.class, expression.left());
    assertEquals(BinaryOperator.AND, left.operator());
    assertEquals("a", ((VariableExpression) left.left()).name());
    assertEquals("b", ((VariableExpression) left.right()).name());
  }

  @Test
  void respectsEqualityPrecedence() {
    BinaryExpression expression = (BinaryExpression) new LogicalAndParser(
        cursor("a == b && c != d")
    ).parse();
    assertEquals(BinaryOperator.AND, expression.operator());
    BinaryExpression left = assertInstanceOf(BinaryExpression.class, expression.left());
    BinaryExpression right = assertInstanceOf(BinaryExpression.class, expression.right());
    assertEquals(BinaryOperator.EQUAL, left.operator());
    assertEquals(BinaryOperator.NOT_EQUAL, right.operator());
  }

  @Test
  void stopsBeforeLogicalOr() {
    BinaryExpression expression = (BinaryExpression) new LogicalAndParser(
        cursor("a && b || c")
    ).parse();
    assertEquals(BinaryOperator.AND, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }
}