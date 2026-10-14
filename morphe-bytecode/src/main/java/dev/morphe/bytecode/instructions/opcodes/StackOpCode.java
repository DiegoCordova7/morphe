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
 * Stack-related bytecode operations supported by the Morphe VM.
 *
 * <p>
 * These instructions manipulate values on the VM operand stack. Some
 * operations use immediate operands to reference entries in the constant
 * pool or memory locations.
 * </p>
 */
public enum StackOpCode implements IOpCode {

  /**
   * Pushes onto the stack the constant identified by the operand.
   */
  PUSH(1),

  /**
   * Removes the value at the top of the stack.
   */
  POP(0),

  /**
   * Copies the second value from the top of the stack onto the top.
   */
  OVER(0),

  /**
   * Duplicates the value at the top of the stack.
   */
  DUP(0),

  /**
   * Duplicates the top two values of the stack.
   */
  DUP2(0),

  /**
   * Swaps the two values at the top of the stack.
   */
  SWAP(0),

  /**
   * Rotates the top three values of the stack.
   */
  ROT(0);

  private final int operandCount;

  StackOpCode(int operandCount) {
    this.operandCount = operandCount;
  }

  @Override
  public int operandCount() {
    return operandCount;
  }
}