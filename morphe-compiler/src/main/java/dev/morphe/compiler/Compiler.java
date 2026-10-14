package dev.morphe.compiler;

import dev.morphe.ast.CompilationUnit;
import dev.morphe.ast.declaration.IDeclaration;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.compiler.context.CompilerContext;
import dev.morphe.compiler.natives.NativeInstructionResolver;
import dev.morphe.compiler.type.CompilerTypeResolver;
import dev.morphe.compiler.visitors.CompilerASTVisitor;
import dev.morphe.compiler.visitors.CompilerDispatch;
import dev.morphe.semantic.visitors.context.SemanticContext;

public final class Compiler {

  private final CompilationUnit compilationUnit;
  private final SemanticContext semanticContext;

  public Compiler(CompilationUnit compilationUnit, SemanticContext semanticContext) {
    if (compilationUnit == null) throw new IllegalArgumentException("compilationUnit cannot be null");
    if (semanticContext == null) throw new IllegalArgumentException("semanticContext cannot be null");
    this.compilationUnit = compilationUnit;
    this.semanticContext = semanticContext;
  }

  public BytecodeProgram compile() {
    BytecodeBuilder builder = new BytecodeBuilder();
    NativeInstructionResolver nativeInstructions = new NativeInstructionResolver();
    CompilerContext context = new CompilerContext(builder, semanticContext, nativeInstructions);
    CompilerTypeResolver typeResolver = new CompilerTypeResolver(context);
    CompilerASTVisitor visitor = new CompilerASTVisitor(typeResolver);
    CompilerDispatch dispatch = new CompilerDispatch(
        visitor.expressionVisitor(), visitor.statementVisitor(), visitor.patternVisitor()
    );
    context.dispatch(dispatch);
    for (IDeclaration declaration : compilationUnit.declarations()) declaration.accept(visitor, context);
    return builder.build();
  }
}