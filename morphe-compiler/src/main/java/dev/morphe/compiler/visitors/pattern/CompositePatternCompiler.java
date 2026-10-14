package dev.morphe.compiler.visitors.pattern;

import dev.morphe.ast.pattern.CompositePattern;
import dev.morphe.ast.type.IASTType;
import dev.morphe.ast.type.NamedType;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.builder.Label;
import dev.morphe.bytecode.constants.StringConstant;
import dev.morphe.bytecode.instructions.opcodes.CompositeOpCode;
import dev.morphe.bytecode.instructions.opcodes.ControlOpCode;
import dev.morphe.bytecode.instructions.opcodes.ScopeOpCode;
import dev.morphe.bytecode.type.CompositeBytecodeType;
import dev.morphe.bytecode.type.IBytecodeType;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.context.CompilerPatternContext;
import dev.morphe.compiler.type.CompilerTypeResolver;

public final class CompositePatternCompiler {

  private final CompilerTypeResolver typeResolver;

  public CompositePatternCompiler(CompilerTypeResolver typeResolver) {
    if (typeResolver == null) throw new IllegalArgumentException("typeResolver cannot be null");
    this.typeResolver = typeResolver;
  }

  public Void compile(CompositePattern pattern, CompilerPatternContext context) {
    if (pattern == null) throw new IllegalArgumentException("pattern cannot be null");
    if (context == null) throw new IllegalArgumentException("context cannot be null");
    BytecodeBuilder builder = context.function().compiler().builder();
    CompilerFunctionContext function = context.function();
    CompositeBytecodeType type = resolveCompositeType(pattern, context);
    for (CompositePattern.FieldPattern field : pattern.fields()) {
      int fieldIndex = type.fieldIndex(field.name());
      if (fieldIndex < 0) throw new IllegalStateException("Field was not found in composite type: " + field.name());
      int fieldConstant = builder.constant(new StringConstant(field.name()));
      int fieldSlot = function.allocateTemporarySlot();
      builder.emit(ScopeOpCode.DECLARE, fieldSlot);
      Label matched = builder.createLabel();
      builder.emit(ScopeOpCode.LOAD, context.subjectSlot());
      builder.emit(CompositeOpCode.GET_FIELD, fieldConstant);
      builder.emit(ScopeOpCode.STORE, fieldSlot);
      CompilerPatternContext fieldContext = new CompilerPatternContext(function, fieldSlot, matched, context.failed());
      field.pattern().accept(function.compiler().dispatch().patterns(), fieldContext);
      builder.mark(matched);
    }
    builder.emit(ControlOpCode.JMP, context.matched());
    return null;
  }

  private CompositeBytecodeType resolveCompositeType(CompositePattern pattern, CompilerPatternContext context) {
    NamedType namedType = new NamedType(pattern.typeName());
    IASTType astType = context
        .function()
        .compiler()
        .semantic()
        .resolver()
        .resolve(namedType);
    IBytecodeType bytecodeType = typeResolver.resolve(astType);
    if (!(bytecodeType instanceof CompositeBytecodeType composite))
      throw new IllegalStateException("Pattern type is not a composite type: " + pattern.typeName());
    return composite;
  }
}