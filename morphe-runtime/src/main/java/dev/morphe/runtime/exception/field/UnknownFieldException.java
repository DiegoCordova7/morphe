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
 * Thrown when a field is requested or provided that is not declared
 * by the corresponding Morphe composite type.
 */
public final class UnknownFieldException extends FieldException {

  /**
   * Creates an exception for an unknown field.
   *
   * @param fieldName the name of the unknown field
   * @param typeName the name of the type that does not declare the field
   */
  public UnknownFieldException(String fieldName, String typeName) {
    super("Unknown field '%s' for type '%s'", fieldName, typeName);
  }

  /**
   * Creates an exception for an unknown field
   * with an underlying cause.
   *
   * @param cause the underlying cause
   * @param fieldName the name of the unknown field
   * @param typeName the name of the type that does not declare the field
   */
  public UnknownFieldException(Throwable cause, String fieldName, String typeName) {
    super(cause, "Unknown field '%s' for type '%s'", fieldName, typeName);
  }
}