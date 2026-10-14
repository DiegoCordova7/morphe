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
 * Comparison bytecode operations supported by the Morphe VM.
 *
 * <p>
 * These instructions consume two values from the VM stack, compare them,
 * and push the resulting boolean value onto the stack.
 * </p>
 */
public enum ComparisonOpCode implements IOpCode {

  /**
   * Tests whether two values are equal.
   */
  EQ(0),

  /**
   * Tests whether two values are not equal.
   */
  NEQ(0),

  /**
   * Tests whether the first value is greater than the second.
   */
  GT(0),

  /**
   * Tests whether the first value is greater than or equal to the second.
   */
  GTE(0),

  /**
   * Tests whether the first value is less than the second.
   */
  LT(0),

  /**
   * Tests whether the first value is less than or equal to the second.
   */
  LTE(0);

  private final int operandCount;

  ComparisonOpCode(int operandCount) {
    this.operandCount = operandCount;
  }

  @Override
  public int operandCount() {
    return operandCount;
  }
}