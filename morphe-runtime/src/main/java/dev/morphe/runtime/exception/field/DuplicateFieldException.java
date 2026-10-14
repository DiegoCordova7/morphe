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
 * Thrown when multiple fields with the same name are declared
 * within a Morphe composite type.
 */
public final class DuplicateFieldException extends FieldException {

  /**
   * Creates an exception for a duplicate field declaration.
   *
   * @param fieldName the name of the duplicated field
   * @param typeName the name of the type containing the duplicate field
   */
  public DuplicateFieldException(String fieldName, String typeName) {
    super("Duplicate field '%s' in type '%s'", fieldName, typeName);
  }

  /**
   * Creates an exception for a duplicate field declaration
   * with an underlying cause.
   *
   * @param cause the underlying cause
   * @param fieldName the name of the duplicated field
   * @param typeName the name of the type containing the duplicate field
   */
  public DuplicateFieldException(Throwable cause, String fieldName, String typeName) {
    super(cause, "Duplicate field '%s' in type '%s'", fieldName, typeName);
  }
}