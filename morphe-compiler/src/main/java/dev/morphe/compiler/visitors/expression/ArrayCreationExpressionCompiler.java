package dev.morphe.compiler.visitors.expression;

import dev.morphe.ast.expression.ArrayCreationExpression;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.instructions.opcodes.ArrayOpCode;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.type.CompilerTypeResolver;
import dev.morphe.compiler.visitors.CompilerExpressionVisitor;
import dev.morphe.bytecode.type.IBytecodeType;
import dev.morphe.semantic.result.ExpressionResult;

public final class ArrayCreationExpressionCompiler {

  private final CompilerExpressionVisitor expressionVisitor;
  private final CompilerTypeResolver typeResolver;

  public ArrayCreationExpressionCompiler(CompilerExpressionVisitor expressionVisitor, CompilerTypeResolver typeResolver) {
    if (expressionVisitor == null) throw new IllegalArgumentException("expressionVisitor cannot be null");
    if (typeResolver == null) throw new IllegalArgumentException("typeResolver cannot be null");
    this.expressionVisitor = expressionVisitor;
    this.typeResolver = typeResolver;
  }

  public Void compile(ArrayCreationExpression expression, CompilerFunctionContext context) {
    BytecodeBuilder builder = context.compiler().builder();
    IBytecodeType arrayType = resolveArrayType(expression, context);
    int arrayTypeIndex = builder.type(arrayType);
    for (IExpression dimension : expression.dimensions()) dimension.accept(expressionVisitor, context);
    builder.emit(ArrayOpCode.ARRAY_NEW_MULTI, arrayTypeIndex, expression.dimensions().size());
    return null;
  }

  private IBytecodeType resolveArrayType(ArrayCreationExpression expression, CompilerFunctionContext context) {
    ExpressionResult result = context.compiler()
        .semantic()
        .expressionResult(expression);
    return typeResolver.resolve(result.type());
  }
}