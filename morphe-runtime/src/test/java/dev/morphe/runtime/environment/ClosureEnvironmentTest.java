package dev.morphe.runtime.environment;

import dev.morphe.runtime.exception.argument.InvalidClosureEnvironmentIndexException;
import dev.morphe.runtime.exception.argument.InvalidFunctionIdException;
import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClosureEnvironmentTest {

  @Test
  void startsEmpty() {
    ClosureEnvironment environment = new ClosureEnvironment();
    assertEquals(0, environment.size());
  }

  @Test
  void rejectsNullCellOnCapture() {
    ClosureEnvironment environment = new ClosureEnvironment();
    assertThrows(NullArgumentException.class, () -> environment.capture(null));
  }

  @Test
  void captureReturnsSequentialIndices() {
    ClosureEnvironment environment = new ClosureEnvironment();
    VariableCell first = new VariableCell(new IntegerValue(10));
    VariableCell second = new VariableCell(new IntegerValue(20));
    int firstIndex = environment.capture(first);
    int secondIndex = environment.capture(second);
    assertEquals(0, firstIndex);
    assertEquals(1, secondIndex);
    assertEquals(2, environment.size());
  }

  @Test
  void getReturnsCapturedCell() {
    ClosureEnvironment environment = new ClosureEnvironment();
    VariableCell cell = new VariableCell(new IntegerValue(10));
    int index = environment.capture(cell);
    assertSame(cell, environment.get(index));
  }

  @Test
  void rejectsNegativeIndex() {
    ClosureEnvironment environment = new ClosureEnvironment();
    assertThrows(InvalidClosureEnvironmentIndexException.class, () -> environment.get(-1));
  }

  @Test
  void rejectsIndexOutsideEnvironment() {
    ClosureEnvironment environment = new ClosureEnvironment();
    environment.capture(new VariableCell(new IntegerValue(10)));
    assertThrows(InvalidClosureEnvironmentIndexException.class, () -> environment.get(1));
  }

  @Test
  void snapshotCapturesCurrentValues() {
    VariableCell first = new VariableCell(new IntegerValue(10));
    VariableCell second = new VariableCell(new IntegerValue(20));
    ClosureEnvironment environment = new ClosureEnvironment();
    environment.capture(first);
    environment.capture(second);
    ClosureEnvironment.Snapshot snapshot = environment.snapshot();
    assertEquals(2, snapshot.values().size());
    assertEquals(new IntegerValue(10), snapshot.values().get(0));
    assertEquals(new IntegerValue(20), snapshot.values().get(1));
  }

  @Test
  void snapshotIsIndependentFromLaterCellChanges() {
    VariableCell cell = new VariableCell(new IntegerValue(10));
    ClosureEnvironment environment = new ClosureEnvironment();
    environment.capture(cell);
    ClosureEnvironment.Snapshot snapshot = environment.snapshot();
    cell.set(new IntegerValue(20));
    assertEquals(new IntegerValue(10), snapshot.values().get(0));
  }

  @Test
  void snapshotValuesAreImmutable() {
    VariableCell cell = new VariableCell(new IntegerValue(10));
    ClosureEnvironment environment = new ClosureEnvironment();
    environment.capture(cell);
    ClosureEnvironment.Snapshot snapshot = environment.snapshot();
    assertThrows(UnsupportedOperationException.class, () -> snapshot.values().add(new IntegerValue(20)));
  }

  @Test
  void restoreRestoresPreviousValues() {
    VariableCell first = new VariableCell(new IntegerValue(10));
    VariableCell second = new VariableCell(new IntegerValue(20));
    ClosureEnvironment environment = new ClosureEnvironment();
    environment.capture(first);
    environment.capture(second);
    ClosureEnvironment.Snapshot snapshot = environment.snapshot();
    first.set(new IntegerValue(100));
    second.set(new IntegerValue(200));
    environment.restore(snapshot);
    assertEquals(new IntegerValue(10), first.get());
    assertEquals(new IntegerValue(20), second.get());
  }

  @Test
  void restorePreservesCellIdentity() {
    VariableCell cell = new VariableCell(new IntegerValue(10));
    ClosureEnvironment environment = new ClosureEnvironment();
    environment.capture(cell);
    ClosureEnvironment.Snapshot snapshot = environment.snapshot();
    cell.set(new IntegerValue(20));
    environment.restore(snapshot);
    assertSame(cell, environment.get(0));
    assertEquals(new IntegerValue(10), cell.get());
  }

  @Test
  void rejectsNullSnapshotOnRestore() {
    ClosureEnvironment environment = new ClosureEnvironment();
    assertThrows(NullArgumentException.class, () -> environment.restore(null));
  }

  @Test
  void rejectsSnapshotWithDifferentVariableCount() {
    ClosureEnvironment environment = new ClosureEnvironment();
    environment.capture(new VariableCell(new IntegerValue(10)));
    ClosureEnvironment.Snapshot snapshot = new ClosureEnvironment.Snapshot(
        List.of(new IntegerValue(10), new IntegerValue(20))
    );
    assertThrows(IllegalArgumentException.class, () -> environment.restore(snapshot));
  }
}