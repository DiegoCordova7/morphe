package dev.morphe.vm.operations;

import dev.morphe.runtime.value.primitive.StringValue;
import dev.morphe.runtime.value.primitive.numeric.*;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.execution.DivisionByZeroException;
import dev.morphe.vm.exception.execution.ModuleByZeroException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArithmeticOperationsTest {

  @Test
  void shouldAddIntegers() {
    INumericValue result = ArithmeticOperations.add(new IntegerValue(10), new IntegerValue(20));
    assertEquals(new IntegerValue(30), result);
  }

  @Test
  void shouldAddMixedNumericValues() {
    INumericValue result = ArithmeticOperations.add(new IntegerValue(10), new DoubleValue(2.5));
    assertEquals(new DoubleValue(12.5), result);
  }

  @Test
  void shouldAddDoubles() {
    INumericValue result = ArithmeticOperations.add(new DoubleValue(10.5), new DoubleValue(2.5));
    assertEquals(new DoubleValue(13.0), result);
  }

  @Test
  void shouldConcatenateValues() {
    StringValue result = ArithmeticOperations.concat(new StringValue("Hello "), new StringValue("World"));
    assertEquals(new StringValue("Hello World"), result);
  }

  @Test
  void shouldConcatenateDifferentValueTypes() {
    StringValue result = ArithmeticOperations.concat(new StringValue("Value: "), new IntegerValue(42));
    assertEquals(new StringValue("Value: 42"), result);
  }

  @Test
  void shouldRejectNullLeftOperandWhenConcatenating() {
    assertThrows(NullArgumentException.class, () -> ArithmeticOperations.concat(null, new StringValue("test")));
  }

  @Test
  void shouldRejectNullRightOperandWhenConcatenating() {
    assertThrows(NullArgumentException.class, () -> ArithmeticOperations.concat(new StringValue("test"), null));
  }

  @Test
  void shouldSubtractIntegers() {
    INumericValue result = ArithmeticOperations.sub(new IntegerValue(20), new IntegerValue(8));
    assertEquals(new IntegerValue(12), result);
  }

  @Test
  void shouldSubtractMixedNumericValues() {
    INumericValue result = ArithmeticOperations.sub(new IntegerValue(20), new DoubleValue(7.5));
    assertEquals(new DoubleValue(12.5), result);
  }

  @Test
  void shouldMultiplyIntegers() {
    INumericValue result = ArithmeticOperations.mul(new IntegerValue(6), new IntegerValue(7));
    assertEquals(new IntegerValue(42), result);
  }

  @Test
  void shouldMultiplyMixedNumericValues() {
    INumericValue result = ArithmeticOperations.mul(new IntegerValue(6), new DoubleValue(2.5));
    assertEquals(new DoubleValue(15.0), result);
  }

  @Test
  void shouldDivideIntegersAsDouble() {
    INumericValue result = ArithmeticOperations.div(new IntegerValue(10), new IntegerValue(4));
    assertEquals(new DoubleValue(2.5), result);
  }

  @Test
  void shouldDivideDoubles() {
    INumericValue result = ArithmeticOperations.div(new DoubleValue(7.5), new DoubleValue(2.5));
    assertEquals(new DoubleValue(3.0), result);
  }

  @Test
  void shouldRejectDivisionByZero() {
    assertThrows(
        DivisionByZeroException.class, () -> ArithmeticOperations.div(new IntegerValue(10), new IntegerValue(0))
    );
  }

  @Test
  void shouldCalculateModuloAsDouble() {
    INumericValue result = ArithmeticOperations.mod(new IntegerValue(10), new IntegerValue(3));
    assertEquals(new DoubleValue(1.0), result);
  }

  @Test
  void shouldRejectModuloByZero() {
    assertThrows(
        ModuleByZeroException.class, () -> ArithmeticOperations.mod(new IntegerValue(10), new IntegerValue(0))
    );
  }

  @Test
  void shouldNegateInteger() {
    INumericValue result = ArithmeticOperations.neg(new IntegerValue(10));
    assertEquals(new IntegerValue(-10), result);
  }

  @Test
  void shouldNegateDouble() {
    INumericValue result = ArithmeticOperations.neg(new DoubleValue(10.5));
    assertEquals(new DoubleValue(-10.5), result);
  }
}