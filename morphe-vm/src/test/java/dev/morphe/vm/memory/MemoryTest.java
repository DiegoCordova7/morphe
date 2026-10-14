package dev.morphe.vm.memory;

import dev.morphe.runtime.environment.VariableCell;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.internal.memory.UninitializedMemoryException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoryTest {

  @Test
  void shouldInitiallyBeEmpty() {
    Memory memory = new Memory();
    assertTrue(memory.isEmpty());
    assertEquals(0, memory.size());
  }

  @Test
  void shouldStoreValue() {
    Memory memory = new Memory();
    IValue value = new IntegerValue(42);
    memory.store(10, value);
    assertTrue(memory.contains(10));
    assertEquals(1, memory.size());
    assertSame(value, memory.load(10));
  }

  @Test
  void shouldRejectNullValue() {
    Memory memory = new Memory();
    assertThrows(NullArgumentException.class, () -> memory.store(10, null));
  }

  @Test
  void shouldUpdateExistingValue() {
    Memory memory = new Memory();
    IValue first = new IntegerValue(10);
    IValue second = new IntegerValue(20);
    memory.store(10, first);
    memory.store(10, second);
    assertEquals(1, memory.size());
    assertSame(second, memory.load(10));
  }

  @Test
  void shouldLoadStoredValue() {
    Memory memory = new Memory();
    IValue value = new BooleanValue(true);
    memory.store(5, value);
    assertSame(value, memory.load(5));
  }

  @Test
  void shouldRejectLoadFromUninitializedAddress() {
    Memory memory = new Memory();
    assertThrows(UninitializedMemoryException.class, () -> memory.load(10));
  }

  @Test
  void shouldReturnCellForInitializedAddress() {
    Memory memory = new Memory();
    IValue value = new IntegerValue(42);
    memory.store(10, value);
    VariableCell cell = memory.cell(10);
    assertNotNull(cell);
    assertSame(value, cell.get());
  }

  @Test
  void shouldRejectCellFromUninitializedAddress() {
    Memory memory = new Memory();
    assertThrows(UninitializedMemoryException.class, () -> memory.cell(10));
  }

  @Test
  void shouldReturnWhetherAddressIsInitialized() {
    Memory memory = new Memory();
    assertFalse(memory.contains(10));
    memory.store(10, new IntegerValue(42));
    assertTrue(memory.contains(10));
  }

  @Test
  void shouldReturnNumberOfInitializedAddresses() {
    Memory memory = new Memory();
    memory.store(10, new IntegerValue(1));
    memory.store(20, new IntegerValue(2));
    assertEquals(2, memory.size());
    memory.store(10, new IntegerValue(3));
    assertEquals(2, memory.size());
  }

  @Test
  void shouldClearInitializedAddress() {
    Memory memory = new Memory();
    memory.store(10, new IntegerValue(42));
    memory.clear(10);
    assertFalse(memory.contains(10));
    assertEquals(0, memory.size());
    assertThrows(UninitializedMemoryException.class, () -> memory.load(10));
  }

  @Test
  void shouldIgnoreClearForUninitializedAddress() {
    Memory memory = new Memory();
    assertDoesNotThrow(() -> memory.clear(10));
    assertTrue(memory.isEmpty());
  }

  @Test
  void shouldCreateSnapshotWithCurrentValues() {
    Memory memory = new Memory();
    IValue first = new IntegerValue(10);
    IValue second = new BooleanValue(true);
    memory.store(10, first);
    memory.store(20, second);
    Memory.Snapshot snapshot = memory.snapshot();
    assertEquals(2, snapshot.values().size());
    assertSame(first, snapshot.values().get(10));
    assertSame(second, snapshot.values().get(20));
  }

  @Test
  void shouldRejectNullSnapshotValues() {
    assertThrows(NullArgumentException.class, () -> new Memory.Snapshot(null));
  }

  @Test
  void shouldRejectNullSnapshotOnRestore() {
    Memory memory = new Memory();
    assertThrows(NullArgumentException.class, () -> memory.restore(null));
  }

  @Test
  void shouldRestorePreviousValues() {
    Memory memory = new Memory();
    IValue original = new IntegerValue(10);
    IValue modified = new IntegerValue(20);
    memory.store(10, original);
    Memory.Snapshot snapshot = memory.snapshot();
    memory.store(10, modified);
    memory.restore(snapshot);
    assertSame(original, memory.load(10));
  }

  @Test
  void shouldRemoveValuesCreatedAfterSnapshot() {
    Memory memory = new Memory();
    memory.store(10, new IntegerValue(10));
    Memory.Snapshot snapshot = memory.snapshot();
    memory.store(20, new IntegerValue(20));
    memory.restore(snapshot);
    assertTrue(memory.contains(10));
    assertFalse(memory.contains(20));
    assertEquals(1, memory.size());
  }

  @Test
  void shouldRestoreValuesRemovedAfterSnapshot() {
    Memory memory = new Memory();
    IValue value = new IntegerValue(42);
    memory.store(10, value);
    Memory.Snapshot snapshot = memory.snapshot();
    memory.clear(10);
    memory.restore(snapshot);
    assertTrue(memory.contains(10));
    assertSame(value, memory.load(10));
  }

  @Test
  void shouldPreserveExistingVariableCellDuringRestore() {
    Memory memory = new Memory();
    IValue original = new IntegerValue(10);
    IValue modified = new IntegerValue(20);
    memory.store(10, original);
    VariableCell cell = memory.cell(10);
    Memory.Snapshot snapshot = memory.snapshot();
    memory.store(10, modified);
    memory.restore(snapshot);
    assertSame(cell, memory.cell(10));
    assertSame(original, cell.get());
  }

  @Test
  void shouldRestoreMultipleAddresses() {
    Memory memory = new Memory();
    IValue first = new IntegerValue(1);
    IValue second = new IntegerValue(2);
    memory.store(10, first);
    memory.store(20, second);
    Memory.Snapshot snapshot = memory.snapshot();
    memory.store(10, new IntegerValue(100));
    memory.clear(20);
    memory.store(30, new IntegerValue(300));
    memory.restore(snapshot);
    assertEquals(2, memory.size());
    assertSame(first, memory.load(10));
    assertSame(second, memory.load(20));
    assertFalse(memory.contains(30));
  }
}