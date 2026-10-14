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

import java.util.Objects;

/**
 * Defines a value captured from a specific bytecode source.
 *
 * <p>A capture is identified by its source kind and index. The source kind
 * determines where the captured value is obtained from, while the index
 * identifies the value within that source.</p>
 *
 * <p>Instances of this class are immutable and are compared by value based
 * on their source kind and index.</p>
 */
public final class CaptureDefinition {

  private final CaptureSourceKind sourceKind;
  private final int index;

  /**
   * Creates a capture definition.
   *
   * @param sourceKind the source from which the value is captured
   * @param index the non-negative index of the captured value
   * @throws NullArgumentException if {@code sourceKind} is {@code null}
   * @throws InvalidArgumentException if {@code index} is negative
   */
  public CaptureDefinition(CaptureSourceKind sourceKind, int index) {
    if (sourceKind == null) throw new NullArgumentException("sourceKind");
    if (index < 0) throw new InvalidArgumentException("index", "capture index must be non-negative");
    this.sourceKind = sourceKind;
    this.index = index;
  }

  /**
   * Returns the source from which the value is captured.
   *
   * @return the capture source kind
   */
  public CaptureSourceKind sourceKind() {
    return sourceKind;
  }

  /**
   * Returns the index of the captured value.
   *
   * @return the non-negative capture index
   */
  public int index() {
    return index;
  }

  /**
   * Compares this capture definition with another object.
   *
   * <p>Two capture definitions are equal when they have the same source kind
   * and index.</p>
   *
   * @param object the object to compare with
   * @return {@code true} if both objects represent the same capture definition
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof CaptureDefinition other)) return false;
    return index == other.index && sourceKind == other.sourceKind;
  }

  @Override
  public int hashCode() {
    return Objects.hash(sourceKind, index);
  }

  @Override
  public String toString() {
    return "CaptureDefinition{" +
        "sourceKind=" + sourceKind +
        ", index=" + index +
        '}';
  }

}