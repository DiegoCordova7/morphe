package dev.morphe.parser.statement.simple;

import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.ast.statement.ReturnStatement;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReturnStatementParserTest extends AParserTest {

  @Test
  void parsesReturnWithoutValue() {
    IStatement statement = new ReturnStatementParser(
        cursor("return;")
    ).parse();
    ReturnStatement result = assertInstanceOf(ReturnStatement.class, statement);
    assertNull(result.value());
  }

  @Test
  void parsesReturnWithLiteralValue() {
    IStatement statement = new ReturnStatementParser(
        cursor("return 42;")
    ).parse();
    ReturnStatement result = assertInstanceOf(ReturnStatement.class, statement);
    LiteralExpression value = assertInstanceOf(LiteralExpression.class, result.value());
    assertEquals(42, value.value());
  }

  @Test
  void parsesReturnWithVariableValue() {
    IStatement statement = new ReturnStatementParser(
        cursor("return value;")
    ).parse();
    ReturnStatement result = assertInstanceOf(ReturnStatement.class, statement);
    VariableExpression value = assertInstanceOf(VariableExpression.class, result.value());
    assertEquals("value", value.name());
  }

  @Test
  void parsesReturnWithComplexExpression() {
    IStatement statement = new ReturnStatementParser(
        cursor("return value + 10;")
    ).parse();
    ReturnStatement result = assertInstanceOf(ReturnStatement.class, statement);
    assertNotNull(result.value());
  }

  @Test
  void preservesSourceRange() {
    ReturnStatement statement = assertInstanceOf(
        ReturnStatement.class, new ReturnStatementParser(cursor("return 42;")).parse()
    );
    assertEquals(0, statement.range().start().offset());
    assertEquals(10, statement.range().end().offset());
  }
}