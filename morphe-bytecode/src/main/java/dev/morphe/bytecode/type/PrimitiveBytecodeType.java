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
package dev.morphe.bytecode.type;

/**
 * Represents the primitive types supported by the Morphe bytecode layer.
 *
 * <p>
 * Primitive bytecode types describe the basic value types supported
 * directly by bytecode. They are independent from the corresponding
 * runtime value implementations.
 * </p>
 */
public enum PrimitiveBytecodeType implements IBytecodeType {

  /**
   * Represents an integer type.
   */
  INTEGER(BytecodeTypeKind.INTEGER),

  /**
   * Represents a double-precision floating-point type.
   */
  DOUBLE(BytecodeTypeKind.DOUBLE),

  /**
   * Represents a boolean type.
   */
  BOOLEAN(BytecodeTypeKind.BOOLEAN),

  /**
   * Represents a string type.
   */
  STRING(BytecodeTypeKind.STRING);

  private final BytecodeTypeKind kind;

  PrimitiveBytecodeType(BytecodeTypeKind kind) {
    this.kind = kind;
  }

  /**
   * Returns the general kind of this primitive bytecode type.
   *
   * @return the corresponding primitive bytecode type kind
   */
  @Override
  public BytecodeTypeKind kind() {
    return kind;
  }
}