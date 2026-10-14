package dev.morphe.semantic.type;

import dev.morphe.ast.type.NullType;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.semantic.exception.type.UnsupportedLiteralTypeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LiteralTypeResolverTest {

  private final LiteralTypeResolver resolver = new LiteralTypeResolver();

  @Test
  void resolvesIntegerLiteral() {
    assertSame(PrimitiveType.INTEGER, resolver.resolve(42));
  }

  @Test
  void resolvesDoubleLiteral() {
    assertSame(PrimitiveType.DOUBLE, resolver.resolve(42.0));
  }

  @Test
  void resolvesStringLiteral() {
    assertSame(PrimitiveType.STRING, resolver.resolve("hello"));
  }

  @Test
  void resolvesBooleanLiteral() {
    assertSame(PrimitiveType.BOOLEAN, resolver.resolve(true));
  }

  @Test
  void resolvesNullLiteral() {
    assertSame(NullType.NULL, resolver.resolve(null));
  }

  @Test
  void rejectsUnsupportedLiteralType() {
    assertThrows(UnsupportedLiteralTypeException.class, () -> resolver.resolve(new Object()));
  }
}