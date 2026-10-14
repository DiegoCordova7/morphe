package dev.morphe.vm.executor;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.LogicalOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.operations.LogicalOperations;

/**
 * Executes logical bytecode instructions.
 *
 * <p>
 * This executor implements the execution semantics of every
 * {@link LogicalOpCode logical opcode}. Logical instructions consume
 * Boolean values from the operand stack and push the resulting
 * {@link BooleanValue Boolean value}
 * back onto the stack.
 * </p>
 *
 * <p>
 * Supported operations include:
 * </p>
 *
 * <ul>
 *   <li>{@code AND} – performs a logical conjunction.</li>
 *   <li>{@code OR} – performs a logical disjunction.</li>
 *   <li>{@code NOT} – negates a Boolean value.</li>
 * </ul>
 *
 * <p>
 * The actual logical semantics are delegated to {@link LogicalOperations}.
 * This executor is responsible only for adapting the operand stack to
 * the corresponding logical operation.
 * </p>
 */
public final class LogicalExecutor implements IExecutor<LogicalOpCode> {

  /**
   * Executes a single logical instruction.
   *
   * @param vm the virtual machine executing the instruction
   * @param program the program containing the instruction
   * @param instruction the instruction to execute
   */
  @Override
  public void execute(VirtualMachine vm, BytecodeProgram program, Instruction<LogicalOpCode> instruction) {
    switch (instruction.opcode()) {
      case OR -> binaryOperation(vm, LogicalOperations::or);
      case AND -> binaryOperation(vm, LogicalOperations::and);
      case NOT -> unaryOperation(vm, LogicalOperations::not);
    }
  }

  /**
   * Executes a binary logical operation using the two values at the
   * top of the operand stack.
   *
   * <p>
   * The right operand is popped first, followed by the left operand.
   * The resulting Boolean value is then pushed back onto the stack.
   * </p>
   *
   * @param vm the virtual machine executing the operation
   * @param operation the binary logical operation to execute
   */
  private void binaryOperation(VirtualMachine vm, BinaryLogicalOperation operation) {
    IValue right = vm.context().stack().pop();
    IValue left = vm.context().stack().pop();
    vm.context().stack().push(operation.apply(left, right));
  }

  /**
   * Executes a unary logical operation using the value at the top
   * of the operand stack.
   *
   * @param vm the virtual machine executing the operation
   * @param operation the unary logical operation to execute
   */
  private void unaryOperation(VirtualMachine vm, UnaryLogicalOperation operation) {
    IValue value = vm.context().stack().pop();
    vm.context().stack().push(operation.apply(value));
  }

  /**
   * Represents a binary logical operation.
   */
  @FunctionalInterface
  private interface BinaryLogicalOperation {
    IValue apply(IValue left, IValue right);
  }

  /**
   * Represents a unary logical operation.
   */
  @FunctionalInterface
  private interface UnaryLogicalOperation {
    IValue apply(IValue value);
  }
}