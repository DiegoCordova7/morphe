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
package dev.morphe.runtime.kind;

/**
 * Represents the kind of a type in the Morphe runtime.
 *
 * <p>Each constant identifies a broad category of runtime type and is used
 * to distinguish primitive, structural, and callable types.</p>
 */
public enum TypeKind {

  /**
   * Represents the {@code Integer} primitive type.
   */
  INTEGER,

  /**
   * Represents the {@code Double} primitive type.
   */
  DOUBLE,

  /**
   * Represents the {@code Boolean} primitive type.
   */
  BOOLEAN,

  /**
   * Represents the {@code String} primitive type.
   */
  STRING,

  /**
   * Represents an array type.
   */
  ARRAY,

  /**
   * Represents a composite type.
   */
  COMPOSITE,

  /**
   * Represents the {@code null} type.
   */
  NULL,

  /**
   * Represents a function type.
   */
  FUNCTION
}