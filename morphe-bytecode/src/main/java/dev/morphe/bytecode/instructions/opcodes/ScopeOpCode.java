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
 * Scope-related bytecode operations supported by the Morphe VM.
 *
 * <p>
 * These instructions provide variable access and runtime scope management.
 * Variable access instructions use an immediate operand identifying the
 * variable slot within the active scope.
 * </p>
 */
public enum ScopeOpCode implements IOpCode {

  /**
  * Declares a variable slot in the current runtime scope.
  *
  * <p>The operand identifies the variable slot that is introduced into the
  * active scope. The declaration itself does not initialize the variable;
  * initialization and assignment are handled separately by the appropriate
  * stack and scope operations.</p>
  */
  DECLARE(1),

  /**
   * Removes the value at the top of the stack and stores it in the variable
   * slot identified by the operand.
   */
  STORE(1),

  /**
   * Loads the value from the variable slot identified by the operand and
   * pushes it onto the stack.
   */
  LOAD(1),

  /**
   * Loads the value from the captured variable slot identified by the operand
   * and pushes it onto the stack.
   */
  LOAD_CAPTURE(1),

  /**
   * Removes the value at the top of the stack and stores it in the captured
   * variable slot identified by the operand.
   */
  STORE_CAPTURE(1),

  /**
   * Creates and enters a new nested runtime scope.
   */
  ENTER_SCOPE(0),

  /**
   * Exits the current runtime scope and restores its parent.
   */
  EXIT_SCOPE(0);

  private final int operandCount;

  ScopeOpCode(int operandCount) {
    this.operandCount = operandCount;
  }

  @Override
  public int operandCount() {
    return operandCount;
  }
}