package dev.morphe.vm.scope;

import dev.morphe.runtime.environment.ClosureEnvironment;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.internal.stack.EmptyStackException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CallStackTest {

  @Test
  void shouldInitiallyBeEmpty() {
    CallStack stack = new CallStack();
    assertTrue(stack.isEmpty());
    assertEquals(0, stack.size());
  }

  @Test
  void shouldPushFrame() {
    CallStack stack = new CallStack();
    CallFrame frame = createFrame(10);
    stack.push(frame);
    assertFalse(stack.isEmpty());
    assertEquals(1, stack.size());
    assertSame(frame, stack.peek());
  }

  @Test
  void shouldRejectNullFrame() {
    CallStack stack = new CallStack();
    assertThrows(NullArgumentException.class, () -> stack.push(null));
  }

  @Test
  void shouldPopTopFrame() {
    CallStack stack = new CallStack();
    CallFrame frame = createFrame(10);
    stack.push(frame);
    CallFrame result = stack.pop();
    assertSame(frame, result);
    assertTrue(stack.isEmpty());
  }

  @Test
  void shouldFollowLifoOrder() {
    CallStack stack = new CallStack();
    CallFrame first = createFrame(10);
    CallFrame second = createFrame(20);
    stack.push(first);
    stack.push(second);
    assertSame(second, stack.pop());
    assertSame(first, stack.pop());
  }

  @Test
  void shouldPeekTopFrameWithoutRemovingIt() {
    CallStack stack = new CallStack();
    CallFrame frame = createFrame(10);
    stack.push(frame);
    assertSame(frame, stack.peek());
    assertEquals(1, stack.size());
  }

  @Test
  void shouldRejectPopWhenEmpty() {
    CallStack stack = new CallStack();
    assertThrows(EmptyStackException.class, stack::pop);
  }

  @Test
  void shouldRejectPeekWhenEmpty() {
    CallStack stack = new CallStack();
    assertThrows(EmptyStackException.class, stack::peek);
  }

  @Test
  void shouldUpdateSizeWhenFramesArePushedAndPopped() {
    CallStack stack = new CallStack();
    stack.push(createFrame(10));
    stack.push(createFrame(20));
    assertEquals(2, stack.size());
    stack.pop();
    assertEquals(1, stack.size());
    stack.pop();
    assertEquals(0, stack.size());
  }

  @Test
  void shouldCreateEmptySnapshot() {
    CallStack stack = new CallStack();
    CallStack.Snapshot snapshot = stack.snapshot();
    assertNotNull(snapshot);
    assertTrue(snapshot.frames().isEmpty());
  }

  @Test
  void shouldCreateSnapshotWithFrames() {
    CallStack stack = new CallStack();
    CallFrame first = createFrame(10);
    CallFrame second = createFrame(20);
    stack.push(first);
    stack.push(second);
    CallStack.Snapshot snapshot = stack.snapshot();
    assertEquals(2, snapshot.frames().size());
    assertSame(second, snapshot.frames().get(0).frame());
    assertSame(first, snapshot.frames().get(1).frame());
  }

  @Test
  void shouldPreserveFrameStateInSnapshot() {
    CallStack stack = new CallStack();
    CallFrame frame = createFrame(10);
    frame.scope().declare(0, 100);
    stack.push(frame);
    CallStack.Snapshot snapshot = stack.snapshot();
    assertSame(frame, snapshot.frames().get(0).frame());
    assertEquals(100, snapshot.frames().get(0).state().scopeState().variables().get(0));
  }

  @Test
  void shouldRejectNullFrameSnapshotComponents() {
    CallFrame frame = createFrame(10);
    CallFrame.Snapshot state = frame.snapshot();
    assertThrows(NullArgumentException.class, () -> new CallStack.FrameSnapshot(null, state));
    assertThrows(NullArgumentException.class, () -> new CallStack.FrameSnapshot(frame, null));
  }

  @Test
  void shouldRejectNullSnapshotFrames() {
    assertThrows(NullArgumentException.class, () -> new CallStack.Snapshot(null));
  }

  @Test
  void shouldRejectNullSnapshotOnRestore() {
    CallStack stack = new CallStack();
    assertThrows(NullArgumentException.class, () -> stack.restore(null));
  }

  @Test
  void shouldRestorePreviousFrames() {
    CallStack stack = new CallStack();
    CallFrame first = createFrame(10);
    CallFrame second = createFrame(20);
    stack.push(first);
    stack.push(second);
    CallStack.Snapshot snapshot = stack.snapshot();
    stack.pop();
    stack.push(createFrame(30));
    stack.restore(snapshot);
    assertEquals(2, stack.size());
    assertSame(second, stack.peek());
    assertSame(second, stack.pop());
    assertSame(first, stack.pop());
  }

  @Test
  void shouldDiscardFramesCreatedAfterSnapshot() {
    CallStack stack = new CallStack();
    CallFrame original = createFrame(10);
    stack.push(original);
    CallStack.Snapshot snapshot = stack.snapshot();
    CallFrame added = createFrame(20);
    stack.push(added);
    stack.restore(snapshot);
    assertEquals(1, stack.size());
    assertSame(original, stack.peek());
  }

  @Test
  void shouldRestoreFrameState() {
    CallStack stack = new CallStack();
    CallFrame frame = createFrame(10);
    frame.scope().declare(0, 100);
    stack.push(frame);
    CallStack.Snapshot snapshot = stack.snapshot();
    frame.scope().store(0, 200);
    stack.restore(snapshot);
    assertSame(frame, stack.peek());
    assertEquals(100, frame.scope().load(0));
  }

  @Test
  void shouldPreserveFrameInstancesDuringRestore() {
    CallStack stack = new CallStack();
    CallFrame first = createFrame(10);
    CallFrame second = createFrame(20);
    stack.push(first);
    stack.push(second);
    CallStack.Snapshot snapshot = stack.snapshot();
    stack.pop();
    stack.pop();
    stack.restore(snapshot);
    assertSame(second, stack.pop());
    assertSame(first, stack.pop());
  }

  @Test
  void shouldRestoreEmptySnapshot() {
    CallStack stack = new CallStack();
    CallStack.Snapshot snapshot = stack.snapshot();
    stack.push(createFrame(10));
    stack.restore(snapshot);
    assertTrue(stack.isEmpty());
    assertEquals(0, stack.size());
  }

  @Test
  void shouldPreserveFrameOrderDuringRestore() {
    CallStack stack = new CallStack();
    CallFrame first = createFrame(10);
    CallFrame second = createFrame(20);
    CallFrame third = createFrame(30);
    stack.push(first);
    stack.push(second);
    stack.push(third);
    CallStack.Snapshot snapshot = stack.snapshot();
    stack.pop();
    stack.pop();
    stack.pop();
    stack.restore(snapshot);
    assertSame(third, stack.pop());
    assertSame(second, stack.pop());
    assertSame(first, stack.pop());
  }

  private static CallFrame createFrame(int returnAddress) {
    return new CallFrame(returnAddress, new Scope(null), new ClosureEnvironment());
  }
}