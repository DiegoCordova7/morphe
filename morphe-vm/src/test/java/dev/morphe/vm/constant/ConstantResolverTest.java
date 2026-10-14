package dev.morphe.vm.constant;

import dev.morphe.bytecode.constants.*;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.*;
import dev.morphe.runtime.value.primitive.numeric.*;
import dev.morphe.vm.exception.argument.NullArgumentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstantResolverTest {

  @Test
  void shouldResolveIntegerConstant() {
    IValue result = ConstantResolver.resolve(new IntegerConstant(42));
    assertInstanceOf(IntegerValue.class, result);
    assertEquals(new IntegerValue(42), result);
  }

  @Test
  void shouldResolveDoubleConstant() {
    IValue result = ConstantResolver.resolve(new DoubleConstant(42.5));
    assertInstanceOf(DoubleValue.class, result);
    assertEquals(new DoubleValue(42.5), result);
  }

  @Test
  void shouldResolveStringConstant() {
    IValue result = ConstantResolver.resolve(new StringConstant("Morphe"));
    assertInstanceOf(StringValue.class, result);
    assertEquals(new StringValue("Morphe"), result);
  }

  @Test
  void shouldResolveBooleanConstant() {
    IValue result = ConstantResolver.resolve(new BooleanConstant(true));
    assertInstanceOf(BooleanValue.class, result);
    assertEquals(new BooleanValue(true), result);
  }

  @Test
  void shouldResolveFalseBooleanConstant() {
    IValue result = ConstantResolver.resolve(new BooleanConstant(false));
    assertInstanceOf(BooleanValue.class, result);
    assertEquals(new BooleanValue(false), result);
  }

  @Test
  void shouldRejectNullConstant() {
    assertThrows(NullArgumentException.class, () -> ConstantResolver.resolve(null));
  }

  @Test
  void shouldCreateNewRuntimeValueOnEachResolution() {
    IntegerConstant constant = new IntegerConstant(42);
    IValue first = ConstantResolver.resolve(constant);
    IValue second = ConstantResolver.resolve(constant);
    assertNotSame(first, second);
    assertEquals(first, second);
  }
}