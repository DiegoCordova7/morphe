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
package dev.morphe.bytecode.program;

import dev.morphe.bytecode.exception.argument.InvalidArgumentException;
import dev.morphe.bytecode.exception.argument.NullArgumentException;

/**
 * Defines a handler for a specific bytecode error.
 *
 * <p>An error handler associates a {@link BytecodeErrorKind} with the entry
 * point at which execution should continue when that error is handled.</p>
 *
 * <p>Instances of this class are immutable and are compared by value based
 * on their error kind and entry point.</p>
 */
public final class ErrorHandler {

  private final BytecodeErrorKind errorKind;
  private final int entryPoint;

  /**
   * Creates an error handler.
   *
   * @param errorKind the kind of bytecode error handled by this handler
   * @param entryPoint the non-negative instruction entry point of the handler
   * @throws NullArgumentException if {@code errorKind} is {@code null}
   * @throws InvalidArgumentException if {@code entryPoint} is negative
   */
  public ErrorHandler(BytecodeErrorKind errorKind, int entryPoint) {
    if (errorKind == null) throw new NullArgumentException("errorKind");
    if (entryPoint < 0) throw new InvalidArgumentException("entryPoint", "entry point must be non-negative");
    this.errorKind = errorKind;
    this.entryPoint = entryPoint;
  }

  /**
   * Returns the kind of bytecode error handled by this handler.
   *
   * @return the handled error kind
   */
  public BytecodeErrorKind errorKind() {
    return errorKind;
  }

  /**
   * Returns the instruction entry point of this handler.
   *
   * @return the non-negative handler entry point
   */
  public int entryPoint() {
    return entryPoint;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Two error handlers are equal when they have the same error kind and
   * entry point.</p>
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof ErrorHandler other)) return false;
    return entryPoint == other.entryPoint && errorKind == other.errorKind;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The hash code is based on the error kind and entry point.</p>
   */
  @Override
  public int hashCode() {
    int result = errorKind.hashCode();
    result = 31 * result + Integer.hashCode(entryPoint);
    return result;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned representation includes the error kind and entry point.</p>
   */
  @Override
  public String toString() {
    return "ErrorHandler{" +
        "errorKind=" + errorKind +
        ", entryPoint=" + entryPoint +
        '}';
  }
}