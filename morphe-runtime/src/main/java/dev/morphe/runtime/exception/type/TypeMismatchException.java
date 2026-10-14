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
package dev.morphe.runtime.exception.type;

import dev.morphe.runtime.type.IType;

/**
 * Thrown when a runtime value does not match the expected type.
 */
public final class TypeMismatchException extends TypeException {

  /**
   * Creates an exception for a value that does not match
   * the expected runtime type.
   *
   * @param expectedType the type expected by the operation
   * @param actualType the runtime type of the received value
   */
  public TypeMismatchException(IType expectedType, String actualType) {
    super("Invalid value: expected %s but received %s", expectedType.name(), actualType);
  }

  /**
   * Creates an exception for a value that does not match
   * the expected runtime type with an underlying cause.
   *
   * @param cause the underlying cause
   * @param expectedType the type expected by the operation
   * @param actualType the runtime type of the received value
   */
  public TypeMismatchException(Throwable cause, IType expectedType, String actualType) {
    super(cause, "Invalid value: expected %s but received %s", expectedType.name(), actualType);
  }
}