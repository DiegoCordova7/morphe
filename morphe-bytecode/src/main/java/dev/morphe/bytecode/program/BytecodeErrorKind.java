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
package dev.morphe.bytecode.program;

/**
 * Identifies the kinds of execution errors that can be handled by an
 * {@link AttemptRegion}.
 *
 * <p>
 * These values represent error categories at the bytecode level and are
 * independent from the concrete exception types used by the virtual machine.
 * </p>
 */
public enum BytecodeErrorKind {

  /**
   * Indicates an attempt to divide a numeric value by zero.
   */
  DIVISION_BY_ZERO,

  /**
   * Indicates an attempt to calculate the modulo of a numeric value by zero.
   */
  MODULO_BY_ZERO,

  /**
   * Indicates an array access using an invalid index.
   */
  INVALID_ARRAY_INDEX
}