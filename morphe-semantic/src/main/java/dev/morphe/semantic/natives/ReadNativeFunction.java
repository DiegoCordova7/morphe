package dev.morphe.semantic.natives;

import dev.morphe.ast.type.FunctionType;
import dev.morphe.ast.type.PrimitiveType;

import java.util.List;

public final class ReadNativeFunction implements INativeFunction {

  private final String name;
  private final PrimitiveType returnType;

  public ReadNativeFunction(String name, PrimitiveType returnType) {
    this.name = name;
    this.returnType = returnType;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public FunctionType type() {
    return new FunctionType(List.of(), returnType);
  }
}