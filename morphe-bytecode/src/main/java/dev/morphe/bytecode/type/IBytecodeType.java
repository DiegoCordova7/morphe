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
 * Represents a type known by the Morphe bytecode layer.
 *
 * <p>
 * Bytecode types describe type information encoded in a compiled
 * program independently of the runtime type system.
 * </p>
 *
 * <p>
 * Implementations define the specific type information represented by
 * the bytecode program, while {@link #kind()} identifies its general
 * category.
 * </p>
 */
public interface IBytecodeType {

  /**
   * Returns the general category of this bytecode type.
   *
   * @return the bytecode type kind
   */
  BytecodeTypeKind kind();
}