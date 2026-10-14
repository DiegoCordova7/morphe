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
package dev.morphe.runtime.exception;

/**
 * Base exception for runtime errors raised by the Morphe runtime.
 *
 * <p>All Morphe runtime-specific exceptions extend this class,
 * allowing callers to catch either a specific error or any runtime
 * error originating from the runtime system.</p>
 */
public abstract class MorpheRuntimeException extends RuntimeException {

  /**
   * Creates a runtime exception with a formatted message.
   *
   * @param message the message format
   * @param args the arguments used to format the message
   */
  protected MorpheRuntimeException(String message, Object... args) {
    super(String.format(message, args));
  }

  /**
   * Creates a runtime exception with a formatted message and
   * an underlying cause.
   *
   * @param cause the underlying cause
   * @param message the message format
   * @param args the arguments used to format the message
   */
  protected MorpheRuntimeException(
      Throwable cause,
      String message,
      Object... args
  ) {
    super(String.format(message, args), cause);
  }
}