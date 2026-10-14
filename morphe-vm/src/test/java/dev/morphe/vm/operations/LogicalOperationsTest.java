package dev.morphe.vm.operations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.vm.exception.internal.type.ExpectedBooleanValueException;

class LogicalOperationsTest {

  @Test
  void andShouldReturnTrueWhenBothOperandsAreTrue() {
    assertEquals(BooleanValue.TRUE, LogicalOperations.and(BooleanValue.TRUE, BooleanValue.TRUE));
  }

  @Test
  void andShouldReturnFalseWhenLeftOperandIsFalse() {
    assertEquals(BooleanValue.FALSE, LogicalOperations.and(BooleanValue.FALSE, BooleanValue.TRUE));
  }

  @Test
  void andShouldReturnFalseWhenRightOperandIsFalse() {
    assertEquals(BooleanValue.FALSE, LogicalOperations.and(BooleanValue.TRUE, BooleanValue.FALSE));
  }

  @Test
  void andShouldReturnFalseWhenBothOperandsAreFalse() {
    assertEquals(BooleanValue.FALSE, LogicalOperations.and(BooleanValue.FALSE, BooleanValue.FALSE));
  }

  @Test
  void orShouldReturnTrueWhenBothOperandsAreTrue() {
    assertEquals(BooleanValue.TRUE, LogicalOperations.or(BooleanValue.TRUE, BooleanValue.TRUE));
  }

  @Test
  void orShouldReturnTrueWhenLeftOperandIsTrue() {
    assertEquals(BooleanValue.TRUE, LogicalOperations.or(BooleanValue.TRUE, BooleanValue.FALSE));
  }

  @Test
  void orShouldReturnTrueWhenRightOperandIsTrue() {
    assertEquals(BooleanValue.TRUE, LogicalOperations.or(BooleanValue.FALSE, BooleanValue.TRUE));
  }

  @Test
  void orShouldReturnFalseWhenBothOperandsAreFalse() {
    assertEquals(BooleanValue.FALSE, LogicalOperations.or(BooleanValue.FALSE, BooleanValue.FALSE));
  }

  @Test
  void notShouldReturnFalseWhenOperandIsTrue() {
    assertEquals(BooleanValue.FALSE, LogicalOperations.not(BooleanValue.TRUE));
  }

  @Test
  void notShouldReturnTrueWhenOperandIsFalse() {
    assertEquals(BooleanValue.TRUE, LogicalOperations.not(BooleanValue.FALSE));
  }

  @Test
  void andShouldRejectNonBooleanLeftOperand() {
    IValue value = new IntegerValue(10);
    assertThrows(ExpectedBooleanValueException.class, () -> LogicalOperations.and(value, BooleanValue.TRUE));
  }

  @Test
  void andShouldRejectNonBooleanRightOperand() {
    IValue value = new IntegerValue(10);
    assertThrows(ExpectedBooleanValueException.class, () -> LogicalOperations.and(BooleanValue.TRUE, value));
  }

  @Test
  void orShouldRejectNonBooleanLeftOperand() {
    IValue value = new IntegerValue(10);
    assertThrows(ExpectedBooleanValueException.class, () -> LogicalOperations.or(value, BooleanValue.TRUE));
  }

  @Test
  void orShouldRejectNonBooleanRightOperand() {
    IValue value = new IntegerValue(10);
    assertThrows(ExpectedBooleanValueException.class, () -> LogicalOperations.or(BooleanValue.TRUE, value));
  }

  @Test
  void notShouldRejectNonBooleanOperand() {
    IValue value = new IntegerValue(10);
    assertThrows(ExpectedBooleanValueException.class, () -> LogicalOperations.not(value));
  }
}