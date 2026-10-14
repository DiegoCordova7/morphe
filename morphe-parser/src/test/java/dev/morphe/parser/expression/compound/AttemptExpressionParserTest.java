package dev.morphe.parser.expression.compound;

import dev.morphe.ast.expression.AttemptExpression;
import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.ast.expression.UnaryExpression;
import dev.morphe.ast.statement.ExpressionStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.ast.type.ErrorType;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttemptExpressionParserTest extends AParserTest {

  @Test
  void parsesAttemptExpression() {
    AttemptExpression attempt = assertInstanceOf(
        AttemptExpression.class,
        new AttemptExpressionParser(
            cursor("""
                attempt {
                    10 / x;
                } => -1
                """)
        ).parse()
    );
    assertNotNull(attempt.body());
    assertNotNull(attempt.fallback());
    assertTrue(attempt.handlers().isEmpty());
  }

  @Test
  void parsesFallbackExpression() {
    AttemptExpression attempt = assertInstanceOf(
        AttemptExpression.class,
        new AttemptExpressionParser(
            cursor("""
                attempt {
                    10 / x;
                } => -1
                """)
        ).parse()
    );
    UnaryExpression fallback = assertInstanceOf(UnaryExpression.class, attempt.fallback());
    LiteralExpression operand = assertInstanceOf(LiteralExpression.class, fallback.operand());
    assertEquals(1, operand.value());
  }

  @Test
  void parsesErrorHandler() {
    AttemptExpression attempt = assertInstanceOf(
        AttemptExpression.class,
        new AttemptExpressionParser(
            cursor("""
                attempt {
                    10 / x;
                } => -1
                onError(DivisionByZero) => print("Division by zero");
                """)
        ).parse()
    );
    assertEquals(1, attempt.handlers().size());
    AttemptExpression.ErrorHandler handler = attempt.handlers().get(0);
    ErrorType errorType = assertInstanceOf(ErrorType.class, handler.errorType());
    assertEquals("DIVISION_BY_ZERO", errorType.errorKind().name());
  }

  @Test
  void parsesErrorHandlerAction() {
    AttemptExpression attempt = assertInstanceOf(
        AttemptExpression.class,
        new AttemptExpressionParser(
            cursor("""
                attempt {
                    10 / x;
                } => -1
                onError(DivisionByZero) => print("Division by zero");
                """)
        ).parse()
    );
    IStatement action = attempt.handlers().get(0).action();
    ExpressionStatement statement = assertInstanceOf(ExpressionStatement.class, action);
    assertNotNull(statement.expression());
  }
}