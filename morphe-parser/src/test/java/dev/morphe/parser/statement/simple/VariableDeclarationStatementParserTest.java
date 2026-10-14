package dev.morphe.parser.statement.simple;

import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.ast.statement.VariableDeclarationStatement;
import dev.morphe.ast.type.ArrayType;
import dev.morphe.ast.type.NamedType;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VariableDeclarationStatementParserTest extends AParserTest {

  @Test
  void parsesVariableWithoutInitializer() {
    IStatement statement = new VariableDeclarationStatementParser(
        cursor("Integer value;")
    ).parse();
    VariableDeclarationStatement declaration = assertInstanceOf(VariableDeclarationStatement.class, statement);
    assertEquals("value", declaration.name());
    assertEquals(PrimitiveType.INTEGER, declaration.type());
    assertFalse(declaration.isConstant());
    assertNull(declaration.initializer());
  }

  @Test
  void parsesVariableWithInitializer() {
    IStatement statement = new VariableDeclarationStatementParser(
        cursor("Integer value = 10;")
    ).parse();
    VariableDeclarationStatement declaration = assertInstanceOf(VariableDeclarationStatement.class, statement);
    assertEquals("value", declaration.name());
    assertEquals(PrimitiveType.INTEGER, declaration.type());
    assertFalse(declaration.isConstant());
    LiteralExpression initializer = assertInstanceOf(LiteralExpression.class, declaration.initializer());
    assertEquals(10, initializer.value());
  }

  @Test
  void parsesConstantWithInitializer() {
    IStatement statement = new VariableDeclarationStatementParser(
        cursor("#Integer MAX = 100;")
    ).parse();
    VariableDeclarationStatement declaration = assertInstanceOf(VariableDeclarationStatement.class, statement);
    assertEquals("MAX", declaration.name());
    assertEquals(PrimitiveType.INTEGER, declaration.type());
    assertTrue(declaration.isConstant());
    LiteralExpression initializer = assertInstanceOf(LiteralExpression.class, declaration.initializer());
    assertEquals(100, initializer.value());
  }

  @Test
  void parsesNamedTypeDeclaration() {
    IStatement statement = new VariableDeclarationStatementParser(
        cursor("Person person;")
    ).parse();
    VariableDeclarationStatement declaration = assertInstanceOf(VariableDeclarationStatement.class, statement);
    NamedType type = assertInstanceOf(NamedType.class, declaration.type());
    assertEquals("Person", type.name());
    assertEquals("person", declaration.name());
    assertFalse(declaration.isConstant());
    assertNull(declaration.initializer());
  }

  @Test
  void parsesNamedTypeConstant() {
    IStatement statement = new VariableDeclarationStatementParser(
        cursor("#Person DEFAULT_PERSON = person;")
    ).parse();
    VariableDeclarationStatement declaration = assertInstanceOf(VariableDeclarationStatement.class, statement);
    NamedType type = assertInstanceOf(NamedType.class, declaration.type());
    assertEquals("Person", type.name());
    assertEquals("DEFAULT_PERSON", declaration.name());
    assertTrue(declaration.isConstant());
    assertNotNull(declaration.initializer());
  }

  @Test
  void parsesArrayTypeDeclaration() {
    IStatement statement = new VariableDeclarationStatementParser(
        cursor("Integer[][] values;")
    ).parse();
    VariableDeclarationStatement declaration = assertInstanceOf(VariableDeclarationStatement.class, statement);
    assertEquals("values", declaration.name());
    ArrayType outer = assertInstanceOf(ArrayType.class, declaration.type());
    ArrayType inner = assertInstanceOf(ArrayType.class, outer.elementType());
    assertEquals(PrimitiveType.INTEGER, inner.elementType());
  }

  @Test
  void parsesInitializerExpression() {
    IStatement statement = new VariableDeclarationStatementParser(
        cursor("Integer value = 1 + 2 * 3;")
    ).parse();
    VariableDeclarationStatement declaration = assertInstanceOf(VariableDeclarationStatement.class, statement);
    assertNotNull(declaration.initializer());
  }

  @Test
  void rejectsConstantWithoutInitializer() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new VariableDeclarationStatementParser(cursor("#Integer PI;")).parse()
    );
  }

  @Test
  void preservesSourceRange() {
    IStatement statement = new VariableDeclarationStatementParser(
        cursor("Integer value = 10;")
    ).parse();
    SourceRange range = statement.range();
    assertEquals(0, range.start().offset());
    assertEquals(19, range.end().offset());
  }
}