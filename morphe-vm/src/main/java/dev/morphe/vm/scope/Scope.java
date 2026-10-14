package dev.morphe.vm.scope;

import dev.morphe.runtime.environment.VariableCell;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.internal.scope.InvalidVariableSlotException;
import dev.morphe.vm.exception.internal.scope.UndefinedVariableException;
import dev.morphe.vm.memory.Memory;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a variable scope in the Morphe virtual machine.
 *
 * <p>
 * A scope associates variable slots with memory addresses. Scopes may be
 * nested, forming a parent-child hierarchy that allows variables declared
 * in outer scopes to be accessed from nested scopes.
 * </p>
 *
 * <p>
 * Variable lookup starts in the current scope and continues recursively
 * through its parent scopes until the slot is found.
 * </p>
 *
 * <p>
 * Variable assignment follows the same lookup rules. If the slot already
 * exists in the current scope or one of its ancestors, its associated
 * memory address is updated in the scope where it was originally declared.
 * Otherwise, a new binding is created in the current scope.
 * </p>
 */
public final class Scope {

  private final Scope parent;
  private final Map<Integer, Integer> variables = new HashMap<>();

  /**
   * Represents an immutable snapshot of a scope and its parent scope chain.
   *
   * <p>
   * The snapshot stores the variable bindings defined directly by the scope
   * and recursively captures the state of its parent scope. This allows the
   * complete lexical scope chain to be restored to its previous state.
   * </p>
   *
   * @param variables the variable bindings defined directly by this scope
   * @param parent the snapshot of the parent scope, or {@code null} if this
   *     scope is the root scope
   */
  public record Snapshot(Map<Integer, Integer> variables, Snapshot parent) {

    public Snapshot {
      if (variables == null) throw new NullArgumentException("variables");
      variables = Map.copyOf(variables);
    }
  }

  /**
   * Creates a new scope with the specified parent.
   *
   * @param parent the parent scope, or {@code null} for the root scope
   */
  public Scope(Scope parent) {
    this.parent = parent;
  }

  /**
   * Returns the parent scope.
   *
   * @return the parent scope, or {@code null} if this is the root scope
   */
  public Scope parent() {
    return parent;
  }

  /**
   * Declares a variable slot in the current scope.
   *
   * @param slot the variable slot
   * @param memoryAddress the associated memory address
   *
   * @throws InvalidVariableSlotException if {@code slot} is negative
   */
  public void declare(int slot, int memoryAddress) {
    if (slot < 0) throw new InvalidVariableSlotException(slot);
    variables.put(slot, memoryAddress);
  }

  /**
   * Stores a memory address into the specified variable slot.
   *
   * <p>
   * If the slot exists in the current scope or an enclosing scope,
   * the existing binding is updated. Otherwise, a new binding is
   * created in the current scope.
   * </p>
   *
   * @param slot the variable slot
   * @param memoryAddress the associated memory address
   *
   * @throws InvalidVariableSlotException if {@code slot} is negative
   */
  public void store(int slot, int memoryAddress) {
    if (slot < 0) throw new InvalidVariableSlotException(slot);
    Scope scope = resolve(slot);
    if (scope != null) {
      scope.variables.put(slot, memoryAddress);
      return;
    }
    variables.put(slot, memoryAddress);
  }

  /**
   * Resolves a variable slot to its associated memory address.
   *
   * @param slot the variable slot
   * @return the associated memory address
   *
   * @throws InvalidVariableSlotException if {@code slot} is negative
   * @throws UndefinedVariableException if the slot is not defined
   */
  public int load(int slot) {
    if (slot < 0) throw new InvalidVariableSlotException(slot);
    Scope scope = resolve(slot);
    if (scope == null) throw new UndefinedVariableException(slot);
    return scope.variables.get(slot);
  }

  /**
   * Finds the scope that directly defines the specified slot.
   *
   * @param slot the variable slot
   * @return the scope defining the slot, or {@code null} if not found
   */
  private Scope resolve(int slot) {
    if (variables.containsKey(slot)) return this;
    if (parent == null) return null;
    return parent.resolve(slot);
  }

  public VariableCell resolveCell(Memory memory, int slot) {
    if (memory == null) throw new NullArgumentException("memory");
    if (slot < 0) throw new InvalidVariableSlotException(slot);
    Scope scope = resolve(slot);
    if (scope == null) throw new UndefinedVariableException(slot);
    return memory.cell(scope.variables.get(slot));
  }

  /**
   * Captures the current state of this scope and its parent scope chain.
   *
   * <p>
   * The returned snapshot contains an immutable copy of the bindings defined
   * by this scope and recursively captures all enclosing scopes.
   * </p>
   *
   * @return an immutable snapshot of this scope chain
   */
  public Snapshot snapshot() {
    return new Snapshot(Map.copyOf(variables), parent == null ? null : parent.snapshot());
  }

  /**
   * Restores this scope and its parent scope chain from a snapshot.
   *
   * <p>
   * Existing scope instances are preserved. Only their variable bindings are
   * replaced with the bindings stored in the snapshot.
   * </p>
   *
   * @param snapshot the snapshot from which to restore the scope chain
   *
   * @throws NullArgumentException if {@code snapshot} is {@code null}
   * @throws IllegalArgumentException if the snapshot does not match the
   *     structure of this scope chain
   */
  public void restore(Snapshot snapshot) {
    if (snapshot == null) throw new NullArgumentException("snapshot");
    variables.clear();
    variables.putAll(snapshot.variables());
    if (parent == null) {
      if (snapshot.parent() != null) throw new IllegalArgumentException("snapshot contains a parent for a root scope");
      return;
    }
    if (snapshot.parent() == null) throw new IllegalArgumentException("snapshot is missing a parent scope");
    parent.restore(snapshot.parent());
  }
}