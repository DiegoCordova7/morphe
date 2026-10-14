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
 * Defines the bytecode instructions responsible for function invocation
 * and returning from a function.
 *
 * <p>
 * These instructions control the creation and destruction of call frames
 * during execution. Their concrete semantics are implemented by the virtual
 * machine.
 * </p>
 */
public enum CallOpCode implements IOpCode {

  /**
   * Invokes a function identified by the function table index.
   */
  CALL(2),

  /**
   * Returns from the current function.
   */
  CALL_VALUE(0),

  /**
   * Creates a closure for the function identified by the operand.
   */
  CREATE_CLOSURE(1),

  /**
   * Returns from the current function.
   */
  RETURN(0);

  private final int operandCount;

  CallOpCode(int operandCount) {
    this.operandCount = operandCount;
  }

  @Override
  public int operandCount() {
    return operandCount;
  }
}