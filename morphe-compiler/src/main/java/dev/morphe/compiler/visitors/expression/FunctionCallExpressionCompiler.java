package dev.morphe.compiler.visitors.expression;

import dev.morphe.ast.expression.FunctionCallExpression;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.bytecode.instructions.opcodes.CallOpCode;
import dev.morphe.bytecode.instructions.opcodes.IOpCode;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.visitors.CompilerExpressionVisitor;
import dev.morphe.semantic.symbol.Symbol;

public final class FunctionCallExpressionCompiler {

  private final CompilerExpressionVisitor expressionVisitor;

  public FunctionCallExpressionCompiler(CompilerExpressionVisitor expressionVisitor) {
    if (expressionVisitor == null) throw new IllegalArgumentException("expressionVisitor cannot be null");
    this.expressionVisitor = expressionVisitor;
  }

  public Void compile(FunctionCallExpression expression, CompilerFunctionContext context) {
    Symbol symbol = context.compiler().semantic().symbol(expression.function());
    for (IExpression argument : expression.arguments()) argument.accept(expressionVisitor, context);
    if (symbol.nativeFunction()) {
      IOpCode opcode = context.compiler().nativeInstructions().resolve(symbol.name());
      context.compiler().builder().emit(opcode);
      return null;
    }
    int functionIndex = context.compiler().functionIndex(symbol);
    context.compiler().builder().emit(CallOpCode.CALL, functionIndex, expression.arguments().size());
    return null;
  }
}