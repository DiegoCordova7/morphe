package dev.morphe.vm.operations;

import dev.morphe.runtime.value.primitive.*;
import dev.morphe.runtime.value.primitive.numeric.*;
import dev.morphe.vm.ExecutionContext;
import dev.morphe.vm.exception.internal.type.ExpectedNumericValueException;
import dev.morphe.vm.memory.*;
import dev.morphe.vm.scope.CallStack;
import dev.morphe.vm.stack.ValueStack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComparisonOperationsTest {

  @Test
  void shouldReturnTrueWhenValuesAreEqual() {
    BooleanValue result = ComparisonOperations.eq(new ExecutionContext(), new IntegerValue(10), new IntegerValue(10));
    assertSame(BooleanValue.TRUE, result);
  }

  @Test
  void shouldReturnFalseWhenValuesAreDifferent() {
    BooleanValue result = ComparisonOperations.eq(new ExecutionContext(), new IntegerValue(10), new IntegerValue(20));
    assertSame(BooleanValue.FALSE, result);
  }

  @Test
  void shouldReturnTrueWhenValuesAreNotEqual() {
    BooleanValue result = ComparisonOperations.neq(new ExecutionContext(), new StringValue("Morphe"), new StringValue("Java"));
    assertSame(BooleanValue.TRUE, result);
  }

  @Test
  void shouldReturnFalseWhenValuesAreEqualForNeq() {
    BooleanValue result = ComparisonOperations.neq(new ExecutionContext(), new DoubleValue(10.5), new DoubleValue(10.5));
    assertSame(BooleanValue.FALSE, result);
  }

  @Test
  void shouldReturnTrueWhenLeftIsLessThanRight() {
    assertSame(BooleanValue.TRUE, ComparisonOperations.lt(new IntegerValue(10), new IntegerValue(20)));
  }

  @Test
  void shouldReturnFalseWhenLeftIsNotLessThanRight() {
    assertSame(BooleanValue.FALSE, ComparisonOperations.lt(new IntegerValue(20), new IntegerValue(10)));
  }

  @Test
  void shouldReturnTrueWhenLeftIsLessThanOrEqualToRight() {
    assertSame(BooleanValue.TRUE, ComparisonOperations.lte(new IntegerValue(10), new IntegerValue(10)));
  }

  @Test
  void shouldReturnTrueWhenLeftIsGreaterThanRight() {
    assertSame(BooleanValue.TRUE, ComparisonOperations.gt(new DoubleValue(20.5), new IntegerValue(10)));
  }

  @Test
  void shouldReturnTrueWhenLeftIsGreaterThanOrEqualToRight() {
    assertSame(BooleanValue.TRUE, ComparisonOperations.gte(new IntegerValue(20), new DoubleValue(20)));
  }

  @Test
  void shouldRejectNonNumericLeftOperand() {
    assertThrows(
        ExpectedNumericValueException.class,
        () -> ComparisonOperations.lt(new StringValue("Morphe"), new IntegerValue(10))
    );
  }

  @Test
  void shouldRejectNonNumericRightOperand() {
    assertThrows(
        ExpectedNumericValueException.class,
        () -> ComparisonOperations.gt(new IntegerValue(10), new StringValue("Morphe"))
    );
  }
}