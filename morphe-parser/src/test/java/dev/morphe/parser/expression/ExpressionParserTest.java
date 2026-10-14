package dev.morphe.parser.expression;

import dev.morphe.ast.expression.*;
import dev.morphe.ast.operator.BinaryOperator;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ExpressionParserTest extends AParserTest {

  @Test
  void parsesIfExpressionThroughExpressionParser() {
    IExpression expression = new ExpressionParser(
        cursor("if (condition) 1 else 0")
    ).parse();
    assertInstanceOf(IfExpression.class, expression);
    IfExpression ifExpression = (IfExpression) expression;
    assertInstanceOf(VariableExpression.class, ifExpression.condition());
    assertEquals("condition", ((VariableExpression) ifExpression.condition()).name());
    assertInstanceOf(LiteralExpression.class, ifExpression.thenBranch());
    assertEquals(1, ((LiteralExpression) ifExpression.thenBranch()).value());
    assertInstanceOf(LiteralExpression.class, ifExpression.elseBranch());
    assertEquals(0, ((LiteralExpression) ifExpression.elseBranch()).value());
  }

  @Test
  void parsesIfExpressionWithPostfixBranches() {
    IExpression expression = new ExpressionParser(
        cursor("if (condition) person.getValue() else values[0]")
    ).parse();
    assertInstanceOf(IfExpression.class, expression);
    IfExpression ifExpression = (IfExpression) expression;
    assertInstanceOf(FunctionCallExpression.class, ifExpression.thenBranch());
    FunctionCallExpression call = (FunctionCallExpression) ifExpression.thenBranch();
    assertInstanceOf(FieldAccessExpression.class, call.function());
    assertInstanceOf(ArrayAccessExpression.class, ifExpression.elseBranch());
  }

  @Test
  void parsesMatchExpressionThroughExpressionParser() {
    IExpression expression = new ExpressionParser(
        cursor("""
          match (value) {
            1 => "one"
            _ => "other"
          }
          """)
    ).parse();
    assertInstanceOf(MatchExpression.class, expression);
  }

  @Test
  void parsesGroupedExpression() {
    IExpression expression = new ExpressionParser(
        cursor("(1 + 2) * 3")
    ).parse();
    BinaryExpression multiply = assertInstanceOf(BinaryExpression.class, expression);
    assertEquals(BinaryOperator.MULTIPLY, multiply.operator());
    BinaryExpression addition = assertInstanceOf(BinaryExpression.class, multiply.left());
    assertEquals(BinaryOperator.ADD, addition.operator());
    LiteralExpression left = assertInstanceOf(LiteralExpression.class, addition.left());
    assertEquals(1, left.value());
    LiteralExpression right = assertInstanceOf(LiteralExpression.class, addition.right());
    assertEquals(2, right.value());
    LiteralExpression multiplier = assertInstanceOf(LiteralExpression.class, multiply.right());
    assertEquals(3, multiplier.value());
  }

}