package dev.morphe.compiler.operator;

import dev.morphe.ast.operator.UpdateOperator;
import dev.morphe.bytecode.instructions.opcodes.ArithmeticOpCode;

public final class UpdateOpCodeResolver {

  private UpdateOpCodeResolver() {}

  public static ArithmeticOpCode resolve(UpdateOperator operator) {
    if (operator == null) throw new IllegalArgumentException("operator cannot be null");
    return switch (operator) {
      case INCREMENT -> ArithmeticOpCode.ADD;
      case DECREMENT -> ArithmeticOpCode.SUB;
    };
  }
}