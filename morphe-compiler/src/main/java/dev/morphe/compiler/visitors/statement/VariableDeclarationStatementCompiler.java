package dev.morphe.compiler.visitors.statement;

import dev.morphe.ast.statement.VariableDeclarationStatement;
import dev.morphe.bytecode.instructions.opcodes.ScopeOpCode;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.visitors.CompilerDispatch;
import dev.morphe.semantic.symbol.Symbol;

public final class VariableDeclarationStatementCompiler {

  public Void compile(VariableDeclarationStatement statement, CompilerFunctionContext context) {
    CompilerDispatch dispatch = context.compiler().dispatch();
    Symbol symbol = context.compiler()
        .semantic()
        .symbol(statement);
    if (symbol == null) throw new IllegalStateException("Variable symbol was not found: " + statement.name());
    int slot = context.allocateSlot(symbol);
    context.compiler().builder().emit(ScopeOpCode.DECLARE, slot);
    if (statement.initializer() != null) {
      statement.initializer().accept(dispatch.expressions(), context);
      context.compiler().builder().emit(ScopeOpCode.STORE, slot);
    }
    return null;
  }
}