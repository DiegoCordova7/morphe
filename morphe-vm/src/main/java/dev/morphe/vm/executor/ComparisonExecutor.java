package dev.morphe.vm.executor;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.ComparisonOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.runtime.value.IValue;
import dev.morphe.vm.ExecutionContext;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.operations.ComparisonOperations;

/**
 * Executes comparison-related bytecode instructions.
 *
 * <p>
 * The {@code ComparisonExecutor} acts as the bridge between comparison
 * bytecode instructions and their runtime implementation. It consumes the
 * operands required by each comparison from the VM operand stack, delegates
 * the comparison to {@link ComparisonOperations}, and pushes the resulting
 * Boolean value back onto the stack.
 * </p>
 *
 * <p>
 * The supported instructions are:
 * </p>
 *
 * <ul>
 *   <li>{@link ComparisonOpCode#EQ} checks value equality.</li>
 *   <li>{@link ComparisonOpCode#NEQ} checks value inequality.</li>
 *   <li>{@link ComparisonOpCode#LT} checks whether the left operand is less
 *       than the right operand.</li>
 *   <li>{@link ComparisonOpCode#LTE} checks whether the left operand is less
 *       than or equal to the right operand.</li>
 *   <li>{@link ComparisonOpCode#GT} checks whether the left operand is greater
 *       than the right operand.</li>
 *   <li>{@link ComparisonOpCode#GTE} checks whether the left operand is
 *       greater than or equal to the right operand.</li>
 * </ul>
 *
 * <p>
 * Equality and inequality comparisons receive the current
 * {@link ExecutionContext} because their runtime implementation may require
 * access to VM-managed state when comparing reference values.
 * </p>
 */
public final class ComparisonExecutor implements IExecutor<ComparisonOpCode> {

  /**
   * Executes a single comparison instruction.
   *
   * <p>
   * The instruction opcode determines which comparison operation is
   * performed. The corresponding operation consumes its operands from the
   * operand stack and pushes the resulting Boolean value.
   * </p>
   *
   * @param vm the virtual machine executing the instruction
   * @param program the program containing the instruction
   * @param instruction the comparison instruction to execute
   */
  @Override
  public void execute(VirtualMachine vm, BytecodeProgram program, Instruction<ComparisonOpCode> instruction) {
    switch (instruction.opcode()) {
      case EQ -> contextualBinaryOperation(vm, ComparisonOperations::eq);
      case NEQ -> contextualBinaryOperation(vm, ComparisonOperations::neq);
      case LT -> binaryOperation(vm, ComparisonOperations::lt);
      case LTE -> binaryOperation(vm, ComparisonOperations::lte);
      case GT -> binaryOperation(vm, ComparisonOperations::gt);
      case GTE -> binaryOperation(vm, ComparisonOperations::gte);
    }
  }

  /**
   * Executes a binary comparison operation that requires the current
   * execution context.
   *
   * <p>
   * The right operand is removed from the top of the operand stack first,
   * followed by the left operand. The original operand order is preserved
   * when the operation is invoked. The resulting Boolean value is then
   * pushed back onto the operand stack.
   * </p>
   *
   * @param vm the virtual machine executing the operation
   * @param operation the contextual comparison operation to execute
   */
  private void contextualBinaryOperation(VirtualMachine vm, ContextualBinaryOperation operation) {
    IValue right = vm.context().stack().pop();
    IValue left = vm.context().stack().pop();
    vm.context().stack().push(operation.apply(vm.context(), left, right));
  }

  /**
   * Executes a binary comparison operation that does not require execution
   * context.
   *
   * <p>
   * The right operand is removed from the top of the operand stack first,
   * followed by the left operand. The original operand order is preserved
   * when the operation is invoked. The resulting Boolean value is then
   * pushed back onto the operand stack.
   * </p>
   *
   * @param vm the virtual machine executing the operation
   * @param operation the comparison operation to execute
   */
  private void binaryOperation(VirtualMachine vm, BinaryOperation operation) {
    IValue right = vm.context().stack().pop();
    IValue left = vm.context().stack().pop();
    vm.context().stack().push(operation.apply(left, right));
  }

  /**
   * Represents a binary comparison operation that does not require execution
   * context.
   */
  @FunctionalInterface
  private interface BinaryOperation {

    /**
     * Applies the comparison to two runtime values.
     *
     * @param left the left operand
     * @param right the right operand
     * @return the Boolean result of the comparison
     */
    IValue apply(IValue left, IValue right);
  }

  /**
   * Represents a binary comparison operation that requires the current
   * execution context.
   */
  @FunctionalInterface
  private interface ContextualBinaryOperation {

    /**
     * Applies the comparison using the current execution context.
     *
     * @param context the current VM execution context
     * @param left the left operand
     * @param right the right operand
     * @return the Boolean result of the comparison
     */
    IValue apply(ExecutionContext context, IValue left, IValue right);
  }
}