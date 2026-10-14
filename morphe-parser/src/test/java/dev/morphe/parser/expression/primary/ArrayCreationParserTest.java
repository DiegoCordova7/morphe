package dev.morphe.parser.expression.primary;

import dev.morphe.ast.expression.ArrayCreationExpression;
import dev.morphe.ast.expression.BinaryExpression;
import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayCreationParserTest extends AParserTest {

  @Test
  void parsesSingleDimensionArrayCreation() {
    ArrayCreationExpression expression =
        assertInstanceOf(
            ArrayCreationExpression.class,
            new ArrayCreationParser(cursor("new Integer[10]")).parse()
        );
    assertEquals(PrimitiveType.INTEGER, expression.elementType());
    assertEquals(1, expression.dimensions().size());
    LiteralExpression dimension = assertInstanceOf(LiteralExpression.class, expression.dimensions().get(0));
    assertEquals(10, dimension.value());
  }

  @Test
  void parsesMultidimensionalArrayCreation() {
    ArrayCreationExpression expression =
        assertInstanceOf(
            ArrayCreationExpression.class,
            new ArrayCreationParser(
                cursor("new Integer[10][20]")
            ).parse()
        );
    assertEquals(PrimitiveType.INTEGER, expression.elementType());
    assertEquals(2, expression.dimensions().size());
  }

  @Test
  void parsesExpressionAsDimension() {
    ArrayCreationExpression expression =
        assertInstanceOf(
            ArrayCreationExpression.class,
            new ArrayCreationParser(
                cursor("new Integer[size + 1]")
            ).parse()
        );

    assertEquals(1, expression.dimensions().size());
    assertInstanceOf(BinaryExpression.class, expression.dimensions().get(0));
  }

  @Test
  void rejectsMissingDimension() {
    assertThrows(
        UnexpectedTokenException.class, () -> new ArrayCreationParser(cursor("new Integer")).parse()
    );
  }

  @Test
  void rejectsEmptyDimension() {
    assertThrows(
        UnexpectedTokenException.class, () -> new ArrayCreationParser(cursor("new Integer[]")).parse()
    );
  }
}