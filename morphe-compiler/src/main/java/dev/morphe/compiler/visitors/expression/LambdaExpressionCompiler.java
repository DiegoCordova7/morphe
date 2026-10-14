package dev.morphe.compiler.visitors.expression;

import dev.morphe.ast.declaration.Parameter;
import dev.morphe.ast.expression.LambdaExpression;
import dev.morphe.ast.type.IASTType;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.builder.Label;
import dev.morphe.bytecode.instructions.opcodes.CallOpCode;
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.context.CompilerContext;
import dev.morphe.compiler.type.CompilerTypeResolver;
import dev.morphe.compiler.visitors.CompilerExpressionVisitor;
import dev.morphe.semantic.symbol.Symbol;

public final class LambdaExpressionCompiler {

  private final CompilerTypeResolver typeResolver;
  private final CompilerExpressionVisitor expressionVisitor;

  public LambdaExpressionCompiler(CompilerTypeResolver typeResolver, CompilerExpressionVisitor expressionVisitor) {
    if (typeResolver == null) throw new IllegalArgumentException("typeResolver cannot be null");
    if (expressionVisitor == null) throw new IllegalArgumentException("expressionVisitor cannot be null");
    this.typeResolver = typeResolver;
    this.expressionVisitor = expressionVisitor;
  }

  public Void compile(LambdaExpression expression, CompilerFunctionContext context) {
    CompilerContext compiler = context.compiler();
    BytecodeBuilder builder = compiler.builder();
    IASTType semanticType = context.compiler()
        .semantic()
        .expressionResult(expression)
        .type();
    FunctionBytecodeType functionType = (FunctionBytecodeType) typeResolver.resolve(semanticType);
    Label entryPoint = builder.createLabel();
    int functionIndex = builder.function("<lambda>", functionType, entryPoint);
    CompilerFunctionContext functionContext = new CompilerFunctionContext(compiler, context);
    for (Parameter parameter : expression.parameters()) {
      Symbol symbol = compiler.semantic().symbols().resolve(parameter.name());
      if (symbol == null) throw new IllegalStateException("Parameter symbol was not found: " + parameter.name());
      functionContext.allocateSlot(symbol);
    }
    builder.mark(entryPoint);
    expression.body().accept(expressionVisitor, functionContext);
    builder.emit(CallOpCode.RETURN);
    builder.completeFunction(functionIndex, functionContext.captures());
    builder.emit(CallOpCode.CREATE_CLOSURE, functionIndex);
    return null;
  }
}