package dev.morphe.vm.memory;

import dev.morphe.runtime.environment.VariableCell;
import dev.morphe.runtime.value.IValue;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.internal.memory.UninitializedMemoryException;
import dev.morphe.vm.memory.MemoryAllocator;
import dev.morphe.vm.stack.ValueStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the addressable memory of the Morphe virtual machine.
 *
 * <p>
 * Memory provides address-based storage for runtime values during bytecode
 * execution. Values are associated with integer memory addresses and can be
 * explicitly stored and loaded by VM operations.
 * </p>
 *
 * <p>
 * Unlike the operand {@link ValueStack}, which is used for temporary operands
 * and intermediate execution values, memory provides address-based storage
 * whose contents remain available independently of the current stack state.
 * </p>
 *
 * <p>
 * Unlike the {@link Heap}, memory does not manage dynamically allocated
 * runtime objects or references. It is a direct address-to-value storage
 * mechanism owned by the virtual machine.
 * </p>
 *
 * <p>
 * {@link Memory} does not manage the allocation state of addresses. It accepts
 * an address and associates it with a runtime value when store is called.
 * Storing a value at an uninitialized address creates a new variable cell,
 * while storing another value at an initialized address updates the existing
 * cell. Address allocation and release are managed separately by
 * {@link MemoryAllocator}.
 * </p>
 *
 * <p>
 * Loading a value from an address that has not been initialized results in
 * an {@link UninitializedMemoryException}.
 * </p>
 *
 * <p>
 * {@code null} values are not permitted in memory.
 * </p>
 */
public final class Memory {

  /**
   * Represents an immutable snapshot of the memory state.
   *
   * <p>
   * The snapshot stores the value associated with each initialized address,
   * but not the mutable {@link VariableCell} instances themselves. During
   * restoration, existing cells are reused whenever possible so that
   * references held by closures remain valid.
   * </p>
   *
   * @param values the values stored at initialized addresses
   */
  public record Snapshot(Map<Integer, IValue> values) {

    public Snapshot {
      if (values == null) throw new NullArgumentException("values");
      values = Map.copyOf(values);
    }
  }
  private final Map<Integer, VariableCell> values = new HashMap<>();

  /**
   * Stores a value at the given memory address.
   *
   * <p>
   * If the address is not initialized, a new {@link VariableCell} is created.
   * If the address is already initialized, the existing cell is updated.
   * This preserves references held by closures to the same variable cell.
   * </p>
   *
   * @param address the memory address at which to store the value
   * @param value the value to store
   *
   * @throws NullArgumentException if {@code value} is {@code null}
   */
  public void store(int address, IValue value) {
    if (value == null) throw new NullArgumentException("value");
    VariableCell cell = values.get(address);
    if (cell == null) {
      values.put(address, new VariableCell(value));
      return;
    }
    cell.set(value);
  }

  /**
   * Loads the value stored at the given memory address.
   *
   * @param address the memory address from which to load the value
   *
   * @return the value stored at the address
   *
   * @throws UninitializedMemoryException if no value is stored at the address
   */
  public IValue load(int address) {
    VariableCell cell = values.get(address);
    if (cell == null) throw new UninitializedMemoryException(address);
    return cell.get();
  }

  /**
   * Returns the variable cell associated with the given memory address.
   *
   * <p>
   * This method is used by closure creation to retain a reference to the
   * same mutable cell used by normal memory operations.
   * </p>
   *
   * @param address the memory address whose cell should be returned
   *
   * @return the variable cell associated with the address
   *
   * @throws UninitializedMemoryException if no value is stored at the address
   */
  public VariableCell cell(int address) {
    VariableCell cell = values.get(address);
    if (cell == null) throw new UninitializedMemoryException(address);
    return cell;
  }

  /**
   * Returns whether the given memory address contains a value.
   *
   * @param address the memory address
   *
   * @return {@code true} if the address is initialized;
   *     {@code false} otherwise
   */
  public boolean contains(int address) {
    return values.containsKey(address);
  }

  /**
   * Returns the number of memory addresses that currently contain values.
   *
   * <p>
   * This represents the number of initialized storage locations and does not
   * represent the number of addresses allocated by {@link MemoryAllocator}.
   * </p>
   *
   * @return the number of initialized addresses
   */
  public int size() {
    return values.size();
  }

  /**
   * Returns whether no memory addresses are initialized.
   *
   * @return {@code true} if memory contains no initialized addresses;
   *     {@code false} otherwise
   */
  public boolean isEmpty() {
    return values.isEmpty();
  }

  /**
   * Removes the value stored at the given memory address.
   *
   * <p>
   * If the address is initialized, its associated variable cell is removed
   * from memory. The address itself is not released or made available for
   * reuse by this operation. Address lifecycle is managed separately by
   * {@link MemoryAllocator}.
   *
   * @param address the memory address whose stored value should be removed
   */
  public void clear(int address) {
    values.remove(address);
  }

  /**
   * Captures the current state of memory.
   *
   * <p>
   * Only the values are copied into the snapshot. The mutable variable cells
   * remain owned by the live memory instance.
   * </p>
   *
   * @return an immutable snapshot of the current memory state
   */
  public Snapshot snapshot() {
    Map<Integer, IValue> snapshot = new HashMap<>();
    for (Map.Entry<Integer, VariableCell> entry : values.entrySet()) {
      snapshot.put(entry.getKey(), entry.getValue().get());
    }
    return new Snapshot(snapshot);
  }

  /**
   * Restores memory to a previously captured state.
   *
   * <p>
   * Existing {@link VariableCell} instances are reused whenever their
   * addresses existed in the snapshot. This preserves references held by
   * closure environments.
   * </p>
   *
   * <p>
   * Cells created after the snapshot are removed. Addresses that existed
   * in the snapshot but are not currently initialized receive new cells.
   * </p>
   *
   * @param snapshot the snapshot to restore
   *
   * @throws NullArgumentException if {@code snapshot} is {@code null}
   */
  public void restore(Snapshot snapshot) {
    if (snapshot == null) throw new NullArgumentException("snapshot");

    /*
     * Restore existing cells first so that any closures referencing them
     * observe the rollback through the same VariableCell instance.
     */
    for (Map.Entry<Integer, IValue> entry : snapshot.values().entrySet()) {
      VariableCell cell = values.get(entry.getKey());
      if (cell == null) values.put(entry.getKey(), new VariableCell(entry.getValue()));
      else cell.set(entry.getValue());
    }

    /*
     * Remove cells that were created after the snapshot.
     */
    values.keySet().removeIf(address -> !snapshot.values().containsKey(address));
  }
}