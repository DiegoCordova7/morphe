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
package dev.morphe.bytecode.exception.validation;

import dev.morphe.bytecode.exception.BytecodeException;

/**
 * Base exception for errors involving invalid bytecode structure or
 * definitions.
 */
public abstract class ValidationException extends BytecodeException {

  /**
   * Creates a validation exception.
   *
   * @param message the message format
   * @param args the arguments used to format the message
   */
  protected ValidationException(String message, Object... args) {
    super(message, args);
  }

  /**
   * Creates a validation exception with an underlying cause.
   *
   * @param cause the underlying cause
   * @param message the message format
   * @param args the arguments used to format the message
   */
  protected ValidationException(Throwable cause, String message, Object... args) {
    super(cause, message, args);
  }
}