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

/**
 * Thrown when a required argument provided to a Morphe runtime operation
 * is {@code null}.
 */
public final class NullArgumentException extends ArgumentException {

  /**
   * Creates an exception for a null argument.
   *
   * @param argumentName the name of the argument that is null
   */
  public NullArgumentException(String argumentName) {
    super("Argument '%s' cannot be null", argumentName);
  }

  /**
   * Creates an exception for a null argument with an underlying cause.
   *
   * @param cause the underlying cause
   * @param argumentName the name of the argument that is null
   */
  public NullArgumentException(Throwable cause, String argumentName) {
    super(cause, "Argument '%s' cannot be null", argumentName);
  }
}