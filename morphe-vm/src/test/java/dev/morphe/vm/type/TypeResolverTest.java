package dev.morphe.vm.type;

import dev.morphe.bytecode.type.ArrayBytecodeType;
import dev.morphe.bytecode.type.CompositeBytecodeType;
import dev.morphe.bytecode.type.IBytecodeType;
import dev.morphe.bytecode.type.PrimitiveBytecodeType;
import dev.morphe.runtime.type.IType;
import dev.morphe.runtime.type.collection.ArrayType;
import dev.morphe.runtime.type.composite.CompositeType;
import dev.morphe.runtime.type.primitive.*;
import dev.morphe.vm.exception.argument.NullArgumentException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TypeResolverTest {

  @Test
  void shouldResolveIntegerType() {
    assertSame(IntegerType.INSTANCE, TypeResolver.resolve(PrimitiveBytecodeType.INTEGER));
  }

  @Test
  void shouldResolveDoubleType() {
    assertSame(DoubleType.INSTANCE, TypeResolver.resolve(PrimitiveBytecodeType.DOUBLE));
  }

  @Test
  void shouldResolveBooleanType() {
    assertSame(BooleanType.INSTANCE, TypeResolver.resolve(PrimitiveBytecodeType.BOOLEAN));
  }

  @Test
  void shouldResolveStringType() {
    assertSame(StringType.INSTANCE, TypeResolver.resolve(PrimitiveBytecodeType.STRING));
  }

  @Test
  void shouldResolveArrayType() {
    IType result = TypeResolver.resolve(new ArrayBytecodeType(PrimitiveBytecodeType.INTEGER));
    assertInstanceOf(ArrayType.class, result);
    ArrayType arrayType = (ArrayType) result;
    assertEquals(IntegerType.INSTANCE, arrayType.elementType());
  }

  @Test
  void shouldResolveNestedArrayType() {
    IBytecodeType nestedType = new ArrayBytecodeType(new ArrayBytecodeType(PrimitiveBytecodeType.INTEGER));
    IType result = TypeResolver.resolve(nestedType);
    assertInstanceOf(ArrayType.class, result);
    ArrayType outer = (ArrayType) result;
    assertInstanceOf(ArrayType.class, outer.elementType());
    ArrayType inner = (ArrayType) outer.elementType();
    assertEquals(IntegerType.INSTANCE, inner.elementType());
  }

  @Test
  void shouldResolveCompositeType() {
    CompositeBytecodeType bytecodeType = new CompositeBytecodeType("Person", List.of());
    IType result = TypeResolver.resolve(bytecodeType);
    assertInstanceOf(CompositeType.class, result);
    CompositeType composite = (CompositeType) result;
    assertEquals("Person", composite.name());
    assertTrue(composite.fields().isEmpty());
  }

  @Test
  void shouldRejectNullType() {
    assertThrows(NullArgumentException.class, () -> TypeResolver.resolve(null));
  }
}