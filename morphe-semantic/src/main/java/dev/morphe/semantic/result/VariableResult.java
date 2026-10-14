package dev.morphe.semantic.result;

import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.symbol.SymbolKind;

public final class VariableResult {

  private final IASTType type;
  private final SymbolKind kind;

  public VariableResult(IASTType type, SymbolKind kind) {
    this.type = type;
    this.kind = kind;
  }

  public IASTType type() {
    return type;
  }

  public SymbolKind kind() {
    return kind;
  }
}