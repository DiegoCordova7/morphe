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
package dev.morphe.runtime.exception.field;

/**
 * Thrown when a required field declared by a Morphe composite type
 * is missing from a composite value.
 */
public final class MissingFieldException extends FieldException {

  /**
   * Creates an exception for a missing required field.
   *
   * @param fieldName the name of the missing field
   * @param typeName the name of the type declaring the field
   */
  public MissingFieldException(String fieldName, String typeName) {
    super("Missing field '%s' for type '%s'", fieldName, typeName);
  }

  /**
   * Creates an exception for a missing required field
   * with an underlying cause.
   *
   * @param cause the underlying cause
   * @param fieldName the name of the missing field
   * @param typeName the name of the type declaring the field
   */
  public MissingFieldException(Throwable cause, String fieldName, String typeName) {
    super(cause, "Missing field '%s' for type '%s'", fieldName, typeName);
  }
}