package dev.morphe.vm;

import dev.morphe.runtime.environment.ClosureEnvironment;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.internal.stack.EmptyStackException;
import dev.morphe.vm.memory.*;
import dev.morphe.vm.scope.*;
import dev.morphe.vm.stack.*;

/**
 * Represents the mutable execution state of a Morphe virtual machine.
 *
 * <p>An execution context contains the runtime resources required while
 * executing bytecode instructions. It provides access to the operand stack,
 * linear memory, memory allocator, heap storage, and call stack used during
 * bytecode execution.</p>
 *
 * <p>The context also provides immutable snapshots of its complete execution
 * state. These snapshots allow the virtual machine to restore its state to a
 * previous point in execution, such as when recovering from an {@code attempt}
 * block.</p>
 *
 * <p>The context itself does not define instruction semantics. Instead,
 * instruction executors operate on the resources exposed by this context
 * to manipulate the virtual machine state.</p>
 */
public final class ExecutionContext {

  /**
   * Represents an immutable snapshot of the complete execution state.
   *
   * <p>The snapshot contains the state of every mutable execution resource.
   * The resources themselves are not replaced when restoring; instead, their
   * internal state is restored through their respective snapshot types.</p>
   *
   * @param stack the operand stack state
   * @param memory the linear memory state
   * @param allocator the memory allocator state
   * @param heap the heap state
   * @param callStack the call stack state
   */
  public record Snapshot(ValueStack.Snapshot stack, Memory.Snapshot memory, MemoryAllocator.Snapshot allocator,
      Heap.Snapshot heap, CallStack.Snapshot callStack) {

    /**
     * Creates an execution context snapshot.
     *
     * @throws NullArgumentException if any component snapshot is {@code null}
     */
    public Snapshot {
      if (stack == null) throw new NullArgumentException("stack");
      if (memory == null) throw new NullArgumentException("memory");
      if (allocator == null) throw new NullArgumentException("allocator");
      if (heap == null) throw new NullArgumentException("heap");
      if (callStack == null) throw new NullArgumentException("callStack");
    }
  }

  private final ValueStack stack;
  private final Memory memory;
  private final MemoryAllocator allocator;
  private final Heap heap;
  private final CallStack callStack;

  /**
   * Creates an execution context with an operand stack,
   * memory, memory allocator, heap, and call stack.
   */
  public ExecutionContext() {
    this.stack = new ValueStack();
    this.memory = new Memory();
    this.allocator = new MemoryAllocator();
    this.heap = new Heap();
    this.callStack = createInitialCallStack();
  }

  /**
   * Creates the initial call stack for a new execution context.
   *
   * <p>The initial stack contains a root call frame representing the global
   * execution scope. The root frame uses {@code -1} as its return address because
   * it does not correspond to a function call and therefore has no caller to
   * return to.</p>
   *
   * @return a call stack containing the initial root frame
   */
  private static CallStack createInitialCallStack() {
    CallStack callStack = new CallStack();
    callStack.push(new CallFrame(-1, new Scope(null), new ClosureEnvironment()));
    return callStack;
  }

  /**
   * Returns the operand stack associated with this execution context.
   *
   * @return the operand stack
   */
  public ValueStack stack() {
    return stack;
  }

  /**
   * Returns the memory associated with this execution context.
   *
   * @return the execution memory
   */
  public Memory memory() {
    return memory;
  }

  /**
   * Returns the memory allocator associated with this execution context.
   *
   * @return the memory allocator
   */
  public MemoryAllocator allocator() {
    return allocator;
  }

  /**
   * Returns the heap associated with this execution context.
   *
   * @return the execution heap
   */
  public Heap heap() {
    return heap;
  }

  /**
   * Returns the call stack associated with this execution context.
   *
   * @return the call stack
   */
  public CallStack callStack() {
    return callStack;
  }

  /**
   * Returns the current call frame.
   *
   * <p>The current call frame is the frame at the top of the call stack.</p>
   *
   * @return the current call frame
   * @throws EmptyStackException if the call stack is empty
   */
  public CallFrame currentFrame() {
    return callStack.peek();
  }

  /**
   * Returns the current variable scope.
   *
   * <p>The current scope is the scope associated with the active call frame
   * at the top of the call stack.</p>
   *
   * @return the current variable scope
   * @throws EmptyStackException if the call stack is empty
   */
  public Scope scope() {
    return currentFrame().scope();
  }

  /**
   * Returns whether the execution context currently contains an active
   * call frame.
   *
   * @return {@code true} if at least one call frame is active;
   *     {@code false} otherwise
   */
  public boolean hasFrame() {
    return !callStack.isEmpty();
  }

  /**
   * Creates an immutable snapshot of the complete execution state.
   *
   * <p>The snapshot captures every mutable component managed by this
   * context. The resulting snapshot can later be supplied to
   * {@link #restore(Snapshot)} to return the context to the captured state.</p>
   *
   * @return an immutable snapshot of the current execution state
   */
  public Snapshot snapshot() {
    return new Snapshot(stack.snapshot(), memory.snapshot(), allocator.snapshot(), heap.snapshot(), callStack.snapshot());
  }

  /**
   * Restores the execution state from a previously captured snapshot.
   *
   * <p>The existing execution resources are preserved. Only their mutable
   * internal state is restored. This allows references held by other runtime
   * objects to remain valid after restoration.</p>
   *
   * <p>This operation is used by the virtual machine to roll back execution
   * to a previously captured state, such as when recovering from an
   * {@code attempt} block.</p>
   *
   * @param snapshot the snapshot to restore
   * @throws NullArgumentException if {@code snapshot} is {@code null}
   */
  public void restore(Snapshot snapshot) {
    if (snapshot == null) throw new NullArgumentException("snapshot");
    stack.restore(snapshot.stack());
    memory.restore(snapshot.memory());
    allocator.restore(snapshot.allocator());
    heap.restore(snapshot.heap());
    callStack.restore(snapshot.callStack());
  }
}