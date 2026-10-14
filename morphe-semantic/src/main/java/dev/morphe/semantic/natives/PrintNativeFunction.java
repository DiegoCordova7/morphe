package dev.morphe.semantic.natives;

import dev.morphe.ast.type.AnyType;
import dev.morphe.ast.type.FunctionType;
import dev.morphe.ast.type.PrimitiveType;

import java.util.List;

/**
 * Represents Morphe's native {@code print} function.
 *
 * <p>The function accepts exactly one argument of any value type and
 * produces no value.</p>
 */
public final class PrintNativeFunction implements INativeFunction {

  @Override
  public String name() {
    return "print";
  }

  @Override
  public FunctionType type() {
    return new FunctionType(List.of(AnyType.ANY), PrimitiveType.VOID);
  }
}