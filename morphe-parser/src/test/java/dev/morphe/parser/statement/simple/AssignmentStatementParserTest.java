package dev.morphe.parser.statement.simple;

import dev.morphe.ast.expression.*;
import dev.morphe.ast.operator.AssignmentOperator;
import dev.morphe.ast.statement.AssignmentStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentStatementParserTest extends AParserTest {


  static Stream<Arguments> assignmentOperators() {
    return Stream.of(
        Arguments.of("value = 10;", AssignmentOperator.ASSIGN),
        Arguments.of("value += 10;", AssignmentOperator.ADD),
        Arguments.of("value -= 10;", AssignmentOperator.SUBTRACT),
        Arguments.of("value *= 10;", AssignmentOperator.MULTIPLY),
        Arguments.of("value /= 10;", AssignmentOperator.DIVIDE),
        Arguments.of("value %= 10;", AssignmentOperator.MODULO)
    );
  }

  @ParameterizedTest
  @MethodSource("assignmentOperators")
  void parsesAssignmentOperator(String source, AssignmentOperator expected) {
    IStatement statement = new AssignmentStatementParser(
        cursor(source)
    ).parse();
    AssignmentStatement assignment = assertInstanceOf(AssignmentStatement.class, statement);
    assertEquals(expected, assignment.operator());
    VariableExpression target = assertInstanceOf(VariableExpression.class, assignment.target());
    assertEquals("value", target.name());
    LiteralExpression value = assertInstanceOf(LiteralExpression.class, assignment.value());
    assertEquals(10, value.value());
  }

  @Test
  void parsesArrayAccessTarget() {
    AssignmentStatement assignment = assertInstanceOf(AssignmentStatement.class, new AssignmentStatementParser(
        cursor("values[index] = 10;")).parse()
    );
    assertInstanceOf(ArrayAccessExpression.class, assignment.target());
    LiteralExpression value = assertInstanceOf(LiteralExpression.class, assignment.value());
    assertEquals(10, value.value());
  }

  @Test
  void parsesFieldAccessTarget() {
    AssignmentStatement assignment = assertInstanceOf(AssignmentStatement.class, new AssignmentStatementParser(
        cursor("person.name = 10;")).parse()
    );
    assertInstanceOf(FieldAccessExpression.class, assignment.target());
  }

  @Test
  void parsesNestedFieldAccessTarget() {
    AssignmentStatement assignment = assertInstanceOf(AssignmentStatement.class, new AssignmentStatementParser(
        cursor("person.address.city = 10;")).parse()
    );
    assertInstanceOf(FieldAccessExpression.class, assignment.target());
  }

  @Test
  void parsesExpressionAsValue() {
    AssignmentStatement assignment = assertInstanceOf(AssignmentStatement.class, new AssignmentStatementParser(
        cursor("value = other + 10;")).parse()
    );
    assertInstanceOf(BinaryExpression.class, assignment.value());
  }

  @Test
  void preservesSourceRange() {
    AssignmentStatement assignment = assertInstanceOf(AssignmentStatement.class, new AssignmentStatementParser(
        cursor("value = 10;")).parse()
    );
    assertEquals(0, assignment.range().start().offset());
    assertEquals(11, assignment.range().end().offset());
  }

  @Test
  void rejectsNonAssignmentOperator() {
    assertThrows(
        UnexpectedTokenException.class, () -> new AssignmentStatementParser(cursor("value == 10;")).parse()
    );
  }

  @Test
  void rejectsMissingAssignmentValue() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new AssignmentStatementParser(
            cursor("value = ;")
        ).parse()
    );
  }

  @Test
  void rejectsMissingSemicolon() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new AssignmentStatementParser(
            cursor("value = 10")
        ).parse()
    );
  }
}