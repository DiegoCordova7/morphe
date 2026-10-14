package dev.morphe.compiler.visitors.expression;

import dev.morphe.ast.expression.AttemptExpression;
import dev.morphe.ast.statement.BlockStatement;
import dev.morphe.ast.statement.ExpressionStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.builder.Label;
import dev.morphe.bytecode.instructions.opcodes.ControlOpCode;
import dev.morphe.bytecode.program.BytecodeErrorKind;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.visitors.CompilerDispatch;

import java.util.List;

public final class AttemptExpressionCompiler {

  public Void compile(AttemptExpression expression, CompilerFunctionContext context) {
    CompilerDispatch dispatch = context.compiler().dispatch();
    BytecodeBuilder builder = context.compiler().builder();
    Label startLabel = builder.createLabel();
    Label endLabel = builder.createLabel();
    Label fallbackLabel = builder.createLabel();
    Label finishLabel = builder.createLabel();
    int regionIndex = builder.reserveAttempt(startLabel, endLabel, fallbackLabel);
    builder.mark(startLabel);
    compileResultBlock(expression.body(), context);
    builder.emit(ControlOpCode.JMP, finishLabel);
    builder.mark(endLabel);
    for (AttemptExpression.ErrorHandler handler : expression.handlers()) {
      Label handlerLabel = builder.createLabel();
      BytecodeErrorKind errorKind = resolveErrorKind(handler);
      builder.addErrorHandler(regionIndex, errorKind, handlerLabel);
      builder.mark(handlerLabel);
      handler.action().accept(dispatch.statements(), context);
      builder.emit(ControlOpCode.JMP, fallbackLabel);
    }
    builder.mark(fallbackLabel);
    expression.fallback().accept(dispatch.expressions(), context);
    builder.mark(finishLabel);
    builder.completeAttemptRegion(regionIndex);
    return null;
  }


  private void compileResultBlock(BlockStatement block, CompilerFunctionContext context) {
    List<IStatement> statements = block.statements();
    for (int i = 0; i < statements.size() - 1; i++) {
      statements.get(i).accept(context.compiler().dispatch().statements(), context);
    }
    IStatement last = statements.get(statements.size() - 1);
    if (last instanceof ExpressionStatement expressionStatement) {
      expressionStatement.expression().accept(context.compiler().dispatch().expressions(), context);
      return;
    }
    last.accept(context.compiler().dispatch().statements(), context);
  }

  private BytecodeErrorKind resolveErrorKind(AttemptExpression.ErrorHandler handler) {
    return switch (handler.errorType().errorKind()) {
      case DIVISION_BY_ZERO -> BytecodeErrorKind.DIVISION_BY_ZERO;
      case MODULO_BY_ZERO -> BytecodeErrorKind.MODULO_BY_ZERO;
      case INVALID_ARRAY_INDEX -> BytecodeErrorKind.INVALID_ARRAY_INDEX;
    };
  }
}