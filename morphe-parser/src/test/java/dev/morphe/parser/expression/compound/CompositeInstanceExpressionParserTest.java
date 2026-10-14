package dev.morphe.parser.expression.compound;

import dev.morphe.ast.expression.BinaryExpression;
import dev.morphe.ast.expression.CompositeInstanceExpression;
import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompositeInstanceExpressionParserTest extends AParserTest {

  @Test
  void parsesEmptyCompositeInstance() {
    CompositeInstanceExpression expression = (CompositeInstanceExpression) new CompositeInstanceExpressionParser(
        cursor("new Person {}")
    ).parse();
    assertEquals("Person", expression.typeName());
    assertTrue(expression.fields().isEmpty());
  }

  @Test
  void parsesSingleFieldInitializer() {
    CompositeInstanceExpression expression = (CompositeInstanceExpression) new CompositeInstanceExpressionParser(
        cursor("""
            new  Person {
                name = "Diego";
            }
            """)
        ).parse();
    assertEquals("Person", expression.typeName());
    assertEquals(1, expression.fields().size());
    CompositeInstanceExpression.FieldInitializer field = expression.fields().get(0);
    assertEquals("name", field.name());
    LiteralExpression value = assertInstanceOf(LiteralExpression.class, field.value());
    assertEquals("Diego", value.value());
  }

  @Test
  void parsesMultipleFieldInitializers() {
    CompositeInstanceExpression expression = (CompositeInstanceExpression) new CompositeInstanceExpressionParser(
        cursor("""
            new Person {
                name = "Diego";
                age = 25;
            }
            """)
        ).parse();
    assertEquals("Person", expression.typeName());
    assertEquals(2, expression.fields().size());
    assertEquals("name", expression.fields().get(0).name());
    assertEquals("age", expression.fields().get(1).name());
    LiteralExpression name = assertInstanceOf(LiteralExpression.class, expression.fields().get(0).value());
    LiteralExpression age = assertInstanceOf(LiteralExpression.class, expression.fields().get(1).value());
    assertEquals("Diego", name.value());
    assertEquals(25, age.value());
  }

  @Test
  void parsesExpressionAsFieldValue() {
    CompositeInstanceExpression expression = (CompositeInstanceExpression) new CompositeInstanceExpressionParser(
        cursor("""
            new Person {
                age = 20 + 5;
            }
            """)
        ).parse();
    CompositeInstanceExpression.FieldInitializer field = expression.fields().get(0);
    assertEquals("age", field.name());
    assertNotNull(field.value());
  }

  @Test
  void parsesVariableAsFieldValue() {
    CompositeInstanceExpression expression = (CompositeInstanceExpression) new CompositeInstanceExpressionParser(
        cursor("""
            new Person {
                name = otherName;
            }
            """)
        ).parse();
    CompositeInstanceExpression.FieldInitializer field = expression.fields().get(0);
    VariableExpression value = assertInstanceOf(VariableExpression.class, field.value());
    assertEquals("otherName", value.name());
  }

  @Test
  void rejectsMissingFieldValue() {
    assertThrows(UnexpectedTokenException.class, () -> new CompositeInstanceExpressionParser(
        cursor("""
            new Person {
                name = ;
            }
            """)
        ).parse()
    );
  }

  @Test
  void rejectsMissingFieldTerminator() {
    assertThrows(
        UnexpectedTokenException.class, () -> new CompositeInstanceExpressionParser(
            cursor("""
                new Person {
                    name = "Diego"
                }
                """)
        ).parse()
    );
  }

  @Test
  void rejectsUnclosedCompositeInstance() {
    assertThrows(
        UnexpectedTokenException.class, () -> new CompositeInstanceExpressionParser(
            cursor("""
                new Person {
                    name = "Diego";
                """)
        ).parse()
    );
  }

  @Test
  void parsesBinaryExpressionAsFieldValue() {
    CompositeInstanceExpression expression = (CompositeInstanceExpression) new CompositeInstanceExpressionParser(
        cursor("new Person { age = 20 + 5; }")
    ).parse();
    CompositeInstanceExpression.FieldInitializer field = expression.fields().get(0);
    assertEquals("age", field.name());
    assertInstanceOf(BinaryExpression.class, field.value());
  }

  @Test
  void rejectsMissingFieldName() {
    assertThrows(
        UnexpectedTokenException.class, () -> new CompositeInstanceExpressionParser(
            cursor("new Person { = 25; }")
        ).parse()
    );
  }

  @Test
  void rejectsMissingAssignmentOperator() {
    assertThrows(
        UnexpectedTokenException.class, () -> new CompositeInstanceExpressionParser(
            cursor("new Person { age 25; }")
        ).parse()
    );
  }
}