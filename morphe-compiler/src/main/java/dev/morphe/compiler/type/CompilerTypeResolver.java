package dev.morphe.compiler.type;

import dev.morphe.ast.type.*;
import dev.morphe.bytecode.type.*;
import dev.morphe.compiler.context.CompilerContext;

import java.util.ArrayList;
import java.util.List;

public final class CompilerTypeResolver {

  private final CompilerContext context;

  public CompilerTypeResolver(CompilerContext context) {
    if (context == null) throw new IllegalArgumentException("context cannot be null");
    this.context = context;
  }

  public IBytecodeType resolve(IASTType type) {
    if (type == null) throw new IllegalArgumentException("type cannot be null");
    IASTType resolved = context.semantic().resolver().resolve(type);
    if (resolved instanceof PrimitiveType primitive) return resolvePrimitive(primitive);
    if (resolved instanceof NullType) throw new IllegalArgumentException("Null type has no bytecode representation");
    if (resolved instanceof ArrayType array) return resolveArray(array);
    if (resolved instanceof CompositeType composite) return resolveComposite(composite);
    if (resolved instanceof AnonymousCompositeType anonymous) return resolveAnonymousComposite(anonymous);
    if (resolved instanceof FunctionType function) return resolveFunction(function);
    if (resolved instanceof ErrorType error) throw new IllegalArgumentException("Error type has no bytecode representation: " + error.errorKind());
    if (resolved instanceof NamedType named) throw new IllegalStateException("Named type was not resolved: " + named.name());
    throw new IllegalStateException("Unsupported AST type: " + resolved.getClass().getName());
  }

  private IBytecodeType resolvePrimitive(PrimitiveType type) {
    return switch (type) {
      case INTEGER -> PrimitiveBytecodeType.INTEGER;
      case DOUBLE -> PrimitiveBytecodeType.DOUBLE;
      case BOOLEAN -> PrimitiveBytecodeType.BOOLEAN;
      case STRING -> PrimitiveBytecodeType.STRING;
      case VOID -> VoidBytecodeType.INSTANCE;
    };
  }

  private IBytecodeType resolveArray(ArrayType type) {
    return new ArrayBytecodeType(resolve(type.elementType()));
  }

  private IBytecodeType resolveComposite(CompositeType type) {
    return resolveCompositeFields(type.name(), type.fields());
  }

  private IBytecodeType resolveAnonymousComposite(AnonymousCompositeType type) {
    return resolveCompositeFields("$anonymous", type.fields());
  }

  private IBytecodeType resolveCompositeFields(String name, List<CompositeType.CompositeField> astFields) {
    List<CompositeBytecodeType.CompositeBytecodeField> bytecodeFields = new ArrayList<>(astFields.size());
    for (CompositeType.CompositeField field : astFields) {
      bytecodeFields.add(new CompositeBytecodeType.CompositeBytecodeField(field.name(), resolve(field.type())));
    }
    return new CompositeBytecodeType(name, bytecodeFields);
  }

  private IBytecodeType resolveFunction(FunctionType type) {
    List<IBytecodeType> parameterTypes = new ArrayList<>(type.parameterTypes().size());
    for (IASTType parameterType : type.parameterTypes()) {
      parameterTypes.add(resolve(parameterType));
    }
    return new FunctionBytecodeType(parameterTypes, resolve(type.returnType()));
  }
}