package dev.morphe.vm.scope;

import dev.morphe.runtime.environment.ClosureEnvironment;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.internal.scope.UndefinedVariableException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CallFrameTest {

  @Test
  void shouldRejectNullScope() {
    assertThrows(NullArgumentException.class, () -> new CallFrame(10, null, new ClosureEnvironment()));
  }

  @Test
  void shouldRejectNullEnvironment() {
    assertThrows(NullArgumentException.class, () -> new CallFrame(10, new Scope(null), null));
  }

  @Test
  void shouldStoreReturnAddress() {
    CallFrame frame = new CallFrame(42, new Scope(null), new ClosureEnvironment());
    assertEquals(42, frame.returnAddress());
  }

  @Test
  void shouldExposeInitialScope() {
    Scope scope = new Scope(null);
    CallFrame frame = new CallFrame(42, scope, new ClosureEnvironment());
    assertSame(scope, frame.scope());
  }

  @Test
  void shouldExposeEnvironment() {
    ClosureEnvironment environment = new ClosureEnvironment();
    CallFrame frame = new CallFrame(42, new Scope(null), environment);
    assertSame(environment, frame.environment());
  }

  @Test
  void shouldReplaceCurrentScope() {
    Scope first = new Scope(null);
    Scope second = new Scope(null);
    CallFrame frame = new CallFrame(42, first, new ClosureEnvironment());
    frame.scope(second);
    assertSame(second, frame.scope());
  }

  @Test
  void shouldRejectNullScopeReplacement() {
    CallFrame frame = new CallFrame(42, new Scope(null), new ClosureEnvironment());
    assertThrows(NullArgumentException.class, () -> frame.scope(null));
  }

  @Test
  void shouldCreateSnapshot() {
    Scope scope = new Scope(null);
    ClosureEnvironment environment = new ClosureEnvironment();
    CallFrame frame = new CallFrame(42, scope, environment);
    CallFrame.Snapshot snapshot = frame.snapshot();
    assertNotNull(snapshot);
    assertSame(scope, snapshot.scope());
    assertNotNull(snapshot.scopeState());
    assertSame(environment, snapshot.environment());
    assertNotNull(snapshot.environmentState());
  }

  @Test
  void shouldRejectNullSnapshotComponents() {
    Scope scope = new Scope(null);
    ClosureEnvironment environment = new ClosureEnvironment();
    CallFrame.Snapshot valid = new CallFrame.Snapshot(scope, scope.snapshot(), environment, environment.snapshot()
    );
    assertNotNull(valid);
    assertThrows(
        NullArgumentException.class,
        () -> new CallFrame.Snapshot(null, scope.snapshot(), environment, environment.snapshot())
    );
    assertThrows(
        NullArgumentException.class,
        () -> new CallFrame.Snapshot(scope, null, environment, environment.snapshot())
    );
    assertThrows(
        NullArgumentException.class,
        () -> new CallFrame.Snapshot(scope, scope.snapshot(), null, environment.snapshot())
    );
    assertThrows(
        NullArgumentException.class,
        () -> new CallFrame.Snapshot(scope, scope.snapshot(), environment, null)
    );
  }

  @Test
  void shouldRejectNullSnapshotOnRestore() {
    CallFrame frame = new CallFrame(42, new Scope(null), new ClosureEnvironment());
    assertThrows(NullArgumentException.class, () -> frame.restore(null));
  }

  @Test
  void shouldRestorePreviousScope() {
    Scope original = new Scope(null);
    Scope replacement = new Scope(null);
    CallFrame frame = new CallFrame(42, original, new ClosureEnvironment());
    CallFrame.Snapshot snapshot = frame.snapshot();
    frame.scope(replacement);
    frame.restore(snapshot);
    assertSame(original, frame.scope());
  }

  @Test
  void shouldRestoreScopeState() {
    Scope scope = new Scope(null);
    CallFrame frame = new CallFrame(42, scope, new ClosureEnvironment());
    scope.declare(0, 10);
    CallFrame.Snapshot snapshot = frame.snapshot();
    scope.store(0, 20);
    frame.restore(snapshot);
    assertSame(scope, frame.scope());
    assertEquals(10, scope.load(0));
  }

  @Test
  void shouldRemoveScopeBindingsCreatedAfterSnapshot() {
    Scope scope = new Scope(null);
    CallFrame frame = new CallFrame(42, scope, new ClosureEnvironment());
    scope.declare(0, 10);
    CallFrame.Snapshot snapshot = frame.snapshot();
    scope.declare(1, 20);
    frame.restore(snapshot);
    assertEquals(10, scope.load(0));
    assertThrows(UndefinedVariableException.class, () -> scope.load(1));
  }

  @Test
  void shouldPreserveReturnAddressWhenRestoring() {
    Scope scope = new Scope(null);
    CallFrame frame = new CallFrame(42, scope, new ClosureEnvironment());
    CallFrame.Snapshot snapshot = frame.snapshot();
    frame.restore(snapshot);
    assertEquals(42, frame.returnAddress());
  }

  @Test
  void shouldPreserveScopeInstanceDuringRestore() {
    Scope scope = new Scope(null);
    CallFrame frame = new CallFrame(42, scope, new ClosureEnvironment());
    CallFrame.Snapshot snapshot = frame.snapshot();
    frame.restore(snapshot);
    assertSame(scope, frame.scope());
  }

  @Test
  void shouldPreserveEnvironmentInstanceDuringRestore() {
    ClosureEnvironment environment = new ClosureEnvironment();
    CallFrame frame = new CallFrame(42, new Scope(null), environment);
    CallFrame.Snapshot snapshot = frame.snapshot();
    frame.restore(snapshot);
    assertSame(environment, frame.environment());
  }
}