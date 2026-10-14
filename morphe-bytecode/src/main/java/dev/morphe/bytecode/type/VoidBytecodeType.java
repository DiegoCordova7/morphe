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
 * Represents the absence of a return value at the bytecode level.
 *
 * <p>
 * This type is used exclusively as the declared return type of
 * {@link FunctionBytecodeType function types} whose execution does not
 * produce a value.
 * </p>
 *
 * <p>
 * Unlike primitive bytecode types, this type does not describe a runtime
 * value. It exists only to complete function signatures encoded in
 * bytecode.
 * </p>
 *
 * <p>
 * Since all void bytecode types are identical and stateless, this class
 * exposes a single shared {@link #INSTANCE} rather than allowing multiple
 * instances to be created.
 * </p>
 */
public final class VoidBytecodeType implements IBytecodeType {

  /**
   * The shared instance of the void bytecode type.
   */
  public static final VoidBytecodeType INSTANCE = new VoidBytecodeType();

  /**
   * Prevents external instantiation.
   */
  private VoidBytecodeType() {}

  /**
   * Returns the general kind of this bytecode type.
   *
   * @return {@link BytecodeTypeKind#VOID}
   */
  @Override
  public BytecodeTypeKind kind() {
    return BytecodeTypeKind.VOID;
  }

  @Override
  public String toString() {
    return "VOID";
  }
}