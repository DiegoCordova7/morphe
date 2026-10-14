package dev.morphe.compiler.operator;

import dev.morphe.ast.operator.AssignmentOperator;
import dev.morphe.bytecode.instructions.opcodes.ArithmeticOpCode;

public final class ArithmeticOpCodeResolver {

  private ArithmeticOpCodeResolver() {}

  public static ArithmeticOpCode resolve(AssignmentOperator operator) {
    if (operator == null) throw new IllegalArgumentException("operator cannot be null");
    return switch (operator) {
      case ADD -> ArithmeticOpCode.ADD;
      case SUBTRACT -> ArithmeticOpCode.SUB;
      case MULTIPLY -> ArithmeticOpCode.MUL;
      case DIVIDE -> ArithmeticOpCode.DIV;
      case MODULO -> ArithmeticOpCode.MOD;
      case ASSIGN -> throw new IllegalArgumentException("ASSIGN is not an arithmetic operator");
    };
  }
}