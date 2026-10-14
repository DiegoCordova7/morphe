package dev.morphe.semantic.symbol;

import dev.morphe.ast.type.IASTType;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.semantic.exception.scope.CannotExitGlobalScopeException;
import dev.morphe.semantic.scope.Scope;
import dev.morphe.semantic.scope.ScopeKind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SymbolTableTest {

  @Test
  void shouldStartInGlobalScope() {
    SymbolTable table = new SymbolTable();
    assertEquals(ScopeKind.GLOBAL, table.currentScope().kind());
    assertNull(table.currentScope().parent());
  }

  @Test
  void shouldEnterScope() {
    SymbolTable table = new SymbolTable();
    Scope global = table.currentScope();
    table.enterScope(ScopeKind.FUNCTION);
    assertEquals(ScopeKind.FUNCTION, table.currentScope().kind());
    assertSame(global, table.currentScope().parent());
  }

  @Test
  void shouldExitScope() {
    SymbolTable table = new SymbolTable();
    Scope global = table.currentScope();
    table.enterScope(ScopeKind.FUNCTION);
    table.exitScope();
    assertSame(global, table.currentScope());
  }

  @Test
  void shouldNotExitGlobalScope() {
    SymbolTable table = new SymbolTable();
    CannotExitGlobalScopeException exception = assertThrows(
        CannotExitGlobalScopeException.class,
        table::exitScope
    );
    assertEquals("Cannot exit global scope", exception.getMessage());
  }

  @Test
  void shouldDefineSymbolInCurrentScope() {
    SymbolTable table = new SymbolTable();
    Symbol symbol = createSymbol("value");
    table.define(symbol);
    assertSame(symbol, table.resolve("value"));
  }

  @Test
  void shouldResolveSymbolFromParentScope() {
    SymbolTable table = new SymbolTable();
    Symbol symbol = createSymbol("value");
    table.define(symbol);
    table.enterScope(ScopeKind.FUNCTION);
    assertSame(symbol, table.resolve("value"));
  }

  @Test
  void shouldNotExposeChildSymbolToParentScope() {
    SymbolTable table = new SymbolTable();
    table.enterScope(ScopeKind.FUNCTION);
    Symbol symbol = createSymbol("value");
    table.define(symbol);
    table.exitScope();
    assertNull(table.resolve("value"));
  }

  @Test
  void shouldPreferSymbolFromCurrentScope() {
    SymbolTable table = new SymbolTable();
    Symbol globalSymbol = createSymbol("value");
    table.define(globalSymbol);
    table.enterScope(ScopeKind.FUNCTION);
    Symbol localSymbol = createSymbol("value");
    table.define(localSymbol);
    assertSame(localSymbol, table.resolve("value"));
  }

  private Symbol createSymbol(String name) {
    IASTType type = createType();
    return new Symbol(name, SymbolKind.VARIABLE, type, false);
  }

  private IASTType createType() {
    return PrimitiveType.INTEGER;
  }
}