package dev.morphe.vm.type;

import dev.morphe.bytecode.type.ArrayBytecodeType;
import dev.morphe.bytecode.type.CompositeBytecodeType;
import dev.morphe.bytecode.type.IBytecodeType;
import dev.morphe.bytecode.type.PrimitiveBytecodeType;
import dev.morphe.runtime.type.IType;
import dev.morphe.runtime.type.collection.ArrayType;
import dev.morphe.runtime.type.composite.CompositeType;
import dev.morphe.runtime.type.composite.FieldType;
import dev.morphe.runtime.type.primitive.*;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.internal.type.UnsupportedBytecodeTypeException;

import java.util.List;

/**
 * Resolves bytecode-level types into their corresponding runtime types.
 *
 * <p>
 * The bytecode and runtime modules define their type systems independently.
 * This resolver provides the bridge between both representations during
 * virtual machine execution.
 * </p>
 *
 * <p>
 * Primitive bytecode types are resolved into their corresponding runtime
 * primitive types. Array bytecode types are resolved recursively, allowing
 * multidimensional array types to be represented naturally.
 * </p>
 *
 * <p>
 * Composite bytecode types are converted into runtime composite types while
 * recursively resolving the type of each field.
 * </p>
 */
public final class TypeResolver {

  private TypeResolver() {}

  /**
   * Resolves a bytecode type into a runtime type.
   *
   * <p>
   * Array types are resolved recursively through their element type.
   * </p>
   *
   * @param type the bytecode type to resolve
   * @return the corresponding runtime type
   *
   * @throws NullArgumentException if {@code type} is {@code null}
   * @throws UnsupportedBytecodeTypeException if the bytecode type is not
   *         supported by the virtual machine
   */
  public static IType resolve(IBytecodeType type) {
    if (type == null) throw new NullArgumentException("type");
    if (type instanceof PrimitiveBytecodeType primitive) return resolvePrimitive(primitive);
    if (type instanceof ArrayBytecodeType array) return new ArrayType(resolve(array.elementType()));
    if (type instanceof CompositeBytecodeType composite) return resolveComposite(composite);
    throw new UnsupportedBytecodeTypeException(type);
  }

  /**
   * Resolves a primitive bytecode type into its corresponding runtime type.
   *
   * @param type the primitive bytecode type to resolve
   * @return the corresponding runtime primitive type
   */
  private static IPrimitiveType resolvePrimitive(PrimitiveBytecodeType type) {
    return switch (type) {
      case INTEGER -> IntegerType.INSTANCE;
      case DOUBLE -> DoubleType.INSTANCE;
      case BOOLEAN -> BooleanType.INSTANCE;
      case STRING -> StringType.INSTANCE;
    };
  }

  /**
   * Resolves a composite bytecode type into a runtime composite type.
   *
   * <p>
   * Each bytecode field is converted into a runtime {@link FieldType}.
   * Field types are resolved recursively through {@link #resolve(IBytecodeType)}.
   * </p>
   *
   * @param type the composite bytecode type to resolve
   * @return the corresponding runtime composite type
   */
  private static CompositeType resolveComposite(CompositeBytecodeType type) {
    List<FieldType> fields = type.fields().stream()
        .map(field -> new FieldType(field.name(), resolve(field.type())))
        .toList();
    return new CompositeType(type.name(), fields);
  }
}