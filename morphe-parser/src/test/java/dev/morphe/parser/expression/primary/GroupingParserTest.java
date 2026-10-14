package dev.morphe.parser.expression.primary;

import dev.morphe.ast.expression.BinaryExpression;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.ast.operator.BinaryOperator;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import dev.morphe.parser.expression.ExpressionParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupingParserTest extends AParserTest {

  @Test
  void parsesGroupedExpression() {
    IExpression expression = new GroupingParser(
        cursor("(1 + 2)")
    ).parse();
    BinaryExpression addition = assertInstanceOf(BinaryExpression.class, expression);
    assertEquals(BinaryOperator.ADD, addition.operator());
    assertEquals(1, assertInstanceOf(LiteralExpression.class, addition.left()).value());
    assertEquals(2, assertInstanceOf(LiteralExpression.class, addition.right()).value());
  }

  @Test
  void parsesNestedGrouping() {
    IExpression expression = new GroupingParser(
        cursor("((42))")
    ).parse();
    LiteralExpression literal = assertInstanceOf(LiteralExpression.class, expression);
    assertEquals(42, literal.value());
  }

  @Test
  void preservesGroupingPrecedence() {
    IExpression expression = new ExpressionParser(
        cursor("(1 + 2) * 3")
    ).parse();
    BinaryExpression multiplication = assertInstanceOf(BinaryExpression.class, expression);
    assertEquals(BinaryOperator.MULTIPLY, multiplication.operator());
    BinaryExpression addition = assertInstanceOf(BinaryExpression.class, multiplication.left());
    assertEquals(BinaryOperator.ADD, addition.operator());
  }

  @Test
  void rejectsMissingClosingParenthesis() {
    assertThrows(UnexpectedTokenException.class, () -> new GroupingParser(cursor("(42")).parse());
  }
}