package dev.morphe.parser.declaration;

import dev.morphe.ast.declaration.CompositeDeclaration;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.type.ArrayType;
import dev.morphe.ast.type.CompositeType;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.argument.NullArgumentException;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompositeParserTest extends AParserTest {

  @Test
  void parsesCompositeDeclaration() {
    CompositeDeclaration declaration = new CompositeParser(
        cursor("""
            type Person {
                name : String;
                age : Integer;
            }
            """)
    ).parse();
    assertEquals("Person", declaration.name());
    assertEquals(2, declaration.fields().size());
    CompositeType.CompositeField name = declaration.fields().get(0);
    CompositeType.CompositeField age = declaration.fields().get(1);
    assertEquals("name", name.name());
    assertEquals(PrimitiveType.STRING, name.type());
    assertEquals("age", age.name());
    assertEquals(PrimitiveType.INTEGER, age.type());
  }

  @Test
  void parsesCompositeWithoutFields() {
    CompositeDeclaration declaration = new CompositeParser(
        cursor("""
            type Empty {
            }
            """)
    ).parse();
    assertEquals("Empty", declaration.name());
    assertEquals(List.of(), declaration.fields());
  }

  @Test
  void parsesArrayField() {
    CompositeDeclaration declaration = new CompositeParser(
        cursor("""
            type Matrix {
                values : Integer[][];
            }
            """)
    ).parse();
    CompositeType.CompositeField field = declaration.fields().get(0);
    assertEquals("values", field.name());
    ArrayType outer = assertInstanceOf(ArrayType.class, field.type());
    ArrayType inner = assertInstanceOf(ArrayType.class, outer.elementType());
    assertEquals(PrimitiveType.INTEGER, inner.elementType());
  }

  @Test
  void parsesMultipleFieldsInDeclarationOrder() {
    CompositeDeclaration declaration = new CompositeParser(
        cursor("""
            type Person {
                name : String;
                age : Integer;
                active : Boolean;
            }
            """)
    ).parse();
    assertEquals(
        List.of("name", "age", "active"),
        declaration.fields().stream()
            .map(CompositeType.CompositeField::name)
            .toList()
    );
  }

  @Test
  void storesSourceRange() {
    CompositeDeclaration declaration = new CompositeParser(
        cursor("type Person { name : String; }")
    ).parse();
    SourceRange range = declaration.range();
    assertEquals(0, range.start().offset());
    assertEquals(30, range.end().offset());
  }

  @Test
  void rejectsNullCursor() {
    assertThrows(NullArgumentException.class, () -> new CompositeParser(null));
  }

  @Test
  void rejectsMissingTypeKeyword() {
    assertThrows(UnexpectedTokenException.class, () -> new CompositeParser(
        cursor("Person { name : String; }")).parse()
    );
  }

  @Test
  void rejectsMissingName() {
    assertThrows(UnexpectedTokenException.class, () -> new CompositeParser(
        cursor("type { name : String; }")).parse()
    );
  }

  @Test
  void rejectsMissingOpeningBrace() {
    assertThrows(UnexpectedTokenException.class, () -> new CompositeParser(
        cursor("type Person name : String; }")).parse()
    );
  }

  @Test
  void rejectsMissingFieldName() {
    assertThrows(UnexpectedTokenException.class, () -> new CompositeParser(
        cursor("type Person { : String; }")).parse()
    );
  }

  @Test
  void rejectsMissingColon() {
    assertThrows(UnexpectedTokenException.class, () -> new CompositeParser(
        cursor("type Person { name String; }")).parse()
    );
  }

  @Test
  void rejectsMissingFieldType() {
    assertThrows(UnexpectedTokenException.class, () -> new CompositeParser(
        cursor("type Person { name : ; }")).parse()
    );
  }

  @Test
  void rejectsMissingSemicolon() {
    assertThrows(UnexpectedTokenException.class, () -> new CompositeParser(
        cursor("type Person { name : String }")).parse()
    );
  }

  @Test
  void rejectsMissingClosingBrace() {
    assertThrows(UnexpectedTokenException.class, () -> new CompositeParser(
        cursor("type Person { name : String;")).parse()
    );
  }
}