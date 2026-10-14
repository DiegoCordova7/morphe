package dev.morphe.parser.expression.compound;

import dev.morphe.ast.expression.BinaryExpression;
import dev.morphe.ast.expression.IfExpression;
import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.ast.operator.BinaryOperator;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class IfExpressionParserTest extends AParserTest {

  @Test
  void parsesSimpleIfExpression() {
    IfExpression expression = (IfExpression) new IfExpressionParser(
        cursor("if (condition) 1 else 0")
    ).parse();
    assertInstanceOf(VariableExpression.class, expression.condition());
    assertEquals("condition", ((VariableExpression) expression.condition()).name());
    assertInstanceOf(LiteralExpression.class, expression.thenBranch());
    assertEquals(1, ((LiteralExpression) expression.thenBranch()).value());
    assertInstanceOf(LiteralExpression.class, expression.elseBranch());
    assertEquals(0, ((LiteralExpression) expression.elseBranch()).value());
  }

  @Test
  void parsesConditionAsExpression() {
    IfExpression expression = (IfExpression) new IfExpressionParser(
        cursor("if (value > 10) 1 else 0")
    ).parse();
    assertInstanceOf(BinaryExpression.class, expression.condition());
    BinaryExpression condition = (BinaryExpression) expression.condition();
    assertEquals(BinaryOperator.GREATER, condition.operator());
    assertInstanceOf(VariableExpression.class, condition.left());
    assertEquals("value", ((VariableExpression) condition.left()).name());
    assertInstanceOf(LiteralExpression.class, condition.right());
    assertEquals(10, ((LiteralExpression) condition.right()).value());
  }

  @Test
  void parsesBranchesAsExpressions() {
    IfExpression expression = (IfExpression) new IfExpressionParser(
        cursor("if (condition) value + 1 else value - 1")
    ).parse();
    assertInstanceOf(BinaryExpression.class, expression.thenBranch());
    assertInstanceOf(BinaryExpression.class, expression.elseBranch());
    BinaryExpression thenBranch = (BinaryExpression) expression.thenBranch();
    BinaryExpression elseBranch = (BinaryExpression) expression.elseBranch();
    assertEquals(BinaryOperator.ADD, thenBranch.operator());
    assertEquals(BinaryOperator.SUBTRACT, elseBranch.operator());
  }

  @Test
  void parsesNestedIfExpression() {
    IfExpression expression = (IfExpression) new IfExpressionParser(
        cursor("if (condition) if (other) 1 else 2 else 3")
    ).parse();
    assertInstanceOf(IfExpression.class, expression.thenBranch());
    IfExpression nested = (IfExpression) expression.thenBranch();
    assertInstanceOf(VariableExpression.class, nested.condition());
    assertEquals("other", ((VariableExpression) nested.condition()).name());
    assertEquals(1, ((LiteralExpression) nested.thenBranch()).value());
    assertEquals(2, ((LiteralExpression) nested.elseBranch()).value());
    assertEquals(3, ((LiteralExpression) expression.elseBranch()).value());
  }

  @Test
  void preservesSourceRange() {
    IfExpression expression = (IfExpression) new IfExpressionParser(
        cursor("if (condition) 1 else 0")
    ).parse();
    SourceRange range = expression.range();
    assertEquals(0, range.start().offset());
    assertEquals(23, range.end().offset());
  }
}