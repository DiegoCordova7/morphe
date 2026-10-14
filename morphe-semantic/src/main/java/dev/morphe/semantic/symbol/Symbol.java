package dev.morphe.semantic.symbol;

import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.scope.Scope;

/**
 * Represents a named semantic entity in the Morphe program.
 *
 * <p>A symbol associates a name with its semantic kind and declared
 * AST type. Symbols are stored in {@link Scope}
 * instances and resolved through the {@link SymbolTable}.</p>
 *
 * <p>All symbol components are required and cannot be {@code null}.</p>
 */
public final class Symbol {

  private final String name;
  private final SymbolKind kind;
  private final IASTType type;
  private final boolean nativeFunction;

  /**
   * Creates a symbol with the specified name, kind, and type.
   *
   * @param name the name associated with the symbol
   * @param kind the semantic category of the symbol
   * @param type the declared AST type of the symbol
   * @throws NullSemanticArgumentException if {@code name}, {@code kind},
   *     or {@code type} is {@code null}
   */
  public Symbol(String name, SymbolKind kind, IASTType type, boolean nativeFunction) {
    if (name == null) throw new NullSemanticArgumentException("name");
    if (kind == null) throw new NullSemanticArgumentException("kind");
    if (type == null) throw new NullSemanticArgumentException("type");
    this.name = name;
    this.kind = kind;
    this.type = type;
    this.nativeFunction = nativeFunction;
  }

  /**
   * Returns the name associated with this symbol.
   *
   * @return the symbol name
   */
  public String name() {
    return name;
  }

  /**
   * Returns the semantic category of this symbol.
   *
   * @return the symbol kind
   */
  public SymbolKind kind() {
    return kind;
  }

  /**
   * Returns the AST type associated with this symbol.
   *
   * @return the symbol type
   */
  public IASTType type() {
    return type;
  }

  public boolean nativeFunction() {
    return nativeFunction;
  }
}