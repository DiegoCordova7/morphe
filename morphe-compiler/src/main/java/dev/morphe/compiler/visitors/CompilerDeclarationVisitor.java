package dev.morphe.compiler.visitors;

import dev.morphe.ast.declaration.*;
import dev.morphe.ast.visitor.IDeclarationVisitor;
import dev.morphe.compiler.context.CompilerContext;
import dev.morphe.compiler.type.CompilerTypeResolver;
import dev.morphe.compiler.visitors.declaration.*;

public final class CompilerDeclarationVisitor implements IDeclarationVisitor<CompilerContext> {

  private final CompositeDeclarationCompiler compositeDeclaration;
  private final FunctionDeclarationCompiler functionDeclaration;

  public CompilerDeclarationVisitor(CompilerTypeResolver typeResolver) {
    if (typeResolver == null) throw new IllegalArgumentException("typeResolver cannot be null");
    this.compositeDeclaration = new CompositeDeclarationCompiler(typeResolver);
    this.functionDeclaration = new FunctionDeclarationCompiler(typeResolver);
  }

  @Override
  public Void visit(CompositeDeclaration declaration, CompilerContext context) {
    return compositeDeclaration.compile(declaration, context);
  }

  @Override
  public Void visit(FunctionDeclaration declaration, CompilerContext context) {
    return functionDeclaration.compile(declaration, context);
  }
}