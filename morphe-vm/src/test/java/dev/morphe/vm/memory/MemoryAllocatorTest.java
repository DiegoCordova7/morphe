package dev.morphe.vm.memory;

import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.internal.memory.InvalidMemoryAddressException;
import dev.morphe.vm.exception.internal.memory.MemoryAddressAlreadyFreedException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MemoryAllocatorTest {

  @Test
  void shouldAllocateAddressesSequentially() {
    MemoryAllocator allocator = new MemoryAllocator();
    assertEquals(0, allocator.allocate());
    assertEquals(1, allocator.allocate());
    assertEquals(2, allocator.allocate());
  }

  @Test
  void shouldReuseFreedAddress() {
    MemoryAllocator allocator = new MemoryAllocator();
    int first = allocator.allocate();
    allocator.allocate();
    allocator.free(first);
    assertEquals(first, allocator.allocate());
  }

  @Test
  void shouldReuseFreedAddressesInLifoOrder() {
    MemoryAllocator allocator = new MemoryAllocator();
    int first = allocator.allocate();
    int second = allocator.allocate();
    int third = allocator.allocate();
    allocator.free(first);
    allocator.free(second);
    allocator.free(third);
    assertEquals(third, allocator.allocate());
    assertEquals(second, allocator.allocate());
    assertEquals(first, allocator.allocate());
  }

  @Test
  void shouldContinueSequentialAllocationAfterReusedAddresses() {
    MemoryAllocator allocator = new MemoryAllocator();
    allocator.allocate();
    allocator.allocate();
    allocator.free(0);
    assertEquals(0, allocator.allocate());
    assertEquals(2, allocator.allocate());
  }

  @Test
  void shouldRejectNegativeAddress() {
    MemoryAllocator allocator = new MemoryAllocator();
    assertThrows(InvalidMemoryAddressException.class, () -> allocator.free(-1));
  }

  @Test
  void shouldRejectNeverAllocatedAddress() {
    MemoryAllocator allocator = new MemoryAllocator();
    assertThrows(InvalidMemoryAddressException.class, () -> allocator.free(0));
  }

  @Test
  void shouldRejectAddressBeyondAllocatedRange() {
    MemoryAllocator allocator = new MemoryAllocator();
    allocator.allocate();
    assertThrows(InvalidMemoryAddressException.class, () -> allocator.free(1));
  }

  @Test
  void shouldRejectDoubleFree() {
    MemoryAllocator allocator = new MemoryAllocator();
    allocator.allocate();
    allocator.free(0);
    assertThrows(MemoryAddressAlreadyFreedException.class, () -> allocator.free(0));
  }

  @Test
  void shouldAllowFreeingAddressAgainAfterItIsReallocated() {
    MemoryAllocator allocator = new MemoryAllocator();
    allocator.allocate();
    allocator.free(0);
    allocator.allocate();
    assertDoesNotThrow(() -> allocator.free(0));
  }

  @Test
  void shouldCreateSnapshotWithCurrentState() {
    MemoryAllocator allocator = new MemoryAllocator();
    allocator.allocate();
    allocator.allocate();
    allocator.free(0);
    MemoryAllocator.Snapshot snapshot = allocator.snapshot();
    assertEquals(1, snapshot.freeAddresses().size());
    assertTrue(snapshot.freeAddresses().contains(0));
    assertEquals(1, snapshot.freeSet().size());
    assertTrue(snapshot.freeSet().contains(0));
    assertEquals(2, snapshot.nextAddress());
  }

  @Test
  void shouldRejectNullSnapshotComponents() {
    assertThrows(NullArgumentException.class, () -> new MemoryAllocator.Snapshot(null, Set.of(), 0));
    assertThrows(NullArgumentException.class, () -> new MemoryAllocator.Snapshot(List.of(), null, 0));
  }

  @Test
  void shouldRejectNullSnapshotOnRestore() {
    MemoryAllocator allocator = new MemoryAllocator();
    assertThrows(NullArgumentException.class, () -> allocator.restore(null));
  }

  @Test
  void shouldRestorePreviousState() {
    MemoryAllocator allocator = new MemoryAllocator();
    allocator.allocate();
    allocator.allocate();
    allocator.free(0);
    MemoryAllocator.Snapshot snapshot = allocator.snapshot();
    allocator.allocate();
    allocator.allocate();
    allocator.restore(snapshot);
    assertEquals(0, allocator.allocate());
    assertEquals(2, allocator.allocate());
  }

  @Test
  void shouldRestoreReuseOrder() {
    MemoryAllocator allocator = new MemoryAllocator();
    allocator.allocate();
    allocator.allocate();
    allocator.allocate();
    allocator.free(0);
    allocator.free(1);
    allocator.free(2);
    MemoryAllocator.Snapshot snapshot = allocator.snapshot();
    allocator.allocate();
    allocator.allocate();
    allocator.allocate();
    allocator.restore(snapshot);
    assertEquals(2, allocator.allocate());
    assertEquals(1, allocator.allocate());
    assertEquals(0, allocator.allocate());
  }

  @Test
  void shouldRestoreNextSequentialAddress() {
    MemoryAllocator allocator = new MemoryAllocator();
    allocator.allocate();
    allocator.allocate();
    MemoryAllocator.Snapshot snapshot = allocator.snapshot();
    allocator.allocate();
    allocator.allocate();
    allocator.restore(snapshot);
    assertEquals(2, allocator.allocate());
  }
}