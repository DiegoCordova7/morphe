package dev.morphe.compiler.visitors.expression;

import dev.morphe.ast.expression.CompositeInstanceExpression;
import dev.morphe.ast.type.IASTType;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.constants.StringConstant;
import dev.morphe.bytecode.instructions.opcodes.CompositeOpCode;
import dev.morphe.bytecode.instructions.opcodes.StackOpCode;
import dev.morphe.bytecode.type.CompositeBytecodeType;
import dev.morphe.bytecode.type.IBytecodeType;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.type.CompilerTypeResolver;
import dev.morphe.compiler.visitors.CompilerExpressionVisitor;
import dev.morphe.semantic.result.ExpressionResult;

import java.util.List;

public final class CompositeInstanceExpressionCompiler {

  private final CompilerExpressionVisitor expressionVisitor;
  private final CompilerTypeResolver typeResolver;

  public CompositeInstanceExpressionCompiler(CompilerExpressionVisitor expressionVisitor, CompilerTypeResolver typeResolver) {
    if (expressionVisitor == null) throw new IllegalArgumentException("expressionVisitor cannot be null");
    if (typeResolver == null) throw new IllegalArgumentException("typeResolver cannot be null");
    this.expressionVisitor = expressionVisitor;
    this.typeResolver = typeResolver;
  }

  public Void compile(CompositeInstanceExpression expression, CompilerFunctionContext context) {
    BytecodeBuilder builder = context.compiler().builder();
    CompositeBytecodeType type = resolveCompositeType(expression, context);
    int typeIndex = builder.type(type);
    for (CompositeBytecodeType.CompositeBytecodeField field : type.fields()) {
      CompositeInstanceExpression.FieldInitializer initializer = findInitializer(expression, field.name());
      ExpressionResult result = context.compiler().semantic().expressionResult(initializer.value());
      IASTType providedAstType = result.type();
      IBytecodeType expectedBytecodeType = field.type();
      IBytecodeType providedBytecodeType = typeResolver.resolve(providedAstType);
      initializer.value().accept(expressionVisitor, context);
      if (!expectedBytecodeType.equals(providedBytecodeType) && expectedBytecodeType instanceof CompositeBytecodeType targetComposite) {
        emitStructuralProjection(builder, targetComposite);
      }
    }

    builder.emit(CompositeOpCode.COMPOSITE_NEW, typeIndex);
    return null;
  }

  private void emitStructuralProjection(BytecodeBuilder builder, CompositeBytecodeType targetComposite) {
    List<CompositeBytecodeType.CompositeBytecodeField> targetFields = targetComposite.fields();
    int targetTypeIndex = builder.type(targetComposite);
    for (int i = 0; i < targetFields.size(); i++) {
      CompositeBytecodeType.CompositeBytecodeField targetField = targetFields.get(i);
      if (i < targetFields.size() - 1) builder.emit(StackOpCode.DUP);
      int fieldIndex = builder.constant(new StringConstant(targetField.name()));
      builder.emit(CompositeOpCode.GET_FIELD, fieldIndex);
    }
    builder.emit(CompositeOpCode.COMPOSITE_NEW, targetTypeIndex);
  }

  private CompositeBytecodeType resolveCompositeType(CompositeInstanceExpression expression, CompilerFunctionContext context) {
    ExpressionResult result = context.compiler()
        .semantic()
        .expressionResult(expression);
    IBytecodeType type = typeResolver.resolve(result.type());
    if (!(type instanceof CompositeBytecodeType composite))
      throw new IllegalStateException("Composite instance expression did not resolve to a composite bytecode type");
    return composite;
  }

  private CompositeInstanceExpression.FieldInitializer findInitializer(CompositeInstanceExpression expression,
                                                                       String fieldName) {
    for (CompositeInstanceExpression.FieldInitializer initializer : expression.fields()) {
      if (initializer.name().equals(fieldName)) return initializer;
    }
    throw new IllegalStateException("Missing initializer for composite field: " + fieldName);
  }
}