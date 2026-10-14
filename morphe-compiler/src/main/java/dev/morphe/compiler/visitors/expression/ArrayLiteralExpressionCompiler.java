package dev.morphe.compiler.visitors.expression;

import dev.morphe.ast.expression.ArrayLiteralExpression;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.constants.IntegerConstant;
import dev.morphe.bytecode.instructions.opcodes.ArrayOpCode;
import dev.morphe.bytecode.instructions.opcodes.StackOpCode;
import dev.morphe.bytecode.type.IBytecodeType;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.type.CompilerTypeResolver;
import dev.morphe.compiler.visitors.CompilerExpressionVisitor;
import dev.morphe.semantic.result.ExpressionResult;

public final class ArrayLiteralExpressionCompiler {

  private final CompilerExpressionVisitor expressionVisitor;
  private final CompilerTypeResolver typeResolver;

  public ArrayLiteralExpressionCompiler(CompilerExpressionVisitor expressionVisitor, CompilerTypeResolver typeResolver) {
    if (expressionVisitor == null) throw new IllegalArgumentException("expressionVisitor cannot be null");
    if (typeResolver == null) throw new IllegalArgumentException("typeResolver cannot be null");
    this.expressionVisitor = expressionVisitor;
    this.typeResolver = typeResolver;
  }

  public Void compile(ArrayLiteralExpression expression, CompilerFunctionContext context) {
    BytecodeBuilder builder = context.compiler().builder();
    IBytecodeType arrayType = resolveArrayType(expression, context);
    int arrayTypeIndex = builder.type(arrayType);
    int size = expression.elements().size();
    int sizeIndex = builder.constant(new IntegerConstant(size));
    builder.emit(StackOpCode.PUSH, sizeIndex);
    builder.emit(ArrayOpCode.ARRAY_NEW, arrayTypeIndex);
    for (int index = 0; index < size; index++) {
      IExpression element = expression.elements().get(index);
      builder.emit(StackOpCode.DUP);
      int indexConstant = builder.constant(new IntegerConstant(index));
      builder.emit(StackOpCode.PUSH, indexConstant);
      element.accept(expressionVisitor, context);
      builder.emit(ArrayOpCode.ARRAY_SET);
    }
    return null;
  }

  private IBytecodeType resolveArrayType(ArrayLiteralExpression expression, CompilerFunctionContext context) {
    ExpressionResult result = context.compiler()
        .semantic()
        .expressionResult(expression);
    return typeResolver.resolve(result.type());
  }
}