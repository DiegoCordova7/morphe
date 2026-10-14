package dev.morphe.compiler.visitors.declaration;

import dev.morphe.ast.declaration.FunctionDeclaration;
import dev.morphe.ast.declaration.Parameter;
import dev.morphe.bytecode.builder.Label;
import dev.morphe.bytecode.instructions.opcodes.CallOpCode;
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.bytecode.type.IBytecodeType;
import dev.morphe.compiler.context.CompilerContext;
import dev.morphe.compiler.context.CompilerFunctionContext;
import dev.morphe.compiler.type.CompilerTypeResolver;
import dev.morphe.semantic.symbol.Symbol;

import java.util.ArrayList;
import java.util.List;

public final class FunctionDeclarationCompiler {

  private final CompilerTypeResolver typeResolver;

  public FunctionDeclarationCompiler(CompilerTypeResolver typeResolver) {
    if (typeResolver == null) throw new IllegalArgumentException("typeResolver cannot be null");
    this.typeResolver = typeResolver;
  }

  public Void compile(FunctionDeclaration declaration, CompilerContext context) {
    List<IBytecodeType> parameterTypes =
        new ArrayList<>(declaration.parameters().size());

    for (Parameter parameter : declaration.parameters())
      parameterTypes.add(typeResolver.resolve(parameter.type()));

    IBytecodeType returnType =
        typeResolver.resolve(declaration.returnType());

    FunctionBytecodeType functionType =
        new FunctionBytecodeType(parameterTypes, returnType);

    Label entryPoint = context.builder().createLabel();

    int functionIndex =
        context.builder().function(
            declaration.name(),
            functionType,
            entryPoint
        );

    Symbol symbol = context.semantic().symbol(declaration);

    context.registerFunction(symbol, functionIndex);

    if (declaration.name().equals("main"))
      context.builder().entryFunction(functionIndex);

    CompilerFunctionContext functionContext =
        new CompilerFunctionContext(context, null);

    List<Symbol> parameterSymbols =
        context.semantic().parameterSymbols(declaration);

    for (Symbol parameterSymbol : parameterSymbols)
      functionContext.allocateSlot(parameterSymbol);

    context.builder().mark(entryPoint);

    context.dispatch()
        .statements()
        .visit(declaration.body(), functionContext);

    context.builder().emit(CallOpCode.RETURN);

    context.builder().completeFunction(
        functionIndex,
        functionContext.captures()
    );
    return null;
  }
}