package dev.morphe.vm.memory;

import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.internal.memory.InvalidHeapAddressException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeapTest {

  @Test
  void shouldInitiallyBeEmpty() {
    Heap heap = new Heap();
    assertTrue(heap.snapshot().values().isEmpty());
  }

  @Test
  void shouldAllocateValueAtSequentialAddress() {
    Heap heap = new Heap();
    IValue first = new IntegerValue(10);
    IValue second = new IntegerValue(20);
    int firstAddress = heap.allocate(first);
    int secondAddress = heap.allocate(second);
    assertEquals(0, firstAddress);
    assertEquals(1, secondAddress);
  }

  @Test
  void shouldLoadAllocatedValue() {
    Heap heap = new Heap();
    IValue value = new IntegerValue(42);
    int address = heap.allocate(value);
    assertSame(value, heap.load(address));
  }

  @Test
  void shouldRejectNullValueOnAllocate() {
    Heap heap = new Heap();
    assertThrows(NullArgumentException.class, () -> heap.allocate(null));
  }

  @Test
  void shouldRejectLoadFromInvalidAddress() {
    Heap heap = new Heap();
    assertThrows(InvalidHeapAddressException.class, () -> heap.load(0));
  }

  @Test
  void shouldRejectLoadFromNegativeAddress() {
    Heap heap = new Heap();
    assertThrows(InvalidHeapAddressException.class, () -> heap.load(-1));
  }

  @Test
  void shouldStoreValueAtExistingAddress() {
    Heap heap = new Heap();
    IValue original = new IntegerValue(10);
    IValue replacement = new IntegerValue(20);
    int address = heap.allocate(original);
    heap.store(address, replacement);
    assertSame(replacement, heap.load(address));
  }

  @Test
  void shouldNotAllocateNewAddressWhenStoring() {
    Heap heap = new Heap();
    int address = heap.allocate(new IntegerValue(10));
    heap.store(address, new IntegerValue(20));
    int nextAddress = heap.allocate(new IntegerValue(30));
    assertEquals(1, nextAddress);
  }

  @Test
  void shouldRejectNullValueOnStore() {
    Heap heap = new Heap();
    int address = heap.allocate(new IntegerValue(42));
    assertThrows(NullArgumentException.class, () -> heap.store(address, null));
  }

  @Test
  void shouldRejectStoreAtInvalidAddress() {
    Heap heap = new Heap();
    assertThrows(InvalidHeapAddressException.class, () -> heap.store(0, new IntegerValue(42)));
  }

  @Test
  void shouldRejectStoreAtNegativeAddress() {
    Heap heap = new Heap();
    assertThrows(InvalidHeapAddressException.class, () -> heap.store(-1, new IntegerValue(42)));
  }

  @Test
  void shouldCreateSnapshotWithCurrentValues() {
    Heap heap = new Heap();
    IValue first = new IntegerValue(10);
    IValue second = new BooleanValue(true);
    int firstAddress = heap.allocate(first);
    int secondAddress = heap.allocate(second);
    Heap.Snapshot snapshot = heap.snapshot();
    assertEquals(2, snapshot.values().size());
    assertSame(first, snapshot.values().get(firstAddress));
    assertSame(second, snapshot.values().get(secondAddress));
    assertEquals(2, snapshot.nextAddress());
  }

  @Test
  void shouldRejectNullSnapshotValues() {
    assertThrows(NullArgumentException.class, () -> new Heap.Snapshot(null, 0));
  }

  @Test
  void shouldRejectNullSnapshotOnRestore() {
    Heap heap = new Heap();
    assertThrows(NullArgumentException.class, () -> heap.restore(null));
  }

  @Test
  void shouldRestorePreviousValues() {
    Heap heap = new Heap();
    IValue original = new IntegerValue(10);
    IValue modified = new IntegerValue(20);
    int address = heap.allocate(original);
    Heap.Snapshot snapshot = heap.snapshot();
    heap.store(address, modified);
    heap.restore(snapshot);
    assertSame(original, heap.load(address));
  }

  @Test
  void shouldRemoveValuesCreatedAfterSnapshot() {
    Heap heap = new Heap();
    int firstAddress = heap.allocate(new IntegerValue(10));
    Heap.Snapshot snapshot = heap.snapshot();
    int secondAddress = heap.allocate(new IntegerValue(20));
    heap.restore(snapshot);
    assertSame(10, ((IntegerValue) heap.load(firstAddress)).value());
    assertThrows(InvalidHeapAddressException.class, () -> heap.load(secondAddress));
  }

  @Test
  void shouldRestoreNextAllocationAddress() {
    Heap heap = new Heap();
    heap.allocate(new IntegerValue(10));
    Heap.Snapshot snapshot = heap.snapshot();
    heap.allocate(new IntegerValue(20));
    heap.allocate(new IntegerValue(30));
    heap.restore(snapshot);
    int address = heap.allocate(new IntegerValue(40));
    assertEquals(1, address);
  }

  @Test
  void shouldRestoreMultipleValues() {
    Heap heap = new Heap();
    IValue first = new IntegerValue(1);
    IValue second = new IntegerValue(2);
    int firstAddress = heap.allocate(first);
    int secondAddress = heap.allocate(second);
    Heap.Snapshot snapshot = heap.snapshot();
    heap.store(firstAddress, new IntegerValue(100));
    heap.store(secondAddress, new IntegerValue(200));
    heap.restore(snapshot);
    assertSame(first, heap.load(firstAddress));
    assertSame(second, heap.load(secondAddress));
  }

  @Test
  void shouldRestoreEmptySnapshot() {
    Heap heap = new Heap();
    Heap.Snapshot snapshot = heap.snapshot();
    heap.allocate(new IntegerValue(42));
    heap.restore(snapshot);
    assertThrows(InvalidHeapAddressException.class, () -> heap.load(0));
    int address = heap.allocate(new IntegerValue(10));
    assertEquals(0, address);
  }
}