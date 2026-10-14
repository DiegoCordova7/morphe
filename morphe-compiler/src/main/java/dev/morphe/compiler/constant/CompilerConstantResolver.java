package dev.morphe.compiler.constant;

import dev.morphe.bytecode.constants.*;

public final class CompilerConstantResolver {

  private CompilerConstantResolver() {}

  public static IConstant resolve(Object value) {
    if (value instanceof Integer integer) return new IntegerConstant(integer);
    if (value instanceof Double doubleValue) return new DoubleConstant(doubleValue);
    if (value instanceof Boolean booleanValue) return new BooleanConstant(booleanValue);
    if (value instanceof String string) return new StringConstant(string);
    throw new IllegalStateException("Unsupported literal value: " + value);
  }
}