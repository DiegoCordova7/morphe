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
package dev.morphe.runtime.exception.argument;

import dev.morphe.runtime.exception.MorpheRuntimeException;

/**
 * Base exception for errors involving invalid arguments provided
 * to Morphe runtime operations.
 *
 * <p>This exception represents failures caused by arguments that
 * violate the requirements of a Morphe runtime operation or component.</p>
 */
public abstract class ArgumentException extends MorpheRuntimeException {

  /**
   * Creates an argument-related exception.
   *
   * @param message the message format
   * @param args the arguments used to format the message
   */
  protected ArgumentException(String message, Object... args) {
    super(message, args);
  }

  /**
   * Creates an argument-related exception with an underlying cause.
   *
   * @param cause the underlying cause
   * @param message the message format
   * @param args the arguments used to format the message
   */
  protected ArgumentException(Throwable cause, String message, Object... args) {
    super(cause, message, args);
  }
}