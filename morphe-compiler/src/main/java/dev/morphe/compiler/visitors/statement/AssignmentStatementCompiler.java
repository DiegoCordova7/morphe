package dev.morphe.compiler.visitors.statement;

import dev.morphe.ast.expression.ArrayAccessExpression;
import dev.morphe.ast.expression.FieldAccessExpression;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.ast.operator.AssignmentOperator;
import dev.morphe.ast.statement.AssignmentStatement;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.constants.StringConstant;
import dev.morphe.bytecode.instructions.opcodes.*;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.operator.ArithmeticOpCodeResolver;
import dev.morphe.compiler.visitors.CompilerDispatch;
import dev.morphe.semantic.symbol.Symbol;

public final class AssignmentStatementCompiler {

  public Void compile(AssignmentStatement statement, CompilerFunctionContext context) {
    if (statement.operator() == AssignmentOperator.ASSIGN) {
      compileSimple(statement, context);
    } else {
      compileCompound(statement, context);
    }
    return null;
  }

  private void compileSimple(AssignmentStatement statement, CompilerFunctionContext context) {
    CompilerDispatch dispatch = context.compiler().dispatch();
    IExpression target = statement.target();
    BytecodeBuilder builder = context.compiler().builder();
    if (target instanceof VariableExpression) {
      Symbol symbol = context.compiler()
          .semantic()
          .symbol(statement);
      statement.value().accept(dispatch.expressions(), context);
      builder.emit(ScopeOpCode.STORE, context.slot(symbol));
      return;
    }

    if (target instanceof ArrayAccessExpression access) {
      access.array().accept(dispatch.expressions(), context);
      access.index().accept(dispatch.expressions(), context);
      statement.value().accept(dispatch.expressions(), context);
      builder.emit(ArrayOpCode.ARRAY_SET);
      return;
    }

    if (target instanceof FieldAccessExpression access) {
      access.target().accept(dispatch.expressions(), context);
      statement.value().accept(dispatch.expressions(), context);
      int fieldIndex = builder.constant(new StringConstant(access.field()));
      builder.emit(CompositeOpCode.SET_FIELD, fieldIndex);
      return;
    }
    throw invalidTarget(target);
  }

  private void compileCompound(AssignmentStatement statement, CompilerFunctionContext context) {
    IExpression target = statement.target();
    if (target instanceof VariableExpression) {
      compileVariableCompound(statement, context);
      return;
    }
    if (target instanceof ArrayAccessExpression access) {
      compileArrayCompound(access, statement, context);
      return;
    }
    if (target instanceof FieldAccessExpression access) {
      compileFieldCompound(access, statement, context);
      return;
    }
    throw invalidTarget(target);
  }

  private void compileVariableCompound(AssignmentStatement statement,
                                       CompilerFunctionContext context) {
    CompilerDispatch dispatch = context.compiler().dispatch();
    BytecodeBuilder builder = context.compiler().builder();
    Symbol symbol = context.compiler().semantic().symbol(statement);
    builder.emit(ScopeOpCode.LOAD, context.slot(symbol));
    statement.value().accept(dispatch.expressions(), context);
    builder.emit(ArithmeticOpCodeResolver.resolve(statement.operator()));
    builder.emit(ScopeOpCode.STORE, context.slot(symbol));
  }

  private void compileArrayCompound(ArrayAccessExpression access, AssignmentStatement statement, CompilerFunctionContext context) {
    CompilerDispatch dispatch = context.compiler().dispatch();
    BytecodeBuilder builder = context.compiler().builder();
    access.array().accept(dispatch.expressions(), context);
    access.index().accept(dispatch.expressions(), context);
    builder.emit(StackOpCode.DUP2);
    builder.emit(ArrayOpCode.ARRAY_GET);
    statement.value().accept(dispatch.expressions(), context);
    builder.emit(ArithmeticOpCodeResolver.resolve(statement.operator()));
    builder.emit(ArrayOpCode.ARRAY_SET);
  }

  private void compileFieldCompound(FieldAccessExpression access, AssignmentStatement statement,
                                    CompilerFunctionContext context) {
    CompilerDispatch dispatch = context.compiler().dispatch();
    BytecodeBuilder builder = context.compiler().builder();
    access.target().accept(dispatch.expressions(), context);
    builder.emit(StackOpCode.DUP);
    int fieldIndex = builder.constant(new StringConstant(access.field()));
    builder.emit(CompositeOpCode.GET_FIELD, fieldIndex);
    statement.value().accept(dispatch.expressions(), context);
    builder.emit(ArithmeticOpCodeResolver.resolve(statement.operator()));
    builder.emit(CompositeOpCode.SET_FIELD, fieldIndex);
  }

  private IllegalStateException invalidTarget(IExpression target) {
    return new IllegalStateException("Invalid assignment target: " + target.getClass().getSimpleName());
  }
}