package dev.morphe.vm.operations;

import dev.morphe.runtime.environment.ClosureEnvironment;
import dev.morphe.runtime.environment.VariableCell;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.vm.ExecutionContext;
import dev.morphe.vm.exception.internal.scope.RootScopeExitException;
import dev.morphe.vm.memory.Memory;
import dev.morphe.vm.memory.MemoryAllocator;
import dev.morphe.vm.scope.Scope;
import dev.morphe.vm.stack.ValueStack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScopeOperationsTest {

  @Test
  void declareShouldAllocateAddressAndBindSlot() {
    MemoryAllocator allocator = new MemoryAllocator();
    Scope scope = new Scope(null);
    ScopeOperations.declare(allocator, scope, 0);
    assertEquals(0, scope.load(0));
  }

  @Test
  void loadShouldPushValueStoredAtSlotAddress() {
    ValueStack stack = new ValueStack();
    Memory memory = new Memory();
    Scope scope = new Scope(null);
    IValue value = new IntegerValue(42);
    scope.declare(0, 10);
    memory.store(10, value);
    ScopeOperations.load(stack, memory, scope, 0);
    assertEquals(value, stack.pop());
  }

  @Test
  void storeShouldPopValueAndStoreItAtSlotAddress() {
    ValueStack stack = new ValueStack();
    Memory memory = new Memory();
    Scope scope = new Scope(null);
    IValue value = new IntegerValue(42);
    scope.declare(0, 10);
    stack.push(value);
    ScopeOperations.store(stack, memory, scope, 0);
    assertEquals(value, memory.load(10));
    assertTrue(stack.isEmpty());
  }

  @Test
  void loadCaptureShouldPushCapturedValue() {
    ValueStack stack = new ValueStack();
    ClosureEnvironment environment = new ClosureEnvironment();
    IValue value = new IntegerValue(42);
    VariableCell cell = new VariableCell(value);
    environment.capture(cell);
    ScopeOperations.loadCapture(stack, environment, 0);
    assertEquals(value, stack.pop());
  }

  @Test
  void storeCaptureShouldUpdateCapturedVariableCell() {
    ValueStack stack = new ValueStack();
    ClosureEnvironment environment = new ClosureEnvironment();
    VariableCell cell = new VariableCell(new IntegerValue(10));
    IValue value = new IntegerValue(42);
    environment.capture(cell);
    stack.push(value);
    ScopeOperations.storeCapture(stack, environment, 0);
    assertEquals(value, cell.get());
    assertTrue(stack.isEmpty());
  }

  @Test
  void enterScopeShouldCreateChildScope() {
    ExecutionContext context = new ExecutionContext();
    Scope parent = context.scope();
    ScopeOperations.enterScope(context);
    assertNotSame(parent, context.scope());
    assertSame(parent, context.scope().parent());
  }

  @Test
  void exitScopeShouldRestoreParentScope() {
    ExecutionContext context = new ExecutionContext();
    Scope parent = context.scope();
    ScopeOperations.enterScope(context);
    Scope child = context.scope();
    ScopeOperations.exitScope(context);
    assertSame(parent, context.scope());
    assertNotSame(child, context.scope());
  }

  @Test
  void exitScopeShouldRejectRootScopeExit() {
    ExecutionContext context = new ExecutionContext();
    assertThrows(RootScopeExitException.class, () -> ScopeOperations.exitScope(context));
  }
}