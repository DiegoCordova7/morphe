package dev.morphe.vm.memory;

import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.reference.ReferenceValue;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.internal.memory.InvalidHeapAddressException;
import dev.morphe.vm.stack.ValueStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the heap storage of the Morphe virtual machine.
 *
 * <p>
 * The heap stores dynamically allocated runtime values that require
 * address-based storage during program execution. Values allocated on the
 * heap are identified by integer addresses managed exclusively by the heap.
 * </p>
 *
 * <p>
 * Unlike the operand {@link ValueStack}, which stores temporary operands
 * and intermediate execution values, and {@link Memory}, which provides
 * direct address-based VM storage, the heap is intended for dynamically
 * allocated runtime objects such as arrays and composite values.
 * </p>
 *
 * <p>
 * Heap addresses are generated sequentially when values are allocated.
 * Once allocated, an address remains valid for the lifetime of the heap.
 * The current heap implementation does not provide deallocation operations.
 * </p>
 *
 * <p>
 * The heap does not expose the allocation mechanism through arbitrary
 * addresses. A value must first be allocated using {@link #allocate(IValue)}
 * before it can be loaded or replaced using {@link #load(int)} or
 * {@link #store(int, IValue)}.
 * </p>
 *
 * <p>
 * References to heap objects are represented by runtime
 * {@link ReferenceValue} instances. A reference stores the heap address,
 * while the heap is responsible for resolving that address to the
 * corresponding runtime value.
 * </p>
 */
public final class Heap {

  /**
   * Represents an immutable snapshot of the heap state.
   *
   * <p>
   * The snapshot contains both the values currently allocated on the heap
   * and the next address that will be assigned by a subsequent allocation.
   * Together, these values are sufficient to restore the heap to the exact
   * state in which the snapshot was created.
   * </p>
   *
   * <p>
   * The snapshot does not create copies of the stored runtime values.
   * Instead, it creates an immutable copy of the address-to-value mapping
   * while preserving references to the original runtime values.
   * </p>
   *
   * @param values the values allocated on the heap, indexed by address
   * @param nextAddress the next address available for allocation
   */
  public record Snapshot(Map<Integer, IValue> values, int nextAddress) {

    public Snapshot {
      if (values == null) throw new NullArgumentException("values");
      values = Map.copyOf(values);
    }
  }

  /**
   * Stores runtime values indexed by their heap address.
   */
  private final Map<Integer, IValue> values;

  /**
   * The next address available for allocation.
   */
  private int nextAddress;

  /**
   * Creates an empty heap.
   *
   * <p>
   * The first allocated value receives address {@code 0}.
   * </p>
   */
  public Heap() {
    this.values = new HashMap<>();
    this.nextAddress = 0;
  }

  /**
   * Allocates a runtime value on the heap.
   *
   * <p>
   * A new unique address is generated and associated with the
   * provided value. The generated address becomes valid for subsequent
   * {@link #load(int)} and {@link #store(int, IValue)} operations.
   * </p>
   *
   * @param value the runtime value to allocate
   * @return the heap address assigned to the allocated value
   * @throws NullArgumentException if {@code value} is {@code null}
   */
  public int allocate(IValue value) {
    if (value == null) throw new NullArgumentException("value");
    int address = nextAddress++;
    values.put(address, value);
    return address;
  }

  /**
   * Loads a runtime value from the specified heap address.
   *
   * @param address the heap address from which to load the value
   * @return the runtime value stored at the address
   * @throws InvalidHeapAddressException if the address is not allocated
   */
  public IValue load(int address) {
    validateAddress(address);
    return values.get(address);
  }

  /**
   * Stores a runtime value at an existing heap address.
   *
   * <p>
   * If a value is already associated with the address, it is replaced
   * by the provided value. This method does not allocate a new address.
   * </p>
   *
   * @param address the heap address at which to store the value
   * @param value the runtime value to store
   * @throws NullArgumentException if {@code value} is {@code null}
   * @throws InvalidHeapAddressException if the address is not allocated
   */
  public void store(int address, IValue value) {
    if (value == null) throw new NullArgumentException("value");
    validateAddress(address);
    values.put(address, value);
  }

  /**
   * Validates that a heap address currently refers to an allocated value.
   *
   * @param address the heap address to validate
   * @throws InvalidHeapAddressException if the address is not allocated
   */
  private void validateAddress(int address) {
    if (!values.containsKey(address)) throw new InvalidHeapAddressException(address);
  }

  /**
   * Captures the current state of the heap.
   *
   * <p>
   * The returned snapshot contains an immutable copy of the heap's address
   * mapping and the current allocation position. Subsequent modifications
   * to the heap do not affect the snapshot.
   * </p>
   *
   * @return an immutable snapshot of the current heap state
   */
  public Snapshot snapshot() {
    return new Snapshot(values, nextAddress);
  }

  /**
   * Restores the heap to the state represented by the specified snapshot.
   *
   * <p>
   * All values currently allocated on the heap are discarded and replaced
   * with the values contained in the snapshot. The next allocation address
   * is also restored, allowing subsequent allocations to continue from
   * the same state as when the snapshot was captured.
   * </p>
   *
   * @param snapshot the snapshot from which to restore the heap
   * @throws NullArgumentException if {@code snapshot} is {@code null}
   */
  public void restore(Snapshot snapshot) {
    if (snapshot == null) throw new NullArgumentException("snapshot");
    values.clear();
    values.putAll(snapshot.values());
    nextAddress = snapshot.nextAddress();
  }
}