package dev.morphe.semantic.scope;

import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.symbol.Symbol;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a lexical scope used during semantic analysis.
 *
 * <p>A scope contains the symbols declared within it and may reference
 * a parent scope. Symbol resolution first searches the current scope
 * and then recursively searches its ancestors.</p>
 *
 * <p>The root scope has no parent and represents the global scope.</p>
 */
public final class Scope {

  private final ScopeKind kind;
  private final Scope parent;
  private final Map<String, Symbol> symbols = new HashMap<>();

  /**
   * Creates a scope with the specified kind and parent.
   *
   * <p>The parent may be {@code null} when creating the root scope.</p>
   *
   * @param kind the kind of scope
   * @param parent the parent scope, or {@code null} for the root scope
   * @throws NullSemanticArgumentException if {@code kind} is {@code null}
   */
  public Scope(ScopeKind kind, Scope parent) {
    if (kind == null) throw new NullSemanticArgumentException("kind");
    this.kind = kind;
    this.parent = parent;
  }

  /**
   * Returns the kind of this scope.
   *
   * @return the scope kind
   */
  public ScopeKind kind() {
    return kind;
  }

  /**
   * Returns the parent of this scope.
   *
   * <p>The root scope returns {@code null}.</p>
   *
   * @return the parent scope, or {@code null} if this is the root scope
   */
  public Scope parent() {
    return parent;
  }

  /**
   * Defines a symbol in this scope.
   *
   * <p>The symbol is stored only in the current scope. It does not
   * affect any parent scope.</p>
   *
   * @param symbol the symbol to define
   */
  public void define(Symbol symbol) {
    symbols.put(symbol.name(), symbol);
  }

  /**
   * Resolves a symbol only within this scope.
   *
   * <p>Parent scopes are not searched.</p>
   *
   * @param name the symbol name
   * @return the matching symbol, or {@code null} if it is not defined
   *     in this scope
   */
  public Symbol resolveLocal(String name) {
    return symbols.get(name);
  }

  /**
   * Resolves a symbol starting from this scope.
   *
   * <p>If the symbol is not defined locally, its parent scopes are
   * searched recursively until the symbol is found or the root scope
   * is reached.</p>
   *
   * @param name the symbol name
   * @return the matching symbol, or {@code null} if no enclosing scope
   *     defines the symbol
   */
  public Symbol resolve(String name) {
    Symbol symbol = symbols.get(name);
    if (symbol != null) return symbol;
    return parent != null ? parent.resolve(name) : null;
  }
}