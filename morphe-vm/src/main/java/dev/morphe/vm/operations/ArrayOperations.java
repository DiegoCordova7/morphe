package dev.morphe.vm.operations;

import dev.morphe.runtime.type.IType;
import dev.morphe.runtime.type.collection.ArrayType;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.collection.ArrayValue;
import dev.morphe.runtime.value.primitive.NullValue;
import dev.morphe.runtime.value.reference.ReferenceValue;
import dev.morphe.vm.ExecutionContext;
import dev.morphe.vm.exception.argument.InvalidArrayDimensionsException;
import dev.morphe.vm.exception.argument.NegativeArrayDimensionException;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.execution.InvalidArrayIndexException;
import dev.morphe.vm.exception.internal.type.ExpectedArrayTypeException;
import dev.morphe.vm.exception.internal.type.ExpectedArrayValueException;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides runtime operations for creating and manipulating arrays.
 *
 * <p>
 * Array values are allocated on the VM heap and are accessed through
 * {@link ReferenceValue} instances containing their heap addresses.
 * </p>
 *
 * <p>
 * This class is responsible for the runtime semantics of array operations,
 * including array allocation, element access, element replacement, and
 * retrieving array length.
 * </p>
 *
 * <p>
 * Arrays are represented by {@link ArrayValue} instances stored in the heap.
 * Since {@link ArrayValue} is immutable, modifying an array element creates
 * a new array value that replaces the previous value at the same heap
 * address. Existing references to the array therefore remain valid.
 * </p>
 *
 * <p>
 * Multidimensional arrays are represented as nested arrays. Each dimension
 * is allocated recursively, and every nested array is stored independently
 * on the heap.
 * </p>
 */
public final class ArrayOperations {

  /**
   * Prevents instantiation of this utility class.
   */
  private ArrayOperations() {}

  /**
   * Creates a new array with the specified runtime type and size,
   * allocates it on the VM heap, and returns a reference to it.
   *
   * <p>
   * Every element of the newly created array is initialized with
   * {@link NullValue}.
   * </p>
   *
   * @param context the current execution context
   * @param type the runtime type of the array
   * @param size the number of elements in the array
   * @return a reference to the allocated array
   *
   * @throws NullArgumentException if {@code context} or {@code type} is {@code null}
   * @throws NegativeArrayDimensionException if {@code size} is negative
   */
  public static ReferenceValue newArray(ExecutionContext context, ArrayType type, int size) {
    if (context == null) throw new NullArgumentException("context");
    if (type == null) throw new NullArgumentException("type");
    if (size < 0) throw new NegativeArrayDimensionException(size);
    List<IValue> values = new ArrayList<>(size);
    for (int i = 0; i < size; i++) values.add(new NullValue());
    ArrayValue array = new ArrayValue(type, values);
    int address = context.heap().allocate(array);
    return new ReferenceValue(address);
  }

  /**
   * Creates a multidimensional array with the specified runtime type and
   * dimensions, allocates all nested arrays on the VM heap, and returns
   * a reference to the outermost array.
   *
   * <p>
   * Each dimension specifies the number of elements at its corresponding
   * nesting level. For example, dimensions {@code [2, 3]} create an outer
   * array containing two inner arrays, each containing three elements.
   * </p>
   *
   * <p>
   * Every nested array is allocated independently on the heap. The elements
   * of the innermost arrays are initialized with {@link NullValue}.
   * </p>
   *
   * @param context the current execution context
   * @param type the runtime type of the multidimensional array
   * @param dimensions the size of each array dimension
   * @return a reference to the outermost allocated array
   *
   * @throws NullArgumentException if {@code context}, {@code type}, or {@code dimensions} is {@code null}
   * @throws NegativeArrayDimensionException if any dimension is negative
   * @throws InvalidArrayDimensionsException if the runtime array type does not describe the required nested array structure
   */
  public static ReferenceValue newMultiArray(ExecutionContext context, ArrayType type, int[] dimensions) {
    if (context == null) throw new NullArgumentException("context");
    if (type == null) throw new NullArgumentException("type");
    if (dimensions == null) throw new NullArgumentException("dimensions");
    if (dimensions.length == 0) throw new InvalidArrayDimensionsException();
    return buildArray(context, type, dimensions, 0);
  }

  /**
   * Recursively constructs a multidimensional array.
   *
   * <p>
   * The innermost dimension is created using {@link #newArray(ExecutionContext,
   * ArrayType, int)}. Higher dimensions contain references to independently
   * allocated child arrays.
   * </p>
   *
   * @param context the current execution context
   * @param type the runtime type of the current array
   * @param dimensions the sizes of all array dimensions
   * @param level the current dimension being constructed
   * @return a reference to the allocated array at the current level
   *
   * @throws NegativeArrayDimensionException if the current dimension is negative
   * @throws ExpectedArrayTypeException if the current array type does not contain
   *     a nested array element type when another dimension remains
   */
  private static ReferenceValue buildArray(ExecutionContext context, ArrayType type, int[] dimensions, int level) {
    if (level == dimensions.length - 1) return newArray(context, type, dimensions[level]);
    IType elementType = type.elementType();
    if (!(elementType instanceof ArrayType childType)) throw new ExpectedArrayTypeException(elementType.name());
    int size = dimensions[level];
    if (size < 0) throw new NegativeArrayDimensionException(size);
    List<IValue> values = new ArrayList<>(size);
    for (int i = 0; i < size; i++) values.add(buildArray(context, childType, dimensions, level + 1));
    ArrayValue array = new ArrayValue(type, values);
    int address = context.heap().allocate(array);
    return new ReferenceValue(address);
  }

  /**
   * Retrieves an element from an array referenced by the given reference.
   *
   * @param context the current execution context
   * @param reference the reference to the array
   * @param index the element index
   * @return the value stored at the specified index
   *
   * @throws NullArgumentException if {@code context} or {@code reference} is {@code null}
   * @throws InvalidArrayIndexException if {@code index} is outside the array bounds
   * @throws ExpectedArrayValueException if the referenced heap value is not an array
   */
  public static IValue get(ExecutionContext context, ReferenceValue reference, int index) {
    ArrayValue array = resolveArray(context, reference);
    if (index < 0 || index >= array.size()) throw new InvalidArrayIndexException(index, array.size());
    return array.values().get(index);
  }

  /**
   * Replaces an element in an array referenced by the given reference.
   *
   * <p>
   * Since {@link ArrayValue} is immutable, this operation creates a new
   * array value containing the updated element and replaces the previous
   * array value at the same heap address. The original reference therefore
   * remains valid and continues to refer to the updated array.
   * </p>
   *
   * @param context the current execution context
   * @param reference the reference to the array
   * @param index the element index
   * @param value the new element value
   *
   * @throws NullArgumentException if {@code context}, {@code reference}, or {@code value} is {@code null}
   * @throws InvalidArrayIndexException if {@code index} is outside the array bounds
   * @throws ExpectedArrayValueException if the referenced heap value is not an array
   */
  public static void set(ExecutionContext context, ReferenceValue reference, int index, IValue value) {
    if (value == null) throw new NullArgumentException("value");
    ArrayValue array = resolveArray(context, reference);
    if (index < 0 || index >= array.size()) throw new InvalidArrayIndexException(index, array.size());
    List<IValue> values = new ArrayList<>(array.values());
    values.set(index, value);
    ArrayValue updatedArray = new ArrayValue(array.type(), values);
    context.heap().store(reference.address(), updatedArray);
  }

  /**
   * Returns the number of elements in an array referenced by the given
   * reference.
   *
   * @param context the current execution context
   * @param reference the reference to the array
   * @return the number of elements in the array
   *
   * @throws NullArgumentException if {@code context} or {@code reference} is {@code null}
   * @throws ExpectedArrayValueException if the referenced heap value is not an array
   */
  public static int length(ExecutionContext context, ReferenceValue reference) {
    return resolveArray(context, reference).size();
  }

  /**
   * Resolves a heap reference and verifies that it points to an array value.
   *
   * @param context the current execution context
   * @param reference the reference to resolve
   * @return the referenced array value
   *
   * @throws NullArgumentException if {@code context} or {@code reference} is {@code null}
   * @throws ExpectedArrayValueException if the referenced heap value is not an array
   */
  private static ArrayValue resolveArray(ExecutionContext context, ReferenceValue reference) {
    if (context == null) throw new NullArgumentException("context");
    if (reference == null) throw new NullArgumentException("reference");
    IValue value = context.heap().load(reference.address());
    if (!(value instanceof ArrayValue array)) throw new ExpectedArrayValueException(value);
    return array;
  }
}