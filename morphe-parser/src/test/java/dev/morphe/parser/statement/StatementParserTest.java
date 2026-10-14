package dev.morphe.parser.statement;

import dev.morphe.ast.expression.FieldAccessExpression;
import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.ast.operator.AssignmentOperator;
import dev.morphe.ast.statement.*;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatementParserTest extends AParserTest {

  @Test
  void parsesExpressionStatement() {
    IStatement statement = new StatementParser(
        cursor("42;")
    ).parse();
    ExpressionStatement expression = assertInstanceOf(ExpressionStatement.class, statement);
    LiteralExpression literal = assertInstanceOf(LiteralExpression.class, expression.expression());
    assertEquals(42, literal.value());
  }

  @Test
  void dispatchesPrimitiveVariableDeclaration() {
    IStatement statement = new StatementParser(
        cursor("value : Integer = 10;")
    ).parse();
    assertInstanceOf(VariableDeclarationStatement.class, statement);
  }

  @Test
  void dispatchesNamedTypeVariableDeclaration() {
    IStatement statement = new StatementParser(
        cursor("person : Person;")
    ).parse();
    assertInstanceOf(VariableDeclarationStatement.class, statement);
  }

  @Test
  void dispatchesConstantDeclaration() {
    IStatement statement = new StatementParser(
        cursor("PI : #Integer = 3.1416;")
    ).parse();
    VariableDeclarationStatement declaration = assertInstanceOf(VariableDeclarationStatement.class, statement);
    assertTrue(declaration.isConstant());
  }

  @Test
  void doesNotMistakeAssignmentForDeclaration() {
    IStatement statement = new StatementParser(
        cursor("value = 10;")
    ).parse();
    assertInstanceOf(AssignmentStatement.class, statement);
  }

  @Test
  void dispatchesCompoundAssignment() {
    IStatement statement = new StatementParser(
        cursor("value += 10;")
    ).parse();
    AssignmentStatement assignment = assertInstanceOf(AssignmentStatement.class, statement);
    assertEquals(AssignmentOperator.ADD, assignment.operator());
  }

  @Test
  void dispatchesFieldAssignment() {
    IStatement statement = new StatementParser(
        cursor("person.name = \"Diego\";")
    ).parse();
    AssignmentStatement assignment = assertInstanceOf(AssignmentStatement.class, statement);
    assertInstanceOf(FieldAccessExpression.class, assignment.target());
  }

  @Test
  void dispatchesReturnStatement() {
    IStatement statement = new StatementParser(
        cursor("return 42;")
    ).parse();
    assertInstanceOf(ReturnStatement.class, statement);
  }

  @Test
  void dispatchesBreakStatement() {
    IStatement statement = new StatementParser(
        cursor("break;")
    ).parse();
    assertInstanceOf(BreakStatement.class, statement);
  }

  @Test
  void dispatchesContinueStatement() {
    IStatement statement = new StatementParser(
        cursor("continue;")
    ).parse();
    assertInstanceOf(ContinueStatement.class, statement);
  }

  @Test
  void dispatchesIfStatement() {
    IStatement statement = new StatementParser(
        cursor("if (true) { return 1; }")
    ).parse();
    assertInstanceOf(IfStatement.class, statement);
  }

  @Test
  void rejectsUnexpectedToken() {
    assertThrows(UnexpectedTokenException.class, () -> new StatementParser(cursor(")")).parse());
  }
}