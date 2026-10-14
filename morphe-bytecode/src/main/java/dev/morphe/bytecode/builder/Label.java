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
package dev.morphe.bytecode.builder;

/**
 * Represents a symbolic position in a bytecode program.
 *
 * <p>
 * A label is created by a {@link BytecodeBuilder} and receives its
 * instruction position when it is marked. Until then, the label has no
 * associated instruction position.
 * </p>
 */
public final class Label {

  private Integer position;

  Label() {}

  /**
   * Returns whether this label has been assigned an instruction position.
   *
   * @return {@code true} if the label is marked; {@code false} otherwise
   */
  boolean isMarked() {
    return position != null;
  }

  /**
   * Marks this label at the specified instruction position.
   *
   * @param position the instruction position assigned to this label
   */
  void mark(int position) {
    this.position = position;
  }

  /**
   * Returns the instruction position assigned to this label.
   *
   * @return the instruction position, or {@code null} if the label is not marked
   */
  Integer position() {
    return position;
  }
}