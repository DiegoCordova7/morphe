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
 * Input/output bytecode operations supported by the Morphe VM.
 *
 * <p>
 * These instructions interact with the input and output services configured
 * for the VM. Values written by output operations and produced by input
 * operations are transferred through the VM stack.
 * </p>
 */
public enum IOOpCode implements IOpCode {

  /**
   * Removes the value at the top of the stack and writes it to the
   * configured output.
   */
  PRINT(0),

  /**
   * Requests a string from the configured input and pushes the resulting
   * string value onto the stack.
   */
  INPUT_STRING(0),

  /**
   * Requests an integer from the configured input and pushes the resulting
   * integer value onto the stack.
   */
  INPUT_INTEGER(0),

  /**
   * Requests a double from the configured input and pushes the resulting
   * double value onto the stack.
   */
  INPUT_DOUBLE(0),

  /**
   * Requests a boolean from the configured input and pushes the resulting
   * boolean value onto the stack.
   */
  INPUT_BOOLEAN(0);

  private final int operandCount;

  IOOpCode(int operandCount) {
    this.operandCount = operandCount;
  }

  @Override
  public int operandCount() {
    return operandCount;
  }
}