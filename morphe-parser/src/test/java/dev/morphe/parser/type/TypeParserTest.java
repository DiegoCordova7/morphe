package dev.morphe.parser.type;

import dev.morphe.ast.type.*;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class TypeParserTest extends AParserTest {

  @ParameterizedTest
  @CsvSource({
      "Integer, INTEGER",
      "Double, DOUBLE",
      "Boolean, BOOLEAN",
      "String, STRING",
      "Void, VOID"
  })
  void parsesPrimitiveType(String source, PrimitiveType expected) {
    IASTType type = new TypeParser(cursor(source)).parse();
    assertEquals(expected, type);
  }

  @Test
  void parsesNamedType() {
    IASTType type = new TypeParser(cursor("Person")).parse();
    NamedType named = assertInstanceOf(NamedType.class, type);
    assertEquals("Person", named.name());
  }

  @Test
  void parsesArrayType() {
    IASTType type = new TypeParser(cursor("Integer[]")).parse();
    ArrayType array = assertInstanceOf(ArrayType.class, type);
    assertEquals(PrimitiveType.INTEGER, array.elementType());
  }

  @Test
  void parsesNestedArrayType() {
    IASTType type = new TypeParser(cursor("Integer[][]")).parse();
    ArrayType outer = assertInstanceOf(ArrayType.class, type);
    ArrayType inner = assertInstanceOf(ArrayType.class, outer.elementType());
    assertEquals(PrimitiveType.INTEGER, inner.elementType());
  }

  @Test
  void parsesNamedArrayType() {
    IASTType type = new TypeParser(cursor("Person[]")).parse();
    ArrayType array = assertInstanceOf(ArrayType.class, type);
    NamedType element = assertInstanceOf(NamedType.class, array.elementType());
    assertEquals("Person", element.name());
  }

  @Test
  void rejectsInvalidBaseType() {
    assertThrows(UnexpectedTokenException.class, () -> new TypeParser(cursor("42")).parse());
  }

  @Test
  void rejectsMissingArrayClosingBracket() {
    assertThrows(UnexpectedTokenException.class, () -> new TypeParser(cursor("Integer[")).parse());
  }

  @Test
  void rejectsInvalidArraySyntax() {
    assertThrows(UnexpectedTokenException.class, () -> new TypeParser(cursor("Integer[x]")).parse());
  }

  @Test
  void leavesFollowingTokenUnconsumed() {
    TokenCursor cursor = cursor("Integer[] value");
    new TypeParser(cursor).parse();
    assertEquals("value", cursor.current().lexeme());
  }

  @ParameterizedTest
  @CsvSource({
      "DivisionByZero, DIVISION_BY_ZERO",
      "ModuloByZero, MODULO_BY_ZERO",
      "InvalidArrayIndex, INVALID_ARRAY_INDEX"
  })
  void parsesErrorType(String source, ErrorKind expected) {
    IASTType type = new TypeParser(cursor(source)).parse();
    ErrorType error = assertInstanceOf(ErrorType.class, type);
    assertEquals(expected, error.errorKind());
  }
}