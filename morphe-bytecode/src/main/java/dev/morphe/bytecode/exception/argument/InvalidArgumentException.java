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
package dev.morphe.bytecode.exception.argument;

/**
 * Indicates that an argument has an invalid value for the operation being
 * performed.
 *
 * <p>This exception is used when an argument is present but does not satisfy
 * the constraints required by the receiving operation.</p>
 */
public final class InvalidArgumentException extends ArgumentException {

  /**
   * Creates an exception for an invalid argument.
   *
   * @param argumentName the name of the invalid argument
   * @param reason the reason why the argument is invalid
   */
  public InvalidArgumentException(String argumentName, String reason) {
    super("Invalid argument '%s': %s", argumentName, reason);
  }
}