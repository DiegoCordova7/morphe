package dev.morphe.parser.expression.precedence;

import dev.morphe.ast.expression.BinaryExpression;
import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.ast.operator.BinaryOperator;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class LogicalOrParserTest extends AParserTest {

  @Test
  void parsesLogicalOr() {
    BinaryExpression expression = (BinaryExpression) new LogicalOrParser(
        cursor("a || b")
    ).parse();
    assertEquals(BinaryOperator.OR, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    assertEquals("b", ((VariableExpression) expression.right()).name());
  }

  @Test
  void parsesMultipleOperatorsLeftAssociative() {
    BinaryExpression expression = (BinaryExpression) new LogicalOrParser(
        cursor("a || b || c")
    ).parse();
    assertEquals(BinaryOperator.OR, expression.operator());
    assertEquals("c", ((VariableExpression) expression.right()).name());
    BinaryExpression left = assertInstanceOf(BinaryExpression.class, expression.left());
    assertEquals(BinaryOperator.OR, left.operator());
    assertEquals("a", ((VariableExpression) left.left()).name());
    assertEquals("b", ((VariableExpression) left.right()).name());
  }

  @Test
  void respectsLogicalAndPrecedence() {
    BinaryExpression expression = (BinaryExpression) new LogicalOrParser(
        cursor("a || b && c")
    ).parse();
    assertEquals(BinaryOperator.OR, expression.operator());
    assertEquals("a", ((VariableExpression) expression.left()).name());
    BinaryExpression right = assertInstanceOf(BinaryExpression.class, expression.right());
    assertEquals(BinaryOperator.AND, right.operator());
    assertEquals("b", ((VariableExpression) right.left()).name());
    assertEquals("c", ((VariableExpression) right.right()).name());
  }

  @Test
  void respectsEqualityAndLogicalPrecedence() {
    BinaryExpression expression = (BinaryExpression) new LogicalOrParser(
        cursor("a == b && c != d || e")
    ).parse();
    assertEquals(BinaryOperator.OR, expression.operator());
    assertEquals("e", ((VariableExpression) expression.right()).name());
    BinaryExpression and = assertInstanceOf(BinaryExpression.class, expression.left());
    assertEquals(BinaryOperator.AND, and.operator());
    BinaryExpression equality = assertInstanceOf(BinaryExpression.class, and.left());
    BinaryExpression inequality = assertInstanceOf(BinaryExpression.class, and.right());
    assertEquals(BinaryOperator.EQUAL, equality.operator());
    assertEquals(BinaryOperator.NOT_EQUAL, inequality.operator());
  }

  @Test
  void stopsBeforeLowerLevelExpression() {
    BinaryExpression expression = (BinaryExpression) new LogicalOrParser(
        cursor("a || b")
    ).parse();
    assertEquals(BinaryOperator.OR, expression.operator());
  }
}