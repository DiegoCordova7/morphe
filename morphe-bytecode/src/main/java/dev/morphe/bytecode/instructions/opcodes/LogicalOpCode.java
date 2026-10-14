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
 * Logical bytecode operations supported by the Morphe VM.
 *
 * <p>
 * These instructions consume boolean values from the VM stack and push
 * the resulting boolean value onto the stack.
 * </p>
 */
public enum LogicalOpCode implements IOpCode {

  /**
   * Computes the logical conjunction of two boolean values.
   */
  AND(0),

  /**
   * Computes the logical disjunction of two boolean values.
   */
  OR(0),

  /**
   * Negates a boolean value.
   */
  NOT(0);

  private final int operandCount;

  LogicalOpCode(int operandCount) {
    this.operandCount = operandCount;
  }

  @Override
  public int operandCount() {
    return operandCount;
  }
}