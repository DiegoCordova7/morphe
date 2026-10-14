package dev.morphe.vm;

import dev.morphe.runtime.environment.ClosureEnvironment;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.internal.memory.UninitializedMemoryException;
import dev.morphe.vm.scope.CallFrame;
import dev.morphe.vm.scope.Scope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExecutionContextTest {

  @Test
  void shouldInitiallyContainNoFrame() {
    ExecutionContext context = new ExecutionContext();
    assertTrue(context.hasFrame());
  }

  @Test
  void shouldReturnCurrentFrameWhenCallStackIsNotEmpty() {
    ExecutionContext context = new ExecutionContext();
    CallFrame frame = createFrame(-1);
    context.callStack().push(frame);
    assertSame(frame, context.currentFrame());
  }

  @Test
  void shouldReturnCurrentScopeFromCurrentFrame() {
    ExecutionContext context = new ExecutionContext();
    Scope scope = new Scope(null);
    CallFrame frame = new CallFrame(-1, scope, new ClosureEnvironment());
    context.callStack().push(frame);
    assertSame(scope, context.scope());
  }

  @Test
  void shouldCreateValidSnapshot() {
    ExecutionContext context = new ExecutionContext();
    ExecutionContext.Snapshot snapshot = context.snapshot();
    assertNotNull(snapshot);
    assertNotNull(snapshot.stack());
    assertNotNull(snapshot.memory());
    assertNotNull(snapshot.allocator());
    assertNotNull(snapshot.heap());
    assertNotNull(snapshot.callStack());
  }

  @Test
  void shouldRejectNullSnapshotOnRestore() {
    ExecutionContext context = new ExecutionContext();
    assertThrows(NullArgumentException.class, () -> context.restore(null));
  }

  @Test
  void shouldRestoreValueStackState() {
    ExecutionContext context = new ExecutionContext();
    IValue original = new IntegerValue(10);
    context.stack().push(original);
    ExecutionContext.Snapshot snapshot = context.snapshot();
    context.stack().push(new IntegerValue(20));
    context.restore(snapshot);
    assertEquals(1, context.stack().size());
    assertSame(original, context.stack().peek());
  }

  @Test
  void shouldRestoreMemoryState() {
    ExecutionContext context = new ExecutionContext();
    IValue original = new IntegerValue(10);
    context.memory().store(5, original);
    ExecutionContext.Snapshot snapshot = context.snapshot();
    context.memory().store(5, new IntegerValue(20));
    context.memory().store(10, new IntegerValue(30));
    context.restore(snapshot);
    assertSame(original, context.memory().load(5));
    assertThrows(UninitializedMemoryException.class, () -> context.memory().load(10));
  }

  @Test
  void shouldRestoreAllocatorState() {
    ExecutionContext context = new ExecutionContext();
    context.allocator().allocate();
    context.allocator().allocate();
    context.allocator().free(0);
    ExecutionContext.Snapshot snapshot = context.snapshot();
    context.allocator().allocate();
    context.allocator().allocate();
    context.restore(snapshot);
    assertEquals(0, context.allocator().allocate());
    assertEquals(2, context.allocator().allocate());
  }

  @Test
  void shouldRestoreHeapState() {
    ExecutionContext context = new ExecutionContext();
    IValue original = new IntegerValue(10);
    int address = context.heap().allocate(original);
    ExecutionContext.Snapshot snapshot = context.snapshot();
    context.heap().store(address, new IntegerValue(20));
    context.heap().allocate(new IntegerValue(30));
    context.restore(snapshot);
    assertSame(original, context.heap().load(address));
    assertEquals(1, context.heap().allocate(new IntegerValue(40)));
  }

  @Test
  void shouldRestoreCallStackState() {
    ExecutionContext context = new ExecutionContext();
    CallFrame frame = createFrame(10);
    context.callStack().push(frame);
    ExecutionContext.Snapshot snapshot = context.snapshot();
    context.callStack().pop();
    context.callStack().push(createFrame(20));
    context.restore(snapshot);
    assertEquals(2, context.callStack().size());
    assertSame(frame, context.currentFrame());
  }

  @Test
  void shouldRestoreScopeStateThroughCallStack() {
    ExecutionContext context = new ExecutionContext();
    Scope scope = new Scope(null);
    CallFrame frame = new CallFrame(10, scope, new ClosureEnvironment());
    scope.declare(0, 100);
    context.callStack().push(frame);
    ExecutionContext.Snapshot snapshot = context.snapshot();
    scope.store(0, 200);
    context.restore(snapshot);
    assertSame(frame, context.currentFrame());
    assertEquals(100, context.scope().load(0));
  }

  @Test
  void shouldRestoreCompleteExecutionState() {
    ExecutionContext context = new ExecutionContext();
    IValue stackValue = new BooleanValue(true);
    IValue memoryValue = new IntegerValue(10);
    IValue heapValue = new IntegerValue(20);
    context.stack().push(stackValue);
    context.memory().store(5, memoryValue);
    context.allocator().allocate();
    context.allocator().allocate();
    context.allocator().free(0);
    int heapAddress = context.heap().allocate(heapValue);
    Scope scope = new Scope(null);
    scope.declare(0, 5);
    CallFrame frame = new CallFrame(42, scope, new ClosureEnvironment());
    context.callStack().push(frame);
    ExecutionContext.Snapshot snapshot = context.snapshot();
    context.stack().pop();
    context.memory().store(5, new IntegerValue(100));
    context.memory().store(10, new IntegerValue(200));
    context.allocator().allocate();
    context.heap().store(heapAddress, new IntegerValue(300));
    context.heap().allocate(new IntegerValue(400));
    scope.store(0, 999);
    context.callStack().pop();
    context.restore(snapshot);
    assertSame(stackValue, context.stack().peek());
    assertSame(memoryValue, context.memory().load(5));
    assertThrows(UninitializedMemoryException.class, () -> context.memory().load(10));
    assertEquals(0, context.allocator().allocate());
    assertSame(heapValue, context.heap().load(heapAddress));
    assertEquals(1, context.heap().allocate(new IntegerValue(500)));
    assertEquals(2, context.callStack().size());
    assertSame(frame, context.currentFrame());
    assertEquals(42, context.currentFrame().returnAddress());
    assertEquals(5, context.scope().load(0));
  }

  private static CallFrame createFrame(int returnAddress) {
    return new CallFrame(returnAddress, new Scope(null), new ClosureEnvironment());
  }
}