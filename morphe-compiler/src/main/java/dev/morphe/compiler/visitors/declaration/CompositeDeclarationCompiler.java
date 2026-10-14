package dev.morphe.compiler.visitors.declaration;

import dev.morphe.ast.declaration.CompositeDeclaration;
import dev.morphe.ast.type.CompositeType;
import dev.morphe.bytecode.type.CompositeBytecodeType;
import dev.morphe.bytecode.type.IBytecodeType;
import dev.morphe.compiler.context.CompilerContext;
import dev.morphe.compiler.type.CompilerTypeResolver;

import java.util.ArrayList;
import java.util.List;

public final class CompositeDeclarationCompiler {

  private final CompilerTypeResolver typeResolver;

  public CompositeDeclarationCompiler(CompilerTypeResolver typeResolver) {
    if (typeResolver == null) throw new IllegalArgumentException("typeResolver cannot be null");
    this.typeResolver = typeResolver;
  }

  public Void compile(CompositeDeclaration declaration, CompilerContext context) {
    List<CompositeBytecodeType.CompositeBytecodeField> fields = new ArrayList<>(declaration.fields().size());
    for (CompositeType.CompositeField field : declaration.fields()) {
      IBytecodeType fieldType = typeResolver.resolve(field.type());
      fields.add(new CompositeBytecodeType.CompositeBytecodeField(field.name(), fieldType));
    }
    CompositeBytecodeType type = new CompositeBytecodeType(declaration.name(), fields);
    context.builder().type(type);
    return null;
  }
}