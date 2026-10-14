package dev.morphe.vm.scope;

import dev.morphe.runtime.environment.VariableCell;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.internal.scope.InvalidVariableSlotException;
import dev.morphe.vm.exception.internal.scope.UndefinedVariableException;
import dev.morphe.vm.memory.Memory;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ScopeTest {

  @Test
  void shouldCreateRootScope() {
    Scope scope = new Scope(null);
    assertNull(scope.parent());
  }

  @Test
  void shouldCreateChildScope() {
    Scope parent = new Scope(null);
    Scope child = new Scope(parent);
    assertSame(parent, child.parent());
  }

  @Test
  void shouldDeclareVariable() {
    Scope scope = new Scope(null);
    scope.declare(0, 10);
    assertEquals(10, scope.load(0));
  }

  @Test
  void shouldRejectNegativeSlotOnDeclare() {
    Scope scope = new Scope(null);
    assertThrows(InvalidVariableSlotException.class, () -> scope.declare(-1, 10));
  }

  @Test
  void shouldStoreNewVariableInCurrentScope() {
    Scope scope = new Scope(null);
    scope.store(0, 10);
    assertEquals(10, scope.load(0));
  }

  @Test
  void shouldUpdateExistingVariableInCurrentScope() {
    Scope scope = new Scope(null);
    scope.declare(0, 10);
    scope.store(0, 20);
    assertEquals(20, scope.load(0));
  }

  @Test
  void shouldRejectNegativeSlotOnStore() {
    Scope scope = new Scope(null);
    assertThrows(InvalidVariableSlotException.class, () -> scope.store(-1, 10));
  }

  @Test
  void shouldRejectUndefinedVariable() {
    Scope scope = new Scope(null);
    assertThrows(UndefinedVariableException.class, () -> scope.load(0));
  }

  @Test
  void shouldRejectNegativeSlotOnLoad() {
    Scope scope = new Scope(null);
    assertThrows(InvalidVariableSlotException.class, () -> scope.load(-1));
  }

  @Test
  void shouldResolveVariableFromParentScope() {
    Scope parent = new Scope(null);
    Scope child = new Scope(parent);
    parent.declare(0, 10);
    assertEquals(10, child.load(0));
  }

  @Test
  void shouldResolveNearestVariableInScopeChain() {
    Scope parent = new Scope(null);
    Scope child = new Scope(parent);
    Scope grandchild = new Scope(child);
    parent.declare(0, 10);
    child.declare(0, 20);
    assertEquals(20, grandchild.load(0));
  }

  @Test
  void shouldStoreIntoScopeWhereVariableWasDeclared() {
    Scope parent = new Scope(null);
    Scope child = new Scope(parent);
    parent.declare(0, 10);
    child.store(0, 20);
    assertEquals(20, parent.load(0));
    assertEquals(20, child.load(0));
  }

  @Test
  void shouldCreateNewVariableInCurrentScopeWhenNotDefinedInParent() {
    Scope parent = new Scope(null);
    Scope child = new Scope(parent);
    child.store(0, 20);
    assertThrows(UndefinedVariableException.class, () -> parent.load(0));
    assertEquals(20, child.load(0));
  }

  @Test
  void shouldRejectNegativeSlotWhenResolvingCell() {
    Scope scope = new Scope(null);
    Memory memory = new Memory();
    assertThrows(InvalidVariableSlotException.class, () -> scope.resolveCell(memory, -1));
  }

  @Test
  void shouldRejectNullMemoryWhenResolvingCell() {
    Scope scope = new Scope(null);
    assertThrows(NullArgumentException.class, () -> scope.resolveCell(null, 0));
  }

  @Test
  void shouldRejectUndefinedVariableWhenResolvingCell() {
    Scope scope = new Scope(null);
    Memory memory = new Memory();
    assertThrows(UndefinedVariableException.class, () -> scope.resolveCell(memory, 0));
  }

  @Test
  void shouldResolveVariableCell() {
    Scope scope = new Scope(null);
    Memory memory = new Memory();
    memory.store(10, new IntegerValue(42));
    scope.declare(0, 10);
    VariableCell cell = scope.resolveCell(memory, 0);
    assertNotNull(cell);
    assertEquals(new IntegerValue(42), cell.get());
  }

  @Test
  void shouldResolveVariableCellFromParent() {
    Scope parent = new Scope(null);
    Scope child = new Scope(parent);
    Memory memory = new Memory();
    memory.store(10, new IntegerValue(42));
    parent.declare(0, 10);
    VariableCell cell = child.resolveCell(memory, 0);
    assertNotNull(cell);
    assertEquals(new IntegerValue(42), cell.get());
  }

  @Test
  void shouldCreateSnapshotWithCurrentBindings() {
    Scope scope = new Scope(null);
    scope.declare(0, 10);
    scope.declare(1, 20);
    Scope.Snapshot snapshot = scope.snapshot();
    assertEquals(2, snapshot.variables().size());
    assertEquals(10, snapshot.variables().get(0));
    assertEquals(20, snapshot.variables().get(1));
    assertNull(snapshot.parent());
  }

  @Test
  void shouldCreateSnapshotWithParentChain() {
    Scope parent = new Scope(null);
    Scope child = new Scope(parent);
    parent.declare(0, 10);
    child.declare(1, 20);
    Scope.Snapshot snapshot = child.snapshot();
    assertEquals(20, snapshot.variables().get(1));
    assertNotNull(snapshot.parent());
    assertEquals(10, snapshot.parent().variables().get(0));
    assertNull(snapshot.parent().parent());
  }

  @Test
  void shouldRejectNullSnapshotVariables() {
    assertThrows(NullArgumentException.class, () -> new Scope.Snapshot(null, null));
  }

  @Test
  void shouldRejectNullSnapshotOnRestore() {
    Scope scope = new Scope(null);
    assertThrows(NullArgumentException.class, () -> scope.restore(null));
  }

  @Test
  void shouldRestorePreviousBindings() {
    Scope scope = new Scope(null);
    scope.declare(0, 10);
    Scope.Snapshot snapshot = scope.snapshot();
    scope.store(0, 20);
    scope.restore(snapshot);
    assertEquals(10, scope.load(0));
  }

  @Test
  void shouldRestoreParentScopeBindings() {
    Scope parent = new Scope(null);
    Scope child = new Scope(parent);
    parent.declare(0, 10);
    child.declare(1, 20);
    Scope.Snapshot snapshot = child.snapshot();
    parent.store(0, 100);
    child.store(1, 200);
    child.restore(snapshot);
    assertEquals(10, parent.load(0));
    assertEquals(20, child.load(1));
  }

  @Test
  void shouldRemoveBindingsCreatedAfterSnapshot() {
    Scope scope = new Scope(null);
    scope.declare(0, 10);
    Scope.Snapshot snapshot = scope.snapshot();
    scope.declare(1, 20);
    scope.restore(snapshot);
    assertEquals(10, scope.load(0));
    assertThrows(UndefinedVariableException.class, () -> scope.load(1));
  }

  @Test
  void shouldRestoreEmptyScope() {
    Scope scope = new Scope(null);
    Scope.Snapshot snapshot = scope.snapshot();
    scope.declare(0, 10);
    scope.restore(snapshot);
    assertThrows(UndefinedVariableException.class, () -> scope.load(0));
  }

  @Test
  void shouldPreserveScopeInstancesDuringRestore() {
    Scope parent = new Scope(null);
    Scope child = new Scope(parent);
    Scope.Snapshot snapshot = child.snapshot();
    child.restore(snapshot);
    assertSame(parent, child.parent());
  }

  @Test
  void shouldRejectSnapshotWithUnexpectedParent() {
    Scope root = new Scope(null);
    Scope parent = new Scope(null);
    Scope.Snapshot snapshot = new Scope.Snapshot(Map.of(), parent.snapshot());
    assertThrows(IllegalArgumentException.class, () -> root.restore(snapshot));
  }

  @Test
  void shouldRejectSnapshotMissingParent() {
    Scope parent = new Scope(null);
    Scope child = new Scope(parent);
    Scope.Snapshot snapshot = new Scope.Snapshot(Map.of(), null);
    assertThrows(IllegalArgumentException.class, () -> child.restore(snapshot));
  }
}