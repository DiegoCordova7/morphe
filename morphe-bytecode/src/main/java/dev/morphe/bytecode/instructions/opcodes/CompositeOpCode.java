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
 * Represents bytecode instructions for accessing and modifying fields
 * of composite values.
 */
public enum CompositeOpCode implements IOpCode {

  /**
   * Creates a composite value.
   *
   * <p>Operand 0 is the index of the composite bytecode type in the
   * program type table. The field values are consumed from the stack
   * according to the field order defined by the composite type.</p>
   */
  COMPOSITE_NEW(1),

  /**
   * Retrieves a field from a composite value.
   *
   * <p>Expects the composite value on top of the stack and pushes
   * the value stored in the specified field.</p>
   *
   * <p>Operand:</p>
   * <ul>
   *   <li>constant pool index containing the field name</li>
   * </ul>
   */
  GET_FIELD(1),

  /**
   * Assigns a value to a field of a composite value.
   *
   * <p>Expects the value and composite target on the stack and
   * stores the value in the specified field.</p>
   *
   * <p>Operand:</p>
   * <ul>
   *   <li>constant pool index containing the field name</li>
   * </ul>
   */
  SET_FIELD(1),

  /**
   * Checks structural match between two composite values (~= operator).
   *
   * <p>Expects two composite values on the stack: the target (left)
   * and the model (right). Pops both and pushes true if the target
   * contains all fields and matching values of the model.</p>
   */
  STRUCT_MATCH(0);

  private final int operandCount;

  CompositeOpCode(int operandCount) {
    this.operandCount = operandCount;
  }

  /**
   * Returns the number of immediate operands required by this opcode.
   *
   * @return the required operand count
   */
  @Override
  public int operandCount() {
    return operandCount;
  }
}