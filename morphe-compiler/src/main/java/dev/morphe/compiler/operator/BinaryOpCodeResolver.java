package dev.morphe.compiler.operator;

import dev.morphe.ast.operator.BinaryOperator;
import dev.morphe.bytecode.instructions.opcodes.*;

public final class BinaryOpCodeResolver {

  private BinaryOpCodeResolver() {}

  public static IOpCode resolve(BinaryOperator operator) {
    if (operator == null) throw new IllegalArgumentException("operator cannot be null");
    return switch (operator) {
      case ADD -> ArithmeticOpCode.ADD;
      case SUBTRACT -> ArithmeticOpCode.SUB;
      case MULTIPLY -> ArithmeticOpCode.MUL;
      case DIVIDE -> ArithmeticOpCode.DIV;
      case MODULO -> ArithmeticOpCode.MOD;

      case EQUAL -> ComparisonOpCode.EQ;
      case NOT_EQUAL -> ComparisonOpCode.NEQ;
      case STRUCT_MATCH -> CompositeOpCode.STRUCT_MATCH;
      case GREATER -> ComparisonOpCode.GT;
      case GREATER_OR_EQUAL -> ComparisonOpCode.GTE;
      case LESS -> ComparisonOpCode.LT;
      case LESS_OR_EQUAL -> ComparisonOpCode.LTE;

      case AND -> LogicalOpCode.AND;
      case OR -> LogicalOpCode.OR;
    };
  }
}