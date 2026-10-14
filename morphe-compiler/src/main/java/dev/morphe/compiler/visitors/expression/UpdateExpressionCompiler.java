package dev.morphe.compiler.visitors.expression;

import dev.morphe.ast.expression.UpdateExpression;
import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.constants.IntegerConstant;
import dev.morphe.bytecode.instructions.opcodes.ScopeOpCode;
import dev.morphe.bytecode.instructions.opcodes.StackOpCode;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.operator.UpdateOpCodeResolver;
import dev.morphe.semantic.symbol.Symbol;

public final class UpdateExpressionCompiler {

  public Void compile(UpdateExpression expression, CompilerFunctionContext context) {
    if (expression.operand() instanceof VariableExpression variable) {
      compileVariable(variable, expression, context);
      return null;
    }
    throw new IllegalStateException("Invalid update target: " + expression.operand().getClass().getSimpleName());
  }

  private void compileVariable(VariableExpression variable, UpdateExpression expression, CompilerFunctionContext context) {
    BytecodeBuilder builder = context.compiler().builder();
    Symbol symbol = context.compiler().semantic().symbol(variable);
    int slot = context.slot(symbol);
    builder.emit(ScopeOpCode.LOAD, slot);
    builder.emit(StackOpCode.DUP);
    int one = builder.constant(new IntegerConstant(1));
    builder.emit(StackOpCode.PUSH, one);
    builder.emit(UpdateOpCodeResolver.resolve(expression.operator()));
    builder.emit(ScopeOpCode.STORE, slot);
  }
}