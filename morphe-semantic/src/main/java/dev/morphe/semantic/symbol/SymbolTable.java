package dev.morphe.semantic.symbol;

import dev.morphe.semantic.exception.scope.CannotExitGlobalScopeException;
import dev.morphe.semantic.scope.Scope;
import dev.morphe.semantic.scope.ScopeKind;

/**
 * Maintains the active lexical scope and provides symbol definition and
 * resolution operations.
 *
 * <p>The table starts with a global scope. New nested scopes can be entered
 * with {@link #enterScope(ScopeKind)} and the current scope can be left with
 * {@link #exitScope()}.</p>
 *
 * <p>Symbol resolution searches the current scope first and then continues
 * through its parent scopes until a matching symbol is found or the global
 * scope is reached.</p>
 */
public final class SymbolTable {

  private Scope current;

  /**
   * Creates a symbol table with a new global scope.
   */
  public SymbolTable() {
    current = new Scope(ScopeKind.GLOBAL, null);
  }

  /**
   * Returns the currently active scope.
   *
   * @return the current scope
   */
  public Scope currentScope() {
    return current;
  }

  /**
   * Enters a new nested scope of the specified kind.
   *
   * <p>The new scope becomes the current scope and uses the previous
   * current scope as its parent.</p>
   *
   * @param kind the kind of scope to enter
   */
  public void enterScope(ScopeKind kind) {
    current = new Scope(kind, current);
  }

  /**
   * Exits the current scope and restores its parent as the active scope.
   *
   * @throws CannotExitGlobalScopeException if the current scope is the
   *     global scope
   */
  public void exitScope() {
    if (current.parent() == null) throw new CannotExitGlobalScopeException();
    current = current.parent();
  }

  /**
   * Defines a symbol in the current scope.
   *
   * @param symbol the symbol to define
   */
  public void define(Symbol symbol) {
    current.define(symbol);
  }

  /**
   * Resolves a symbol starting from the current scope.
   *
   * <p>If the symbol is not present in the current scope, parent scopes
   * are searched recursively.</p>
   *
   * @param name the name of the symbol to resolve
   * @return the resolved symbol, or {@code null} if no matching symbol
   *     exists in the current scope or any of its parents
   */
  public Symbol resolve(String name) {
    return current.resolve(name);
  }
}