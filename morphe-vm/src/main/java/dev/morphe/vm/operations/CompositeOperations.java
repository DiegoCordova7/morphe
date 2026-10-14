package dev.morphe.vm.operations;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.CompositeOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.bytecode.type.CompositeBytecodeType;
import dev.morphe.runtime.exception.field.UnknownFieldException;
import dev.morphe.runtime.exception.type.TypeMismatchException;
import dev.morphe.runtime.type.composite.CompositeType;
import dev.morphe.runtime.type.composite.FieldType;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.composite.CompositeValue;
import dev.morphe.runtime.value.reference.ReferenceValue;
import dev.morphe.vm.ExecutionContext;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.exception.internal.composite.InvalidCompositeReferenceException;
import dev.morphe.vm.type.RuntimeTypeChecker;
import dev.morphe.vm.type.TypeResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides operations for creating and manipulating composite runtime values.
 *
 * <p>
 * Composite values are allocated in the VM heap and exposed to the execution
 * environment through {@link ReferenceValue references}. Field values are
 * validated against the composite's runtime type before being stored.
 * </p>
 */
public final class CompositeOperations {

  /**
   * Prevents instantiation of this utility class.
   */
  private CompositeOperations() {}

  /**
   * Creates a composite value from the values currently stored on the VM stack.
   *
   * <p>
   * Values are consumed in reverse field order so that the value associated with
   * each field matches the order in which the fields were declared in the
   * bytecode type. The resulting composite is allocated in the heap and its
   * address is returned as a reference.
   * </p>
   *
   * @param context the current VM execution context
   * @param type the bytecode composite type
   * @return a reference to the newly allocated composite value
   * @throws TypeMismatchException if a field value does not match its declared type
   */
  public static ReferenceValue create(ExecutionContext context, CompositeBytecodeType type) {
    CompositeType runtimeType = (CompositeType) TypeResolver.resolve(type);
    Map<String, IValue> values = new HashMap<>();
    List<CompositeBytecodeType.CompositeBytecodeField> fields = type.fields();
    for (int index = fields.size() - 1; index >= 0; index--) {
      CompositeBytecodeType.CompositeBytecodeField bytecodeField  = fields.get(index);
      IValue value = context.stack().pop();
      FieldType field = runtimeType.field(bytecodeField.name());
      if (!RuntimeTypeChecker.accepts(context, field.type(), value))
        throw new TypeMismatchException(field.type(), value.kind().name());
      values.put(field.name(), value);
    }
    CompositeValue composite = new CompositeValue(runtimeType, values);
    int address = context.heap().allocate(composite);
    return new ReferenceValue(address);
  }

  /**
   * Retrieves the value stored in a composite field.
   *
   * @param context the current VM execution context
   * @param reference the reference to the composite value
   * @param field the name of the field to retrieve
   * @return the value stored in the specified field
   * @throws InvalidCompositeReferenceException if the reference does not
   *     point to a composite value
   * @throws UnknownFieldException if the specified field does not exist
   */
  public static IValue get(ExecutionContext context, ReferenceValue reference, String field) {
    IValue value = context.heap().load(reference.address());
    if (!(value instanceof CompositeValue composite)) throw new InvalidCompositeReferenceException(reference.address());
    return composite.get(field);
  }

  /**
   * Stores a value in a composite field.
   *
   * <p>
   * The value is validated against the declared field type before being stored.
   * A Java {@code null} value is stored directly when provided; otherwise the
   * runtime type checker validates the value.
   * </p>
   *
   * @param context the current VM execution context
   * @param reference the reference to the composite value
   * @param field the name of the field to modify
   * @param value the value to store
   * @throws InvalidCompositeReferenceException if the reference does not
   *     point to a composite value
   * @throws TypeMismatchException if the value does not match the field type
   * @throws UnknownFieldException if the specified field does not exist
   */
  public static void set(ExecutionContext context, ReferenceValue reference, String field, IValue value) {
    IValue stored = context.heap().load(reference.address());
    if (!(stored instanceof CompositeValue composite)) throw new InvalidCompositeReferenceException(reference.address());
    FieldType fieldType = composite.type().field(field);
    if (fieldType == null) {
      composite.set(field, value);
      return;
    }
    if (value == null) {
      composite.set(field, null);
      return;
    }
    if (!RuntimeTypeChecker.accepts(context, fieldType.type(), value))
      throw new TypeMismatchException(fieldType.type(), value.kind().name());
    composite.set(field, value);
  }

  /**
   * Checks whether a target composite structurally satisfies a model composite (~= operator).
   *
   * @param context the execution context containing the heap
   * @param leftRef the reference to the target composite (left operand)
   * @param rightRef the reference to the model composite (right operand)
   * @return true if the target satisfies the model shape
   * @throws InvalidCompositeReferenceException if either reference does not point to a composite value
   */
  public static boolean match(ExecutionContext context, ReferenceValue leftRef, ReferenceValue rightRef) {
    IValue leftStored = context.heap().load(leftRef.address());
    IValue rightStored = context.heap().load(rightRef.address());
    if (!(leftStored instanceof CompositeValue leftComposite))
      throw new InvalidCompositeReferenceException(leftRef.address());
    if (!(rightStored instanceof CompositeValue rightComposite))
      throw new InvalidCompositeReferenceException(rightRef.address());
    return leftComposite.matchesShape(rightComposite);
  }
}