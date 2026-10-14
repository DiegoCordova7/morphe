package dev.morphe.vm.executor;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.ArithmeticOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.StringValue;
import dev.morphe.runtime.value.primitive.numeric.INumericValue;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.exception.internal.type.ExpectedNumericValueException;
import dev.morphe.vm.operations.ArithmeticOperations;

/**
 * Executes arithmetic bytecode instructions.
 *
 * <p>
 * This executor is responsible for handling every instruction defined by
 * {@link ArithmeticOpCode}. Arithmetic operations consume the required
 * numeric operands from the operand stack, perform the requested operation,
 * and push the resulting value back onto the stack.
 * </p>
 *
 * <p>
 * All arithmetic semantics are delegated to {@link ArithmeticOperations},
 * while this executor is responsible only for stack manipulation and
 * operand validation.
 * </p>
 */
public final class ArithmeticExecutor implements IExecutor<ArithmeticOpCode> {

  /**
   * Executes the given arithmetic instruction.
   *
   * @param vm the virtual machine executing the program
   * @param program the current bytecode program
   * @param instruction the arithmetic instruction to execute
   */
  @Override
  public void execute(VirtualMachine vm, BytecodeProgram program, Instruction<ArithmeticOpCode> instruction) {
    switch (instruction.opcode()) {
      case ADD -> add(vm);
      case SUB -> sub(vm);
      case MUL -> mul(vm);
      case DIV -> div(vm);
      case MOD -> mod(vm);
      case NEG -> neg(vm);
    }
  }

  private void add(VirtualMachine vm) {
    IValue right = vm.context().stack().pop();
    IValue left = vm.context().stack().pop();
    if (left instanceof INumericValue leftNumeric && right instanceof INumericValue rightNumeric) {
      vm.context().stack().push(ArithmeticOperations.add(leftNumeric, rightNumeric));
      return;
    }
    if (left instanceof StringValue || right instanceof StringValue) {
      vm.context().stack().push(ArithmeticOperations.concat(left, right));
    }
  }

  private void sub(VirtualMachine vm) {
    binaryOperation(vm, ArithmeticOperations::sub);
  }

  private void mul(VirtualMachine vm) {
    binaryOperation(vm, ArithmeticOperations::mul);
  }

  private void div(VirtualMachine vm) {
    binaryOperation(vm, ArithmeticOperations::div);
  }

  private void mod(VirtualMachine vm) {
    binaryOperation(vm, ArithmeticOperations::mod);
  }

  private void neg(VirtualMachine vm) {
    INumericValue value = requireNumeric(vm.context().stack().pop());
    vm.context().stack().push(ArithmeticOperations.neg(value));
  }

  /**
   * Executes a binary arithmetic operation.
   *
   * <p>
   * The right operand is popped first, followed by the left operand,
   * preserving the operand order expected by arithmetic instructions.
   * The computed result is then pushed onto the operand stack.
   * </p>
   *
   * @param vm the virtual machine
   * @param operation the arithmetic operation to execute
   */
  private void binaryOperation(VirtualMachine vm, BinaryOperation operation) {
    INumericValue right = requireNumeric(vm.context().stack().pop());
    INumericValue left = requireNumeric(vm.context().stack().pop());
    vm.context().stack().push(operation.apply(left, right));
  }

  /**
   * Ensures that the given runtime value is numeric.
   *
   * <p>
   * Arithmetic instructions are expected to operate exclusively on
   * numeric runtime values. Encountering a non-numeric value indicates
   * an invalid internal VM state.
   * </p>
   *
   * @param value the value to validate
   * @return the validated numeric value
   *
   * @throws ExpectedNumericValueException if the value is not numeric
   */
  private static INumericValue requireNumeric(IValue value) {
    if (!(value instanceof INumericValue numeric)) throw new ExpectedNumericValueException(value);
    return numeric;
  }

  /**
   * Represents a binary arithmetic operation.
   */
  @FunctionalInterface
  private interface BinaryOperation {

    /**
     * Applies the operation to the given operands.
     *
     * @param left the left operand
     * @param right the right operand
     * @return the operation result
     */
    INumericValue apply(INumericValue left, INumericValue right);
  }
}