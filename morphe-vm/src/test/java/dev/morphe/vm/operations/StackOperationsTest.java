package dev.morphe.vm.operations;

import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.vm.stack.ValueStack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StackOperationsTest {

  @Test
  void pushShouldAddValueToStack() {
    ValueStack stack = new ValueStack();
    IValue value = new IntegerValue(42);
    StackOperations.push(stack, value);
    assertEquals(value, stack.peek());
  }

  @Test
  void popShouldRemoveAndReturnTopValue() {
    ValueStack stack = new ValueStack();
    IValue value = new IntegerValue(42);
    stack.push(value);
    IValue result = StackOperations.pop(stack);
    assertEquals(value, result);
    assertTrue(stack.isEmpty());
  }

  @Test
  void dupShouldDuplicateTopValue() {
    ValueStack stack = new ValueStack();
    IValue value = new IntegerValue(42);
    stack.push(value);
    StackOperations.dup(stack);
    assertEquals(value, stack.pop());
    assertEquals(value, stack.pop());
  }

  @Test
  void overShouldDuplicateSecondValue() {
    ValueStack stack = new ValueStack();
    IValue first = new IntegerValue(1);
    IValue second = new IntegerValue(2);
    stack.push(first);
    stack.push(second);
    StackOperations.over(stack);
    assertEquals(first, stack.pop());
    assertEquals(second, stack.pop());
    assertEquals(first, stack.pop());
  }

  @Test
  void swapShouldExchangeTopTwoValues() {
    ValueStack stack = new ValueStack();
    IValue first = new IntegerValue(1);
    IValue second = new IntegerValue(2);
    stack.push(first);
    stack.push(second);
    StackOperations.swap(stack);
    assertEquals(first, stack.pop());
    assertEquals(second, stack.pop());
  }

  @Test
  void rotShouldRotateTopThreeValues() {
    ValueStack stack = new ValueStack();
    IValue first = new IntegerValue(1);
    IValue second = new IntegerValue(2);
    IValue third = new IntegerValue(3);
    stack.push(first);
    stack.push(second);
    stack.push(third);
    StackOperations.rot(stack);
    assertEquals(first, stack.pop());
    assertEquals(third, stack.pop());
    assertEquals(second, stack.pop());
  }

  @Test
  void dup2ShouldDuplicateTopTwoValues() {
    ValueStack stack = new ValueStack();
    IValue first = new IntegerValue(1);
    IValue second = new IntegerValue(2);
    stack.push(first);
    stack.push(second);
    StackOperations.dup2(stack);
    assertEquals(second, stack.pop());
    assertEquals(first, stack.pop());
    assertEquals(second, stack.pop());
    assertEquals(first, stack.pop());
  }
}