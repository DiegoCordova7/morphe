package dev.morphe.semantic.symbol;

import dev.morphe.ast.type.IASTType;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SymbolTest {

  @Test
  void shouldCreateSymbol() {
    IASTType type = createType();
    Symbol symbol = new Symbol("value", SymbolKind.VARIABLE, type, false);
    assertEquals("value", symbol.name());
    assertEquals(SymbolKind.VARIABLE, symbol.kind());
    assertSame(type, symbol.type());
  }

  @Test
  void shouldRejectNullName() {
    IASTType type = createType();
    NullSemanticArgumentException exception = assertThrows(
        NullSemanticArgumentException.class,
        () -> new Symbol(null, SymbolKind.VARIABLE, type, false)
    );
    assertEquals("Argument 'name' cannot be null", exception.getMessage());
  }

  @Test
  void shouldRejectNullKind() {
    IASTType type = createType();
    NullSemanticArgumentException exception = assertThrows(
        NullSemanticArgumentException.class,
        () -> new Symbol("value", null, type, false)
    );
    assertEquals("Argument 'kind' cannot be null", exception.getMessage());
  }

  @Test
  void shouldRejectNullType() {
    NullSemanticArgumentException exception = assertThrows(
        NullSemanticArgumentException.class,
        () -> new Symbol("value", SymbolKind.VARIABLE, null, false)
    );
    assertEquals("Argument 'type' cannot be null", exception.getMessage());
  }

  private IASTType createType() {
    return PrimitiveType.INTEGER;
  }
}