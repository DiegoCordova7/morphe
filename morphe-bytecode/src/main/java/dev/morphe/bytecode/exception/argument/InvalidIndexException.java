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
 * Thrown when an index is outside the bounds of a bytecode table.
 */
public final class InvalidIndexException extends ArgumentException {

  /**
   * Creates an invalid index exception.
   *
   * @param table the name of the table being accessed
   * @param index the invalid index
   * @param size the size of the table
   */
  public InvalidIndexException(String table, int index, int size) {
    super("Invalid index for %s: %s (size: %s)", table, index, size);
  }
}