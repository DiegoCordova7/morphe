package dev.morphe.vm.stack;

import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.internal.stack.EmptyStackException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueStackTest {

  @Test
  void shouldInitiallyBeEmpty() {
    ValueStack stack = new ValueStack();
    assertTrue(stack.isEmpty());
    assertEquals(0, stack.size());
  }

  @Test
  void shouldPushValue() {
    ValueStack stack = new ValueStack();
    IValue value = new IntegerValue(42);
    stack.push(value);
    assertFalse(stack.isEmpty());
    assertEquals(1, stack.size());
    assertSame(value, stack.peek());
  }

  @Test
  void shouldRejectNullValue() {
    ValueStack stack = new ValueStack();
    assertThrows(NullArgumentException.class, () -> stack.push(null));
  }

  @Test
  void shouldPopTopValue() {
    ValueStack stack = new ValueStack();
    IValue value = new IntegerValue(42);
    stack.push(value);
    IValue result = stack.pop();
    assertSame(value, result);
    assertTrue(stack.isEmpty());
  }

  @Test
  void shouldFollowLifoOrder() {
    ValueStack stack = new ValueStack();
    IValue first = new IntegerValue(1);
    IValue second = new IntegerValue(2);
    stack.push(first);
    stack.push(second);
    assertSame(second, stack.pop());
    assertSame(first, stack.pop());
  }

  @Test
  void shouldPeekTopValueWithoutRemovingIt() {
    ValueStack stack = new ValueStack();
    IValue value = new IntegerValue(42);
    stack.push(value);
    IValue result = stack.peek();
    assertSame(value, result);
    assertEquals(1, stack.size());
  }

  @Test
  void shouldRejectPopWhenEmpty() {
    ValueStack stack = new ValueStack();
    assertThrows(EmptyStackException.class, stack::pop);
  }

  @Test
  void shouldRejectPeekWhenEmpty() {
    ValueStack stack = new ValueStack();
    assertThrows(EmptyStackException.class, stack::peek);
  }

  @Test
  void shouldUpdateSizeWhenValuesArePushedAndPopped() {
    ValueStack stack = new ValueStack();
    stack.push(new IntegerValue(1));
    stack.push(new IntegerValue(2));
    assertEquals(2, stack.size());
    stack.pop();
    assertEquals(1, stack.size());
    stack.pop();
    assertEquals(0, stack.size());
  }

  @Test
  void shouldCreateSnapshotWithCurrentState() {
    ValueStack stack = new ValueStack();
    IValue first = new IntegerValue(1);
    IValue second = new BooleanValue(true);
    stack.push(first);
    stack.push(second);
    ValueStack.Snapshot snapshot = stack.snapshot();
    assertEquals(2, snapshot.values().size());
    assertSame(second, snapshot.values().get(0));
    assertSame(first, snapshot.values().get(1));
  }

  @Test
  void shouldRestoreSnapshot() {
    ValueStack stack = new ValueStack();
    IValue first = new IntegerValue(1);
    IValue second = new IntegerValue(2);
    stack.push(first);
    ValueStack.Snapshot snapshot = stack.snapshot();
    stack.push(second);
    stack.restore(snapshot);
    assertEquals(1, stack.size());
    assertSame(first, stack.peek());
  }

  @Test
  void shouldRestoreEmptySnapshot() {
    ValueStack stack = new ValueStack();
    ValueStack.Snapshot snapshot = stack.snapshot();
    stack.push(new IntegerValue(42));
    stack.restore(snapshot);
    assertTrue(stack.isEmpty());
    assertEquals(0, stack.size());
  }

  @Test
  void shouldRejectNullSnapshotOnRestore() {
    ValueStack stack = new ValueStack();
    assertThrows(NullArgumentException.class, () -> stack.restore(null));
  }

  @Test
  void shouldCreateIndependentSnapshotList() {
    ValueStack stack = new ValueStack();
    IValue value = new IntegerValue(42);
    stack.push(value);
    ValueStack.Snapshot snapshot = stack.snapshot();
    stack.pop();
    assertEquals(1, snapshot.values().size());
    assertSame(value, snapshot.values().get(0));
  }

  @Test
  void shouldRejectNullSnapshotValues() {
    assertThrows(NullArgumentException.class, () -> new ValueStack.Snapshot(null));
  }
}