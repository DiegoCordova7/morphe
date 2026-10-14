package dev.morphe.parser.expression.lambda;

import dev.morphe.ast.declaration.Parameter;
import dev.morphe.ast.expression.*;
import dev.morphe.ast.type.NamedType;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LambdaExpressionParserTest extends AParserTest {

  @Test
  void parsesLambdaWithoutParameters() {
    LambdaExpression lambda = assertInstanceOf(
        LambdaExpression.class,
        new LambdaExpressionParser(
            cursor("() -> 42")
        ).parse()
    );
    assertTrue(lambda.parameters().isEmpty());
    LiteralExpression body = assertInstanceOf(LiteralExpression.class, lambda.body());
    assertEquals(42, body.value());
  }

  @Test
  void parsesLambdaWithOneParameter() {
    LambdaExpression lambda = assertInstanceOf(
        LambdaExpression.class,
        new LambdaExpressionParser(
            cursor("(Integer x) -> x")
        ).parse()
    );
    assertEquals(1, lambda.parameters().size());
    Parameter parameter = lambda.parameters().get(0);
    assertEquals("x", parameter.name());
    assertEquals(PrimitiveType.INTEGER, parameter.type());
    VariableExpression body = assertInstanceOf(VariableExpression.class, lambda.body());
    assertEquals("x", body.name());
  }

  @Test
  void parsesLambdaWithMultipleParameters() {
    LambdaExpression lambda = assertInstanceOf(
        LambdaExpression.class,
        new LambdaExpressionParser(
            cursor("(Integer a, Integer b) -> a + b")
        ).parse()
    );
    assertEquals(2, lambda.parameters().size());
    assertEquals("a", lambda.parameters().get(0).name());
    assertEquals(PrimitiveType.INTEGER, lambda.parameters().get(0).type());
    assertEquals("b", lambda.parameters().get(1).name());
    assertEquals(PrimitiveType.INTEGER, lambda.parameters().get(1).type());
    assertInstanceOf(BinaryExpression.class, lambda.body());
  }

  @Test
  void parsesLambdaWithNamedTypeParameter() {
    LambdaExpression lambda = assertInstanceOf(
        LambdaExpression.class,
        new LambdaExpressionParser(
            cursor("(Person person) -> person.name")
        ).parse()
    );
    assertEquals(1, lambda.parameters().size());
    Parameter parameter = lambda.parameters().get(0);
    assertEquals("person", parameter.name());
    NamedType type = assertInstanceOf(NamedType.class, parameter.type());
    assertEquals("Person", type.name());
    FieldAccessExpression body = assertInstanceOf(FieldAccessExpression.class, lambda.body());
    VariableExpression target = assertInstanceOf(VariableExpression.class, body.target());
    assertEquals("person", target.name());
    assertEquals("name", body.field());
  }

  @Test
  void parsesLambdaWithArrayParameter() {
    LambdaExpression lambda = assertInstanceOf(
        LambdaExpression.class,
        new LambdaExpressionParser(
            cursor("(Integer[] values) -> values")
        ).parse()
    );
    assertEquals(1, lambda.parameters().size());
    assertEquals("values", lambda.parameters().get(0).name());
  }
}