/*
 * Copyright 2026 DiegoCordova
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.morphe.bytecode.instructions.opcodes;

/**
 * Arithmetic bytecode operations supported by the Morphe VM.
 *
 * <p>
 * These opcodes consume numeric values from the VM stack and produce
 * a numeric result according to the operation performed.
 * </p>
 */
public enum ArithmeticOpCode implements IOpCode {
  /**
   * Adds the two topmost numeric values on the VM stack.
   */
  ADD(0),

  /**
   * Subtracts the topmost numeric value from the preceding value on the VM
   * stack.
   */
  SUB(0),

  /**
   * Multiplies the two topmost numeric values on the VM stack.
   */
  MUL(0),

  /**
   * Divides the preceding numeric value on the VM stack by the topmost
   * numeric value.
   */
  DIV(0),

  /**
   * Computes the remainder of the preceding numeric value on the VM stack
   * divided by the topmost numeric value.
   */
  MOD(0),

  /**
   * Negates the topmost numeric value on the VM stack.
   */
  NEG(0);

  private final int operandCount;

  ArithmeticOpCode(final int operandCount) {
    this.operandCount = operandCount;
  }

  @Override
  public int operandCount() {
    return operandCount;
  }
}