package dev.morphe.vm.constant;

import dev.morphe.bytecode.constants.*;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.runtime.value.primitive.StringValue;
import dev.morphe.runtime.value.primitive.numeric.DoubleValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.internal.constant.InvalidConstantException;

/**
 * Resolves bytecode constants into their corresponding runtime values.
 *
 * <p>Bytecode constants represent immutable literal data stored in a
 * {@code BytecodeProgram} constant pool. Runtime values represent the
 * executable form of that data used by the virtual machine during
 * execution.</p>
 *
 * <p>This resolver is responsible for converting supported implementations
 * of {@link IConstant} into their corresponding {@link IValue} instances.</p>
 *
 * <p>Supported constant types include:</p>
 *
 * <ul>
 *   <li>{@link IntegerConstant} → {@link IntegerValue}</li>
 *   <li>{@link DoubleConstant} → {@link DoubleValue}</li>
 *   <li>{@link StringConstant} → {@link StringValue}</li>
 *   <li>{@link BooleanConstant} → {@link BooleanValue}</li>
 * </ul>
 *
 * <p>The resolver does not modify the constant itself. Each invocation
 * produces a runtime value representing the constant's contents.</p>
 */
public final class ConstantResolver {

  /**
   * Prevents instantiation of this utility class.
   */
  private ConstantResolver() {}

  /**
   * Resolves a bytecode constant into its corresponding runtime value.
   *
   * @param constant the bytecode constant to resolve
   * @return the runtime value represented by the constant
   * @throws NullArgumentException if {@code constant} is {@code null}
   * @throws InvalidConstantException if the constant type is not supported
   */
  public static IValue resolve(IConstant constant) {
    if (constant == null) throw new NullArgumentException("constant");
    if (constant instanceof IntegerConstant integer) return new IntegerValue(integer.value());
    if (constant instanceof DoubleConstant decimal) return new DoubleValue(decimal.value());
    if (constant instanceof StringConstant string) return new StringValue(string.value());
    if (constant instanceof BooleanConstant bool) return new BooleanValue(bool.value());
    throw new InvalidConstantException(constant);
  }
}