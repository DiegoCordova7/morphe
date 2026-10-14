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
 * Array-related bytecode operations supported by the Morphe VM.
 *
 * <p>
 * Array sizes, indices, references, and values are generally obtained
 * from the VM stack. Immediate instruction operands provide metadata
 * required by the operation, such as type table indices and dimension
 * information.
 * </p>
 */
public enum ArrayOpCode implements IOpCode {

  /**
   * Creates a one-dimensional array.
   *
   * <p>
   * The instruction operand specifies the index of the array type
   * in the program's type table. The array size is consumed from the VM stack.
   * </p>
   */
  ARRAY_NEW(1),

  /**
   * Creates a multidimensional array.
   *
   * <p>
   * The first instruction operand specifies the index of the array type
   * in the program's type table. The second operand specifies the number
   * of dimensions. The size of each dimension is consumed from the VM stack.
   * </p>
   */
  ARRAY_NEW_MULTI(2),

  /**
   * Loads an array element using an array reference and index from the stack.
   */
  ARRAY_GET(0),

  /**
   * Stores a value in an array using the value, array reference, and index
   * from the stack.
   */
  ARRAY_SET(0),

  /**
   * Retrieves the length of the array at the top of the stack.
   */
  ARRAY_LENGTH(0);

  private final int operandCount;

  ArrayOpCode(int operandCount) {
    this.operandCount = operandCount;
  }

  @Override
  public int operandCount() {
    return operandCount;
  }
}