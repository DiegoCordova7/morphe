package dev.morphe.bytecode.instructions;

import dev.morphe.bytecode.exception.argument.InvalidArgumentException;
import dev.morphe.bytecode.exception.argument.InvalidIndexException;
import dev.morphe.bytecode.exception.argument.NullArgumentException;
import dev.morphe.bytecode.instructions.opcodes.IOpCode;

import java.util.Arrays;

/**
 * Represents a single bytecode instruction.
 *
 * <p>
 * An instruction consists of an {@link IOpCode opcode} and zero or more
 * integer operands. The opcode defines the operation to perform, while
 * operands provide the data required by that operation.
 * </p>
 *
 * <p>
 * Instructions are immutable. Operands are defensively copied during
 * construction and when accessed.
 * </p>
 *
 * @param <T> the opcode type associated with this instruction
 */
public final class Instruction<T extends IOpCode> {

  private final T opcode;
  private final int[] operands;

  /**
   * Creates a new instruction with the specified opcode and operands.
   *
   * @param opcode the opcode that identifies the operation
   * @param operands the operands associated with the instruction
   *
   * @throws NullArgumentException if {@code opcode} or {@code operands}
   * is {@code null}
   * @throws InvalidArgumentException if the number of operands does not
   * match the opcode's expected operand count
   */
  public Instruction(T opcode, int... operands) {
    if (opcode == null) throw new NullArgumentException("opcode");
    if (operands == null) throw new NullArgumentException("operands");
    int[] safeOperands = operands.clone();
    int expectedOperands = opcode.operandCount();
    if (safeOperands.length != expectedOperands) throw new InvalidArgumentException(
        "operands", String.format(
            "%s expects %d operands, but received %d", opcode, expectedOperands, safeOperands.length
    ));
    this.opcode = opcode;
    this.operands = safeOperands;
  }

  /**
   * Returns the opcode of this instruction.
   *
   * @return the instruction opcode
   */
  public T opcode() {
    return opcode;
  }

  /**
   * Returns a copy of the operands associated with this instruction.
   *
   * @return a defensive copy of the operands array
   */
  public int[] operands() {
    return operands.clone();
  }

  /**
   * Returns the operand at the specified position.
   *
   * @param index the operand index
   * @return the operand value
   *
   * @throws InvalidIndexException if the index is invalid
   */
  public int operand(int index) {
    validateOperandIndex(index);
    return operands[index];
  }

  /**
   * Returns a new instruction with one operand replaced.
   *
   * @param index the operand index to replace
   * @param value the new operand value
   * @return a new instruction with the updated operand
   *
   * @throws InvalidIndexException if the index is invalid
   */
  public Instruction<T> withOperand(int index, int value) {
    validateOperandIndex(index);
    int[] copy = operands.clone();
    copy[index] = value;
    return new Instruction<>(opcode, copy);
  }

  /**
   * Returns a string representation of the instruction.
   *
   * @return a string containing the opcode and operands
   */
  @Override
  public String toString() {
    return opcode + " " + Arrays.toString(operands);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof Instruction<?> other)) return false;
    return opcode.equals(other.opcode) && Arrays.equals(operands, other.operands);
  }

  @Override
  public int hashCode() {
    int result = opcode.hashCode();
    result = 31 * result + Arrays.hashCode(operands);
    return result;
  }

  private void validateOperandIndex(int index) {
    if (index < 0 || index >= operands.length) throw new InvalidIndexException("operand array", index, operands.length);
  }
}