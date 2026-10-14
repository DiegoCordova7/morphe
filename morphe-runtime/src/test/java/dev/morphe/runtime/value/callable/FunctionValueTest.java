package dev.morphe.runtime.value.callable;

import dev.morphe.runtime.environment.ClosureEnvironment;
import dev.morphe.runtime.exception.argument.InvalidFunctionIdException;
import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.kind.ValueKind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FunctionValueTest {

  @Test
  void rejectsNegativeFunctionId() {
    ClosureEnvironment environment = new ClosureEnvironment();
    assertThrows(InvalidFunctionIdException.class, () -> new FunctionValue(-1, environment));
  }

  @Test
  void rejectsNullEnvironment() {
    assertThrows(NullArgumentException.class, () -> new FunctionValue(0, null));
  }

  @Test
  void acceptsNonNegativeFunctionId() {
    ClosureEnvironment environment = new ClosureEnvironment();
    FunctionValue value = new FunctionValue(0, environment);
    assertEquals(0, value.functionId());
    assertSame(environment, value.environment());
  }

  @Test
  void acceptsPositiveFunctionId() {
    ClosureEnvironment environment = new ClosureEnvironment();
    FunctionValue value = new FunctionValue(42, environment);
    assertEquals(42, value.functionId());
  }

  @Test
  void returnsFunctionKind() {
    ClosureEnvironment environment = new ClosureEnvironment();
    FunctionValue value = new FunctionValue(0, environment);
    assertEquals(ValueKind.FUNCTION, value.kind());
  }

  @Test
  void considersSameFunctionIdEqual() {
    ClosureEnvironment firstEnvironment = new ClosureEnvironment();
    ClosureEnvironment secondEnvironment = new ClosureEnvironment();
    FunctionValue first = new FunctionValue(10, firstEnvironment);
    FunctionValue second = new FunctionValue(10, secondEnvironment);
    assertEquals(first, second);
  }

  @Test
  void considersDifferentFunctionIdsNotEqual() {
    ClosureEnvironment environment = new ClosureEnvironment();
    FunctionValue first = new FunctionValue(10, environment);
    FunctionValue second = new FunctionValue(20, environment);
    assertNotEquals(first, second);
  }

  @Test
  void isNotEqualToNull() {
    ClosureEnvironment environment = new ClosureEnvironment();
    FunctionValue value = new FunctionValue(10, environment);
    assertNotEquals(value, null);
  }

  @Test
  void isNotEqualToDifferentValueType() {
    ClosureEnvironment environment = new ClosureEnvironment();
    FunctionValue value = new FunctionValue(10, environment);
    assertNotEquals(value, "Function(10)");
  }

  @Test
  void equalFunctionValuesHaveSameHashCode() {
    ClosureEnvironment firstEnvironment = new ClosureEnvironment();
    ClosureEnvironment secondEnvironment = new ClosureEnvironment();
    FunctionValue first = new FunctionValue(10, firstEnvironment);
    FunctionValue second = new FunctionValue(10, secondEnvironment);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void returnsFunctionRepresentation() {
    ClosureEnvironment environment = new ClosureEnvironment();
    FunctionValue value = new FunctionValue(42, environment);
    assertEquals("Function(42)", value.toString());
  }
}