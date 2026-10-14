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
 * Identifies the general kind of a type represented by the
 * Morphe bytecode layer.
 *
 * <p>
 * These values describe bytecode-level type categories and are
 * independent from the runtime type hierarchy.
 * </p>
 */
public enum BytecodeTypeKind {

  /**
   * Represents the absence of a return value.
   *
   * <p>
   * This type is used as the declared return type of functions that do not
   * produce a value. It is not a primitive value type and cannot be used
   * to represent runtime values.
   * </p>
   */
  VOID,

  /**
   * Represents an integer type.
   */
  INTEGER,

  /**
   * Represents a double-precision floating-point type.
   */
  DOUBLE,

  /**
   * Represents a boolean type.
   */
  BOOLEAN,

  /**
   * Represents a string type.
   */
  STRING,

  /**
   * Represents an array type.
   *
   * <p>
   * The element type is provided by the corresponding
   * {@link ArrayBytecodeType}.
   * </p>
   */
  ARRAY,

  /**
   * Represents a composite type.
   *
   * <p>
   * The fields of the composite type are provided by the corresponding
   * {@link CompositeBytecodeType}.
   * </p>
   */
  COMPOSITE,

  /**
   * Represents a function type.
   *
   * <p>
   * The parameter types and return type are provided by the
   * corresponding {@link FunctionBytecodeType}.
   * </p>
   */
  FUNCTION
}