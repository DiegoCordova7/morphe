package dev.morphe.compiler.visitors.expression;

import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.instructions.opcodes.ScopeOpCode;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.semantic.symbol.Symbol;

public final class VariableExpressionCompiler {

  public Void compile(VariableExpression expression, CompilerFunctionContext context) {
    BytecodeBuilder builder = context.compiler().builder();
    Symbol symbol = context.compiler().semantic().symbol(expression);
    builder.emit(ScopeOpCode.LOAD, context.slot(symbol));
    return null;
  }
}