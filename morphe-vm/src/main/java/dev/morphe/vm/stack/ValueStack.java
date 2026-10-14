package dev.morphe.vm.stack;

import dev.morphe.runtime.value.IValue;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.internal.stack.EmptyStackException;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Represents the operand stack of the Morphe virtual machine.
 *
 * <p>
 * The value stack is a last-in, first-out (LIFO) data structure used by the
 * virtual machine to store operands and intermediate runtime values while
 * executing bytecode instructions.
 * </p>
 *
 * <p>
 * Values are pushed onto and removed from the top of the stack. Instructions
 * that require operands consume them from the stack, while instructions that
 * produce results push those results back onto the stack.
 * </p>
 *
 * <p>
 * The stack does not perform type validation beyond rejecting {@code null}
 * values. Type compatibility and instruction-specific validation are the
 * responsibility of the instructions operating on the stack.
 * </p>
 *
 * <p>
 * An empty stack is considered an invalid state for operations that require
 * a value to be present. Attempting to remove or inspect a value from an
 * empty stack results in an {@link EmptyStackException}.
 * </p>
 */
public final class ValueStack {

  /**
   * Immutable representation of the state of a value stack.
   *
   * <p>
   * Values are stored in top-to-bottom order. Therefore, the first element
   * represents the value that would be returned by {@link #peek()}.
   * </p>
   */
  public record Snapshot(List<IValue> values) {
    public Snapshot {
      if (values == null) throw new NullArgumentException("values");
      values = List.copyOf(values);
    }
  }

  private final Deque<IValue> values = new ArrayDeque<>();

  /**
   * Pushes a value onto the top of the stack.
   *
   * @param value the value to push
   * @throws NullArgumentException if {@code value} is {@code null}
   */
  public void push(IValue value) {
    if (value == null) throw new NullArgumentException("value");
    values.push(value);
  }

  /**
   * Removes and returns the value at the top of the stack.
   *
   * @return the top value
   * @throws EmptyStackException if the stack is empty
   */
  public IValue pop() {
    if (values.isEmpty()) throw new EmptyStackException();
    return values.pop();
  }

  /**
   * Returns the value at the top of the stack without removing it.
   *
   * @return the top value
   * @throws EmptyStackException if the stack is empty
   */
  public IValue peek() {
    if (values.isEmpty()) throw new EmptyStackException();
    return values.peek();
  }

  /**
   * Returns the number of values currently stored in the stack.
   *
   * @return the stack size
   */
  public int size() {
    return values.size();
  }

  /**
   * Returns whether the stack contains no values.
   *
   * @return {@code true} if the stack is empty; {@code false} otherwise
   */
  public boolean isEmpty() {
    return values.isEmpty();
  }

  /**
   * Captures the current state of the stack.
   *
   * @return an immutable snapshot of the current stack state
   */
  public Snapshot snapshot() {
    return new Snapshot(List.copyOf(values));
  }

  /**
   * Restores the stack to a previously captured state.
   *
   * @param snapshot the snapshot to restore
   * @throws NullArgumentException if {@code snapshot} is {@code null}
   */
  public void restore(Snapshot snapshot) {
    if (snapshot == null) throw new NullArgumentException("snapshot");
    values.clear();
    values.addAll(snapshot.values);
  }
}