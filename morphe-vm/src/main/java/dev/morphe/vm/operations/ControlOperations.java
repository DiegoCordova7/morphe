package dev.morphe.vm.operations;

import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.vm.VirtualMachine;

/**
 * Provides low-level control-flow operations used by the Morphe virtual machine.
 *
 * <p>
 * These operations manipulate the execution state of a
 * {@link VirtualMachine} by halting execution or changing its instruction
 * pointer.
 * </p>
 *
 * <p>
 * This class contains the runtime semantics of control-flow operations.
 * Instruction decoding, operand extraction, and runtime type validation are
 * handled by the corresponding VM executor.
 * </p>
 */
public final class ControlOperations {

  /**
   * Prevents instantiation of this utility class.
   */
  private ControlOperations() {}

  /**
   * Halts execution of the specified virtual machine.
   *
   * <p>
   * Once halted, the virtual machine stops processing further instructions
   * during the current execution cycle.
   * </p>
   *
   * @param vm the virtual machine to halt
   */
  public static void halt(VirtualMachine vm) {
    vm.halt();
  }

  /**
   * Unconditionally changes the instruction pointer of the specified
   * virtual machine.
   *
   * <p>
   * The target instruction index is delegated to
   * {@link VirtualMachine#jumpTo(int)}, which is responsible for validating
   * the target position.
   * </p>
   *
   * @param vm the virtual machine whose instruction pointer is changed
   * @param target the target instruction index
   */
  public static void jump(VirtualMachine vm, int target) {
    vm.jumpTo(target);
  }

  /**
   * Jumps to the specified instruction when the given condition is false.
   *
   * <p>
   * If the condition is {@code false}, the virtual machine's instruction
   * pointer is changed to the specified target. If the condition is
   * {@code true}, the instruction pointer is left unchanged.
   * </p>
   *
   * <p>
   * The condition is expected to have already been validated as a
   * {@link BooleanValue} by the instruction executor.
   * </p>
   *
   * @param vm the virtual machine whose instruction pointer may be changed
   * @param condition the boolean condition controlling the jump
   * @param target the target instruction index
   */
  public static void jumpIfFalse(VirtualMachine vm, BooleanValue condition, int target) {
    if (!condition.value()) vm.jumpTo(target);
  }

  /**
   * Jumps to the specified instruction when the given condition is true.
   *
   * <p>
   * If the condition is {@code true}, the virtual machine's instruction
   * pointer is changed to the specified target. If the condition is
   * {@code false}, the instruction pointer is left unchanged.
   * </p>
   *
   * <p>
   * The condition is expected to have already been validated as a
   * {@link BooleanValue} by the instruction executor.
   * </p>
   *
   * @param vm the virtual machine whose instruction pointer may be changed
   * @param condition the boolean condition controlling the jump
   * @param target the target instruction index
   */
  public static void jumpIfTrue(VirtualMachine vm, BooleanValue condition, int target) {
    if (condition.value()) vm.jumpTo(target);
  }
}