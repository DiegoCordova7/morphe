package dev.morphe.compiler.operator;

import dev.morphe.ast.operator.UnaryOperator;
import dev.morphe.bytecode.instructions.opcodes.ArithmeticOpCode;
import dev.morphe.bytecode.instructions.opcodes.IOpCode;
import dev.morphe.bytecode.instructions.opcodes.LogicalOpCode;

public final class UnaryOpCodeResolver {

  private UnaryOpCodeResolver() {}

  public static IOpCode resolve(UnaryOperator operator) {
    if (operator == null) throw new IllegalArgumentException("operator cannot be null");
    return switch (operator) {
      case MINUS -> ArithmeticOpCode.NEG;
      case NOT -> LogicalOpCode.NOT;
    };
  }
}