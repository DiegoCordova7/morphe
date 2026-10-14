package dev.morphe.vm.operations;

import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.StringValue;
import dev.morphe.runtime.value.primitive.numeric.DoubleValue;
import dev.morphe.runtime.value.primitive.numeric.INumericValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.execution.DivisionByZeroException;
import dev.morphe.vm.exception.execution.ModuleByZeroException;

/**
 * Provides arithmetic operations for numeric runtime values.
 *
 * <p>
 * Arithmetic operations produce runtime numeric values according to the
 * semantics of each operation.
 * </p>
 *
 * <p>
 * Addition also supports string concatenation. When either operand is a
 * {@link StringValue}, both operands are converted to their textual
 * representation and concatenated.
 * </p>
 */
public final class ArithmeticOperations {

  private ArithmeticOperations() {}

  /**
   * Creates a numeric result while preserving the integer representation
   * when both operands are integers.
   *
   * @param left the left operand
   * @param right the right operand
   * @param value the computed numeric result
   * @return an integer value if both operands are integers; otherwise,
   *         a double value
   */
  private static INumericValue fromResult(INumericValue left, INumericValue right, double value) {
    if (left.isInteger() && right.isInteger()) return new IntegerValue((int) value);
    return new DoubleValue(value);
  }

  /**
   * Adds two numeric values.
   *
   * @param left the left operand
   * @param right the right operand
   * @return the sum of the two operands
   */
  public static INumericValue add(INumericValue left, INumericValue right) {
    return fromResult(left, right, left.doubleValue() + right.doubleValue());
  }

  /**
   * Concatenates two runtime values into a string.
   *
   * <p>
   * The textual representation of each operand is obtained through
   * {@link Object#toString()}.
   * </p>
   *
   * @param left the left operand
   * @param right the right operand
   * @return the concatenated string value
   *
   * @throws NullArgumentException if either operand is {@code null}
   */
  public static StringValue concat(IValue left, IValue right) {
    if (left == null) throw new NullArgumentException("left");
    if (right == null) throw new NullArgumentException("right");
    return new StringValue(left.toString() + right.toString());
  }

  /**
   * Subtracts the right operand from the left operand.
   *
   * @param left the left operand
   * @param right the right operand
   * @return the result of subtracting {@code right} from {@code left}
   */
  public static INumericValue sub(INumericValue left, INumericValue right) {
    return fromResult(left, right, left.doubleValue() - right.doubleValue());
  }

  /**
   * Multiplies two numeric values.
   *
   * @param left the left operand
   * @param right the right operand
   * @return the product of the two operands
   */
  public static INumericValue mul(INumericValue left, INumericValue right) {
    return fromResult(left, right, left.doubleValue() * right.doubleValue());
  }

  /**
   * Divides the left operand by the right operand.
   *
   * <p>
   * Division always produces a {@link DoubleValue}.
   * </p>
   *
   * @param left the dividend
   * @param right the divisor
   * @return the quotient represented as a {@link DoubleValue}
   *
   * @throws DivisionByZeroException if the right operand is zero
   */
  public static INumericValue div(INumericValue left, INumericValue right) {
    double leftValue = left.doubleValue();
    double rightValue = right.doubleValue();
    if (rightValue == 0.0) throw new DivisionByZeroException(leftValue, rightValue);
    return new DoubleValue(leftValue / rightValue);
  }

  /**
   * Calculates the remainder of dividing the left operand by the right operand.
   *
   * <p>
   * The modulo operation always produces a {@link DoubleValue}.
   * </p>
   *
   * @param left the dividend
   * @param right the divisor
   * @return the remainder represented as a {@link DoubleValue}
   *
   * @throws ModuleByZeroException if the right operand is zero
   */
  public static INumericValue mod(INumericValue left, INumericValue right) {
    double leftValue = left.doubleValue();
    double rightValue = right.doubleValue();
    if (rightValue == 0.0) throw new ModuleByZeroException(leftValue, rightValue);
    return new DoubleValue(leftValue % rightValue);
  }

  /**
   * Negates a numeric value.
   *
   * <p>
   * The returned value preserves the numeric representation of the operand.
   * </p>
   *
   * @param value the numeric value to negate
   * @return the negated value
   *
   * @throws IllegalStateException if the numeric value is not a supported
   *         runtime numeric implementation
   */
  public static INumericValue neg(INumericValue value) {
    if (value instanceof IntegerValue integer) return new IntegerValue(-integer.value());
    if (value instanceof DoubleValue doubleValue) return new DoubleValue(-doubleValue.value());
    throw new IllegalStateException("Unsupported numeric value: " + value.getClass().getSimpleName());
  }
}