package dev.morphe.vm.operations;

import dev.morphe.runtime.environment.ClosureEnvironment;
import dev.morphe.vm.ExecutionContext;
import dev.morphe.vm.exception.internal.scope.RootScopeExitException;
import dev.morphe.vm.memory.Memory;
import dev.morphe.vm.memory.MemoryAllocator;
import dev.morphe.vm.scope.CallFrame;
import dev.morphe.vm.scope.Scope;
import dev.morphe.vm.stack.ValueStack;

/**
 * Provides operations related to runtime variable scopes.
 *
 * <p>
 * These operations implement the semantics of scope-related bytecode
 * instructions. They are responsible for resolving variable slots through
 * the active {@link Scope}, transferring values between memory and the
 * operand stack, and managing nested runtime scopes.
 * </p>
 *
 * <p>
 * Variable bindings are resolved through the scope hierarchy, while
 * runtime values themselves remain stored in {@link Memory}.
 * </p>
 */
public final class ScopeOperations {

  /**
   * Prevents instantiation of this utility class.
   */
  private ScopeOperations() {}

  /**
   * Declares a new variable slot in the active scope.
   *
   * <p>
   * A fresh memory location is allocated and associated with the specified
   * variable slot in the current {@link Scope}. The slot can then be used by
   * subsequent {@code STORE} and {@code LOAD} operations.
   * </p>
   *
   * @param allocator the memory allocator used to allocate the variable slot
   * @param scope the active variable scope
   * @param slot the variable slot to declare
   */
  public static void declare(MemoryAllocator allocator, Scope scope, int slot) {
    int address = allocator.allocate();
    scope.declare(slot, address);
  }

  /**
   * Loads the value associated with the specified variable slot and pushes
   * it onto the operand stack.
   *
   * <p>
   * The variable slot is first resolved to its associated memory address
   * using the supplied {@link Scope}. The value stored at that address is
   * then loaded from {@link Memory} and pushed onto the operand stack.
   * </p>
   *
   * @param stack the operand stack
   * @param memory the execution memory
   * @param scope the active variable scope
   * @param slot the variable slot to load
   */
  public static void load(ValueStack stack, Memory memory, Scope scope, int slot) {
    stack.push(memory.load(scope.load(slot)));
  }

  /**
   * Stores the value at the top of the operand stack into the variable
   * identified by the specified slot.
   *
   * <p>
   * The slot is resolved to its associated memory address through the
   * supplied {@link Scope}. The value at the top of the operand stack is
   * then removed and stored at that memory location.
   * </p>
   *
   * @param stack the operand stack
   * @param memory the execution memory
   * @param scope the active variable scope
   * @param slot the variable slot to store
   */
  public static void store(ValueStack stack, Memory memory, Scope scope, int slot) {
    memory.store(scope.load(slot), stack.pop());
  }

  /**
   * Loads a value from the active closure environment and pushes it onto
   * the operand stack.
   *
   * <p>
   * The specified capture index is resolved through the
   * {@link ClosureEnvironment}. The value contained in the corresponding
   * captured variable cell is then pushed onto the operand stack.
   * </p>
   *
   * @param stack the operand stack
   * @param environment the active closure environment
   * @param index the capture index to load
   */
  public static void loadCapture(ValueStack stack, ClosureEnvironment environment, int index) {
    stack.push(environment.get(index).get());
  }

  /**
   * Stores the value at the top of the operand stack into a captured
   * variable.
   *
   * <p>
   * The specified capture index is resolved through the
   * {@link ClosureEnvironment}. The value at the top of the operand stack is
   * then removed and assigned to the corresponding captured variable cell.
   * </p>
   *
   * @param stack the operand stack
   * @param environment the active closure environment
   * @param index the capture index to store
   */
  public static void storeCapture(ValueStack stack, ClosureEnvironment environment, int index) {
    environment.get(index).set(stack.pop());
  }

  /**
   * Creates and enters a new nested runtime scope.
   *
   * <p>
   * The newly created scope becomes the active scope of the current
   * {@link CallFrame}. Its parent is the previously active scope.
   * </p>
   *
   * @param context the execution context
   */
  public static void enterScope(ExecutionContext context) {
    CallFrame frame = context.currentFrame();
    frame.scope(new Scope(frame.scope()));
  }

  /**
   * Exits the current runtime scope.
   *
   * <p>
   * The parent scope becomes the active scope of the current
   * {@link CallFrame}. Attempting to exit the root scope is not allowed.
   * </p>
   *
   * @param context the execution context
   * @throws RootScopeExitException if the current scope is the root scope
   */
  public static void exitScope(ExecutionContext context) {
    Scope current = context.scope();
    if (current.parent() == null) throw new RootScopeExitException();
    context.currentFrame().scope(current.parent());
  }
}