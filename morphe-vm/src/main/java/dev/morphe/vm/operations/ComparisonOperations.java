package dev.morphe.vm.operations;

import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.runtime.value.primitive.numeric.INumericValue;
import dev.morphe.runtime.value.reference.ReferenceValue;
import dev.morphe.vm.ExecutionContext;
import dev.morphe.vm.exception.internal.type.ExpectedNumericValueException;

/**
 * Provides comparison operations for Morphe runtime values.
 *
 * <p>
 * Equality comparisons operate on the runtime equality semantics defined by
 * {@link Object#equals(Object)}. Relational comparisons require both operands
 * to be numeric values.
 * </p>
 */
public final class ComparisonOperations {

  /**
   * Prevents instantiation of this utility class.
   */
  private ComparisonOperations() {}

  /**
   * Compares two runtime values for equality.
   *
   * @param left the left operand
   * @param right the right operand
   * @return {@link BooleanValue#TRUE} if both values are equal;
   *     {@link BooleanValue#FALSE} otherwise
   */
  public static BooleanValue eq(ExecutionContext context, IValue left, IValue right) {
    IValue resolvedLeft = resolve(context, left);
    IValue resolvedRight = resolve(context, right);
    return BooleanValue.of(resolvedLeft.equals(resolvedRight));
  }

  private static IValue resolve(ExecutionContext context, IValue value) {
    if (value instanceof ReferenceValue reference) return context.heap().load(reference.address());
    return value;
  }

  /**
   * Compares two runtime values for inequality.
   *
   * @param left the left operand
   * @param right the right operand
   * @return {@link BooleanValue#TRUE} if both values are not equal;
   *     {@link BooleanValue#FALSE} otherwise
   */
  public static BooleanValue neq(ExecutionContext context, IValue left, IValue right) {
    IValue resolvedLeft = resolve(context, left);
    IValue resolvedRight = resolve(context, right);
    return BooleanValue.of(!resolvedLeft.equals(resolvedRight));
  }

  /**
   * Determines whether the left numeric operand is less than the right
   * numeric operand.
   *
   * @param left the left operand
   * @param right the right operand
   * @return {@link BooleanValue#TRUE} if {@code left < right};
   *     {@link BooleanValue#FALSE} otherwise
   * @throws ExpectedNumericValueException if either operand is not numeric
   */
  public static BooleanValue lt(IValue left, IValue right) {
    INumericValue numberLeft = requireNumeric(left);
    INumericValue numberRight = requireNumeric(right);
    return BooleanValue.of(numberLeft.doubleValue() < numberRight.doubleValue());
  }

  /**
   * Determines whether the left numeric operand is less than or equal to
   * the right numeric operand.
   *
   * @param left the left operand
   * @param right the right operand
   * @return {@link BooleanValue#TRUE} if {@code left <= right};
   *     {@link BooleanValue#FALSE} otherwise
   * @throws ExpectedNumericValueException if either operand is not numeric
   */
  public static BooleanValue lte(IValue left, IValue right) {
    INumericValue numberLeft = requireNumeric(left);
    INumericValue numberRight = requireNumeric(right);
    return BooleanValue.of(numberLeft.doubleValue() <= numberRight.doubleValue());
  }

  /**
   * Determines whether the left numeric operand is greater than the right
   * numeric operand.
   *
   * @param left the left operand
   * @param right the right operand
   * @return {@link BooleanValue#TRUE} if {@code left > right};
   *     {@link BooleanValue#FALSE} otherwise
   * @throws ExpectedNumericValueException if either operand is not numeric
   */
  public static BooleanValue gt(IValue left, IValue right) {
    INumericValue numberLeft = requireNumeric(left);
    INumericValue numberRight = requireNumeric(right);
    return BooleanValue.of(numberLeft.doubleValue() > numberRight.doubleValue());
  }

  /**
   * Determines whether the left numeric operand is greater than or equal to
   * the right numeric operand.
   *
   * @param left the left operand
   * @param right the right operand
   * @return {@link BooleanValue#TRUE} if {@code left >= right};
   *     {@link BooleanValue#FALSE} otherwise
   * @throws ExpectedNumericValueException if either operand is not numeric
   */
  public static BooleanValue gte(IValue left, IValue right) {
    INumericValue numberLeft = requireNumeric(left);
    INumericValue numberRight = requireNumeric(right);
    return BooleanValue.of(numberLeft.doubleValue() >= numberRight.doubleValue());
  }

  /**
   * Requires the specified runtime value to be numeric.
   *
   * @param value the runtime value to validate
   * @return the value as an {@link INumericValue}
   * @throws ExpectedNumericValueException if the value is not numeric
   */
  private static INumericValue requireNumeric(IValue value) {
    if (!(value instanceof INumericValue numeric)) throw new ExpectedNumericValueException(value);
    return numeric;
  }
}