package dev.morphe.vm.type;

import dev.morphe.runtime.type.IType;
import dev.morphe.runtime.type.collection.ArrayType;
import dev.morphe.runtime.type.composite.CompositeType;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.collection.ArrayValue;
import dev.morphe.runtime.value.reference.ReferenceValue;
import dev.morphe.vm.ExecutionContext;
import dev.morphe.vm.exception.argument.NullArgumentException;

/**
 * Verifies whether runtime values are compatible with expected runtime types.
 *
 * <p>
 * The checker supports direct runtime type compatibility, referenced composite
 * values, and arrays. Array validation is recursive, allowing multidimensional
 * arrays to be checked element by element.
 * </p>
 *
 * <p>
 * Composite and array values may be represented directly or through a
 * {@link ReferenceValue} pointing to a value stored on the VM heap.
 * </p>
 */
public final class RuntimeTypeChecker {

  private RuntimeTypeChecker() {}

  /**
   * Determines whether a runtime value is accepted by an expected type.
   *
   * <p>
   * The method first checks direct compatibility through
   * {@link IType#accepts(IValue)}. If direct compatibility fails, composite
   * and array types are resolved through heap references when necessary.
   * </p>
   *
   * @param context the current VM execution context
   * @param expectedType the type that must accept the value
   * @param value the runtime value to verify
   * @return {@code true} if the value is compatible with the expected type;
   *         {@code false} otherwise
   *
   * @throws NullArgumentException if {@code context}, {@code expectedType},
   *         or {@code value} is {@code null}
   */
  public static boolean accepts(ExecutionContext context, IType expectedType, IValue value) {
    if (context == null) throw new NullArgumentException("context");
    if (expectedType == null) throw new NullArgumentException("expectedType");
    if (value == null) throw new NullArgumentException("value");
    if (expectedType.accepts(value)) return true;
    if (expectedType instanceof CompositeType) return acceptsReferencedComposite(context, expectedType, value);
    if (expectedType instanceof ArrayType arrayType) return acceptsArray(context, arrayType, value);
    return false;
  }

  /**
   * Checks whether a heap-referenced value is accepted by a composite type.
   *
   * @param context the current VM execution context
   * @param expectedType the expected composite type
   * @param value the value that may reference a composite
   * @return {@code true} if the referenced value is accepted;
   *         {@code false} otherwise
   */
  private static boolean acceptsReferencedComposite(ExecutionContext context, IType expectedType, IValue value) {
    if (!(value instanceof ReferenceValue reference)) return false;
    IValue referenced = context.heap().load(reference.address());
    return expectedType.accepts(referenced);
  }

  /**
   * Checks whether a direct or referenced array matches the expected array type.
   *
   * <p>
   * The array type must match structurally, and every element must be accepted
   * by the expected element type.
   * </p>
   *
   * @param context the current VM execution context
   * @param expectedType the expected array type
   * @param value the value that may represent an array
   * @return {@code true} if the value is a compatible array;
   *         {@code false} otherwise
   */
  private static boolean acceptsArray(ExecutionContext context, ArrayType expectedType, IValue value) {
    if (value instanceof ReferenceValue reference) value = context.heap().load(reference.address());
    if (!(value instanceof ArrayValue arrayValue)) return false;
    if (!expectedType.equals(arrayValue.type())) return false;
    for (IValue element : arrayValue.values())
      if (!accepts(context, expectedType.elementType(), element)) return false;
    return true;
  }
}