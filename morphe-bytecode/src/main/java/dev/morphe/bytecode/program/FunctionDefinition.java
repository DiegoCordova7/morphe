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
import dev.morphe.bytecode.type.FunctionBytecodeType;

import java.util.List;
import java.util.Objects;

/**
 * Represents a function definition contained in a bytecode program.
 *
 * <p>
 * A function definition describes the name, signature, entry point, and
 * captured variables of a function.
 * </p>
 *
 * <p>
 * Function definitions are immutable. The order of the captures determines
 * the indices used to reference captured values.
 * </p>
 */
public final class FunctionDefinition {

  private final String name;
  private final FunctionBytecodeType type;
  private final int entryPoint;
  private final List<CaptureDefinition> captures;

  /**
   * Creates a function definition without captured variables.
   *
   * @param name the function name
   * @param type the function signature
   * @param entryPoint the index of the first instruction belonging to
   * the function
   *
   * @throws NullArgumentException if {@code name} or {@code type} is
   * {@code null}
   * @throws InvalidArgumentException if {@code name} is blank or
   * {@code entryPoint} is negative
   */
  public FunctionDefinition(String name, FunctionBytecodeType type, int entryPoint) {
    this(name, type, entryPoint, List.of());
  }

  /**
   * Creates a function definition.
   *
   * @param name the function name
   * @param type the function signature
   * @param entryPoint the index of the first instruction belonging to
   * the function
   * @param captures the variables captured by the function
   *
   * @throws NullArgumentException if any argument is {@code null}, or if
   * {@code captures} contains a {@code null} element
   * @throws InvalidArgumentException if {@code name} is blank or
   * {@code entryPoint} is negative
   */
  public FunctionDefinition(String name, FunctionBytecodeType type, int entryPoint, List<CaptureDefinition> captures) {
    if (name == null) throw new NullArgumentException("name");
    if (type == null) throw new NullArgumentException("type");
    if (captures == null) throw new NullArgumentException("captures");
    if (name.isBlank()) throw new InvalidArgumentException("name", "name cannot be blank");
    if (entryPoint < 0) throw new InvalidArgumentException("entryPoint", "entry point must be non-negative");
    if (captures.stream().anyMatch(Objects::isNull)) throw new NullArgumentException("capture");
    this.name = name;
    this.type = type;
    this.entryPoint = entryPoint;
    this.captures = List.copyOf(captures);
  }

  /**
   * Returns the function name.
   *
   * @return the function name
   */
  public String name() {
    return name;
  }

  /**
   * Returns the function signature.
   *
   * @return the function type
   */
  public FunctionBytecodeType type() {
    return type;
  }

  /**
   * Returns the index of the first instruction executed by this function.
   *
   * @return the function entry point
   */
  public int entryPoint() {
    return entryPoint;
  }

  /**
   * Returns the capture definitions of this function.
   *
   * <p>
   * The order of this list defines the capture indices used by
   * capture-related bytecode instructions.
   * </p>
   *
   * @return the captured variables
   */
  public List<CaptureDefinition> captures() {
    return captures;
  }

  /**
   * Returns the number of values captured by this function.
   *
   * @return the capture count
   */
  public int captureCount() {
    return captures.size();
  }

  /**
   * Returns a capture definition by index.
   *
   * @param index the capture index
   * @return the capture definition
   * @throws InvalidArgumentException if {@code index} is outside the
   * valid capture range
   */
  public CaptureDefinition captureAt(int index) {
    if (index < 0 || index >= captures.size())
      throw new InvalidArgumentException("index", "capture index out of bounds: " + index);
    return captures.get(index);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof FunctionDefinition other)) return false;
    return entryPoint == other.entryPoint && name.equals(other.name)
        && type.equals(other.type) && captures.equals(other.captures);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type, entryPoint, captures);
  }

  @Override
  public String toString() {
    return String.format("%s %s @%d captures=%d", type, name, entryPoint, captures.size());
  }
}