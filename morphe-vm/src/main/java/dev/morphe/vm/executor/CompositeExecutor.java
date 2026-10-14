package dev.morphe.vm.executor;

import dev.morphe.bytecode.constants.IConstant;
import dev.morphe.bytecode.constants.StringConstant;
import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.CompositeOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.bytecode.type.CompositeBytecodeType;
import dev.morphe.bytecode.type.IBytecodeType;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.composite.CompositeValue;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.runtime.value.reference.ReferenceValue;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.exception.internal.constant.ExpectedStringConstantException;
import dev.morphe.vm.exception.internal.type.ExpectedReferenceValueException;
import dev.morphe.vm.exception.internal.type.UnsupportedBytecodeTypeException;
import dev.morphe.vm.operations.CompositeOperations;

/**
 * Executes composite-related bytecode instructions.
 *
 * <p>
 * The {@code CompositeExecutor} acts as the bridge between composite
 * bytecode instructions and their runtime implementation. It resolves
 * composite types and field names from the bytecode program, validates
 * references obtained from the operand stack, and delegates composite
 * semantics to {@link CompositeOperations}.
 * </p>
 *
 * <p>
 * The supported instructions are:
 * </p>
 *
 * <ul>
 *   <li>{@link CompositeOpCode#COMPOSITE_NEW} creates a new composite value.</li>
 *   <li>{@link CompositeOpCode#GET_FIELD} retrieves a field value.</li>
 *   <li>{@link CompositeOpCode#SET_FIELD} updates a field value.</li>
 *   <li>{@link CompositeOpCode#STRUCT_MATCH} checks structural match.</li>
 * </ul>
 */
public final class CompositeExecutor implements IExecutor<CompositeOpCode> {

  /**
   * Executes a single composite-related bytecode instruction.
   *
   * <p>
   * The instruction opcode determines which composite operation is
   * performed. The corresponding operation is delegated to
   * {@link CompositeOperations}.
   * </p>
   *
   * @param vm the virtual machine executing the instruction
   * @param program the bytecode program containing composite types and
   *     field constants
   * @param instruction the composite instruction to execute
   */
  @Override
  public void execute(VirtualMachine vm, BytecodeProgram program, Instruction<CompositeOpCode> instruction) {
    switch (instruction.opcode()) {
      case COMPOSITE_NEW -> newComposite(vm, program, instruction);
      case GET_FIELD -> getField(vm, program, instruction);
      case SET_FIELD -> setField(vm, program, instruction);
      case STRUCT_MATCH -> structMatch(vm, program, instruction);
    }
  }

  /**
   * Creates a new composite value and pushes its reference onto the operand
   * stack.
   *
   * <p>
   * The instruction operand identifies the composite type in the program's
   * type table. The type must be a {@link CompositeBytecodeType}.
   * </p>
   *
   * @param vm the virtual machine executing the instruction
   * @param program the bytecode program containing the composite type
   * @param instruction the composite creation instruction
   *
   * @throws UnsupportedBytecodeTypeException if the referenced bytecode type
   *     is not a {@link CompositeBytecodeType}
   */
  private void newComposite(VirtualMachine vm, BytecodeProgram program, Instruction<CompositeOpCode> instruction) {
    int typeIndex = instruction.operand(0);
    IBytecodeType type = program.typeAt(typeIndex);
    if (!(type instanceof CompositeBytecodeType compositeType)) throw new UnsupportedBytecodeTypeException(type);
    ReferenceValue reference = CompositeOperations.create(vm.context(), compositeType);
    vm.context().stack().push(reference);
  }

  /**
   * Retrieves a field value from a composite.
   *
   * <p>
   * The composite reference is consumed from the operand stack. The field
   * name is resolved from the program's constant table. The retrieved value
   * is then pushed onto the operand stack.
   * </p>
   *
   * <p>
   * Expected stack state:
   * </p>
   *
   * <pre>
   * Before: [..., reference]
   * After:  [..., value]
   * </pre>
   *
   * @param vm the virtual machine executing the instruction
   * @param program the bytecode program containing the field name
   * @param instruction the field access instruction
   *
   * @throws ExpectedReferenceValueException if the stack does not contain
   *     a {@link ReferenceValue}
   * @throws ExpectedStringConstantException if the field operand does not
   *     reference a string constant
   */
  private void getField(VirtualMachine vm, BytecodeProgram program, Instruction<CompositeOpCode> instruction) {
    ReferenceValue reference = popReference(vm);
    String field = resolveField(program, instruction);
    IValue value = CompositeOperations.get(vm.context(), reference, field);
    vm.context().stack().push(value);
  }

  /**
   * Updates a field value in a composite.
   *
   * <p>
   * The value and composite reference are consumed from the operand stack.
   * The field name is resolved from the program's constant table. The
   * operation does not push a result onto the operand stack.
   * </p>
   *
   * <p>
   * Expected stack state:
   * </p>
   *
   * <pre>
   * Before: [..., reference, value]
   * After:  [...]
   * </pre>
   *
   * @param vm the virtual machine executing the instruction
   * @param program the bytecode program containing the field name
   * @param instruction the field access instruction
   *
   * @throws ExpectedReferenceValueException if the value below the field
   *     value is not a {@link ReferenceValue}
   * @throws ExpectedStringConstantException if the field operand does not
   *     reference a string constant
   */
  private void setField(VirtualMachine vm, BytecodeProgram program, Instruction<CompositeOpCode> instruction) {
    IValue value = vm.context().stack().pop();
    ReferenceValue reference = popReference(vm);
    String field = resolveField(program, instruction);
    CompositeOperations.set(vm.context(), reference, field, value);
  }

  /**
   * Checks whether a target composite structurally satisfies a model composite (~= operator).
   *
   * <p>
   * Consumes the right operand (model) and the left operand (target reference) from
   * the stack, verifies their shapes, and pushes a boolean result.
   * </p>
   *
   * @param vm the virtual machine executing the instruction
   * @param program the bytecode program
   * @param instruction the structural match instruction
   */
  private void structMatch(VirtualMachine vm, BytecodeProgram program, Instruction<CompositeOpCode> instruction) {
    ReferenceValue rightRef = popReference(vm);
    ReferenceValue leftRef = popReference(vm);
    boolean matches = CompositeOperations.match(vm.context(), leftRef, rightRef);
    vm.context().stack().push(BooleanValue.of(matches));
  }

  /**
   * Removes and validates a composite reference from the operand stack.
   *
   * @param vm the virtual machine executing the instruction
   * @return the reference removed from the operand stack
   *
   * @throws ExpectedReferenceValueException if the value on top of the
   *     operand stack is not a {@link ReferenceValue}
   */
  private ReferenceValue popReference(VirtualMachine vm) {
    IValue value = vm.context().stack().pop();
    if (!(value instanceof ReferenceValue reference)) throw new ExpectedReferenceValueException(value);
    return reference;
  }

  /**
   * Resolves a composite field name from the program's constant table.
   *
   * <p>
   * The instruction operand identifies a constant in the program's
   * constant table. The constant must be a {@link StringConstant}, whose
   * value is used as the field name.
   * </p>
   *
   * @param program the bytecode program containing the constant
   * @param instruction the instruction containing the constant index
   * @return the resolved field name
   *
   * @throws ExpectedStringConstantException if the referenced constant is
   *     not a {@link StringConstant}
   */
  private String resolveField(BytecodeProgram program, Instruction<CompositeOpCode> instruction) {
    int constantIndex = instruction.operand(0);
    IConstant constant = program.constantAt(constantIndex);
    if (!(constant instanceof StringConstant field)) throw new ExpectedStringConstantException(constant);
    return field.value();
  }
}