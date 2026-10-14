package dev.morphe.vm.executor;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.ArrayOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.bytecode.type.IBytecodeType;
import dev.morphe.runtime.type.IType;
import dev.morphe.runtime.type.collection.ArrayType;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.runtime.value.reference.ReferenceValue;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.exception.internal.type.ExpectedNumericValueException;
import dev.morphe.vm.exception.internal.type.ExpectedReferenceValueException;
import dev.morphe.vm.exception.internal.type.UnsupportedBytecodeTypeException;
import dev.morphe.vm.operations.ArrayOperations;
import dev.morphe.vm.type.TypeResolver;

/**
 * Executes array-related bytecode instructions.
 *
 * <p>
 * The {@code ArrayExecutor} acts as the bridge between bytecode array
 * instructions and their runtime implementation. It extracts operands from
 * the VM operand stack, validates their runtime representations, resolves
 * bytecode types into runtime types when required, and delegates array
 * semantics to {@link ArrayOperations}.
 * </p>
 *
 * <p>
 * Array values are managed by the VM heap and represented during execution
 * by {@link ReferenceValue} instances. The executor does not directly
 * manipulate heap storage; it only passes the execution context to the
 * corresponding array operations.
 * </p>
 *
 * <p>
 * The supported instructions are:
 * </p>
 *
 * <ul>
 *   <li>{@link ArrayOpCode#ARRAY_NEW} creates a new single-dimensional array.</li>
 *   <li>{@link ArrayOpCode#ARRAY_NEW_MULTI} creates a multidimensional array.</li>
 *   <li>{@link ArrayOpCode#ARRAY_GET} retrieves an array element.</li>
 *   <li>{@link ArrayOpCode#ARRAY_SET} replaces an array element.</li>
 *   <li>{@link ArrayOpCode#ARRAY_LENGTH} retrieves the array length.</li>
 * </ul>
 *
 * <p>
 * Instructions that produce a value push their result onto the operand
 * stack. Instructions that modify an existing array consume their operands
 * without pushing a result.
 * </p>
 */
public final class ArrayExecutor implements IExecutor<ArrayOpCode> {

  /**
   * Executes an array-related bytecode instruction.
   *
   * <p>
   * The instruction opcode determines which array operation is performed.
   * Operands required by the operation are consumed from the VM operand
   * stack, and the operation is delegated to {@link ArrayOperations}.
   * </p>
   *
   * @param vm the virtual machine executing the instruction
   * @param program the bytecode program containing the instruction
   * @param instruction the instruction to execute
   */
  @Override
  public void execute(VirtualMachine vm, BytecodeProgram program, Instruction<ArrayOpCode> instruction) {
    switch (instruction.opcode()) {
      case ARRAY_NEW -> newArray(vm, program, instruction);
      case ARRAY_NEW_MULTI -> newMultiArray(vm, program, instruction);
      case ARRAY_GET -> get(vm);
      case ARRAY_SET -> set(vm);
      case ARRAY_LENGTH -> length(vm);
    }
  }

  /**
   * Creates a new single-dimensional array and pushes its reference onto
   * the operand stack.
   *
   * <p>
   * The instruction operand identifies the bytecode type of the array.
   * The type is retrieved from the program type pool and resolved into a
   * runtime {@link ArrayType} through {@link TypeResolver}.
   * </p>
   *
   * <p>
   * The array size is consumed from the top of the operand stack.
   * {@link ArrayOperations#newArray} allocates and initializes the array
   * on the VM heap.
   * </p>
   *
   * <p>
   * The resulting {@link ReferenceValue} is pushed onto the operand stack
   * for subsequent array operations.
   * </p>
   *
   * @param vm the virtual machine executing the instruction
   * @param program the bytecode program containing the array type
   * @param instruction the array creation instruction
   *
   * @throws UnsupportedBytecodeTypeException if the resolved bytecode type
   *         does not represent an array type
   * @throws ExpectedNumericValueException if the array size is not an integer
   */
  private void newArray(VirtualMachine vm, BytecodeProgram program, Instruction<ArrayOpCode> instruction) {
    int typeIndex = instruction.operand(0);
    IBytecodeType bytecodeType = program.typeAt(typeIndex);
    IType type = TypeResolver.resolve(bytecodeType);
    if (!(type instanceof ArrayType arrayType)) throw new UnsupportedBytecodeTypeException(bytecodeType);
    IValue sizeValue = vm.context().stack().pop();
    if (!(sizeValue instanceof IntegerValue size)) throw new ExpectedNumericValueException(sizeValue);
    ReferenceValue reference = ArrayOperations.newArray(vm.context(), arrayType, size.value());
    vm.context().stack().push(reference);
  }

  /**
   * Creates a multidimensional array and pushes its reference onto the
   * operand stack.
   *
   * <p>
   * The first instruction operand identifies the bytecode array type, while
   * the second specifies the number of dimensions. The bytecode type is
   * retrieved from the program type pool and resolved into a runtime
   * {@link ArrayType} through {@link TypeResolver}.
   * </p>
   *
   * <p>
   * Dimension sizes are consumed from the operand stack in reverse order.
   * This restores the original dimension order because the operand stack
   * follows LIFO semantics.
   * </p>
   *
   * <p>
   * For example, if the stack contains:
   * </p>
   *
   * <pre>
   * [..., dim0, dim1, dim2]
   * </pre>
   *
   * <p>
   * the values are popped as {@code dim2}, {@code dim1}, and {@code dim0},
   * then passed to {@link ArrayOperations#newMultiArray} as:
   * </p>
   *
   * <pre>
   * [dim0, dim1, dim2]
   * </pre>
   *
   * <p>
   * The resulting {@link ReferenceValue} is pushed onto the operand stack.
   * </p>
   *
   * @param vm the virtual machine executing the instruction
   * @param program the bytecode program containing the array type
   * @param instruction the multidimensional array creation instruction
   *
   * @throws UnsupportedBytecodeTypeException if the resolved bytecode type
   *         does not represent an array type
   * @throws ExpectedNumericValueException if any dimension size is not
   *         an integer
   */
  private void newMultiArray(VirtualMachine vm, BytecodeProgram program, Instruction<ArrayOpCode> instruction) {
    int typeIndex = instruction.operand(0);
    int dimensionsCount = instruction.operand(1);
    IBytecodeType bytecodeType = program.typeAt(typeIndex);
    IType type = TypeResolver.resolve(bytecodeType);
    if (!(type instanceof ArrayType arrayType)) throw new UnsupportedBytecodeTypeException(bytecodeType);
    int[] dimensions = new int[dimensionsCount];
    for (int i = dimensionsCount - 1; i >= 0; i--) {
      IValue value = vm.context().stack().pop();
      if (!(value instanceof IntegerValue size)) throw new ExpectedNumericValueException(value);
      dimensions[i] = size.value();
    }
    ReferenceValue reference = ArrayOperations.newMultiArray(vm.context(), arrayType, dimensions);
    vm.context().stack().push(reference);
  }

  /**
   * Retrieves an element from an array.
   *
   * <p>
   * The array reference and element index are consumed from the operand
   * stack. The retrieved value is then pushed back onto the stack.
   * </p>
   *
   * <p>
   * The expected stack layout before execution is:
   * </p>
   *
   * <pre>
   * [..., reference, index]
   * </pre>
   *
   * <p>
   * After execution, the stack contains:
   * </p>
   *
   * <pre>
   * [..., value]
   * </pre>
   *
   * @param vm the virtual machine executing the instruction
   *
   * @throws ExpectedNumericValueException if the index is not an integer
   * @throws ExpectedReferenceValueException if the array reference is not
   *         a {@link ReferenceValue}
   */
  private void get(VirtualMachine vm) {
    IValue indexValue = vm.context().stack().pop();
    IValue referenceValue = vm.context().stack().pop();
    if (!(indexValue instanceof IntegerValue index)) throw new ExpectedNumericValueException(indexValue);
    if (!(referenceValue instanceof ReferenceValue reference)) throw new ExpectedReferenceValueException(referenceValue);
    IValue value = ArrayOperations.get(vm.context(), reference, index.value());
    vm.context().stack().push(value);
  }

  /**
   * Replaces an element in an array.
   *
   * <p>
   * The value, index, and array reference are consumed from the operand
   * stack. The operation does not push a result onto the stack.
   * </p>
   *
   * <p>
   * The expected stack layout before execution is:
   * </p>
   *
   * <pre>
   * [..., reference, index, value]
   * </pre>
   *
   * @param vm the virtual machine executing the instruction
   *
   * @throws ExpectedNumericValueException if the index is not an integer
   * @throws ExpectedReferenceValueException if the array reference is not
   *         a {@link ReferenceValue}
   */
  private void set(VirtualMachine vm) {
    IValue value = vm.context().stack().pop();
    IValue indexValue = vm.context().stack().pop();
    IValue referenceValue = vm.context().stack().pop();
    if (!(indexValue instanceof IntegerValue index)) throw new ExpectedNumericValueException(indexValue);
    if (!(referenceValue instanceof ReferenceValue reference)) throw new ExpectedReferenceValueException(referenceValue);
    ArrayOperations.set(vm.context(), reference, index.value(), value);
  }

  /**
   * Retrieves the length of an array.
   *
   * <p>
   * The array reference is consumed from the operand stack and the resulting
   * length is pushed as an {@link IntegerValue}.
   * </p>
   *
   * <p>
   * The expected stack layout before execution is:
   * </p>
   *
   * <pre>
   * [..., reference]
   * </pre>
   *
   * <p>
   * After execution, the stack contains:
   * </p>
   *
   * <pre>
   * [..., length]
   * </pre>
   *
   * @param vm the virtual machine executing the instruction
   *
   * @throws ExpectedReferenceValueException if the array reference is not
   *         a {@link ReferenceValue}
   */
  private void length(VirtualMachine vm) {
    IValue referenceValue = vm.context().stack().pop();
    if (!(referenceValue instanceof ReferenceValue reference)) throw new ExpectedReferenceValueException(referenceValue);
    int length = ArrayOperations.length(vm.context(), reference);
    vm.context().stack().push(new IntegerValue(length));
  }
}