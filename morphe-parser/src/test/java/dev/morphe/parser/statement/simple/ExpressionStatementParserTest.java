package dev.morphe.parser.statement.simple;

import dev.morphe.ast.expression.FunctionCallExpression;
import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.ExpressionStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionStatementParserTest extends AParserTest {

  @Test
  void parsesLiteralExpression() {
    IStatement statement = new ExpressionStatementParser(
        cursor("42;")
    ).parse();
    ExpressionStatement expression = assertInstanceOf(ExpressionStatement.class, statement);
    LiteralExpression literal = assertInstanceOf(LiteralExpression.class, expression.expression());
    assertEquals(42, literal.value());
  }

  @Test
  void parsesVariableExpression() {
    IStatement statement = new ExpressionStatementParser(
        cursor("value;")
    ).parse();
    ExpressionStatement expression = assertInstanceOf(ExpressionStatement.class, statement);
    VariableExpression variable = assertInstanceOf(VariableExpression.class, expression.expression());
    assertEquals("value", variable.name());
  }

  @Test
  void parsesFunctionCallExpression() {
    IStatement statement = new ExpressionStatementParser(
        cursor("print(value);")
    ).parse();
    ExpressionStatement expression = assertInstanceOf(ExpressionStatement.class, statement);
    assertInstanceOf(FunctionCallExpression.class, expression.expression());
  }

  @Test
  void preservesSourceRange() {
    IStatement statement = new ExpressionStatementParser(
        cursor("42;")
    ).parse();
    SourceRange range = statement.range();
    assertEquals(0, range.start().offset());
    assertEquals(3, range.end().offset());
  }
}