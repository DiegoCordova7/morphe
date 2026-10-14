package dev.morphe.vm.memory;

import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.internal.memory.InvalidMemoryAddressException;
import dev.morphe.vm.exception.internal.memory.MemoryAddressAlreadyFreedException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Allocates and recycles memory addresses for the Morphe virtual machine.
 *
 * <p>
 * This allocator is responsible only for managing memory addresses.
 * It generates unique addresses for newly allocated values and recycles
 * addresses that have been explicitly released.
 * </p>
 *
 * <p>
 * Addresses are allocated sequentially until previously released addresses
 * become available. Recycled addresses are reused before allocating new
 * ones, reducing memory growth during execution.
 * </p>
 *
 * <p>
 * This class does not store runtime values and does not reclaim memory
 * automatically. Determining when an address is no longer reachable is the
 * responsibility of higher-level components such as a garbage collector.
 * </p>
 */
public final class MemoryAllocator {

  /**
   * Represents an immutable snapshot of the allocator state.
   *
   * <p>
   * The snapshot preserves the set of released addresses, the order in which
   * released addresses will be reused, and the next sequential address to be
   * allocated.
   * </p>
   *
   * @param freeAddresses the released addresses in allocation order
   * @param freeSet the set of currently released addresses
   * @param nextAddress the next sequential address
   */
  public record Snapshot(List<Integer> freeAddresses, Set<Integer> freeSet, int nextAddress) {

    public Snapshot {
      if (freeAddresses == null) throw new NullArgumentException("freeAddresses");
      if (freeSet == null) throw new NullArgumentException("freeSet");
      freeAddresses = List.copyOf(freeAddresses);
      freeSet = Set.copyOf(freeSet);
    }
  }

  private final Deque<Integer> freeAddresses = new ArrayDeque<>();
  private final Set<Integer> freeSet = new HashSet<>();
  private int nextAddress;

  /**
   * Allocates a memory address.
   *
   * <p>
   * If previously released addresses are available, one of them is reused.
   * Otherwise, a new address is generated.
   * </p>
   *
   * @return the allocated memory address
   */
  public int allocate() {
    if (!freeAddresses.isEmpty()) {
      int address = freeAddresses.pop();
      freeSet.remove(address);
      return address;
    }
    return nextAddress++;
  }

  /**
   * Releases a memory address so it can be allocated again.
   *
   * <p>
   * An address can only be released once before being allocated again.
   * Attempting to release an already freed address results in a
   * {@link MemoryAddressAlreadyFreedException}.
   * </p>
   *
   * @param address the memory address to release
   *
   * @throws InvalidMemoryAddressException if the address has never been
   *     allocated
   * @throws MemoryAddressAlreadyFreedException if the address has already
   *     been released
   */
  public void free(int address) {
    if (address < 0 || address >= nextAddress) throw new InvalidMemoryAddressException(address);
    if (!freeSet.add(address)) throw new MemoryAddressAlreadyFreedException(address);
    freeAddresses.push(address);
  }

  /**
   * Captures the current state of the allocator.
   *
   * @return an immutable snapshot of the allocator state
   */
  public Snapshot snapshot() {
    return new Snapshot(new ArrayList<>(freeAddresses), new HashSet<>(freeSet), nextAddress);
  }

  /**
   * Restores the allocator to a previously captured state.
   *
   * <p>
   * The ordering of recycled addresses is preserved so that subsequent
   * allocations behave exactly as they would have from the original state.
   * </p>
   *
   * @param snapshot the snapshot to restore
   *
   * @throws NullArgumentException if {@code snapshot} is {@code null}
   */
  public void restore(Snapshot snapshot) {
    if (snapshot == null) throw new NullArgumentException("snapshot");
    freeAddresses.clear();
    freeSet.clear();
    for (int address : snapshot.freeAddresses()) freeAddresses.addLast(address);
    freeSet.addAll(snapshot.freeSet());
    nextAddress = snapshot.nextAddress();
  }
}