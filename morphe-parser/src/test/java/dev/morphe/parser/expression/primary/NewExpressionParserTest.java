package dev.morphe.parser.expression.primary;

import dev.morphe.ast.expression.ArrayCreationExpression;
import dev.morphe.ast.expression.CompositeInstanceExpression;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NewExpressionParserTest extends AParserTest {

  @Test
  void parsesArrayCreationExpression() {
    IExpression result = new NewExpressionParser(
        cursor("new Integer[10]")
    ).parse();
    ArrayCreationExpression expression = assertInstanceOf(ArrayCreationExpression.class, result);
    assertEquals(PrimitiveType.INTEGER, expression.elementType());
    assertEquals(1, expression.dimensions().size());
  }

  @Test
  void delegatesToCompositeInstanceExpressionParser() {
    CompositeInstanceExpression expression = assertInstanceOf(
        CompositeInstanceExpression.class,
        new NewExpressionParser(
            cursor("new Person { age = 25; }")
        ).parse()
    );
    assertEquals("Person", expression.typeName());
    assertEquals(1, expression.fields().size());
    assertEquals("age", expression.fields().get(0).name());
  }

  @Test
  void rejectsUnsupportedCreationExpression() {
    assertThrows(
        UnexpectedTokenException.class, () -> new NewExpressionParser(
            cursor("new Integer")
        ).parse()
    );
  }
}