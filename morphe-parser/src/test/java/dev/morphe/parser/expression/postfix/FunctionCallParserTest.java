package dev.morphe.parser.expression.postfix;

import dev.morphe.ast.expression.FunctionCallExpression;
import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import dev.morphe.parser.expression.primary.PrimaryParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FunctionCallParserTest extends AParserTest {

  @Test
  void parsesFunctionCallWithoutArguments() {
    TokenCursor cursor = cursor("foo()");
    VariableExpression function = (VariableExpression) new PrimaryParser(cursor).parse();
    cursor.advance(); // (
    FunctionCallExpression expression = (FunctionCallExpression) new FunctionCallParser(cursor).parse(function);
    assertEquals(function, expression.function());
    assertEquals(0, expression.arguments().size());
  }

  @Test
  void parsesFunctionCallWithArguments() {
    TokenCursor cursor = cursor("foo(10, 20)");
    VariableExpression function = (VariableExpression) new PrimaryParser(cursor).parse();
    cursor.advance(); // (
    FunctionCallExpression expression = (FunctionCallExpression) new FunctionCallParser(cursor).parse(function);
    assertEquals(function, expression.function());
    assertEquals(2, expression.arguments().size());
    LiteralExpression first = (LiteralExpression) expression.arguments().get(0);
    LiteralExpression second = (LiteralExpression) expression.arguments().get(1);
    assertEquals(10, first.value());
    assertEquals(20, second.value());
  }

  @Test
  void parsesArgumentsAsExpressions() {
    TokenCursor cursor = cursor("foo(1 + 2, value * 3)");
    VariableExpression function = (VariableExpression) new PrimaryParser(cursor).parse();
    cursor.advance(); // (
    FunctionCallExpression expression = (FunctionCallExpression) new FunctionCallParser(cursor).parse(function);
    assertEquals(2, expression.arguments().size());
  }

  @Test
  void storesSourceRange() {
    TokenCursor cursor = cursor("foo(10, 20)");
    VariableExpression function = (VariableExpression) new PrimaryParser(cursor).parse();
    cursor.advance(); // (
    FunctionCallExpression expression = (FunctionCallExpression) new FunctionCallParser(cursor).parse(function);
    assertEquals(0, expression.range().start().offset());
    assertEquals(11, expression.range().end().offset());
  }

  @Test
  void rejectsMissingClosingParenthesis() {
    TokenCursor cursor = cursor("foo(10, 20");
    VariableExpression function = (VariableExpression) new PrimaryParser(cursor).parse();
    cursor.advance(); // (
    assertThrows(UnexpectedTokenException.class, () -> new FunctionCallParser(cursor).parse(function));
  }

  @Test
  void rejectsMissingArgument() {
    TokenCursor cursor = cursor("foo(10, )");
    VariableExpression function = (VariableExpression) new PrimaryParser(cursor).parse();
    cursor.advance(); // (
    assertThrows(UnexpectedTokenException.class, () -> new FunctionCallParser(cursor).parse(function));
  }
}