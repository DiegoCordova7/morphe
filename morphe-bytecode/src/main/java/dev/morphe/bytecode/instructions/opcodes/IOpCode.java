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
 * Marker interface for all bytecode opcodes.
 *
 * <p>
 * Opcodes define the operations that can be represented in Morphe bytecode.
 * Implementations are typically provided as enums grouped by category
 * (arithmetic, control flow, stack operations, and so on).
 * </p>
 */
public sealed interface IOpCode
    permits ArithmeticOpCode, ArrayOpCode, CallOpCode, ComparisonOpCode, CompositeOpCode, ControlOpCode, IOOpCode, LogicalOpCode, ScopeOpCode, StackOpCode {
  /**
   * Returns the number of immediate operands required by this opcode.
   *
   * @return the required operand count
   */
  int operandCount();
}