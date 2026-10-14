package dev.morphe.semantic.type;

import dev.morphe.ast.type.*;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.type.InvalidTypeSymbolException;
import dev.morphe.semantic.exception.type.UnknownTypeException;
import dev.morphe.semantic.symbol.Symbol;
import dev.morphe.semantic.symbol.SymbolKind;
import dev.morphe.semantic.symbol.SymbolTable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class TypeResolverTest {

  @Test
  void shouldRejectNullSymbolTable() {
    NullSemanticArgumentException exception = assertThrows(
        NullSemanticArgumentException.class,
        () -> new TypeResolver(null)
    );
    assertEquals("Argument 'symbols' cannot be null", exception.getMessage());
  }

  @Test
  void shouldRejectNullType() {
    TypeResolver resolver = new TypeResolver(new SymbolTable());
    NullSemanticArgumentException exception = assertThrows(
        NullSemanticArgumentException.class,
        () -> resolver.resolve(null)
    );
    assertEquals("Argument 'type' cannot be null", exception.getMessage());
  }

  @Test
  void shouldReturnPrimitiveTypeUnchanged() {
    TypeResolver resolver = new TypeResolver(new SymbolTable());
    IASTType type = PrimitiveType.INTEGER;
    assertSame(type, resolver.resolve(type));
  }

  @Test
  void shouldReturnCompositeTypeUnchanged() {
    TypeResolver resolver = new TypeResolver(new SymbolTable());
    CompositeType type = new CompositeType("Person", List.of());
    assertSame(type, resolver.resolve(type));
  }

  @Test
  void shouldResolveNamedType() {
    SymbolTable symbols = new SymbolTable();
    TypeResolver resolver = new TypeResolver(symbols);
    CompositeType person = new CompositeType("Person", List.of());
    symbols.define(new Symbol("Person", SymbolKind.TYPE, person, false));
    IASTType resolved = resolver.resolve(new NamedType("Person"));
    assertSame(person, resolved);
  }

  @Test
  void shouldResolveNamedTypeFromParentScope() {
    SymbolTable symbols = new SymbolTable();
    TypeResolver resolver = new TypeResolver(symbols);
    CompositeType person = new CompositeType("Person", List.of());
    symbols.define(new Symbol("Person", SymbolKind.TYPE, person, false));
    symbols.enterScope(dev.morphe.semantic.scope.ScopeKind.FUNCTION);
    IASTType resolved = resolver.resolve(new NamedType("Person"));
    assertSame(person, resolved);
  }

  @Test
  void shouldThrowWhenNamedTypeDoesNotExist() {
    TypeResolver resolver = new TypeResolver(new SymbolTable());
    UnknownTypeException exception = assertThrows(
        UnknownTypeException.class,
        () -> resolver.resolve(new NamedType("Person"))
    );
    assertEquals("Unknown type 'Person'", exception.getMessage());
  }

  @Test
  void shouldThrowWhenNamedTypeResolvesToNonTypeSymbol() {
    SymbolTable symbols = new SymbolTable();
    TypeResolver resolver = new TypeResolver(symbols);
    symbols.define(new Symbol("Person", SymbolKind.VARIABLE, PrimitiveType.INTEGER, false));
    InvalidTypeSymbolException exception = assertThrows(
        InvalidTypeSymbolException.class,
        () -> resolver.resolve(new NamedType("Person"))
    );
    assertEquals("Symbol 'Person' is not a type", exception.getMessage());
  }

  @Test
  void shouldResolveNamedTypeInsideArray() {
    SymbolTable symbols = new SymbolTable();
    TypeResolver resolver = new TypeResolver(symbols);
    CompositeType person = new CompositeType("Person", List.of());
    symbols.define(new Symbol("Person", SymbolKind.TYPE, person, false));
    ArrayType type = new ArrayType(new NamedType("Person"));
    IASTType resolved = resolver.resolve(type);
    assertInstanceOf(ArrayType.class, resolved);
    ArrayType array = (ArrayType) resolved;
    assertSame(person, array.elementType());
  }

  @Test
  void shouldResolveNestedArrayTypes() {
    SymbolTable symbols = new SymbolTable();
    TypeResolver resolver = new TypeResolver(symbols);
    CompositeType person = new CompositeType("Person", List.of());
    symbols.define(new Symbol("Person", SymbolKind.TYPE, person, false));
    ArrayType type = new ArrayType(
        new ArrayType(
            new NamedType("Person")
        )
    );
    IASTType resolved = resolver.resolve(type);
    assertInstanceOf(ArrayType.class, resolved);
    ArrayType outer = (ArrayType) resolved;
    assertInstanceOf(ArrayType.class, outer.elementType());
    ArrayType inner = (ArrayType) outer.elementType();
    assertSame(person, inner.elementType());
  }

  @Test
  void shouldReturnArrayTypeUnchangedWhenElementTypeIsAlreadyResolved() {
    SymbolTable symbols = new SymbolTable();
    TypeResolver resolver = new TypeResolver(symbols);
    ArrayType type = new ArrayType(PrimitiveType.INTEGER);
    assertSame(type, resolver.resolve(type));
  }

  @Test
  void shouldResolveNamedTypesInsideFunction() {
    SymbolTable symbols = new SymbolTable();
    TypeResolver resolver = new TypeResolver(symbols);
    CompositeType person = new CompositeType("Person", List.of());
    CompositeType result = new CompositeType("Result", List.of());
    symbols.define(new Symbol("Person", SymbolKind.TYPE, person, false));
    symbols.define(new Symbol("Result", SymbolKind.TYPE, result, false));
    FunctionType type = new FunctionType(
        List.of(
            new NamedType("Person"),
            PrimitiveType.INTEGER
        ),
        new NamedType("Result")
    );
    IASTType resolved = resolver.resolve(type);
    assertInstanceOf(FunctionType.class, resolved);
    FunctionType function = (FunctionType) resolved;
    assertSame(person, function.parameterTypes().get(0));
    assertSame(PrimitiveType.INTEGER, function.parameterTypes().get(1));
    assertSame(result, function.returnType());
  }

  @Test
  void shouldReturnFunctionTypeUnchangedWhenTypesAreAlreadyResolved() {
    SymbolTable symbols = new SymbolTable();
    TypeResolver resolver = new TypeResolver(symbols);
    FunctionType type = new FunctionType(
        List.of(PrimitiveType.INTEGER),
        PrimitiveType.DOUBLE
    );
    assertSame(type, resolver.resolve(type));
  }
}