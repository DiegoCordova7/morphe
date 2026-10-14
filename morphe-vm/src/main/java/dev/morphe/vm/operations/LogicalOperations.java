package dev.morphe.vm.operations;

import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.vm.exception.internal.type.ExpectedBooleanValueException;

/**
 * Provides runtime semantics for logical operations.
 *
 * <p>
 * Logical operations operate exclusively on {@link BooleanValue} instances
 * and produce {@link BooleanValue} results.
 * </p>
 *
 * <p>
 * This class is responsible only for the semantic evaluation of logical
 * operations. Operand extraction and instruction execution are handled by
 * the corresponding VM executor.
 * </p>
 *
 * <p>
 * Supported operations include logical conjunction, logical disjunction,
 * and logical negation.
 * </p>
 */
public final class LogicalOperations {

  /**
   * Prevents instantiation of this utility class.
   */
  private LogicalOperations() {}

  /**
   * Performs a logical conjunction of two Boolean values.
   *
   * <p>
   * The result is {@code true} only when both operands are {@code true}.
   * </p>
   *
   * @param left the left Boolean operand
   * @param right the right Boolean operand
   * @return {@link BooleanValue#TRUE} if both operands are true; {@link BooleanValue#FALSE} otherwise
   * @throws ExpectedBooleanValueException if either operand is not a {@link BooleanValue}
   */
  public static BooleanValue and(IValue left, IValue right) {

    BooleanValue booleanLeft = requireBoolean(left);
    BooleanValue booleanRight = requireBoolean(right);
    return BooleanValue.of(booleanLeft.value() && booleanRight.value());
  }

  /**
   * Performs a logical disjunction of two Boolean values.
   *
   * <p>
   * The result is {@code true} when at least one operand is {@code true}.
   * </p>
   *
   * @param left the left Boolean operand
   * @param right the right Boolean operand
   * @return {@link BooleanValue#TRUE} if at least one operand is true; {@link BooleanValue#FALSE} otherwise
   * @throws ExpectedBooleanValueException if either operand is not a {@link BooleanValue}
   */
  public static BooleanValue or(IValue left, IValue right) {
    BooleanValue booleanLeft = requireBoolean(left);
    BooleanValue booleanRight = requireBoolean(right);
    return BooleanValue.of(booleanLeft.value() || booleanRight.value());
  }

  /**
   * Performs logical negation of a Boolean value.
   *
   * <p>
   * The result is {@code true} when the operand is {@code false}, and
   * {@code false} when the operand is {@code true}.
   * </p>
   *
   * @param value the Boolean operand to negate
   * @return {@link BooleanValue#FALSE} if the operand is true; {@link BooleanValue#TRUE} if the operand is false
   * @throws ExpectedBooleanValueException if {@code value} is not a {@link BooleanValue}
   */
  public static BooleanValue not(IValue value) {
    BooleanValue booleanValue = requireBoolean(value);
    return BooleanValue.of(!booleanValue.value());
  }

  /**
   * Validates that a runtime value is a Boolean value.
   *
   * @param value the runtime value to validate
   * @return the value cast to {@link BooleanValue}
   * @throws ExpectedBooleanValueException if {@code value} is not a {@link BooleanValue}
   */
  private static BooleanValue requireBoolean(IValue value) {
    if (!(value instanceof BooleanValue booleanValue)) throw new ExpectedBooleanValueException(value);
    return booleanValue;
  }
}