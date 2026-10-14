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
 * Control-flow bytecode operations supported by the Morphe VM.
 *
 * <p>
 * Jump instructions use an immediate operand containing the target
 * instruction index. Conditional jumps consume the condition from the
 * VM stack.
 * </p>
 */
public enum ControlOpCode implements IOpCode {

  /**
   * Terminates program execution.
   */
  HALT(0),

  /**
   * Transfers execution unconditionally to the instruction whose index
   * is specified by the operand.
   */
  JMP(1),

  /**
   * Transfers execution to the instruction whose index is specified by
   * the operand when the condition at the top of the stack is false.
   */
  JMP_IF_FALSE(1),

  /**
   * Transfers execution to the instruction whose index is specified by
   * the operand when the condition at the top of the stack is true.
   */
  JMP_IF_TRUE(1);

  private final int operandCount;

  ControlOpCode(int operandCount) {
    this.operandCount = operandCount;
  }

  @Override
  public int operandCount() {
    return operandCount;
  }
}