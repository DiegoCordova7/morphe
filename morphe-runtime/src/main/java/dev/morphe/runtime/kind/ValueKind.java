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
 * Represents the kind of a runtime value in Morphe.
 *
 * <p>Each constant identifies the broad category of value represented by a
 * runtime value.</p>
 */
public enum ValueKind {

  /**
   * Represents an integer value.
   */
  INTEGER,

  /**
   * Represents a double-precision floating-point value.
   */
  DOUBLE,

  /**
   * Represents a boolean value.
   */
  BOOLEAN,

  /**
   * Represents a string value.
   */
  STRING,

  /**
   * Represents an array value.
   */
  ARRAY,

  /**
   * Represents a composite value.
   */
  COMPOSITE,

  /**
   * Represents a reference to another runtime value.
   */
  REFERENCE,

  /**
   * Represents the absence of a runtime value.
   */
  NULL,

  /**
   * Represents a function value.
   */
  FUNCTION
}