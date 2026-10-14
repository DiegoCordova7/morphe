package dev.morphe.semantic.scope;

import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.symbol.Symbol;
import dev.morphe.semantic.symbol.SymbolKind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScopeTest {

  @Test
  void shouldStoreKind() {
    Scope scope = new Scope(ScopeKind.GLOBAL, null);
    assertEquals(ScopeKind.GLOBAL, scope.kind());
  }

  @Test
  void shouldStoreParent() {
    Scope parent = new Scope(ScopeKind.GLOBAL, null);
    Scope child = new Scope(ScopeKind.BLOCK, parent);
    assertSame(parent, child.parent());
  }

  @Test
  void shouldAllowNullParentForRootScope() {
    Scope scope = new Scope(ScopeKind.GLOBAL, null);
    assertNull(scope.parent());
  }

  @Test
  void shouldRejectNullKind() {
    assertThrows(NullSemanticArgumentException.class, () -> new Scope(null, null));
  }

  @Test
  void shouldDefineSymbol() {
    Scope scope = new Scope(ScopeKind.GLOBAL, null);
    Symbol symbol = symbol("value");
    scope.define(symbol);
    assertSame(symbol, scope.resolveLocal("value"));
  }

  @Test
  void shouldResolveLocalSymbol() {
    Scope scope = new Scope(ScopeKind.GLOBAL, null);
    Symbol symbol = symbol("value");
    scope.define(symbol);
    assertSame(symbol, scope.resolveLocal("value"));
  }

  @Test
  void shouldNotResolveParentSymbolLocally() {
    Scope parent = new Scope(ScopeKind.GLOBAL, null);
    Scope child = new Scope(ScopeKind.BLOCK, parent);
    Symbol symbol = symbol("value");
    parent.define(symbol);
    assertNull(child.resolveLocal("value"));
  }

  @Test
  void shouldResolveSymbolFromParent() {
    Scope parent = new Scope(ScopeKind.GLOBAL, null);
    Scope child = new Scope(ScopeKind.BLOCK, parent);
    Symbol symbol = symbol("value");
    parent.define(symbol);
    assertSame(symbol, child.resolve("value"));
  }

  @Test
  void shouldResolveSymbolFromAncestor() {
    Scope global = new Scope(ScopeKind.GLOBAL, null);
    Scope function = new Scope(ScopeKind.FUNCTION, global);
    Scope block = new Scope(ScopeKind.BLOCK, function);
    Symbol symbol = symbol("value");
    global.define(symbol);
    assertSame(symbol, block.resolve("value"));
  }

  @Test
  void shouldReturnNullForUnknownSymbol() {
    Scope scope = new Scope(ScopeKind.GLOBAL, null);
    assertNull(scope.resolve("unknown"));
  }

  @Test
  void shouldReturnNullForUnknownLocalSymbol() {
    Scope scope = new Scope(ScopeKind.GLOBAL, null);
    assertNull(scope.resolveLocal("unknown"));
  }

  @Test
  void shouldAllowChildSymbolToShadowParentSymbol() {
    Scope parent = new Scope(ScopeKind.GLOBAL, null);
    Scope child = new Scope(ScopeKind.BLOCK, parent);
    Symbol parentSymbol = symbol("value");
    Symbol childSymbol = symbol("value");
    parent.define(parentSymbol);
    child.define(childSymbol);
    assertSame(childSymbol, child.resolve("value"));
    assertSame(parentSymbol, parent.resolve("value"));
  }

  @Test
  void shouldResolveMultipleNestedScopes() {
    Scope global = new Scope(ScopeKind.GLOBAL, null);
    Scope function = new Scope(ScopeKind.FUNCTION, global);
    Scope block = new Scope(ScopeKind.BLOCK, function);
    Symbol globalSymbol = symbol("global");
    Symbol functionSymbol = symbol("function");
    Symbol blockSymbol = symbol("block");
    global.define(globalSymbol);
    function.define(functionSymbol);
    block.define(blockSymbol);
    assertSame(globalSymbol, block.resolve("global"));
    assertSame(functionSymbol, block.resolve("function"));
    assertSame(blockSymbol, block.resolve("block"));
  }

  private Symbol symbol(String name) {
    return new Symbol(name, SymbolKind.VARIABLE, PrimitiveType.INTEGER, false);
  }
}