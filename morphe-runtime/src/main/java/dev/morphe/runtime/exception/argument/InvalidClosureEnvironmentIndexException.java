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
 * Exception thrown when an index used to access a closure environment
 * is outside the bounds of the environment.
 */
public final class InvalidClosureEnvironmentIndexException extends ArgumentException {

  /**
   * Creates an exception for an invalid closure environment index.
   *
   * @param index the invalid index
   */
  public InvalidClosureEnvironmentIndexException(int index) {
    super("Closure environment index out of bounds: " + index);
  }
}