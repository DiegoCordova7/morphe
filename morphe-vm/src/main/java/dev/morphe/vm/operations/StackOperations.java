package dev.morphe.vm.operations;

import dev.morphe.runtime.value.IValue;
import dev.morphe.vm.memory.Memory;
import dev.morphe.vm.stack.ValueStack;

/**
 * Provides low-level operations for manipulating the VM operand stack
 * and interacting with VM memory.
 *
 * <p>
 * These operations implement the fundamental semantics of stack-oriented
 * bytecode instructions. They operate directly on the supplied
 * {@link ValueStack} and {@link Memory} instances and do not depend on
 * the {@code VirtualMachine} itself.
 * </p>
 *
 * <p>
 * Supported operations include pushing and removing values from the operand
 * stack, duplicating stack values, and transferring values between the
 * operand stack and VM memory.
 * </p>
 */
public final class StackOperations {

  /**
   * Prevents instantiation of this utility class.
   */
  private StackOperations() {}

  /**
   * Pushes a value onto the operand stack.
   *
   * @param stack the operand stack
   * @param value the value to push
   */
  public static void push(ValueStack stack, IValue value) {
    stack.push(value);
  }

  /**
   * Removes and returns the value at the top of the operand stack.
   *
   * @param stack the operand stack
   * @return the value removed from the top of the stack
   */
  public static IValue pop(ValueStack stack) {
    return stack.pop();
  }

  /**
   * Duplicates the value at the top of the operand stack.
   *
   * <p>
   * The original value remains on the stack and an additional reference
   * to the same runtime value is pushed on top of it.
   * </p>
   *
   * @param stack the operand stack
   */
  public static void dup(ValueStack stack) {
    stack.push(stack.peek());
  }

  /**
   * Duplicates the second value from the top of the operand stack.
   *
   * <p>
   * For a stack containing {@code [..., a, b]}, the resulting stack is
   * {@code [..., a, b, a]}.
   * </p>
   *
   * @param stack the operand stack
   */
  public static void over(ValueStack stack) {
    IValue top = stack.pop();
    IValue second = stack.pop();
    stack.push(second);
    stack.push(top);
    stack.push(second);
  }

  /**
   * Swaps the two values at the top of the operand stack.
   *
   * <p>
   * For a stack containing {@code [..., a, b]}, the resulting stack is
   * {@code [..., b, a]}.
   * </p>
   *
   * @param stack the operand stack
   */
  public static void swap(ValueStack stack) {
    IValue top = stack.pop();
    IValue below = stack.pop();
    stack.push(top);
    stack.push(below);
  }

  /**
   * Rotates the three values at the top of the operand stack.
   *
   * <p>
   * For a stack containing {@code [..., a, b, c]}, the resulting stack is
   * {@code [..., b, c, a]}.
   * </p>
   *
   * @param stack the operand stack
   */
  public static void rot(ValueStack stack) {
    IValue top = stack.pop();
    IValue middle = stack.pop();
    IValue bottom = stack.pop();
    stack.push(middle);
    stack.push(top);
    stack.push(bottom);
  }

  /**
   * Duplicates the two values at the top of the operand stack.
   *
   * <p>
   * For a stack containing {@code [..., a, b]}, the resulting stack is
   * {@code [..., a, b, a, b]}.
   * </p>
   *
   * @param stack the operand stack
   */
  public static void dup2(ValueStack stack) {
    IValue top = stack.pop();
    IValue below = stack.pop();
    stack.push(below);
    stack.push(top);
    stack.push(below);
    stack.push(top);
  }
}