package dev.morphe.parser.declaration;

import dev.morphe.ast.declaration.FunctionDeclaration;
import dev.morphe.ast.declaration.Parameter;
import dev.morphe.ast.type.ArrayType;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FunctionParserTest extends AParserTest {

  @Test
  void parsesFunctionDeclaration() {
    FunctionDeclaration declaration = new FunctionParser(
        cursor("""
            func add(a : Integer, b : Integer) : Integer {
            }
            """)
    ).parse();
    assertEquals("add", declaration.name());
    assertEquals(2, declaration.parameters().size());
    assertEquals(PrimitiveType.INTEGER, declaration.returnType());
    assertNotNull(declaration.body());
  }

  @Test
  void parsesFunctionWithoutParameters() {
    FunctionDeclaration declaration = new FunctionParser(
        cursor("""
            func getValue() : Integer {
            }
            """)
    ).parse();
    assertEquals("getValue", declaration.name());
    assertTrue(declaration.parameters().isEmpty());
    assertEquals(PrimitiveType.INTEGER, declaration.returnType());
  }

  @Test
  void parsesParameters() {
    FunctionDeclaration declaration = new FunctionParser(
        cursor("""
            func greet(name : String, age : Integer) : String {
            }
            """)
    ).parse();
    assertEquals(2, declaration.parameters().size());
    Parameter name = declaration.parameters().get(0);
    assertEquals("name", name.name());
    assertEquals(PrimitiveType.STRING, name.type());
    Parameter age = declaration.parameters().get(1);
    assertEquals("age", age.name());
    assertEquals(PrimitiveType.INTEGER, age.type());
  }

  @Test
  void parsesArrayParameter() {
    FunctionDeclaration declaration = new FunctionParser(
        cursor("""
            func sum(values : Integer[]) : Integer {
            }
            """)
    ).parse();
    Parameter parameter = declaration.parameters().get(0);
    assertEquals("values", parameter.name());
    assertInstanceOf(ArrayType.class, parameter.type());
    assertEquals(PrimitiveType.INTEGER, ((ArrayType) parameter.type()).elementType());
  }

  @Test
  void storesSourceRange() {
    FunctionDeclaration declaration = new FunctionParser(
        cursor("""
          func add(a : Integer) : Integer {
          }
          """)
    ).parse();
    assertEquals(0, declaration.range().start().offset());
    assertTrue(declaration.range().end().offset() > 0);
  }
}