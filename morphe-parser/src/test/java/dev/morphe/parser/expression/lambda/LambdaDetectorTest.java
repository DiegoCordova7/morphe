package dev.morphe.parser.expression.lambda;

import dev.morphe.ast.expression.LambdaExpression;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LambdaDetectorTest extends AParserTest {

  @Test
  void recognizesLambdaWithoutParameters() {
    assertTrue(
        LambdaDetector.starts(cursor("() -> 42"))
    );
  }

  @Test
  void recognizesLambdaWithOneParameter() {
    assertTrue(
        LambdaDetector.starts(cursor("(Integer x) -> x"))
    );
  }

  @Test
  void recognizesLambdaWithMultipleParameters() {
    assertTrue(
        LambdaDetector.starts(cursor("(Integer a, Integer b) -> a + b"))
    );
  }

  @Test
  void recognizesLambdaWithNamedTypeParameter() {
    assertTrue(
        LambdaDetector.starts(cursor("(Person person) -> person.name"))
    );
  }

  @Test
  void recognizesLambdaWithArrayParameter() {
    assertTrue(
        LambdaDetector.starts(
            cursor("(Integer[] values) -> values")
        )
    );
  }

  @Test
  void recognizesLambdaWithNestedArrayParameter() {
    assertTrue(
        LambdaDetector.starts(
            cursor("(Integer[][] values) -> values")
        )
    );
  }

  @Test
  void recognizesLambdaWithNamedArrayParameter() {
    assertTrue(
        LambdaDetector.starts(
            cursor("(Person[] people) -> people")
        )
    );
  }

  @Test
  void rejectsGroupingExpression() {
    assertFalse(
        LambdaDetector.starts(cursor("(42 + 10)"))
    );
  }

  @Test
  void rejectsIncompleteLambda() {
    assertFalse(
        LambdaDetector.starts(cursor("(Integer x)"))
    );
  }

  @Test
  void rejectsMissingParameterName() {
    assertFalse(
        LambdaDetector.starts(cursor("(Integer) -> 42"))
    );
  }

  @Test
  void rejectsMissingArrow() {
    assertFalse(
        LambdaDetector.starts(cursor("(Integer x) 42"))
    );
  }

  @Test
  void rejectsMissingClosingParenthesis() {
    assertFalse(
        LambdaDetector.starts(cursor("(Integer x -> x"))
    );
  }

  @Test
  void rejectsNonParenthesizedExpression() {
    assertFalse(
        LambdaDetector.starts(cursor("42"))
    );
  }

  @Test
  void rejectsTrailingComma() {
    assertFalse(
        LambdaDetector.starts(
            cursor("(Integer x,) -> x")
        )
    );
  }

  @Test
  void preservesSourceRange() {
    LambdaExpression lambda = assertInstanceOf(
        LambdaExpression.class,
        new LambdaExpressionParser(
            cursor("(Integer x) -> x")
        ).parse()
    );
    assertEquals(0, lambda.range().start().offset());
    assertEquals(16, lambda.range().end().offset());
  }
}