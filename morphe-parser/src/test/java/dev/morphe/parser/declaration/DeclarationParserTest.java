package dev.morphe.parser.declaration;

import dev.morphe.ast.declaration.CompositeDeclaration;
import dev.morphe.ast.declaration.FunctionDeclaration;
import dev.morphe.ast.declaration.IDeclaration;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class DeclarationParserTest extends AParserTest {

  @Test
  void dispatchesFunctionDeclaration() {
    IDeclaration declaration = new DeclarationParser(
        cursor("""
            func add() : Integer {
            }
            """)
    ).parse();
    assertInstanceOf(FunctionDeclaration.class, declaration);
  }

  @Test
  void dispatchesCompositeDeclaration() {
    IDeclaration declaration = new DeclarationParser(
        cursor("""
            type Person {
                name : String;
            }
            """)
    ).parse();
    assertInstanceOf(CompositeDeclaration.class, declaration);
  }
}