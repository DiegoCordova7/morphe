package dev.morphe.runtime.type.callable;

import dev.morphe.runtime.environment.ClosureEnvironment;
import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.kind.TypeKind;
import dev.morphe.runtime.type.primitive.BooleanType;
import dev.morphe.runtime.type.primitive.IntegerType;
import dev.morphe.runtime.type.primitive.StringType;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.callable.FunctionValue;
import dev.morphe.runtime.value.primitive.BooleanValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FunctionTypeTest {

  @Test
  void rejectsNullParameterTypes() {
    assertThrows(
        NullArgumentException.class,
        () -> new FunctionType(null, IntegerType.INSTANCE)
    );
  }

  @Test
  void rejectsNullReturnType() {
    assertThrows(
        NullArgumentException.class,
        () -> new FunctionType(List.of(IntegerType.INSTANCE), null)
    );
  }

  @Test
  void acceptsEmptyParameterList() {
    FunctionType type = new FunctionType(
        List.of(),
        IntegerType.INSTANCE
    );
    assertTrue(type.parameterTypes().isEmpty());
    assertEquals(IntegerType.INSTANCE, type.returnType());
  }

  @Test
  void preservesParameterOrder() {
    FunctionType type = new FunctionType(
        List.of(IntegerType.INSTANCE, StringType.INSTANCE, BooleanType.INSTANCE),
        IntegerType.INSTANCE
    );
    assertEquals(
        List.of(IntegerType.INSTANCE, StringType.INSTANCE, BooleanType.INSTANCE),
        type.parameterTypes()
    );
  }

  @Test
  void returnsFunctionName() {
    FunctionType type = new FunctionType(
        List.of(IntegerType.INSTANCE),
        IntegerType.INSTANCE
    );
    assertEquals("Function", type.name());
  }

  @Test
  void returnsFunctionKind() {
    FunctionType type = new FunctionType(
        List.of(IntegerType.INSTANCE),
        IntegerType.INSTANCE
    );
    assertEquals(TypeKind.FUNCTION, type.kind());
  }

  @Test
  void acceptsFunctionValue() {
    FunctionType type = new FunctionType(
        List.of(IntegerType.INSTANCE),
        IntegerType.INSTANCE
    );
    IValue value = new FunctionValue(0, new ClosureEnvironment());
    assertTrue(type.accepts(value));
  }

  @Test
  void rejectsNonFunctionValue() {
    FunctionType type = new FunctionType(
        List.of(IntegerType.INSTANCE),
        IntegerType.INSTANCE
    );
    assertFalse(type.accepts(BooleanValue.TRUE));
  }

  @Test
  void rejectsNullValue() {
    FunctionType type = new FunctionType(
        List.of(IntegerType.INSTANCE),
        IntegerType.INSTANCE
    );
    assertFalse(type.accepts(null));
  }

  @Test
  void considersSameSignaturesEqual() {
    FunctionType first = new FunctionType(
        List.of(IntegerType.INSTANCE, StringType.INSTANCE),
        BooleanType.INSTANCE
    );
    FunctionType second = new FunctionType(
        List.of(IntegerType.INSTANCE, StringType.INSTANCE),
        BooleanType.INSTANCE
    );
    assertEquals(first, second);
  }

  @Test
  void considersDifferentParameterTypesNotEqual() {
    FunctionType first = new FunctionType(
        List.of(IntegerType.INSTANCE),
        BooleanType.INSTANCE
    );
    FunctionType second = new FunctionType(
        List.of(StringType.INSTANCE),
        BooleanType.INSTANCE
    );
    assertNotEquals(first, second);
  }

  @Test
  void considersDifferentParameterOrderNotEqual() {
    FunctionType first = new FunctionType(
        List.of(IntegerType.INSTANCE, StringType.INSTANCE),
        BooleanType.INSTANCE
    );
    FunctionType second = new FunctionType(
        List.of(StringType.INSTANCE, IntegerType.INSTANCE),
        BooleanType.INSTANCE
    );
    assertNotEquals(first, second);
  }

  @Test
  void considersDifferentReturnTypesNotEqual() {
    FunctionType first = new FunctionType(
        List.of(IntegerType.INSTANCE),
        BooleanType.INSTANCE
    );
    FunctionType second = new FunctionType(
        List.of(IntegerType.INSTANCE),
        StringType.INSTANCE
    );
    assertNotEquals(first, second);
  }

  @Test
  void equalFunctionTypesHaveSameHashCode() {
    FunctionType first = new FunctionType(
        List.of(IntegerType.INSTANCE, StringType.INSTANCE),
        BooleanType.INSTANCE
    );
    FunctionType second = new FunctionType(
        List.of(IntegerType.INSTANCE, StringType.INSTANCE),
        BooleanType.INSTANCE
    );
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void returnsFunctionSignature() {
    FunctionType type = new FunctionType(
        List.of(IntegerType.INSTANCE, StringType.INSTANCE),
        BooleanType.INSTANCE
    );
    assertEquals("(Integer, String) -> Boolean", type.toString());
  }

  @Test
  void returnsFunctionSignatureWithoutParameters() {
    FunctionType type = new FunctionType(List.of(), BooleanType.INSTANCE);
    assertEquals("() -> Boolean", type.toString());
  }
}