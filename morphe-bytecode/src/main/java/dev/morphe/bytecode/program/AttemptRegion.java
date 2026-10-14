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

import java.util.List;
import java.util.Objects;

/**
 * Represents a protected region of a Morphe bytecode program.
 *
 * <p>
 * An attempt region defines an instruction range in which recoverable
 * execution errors may be handled. If an error occurs within the region,
 * the virtual machine can use the associated error handlers to determine
 * whether a recovery action is available.
 * </p>
 *
 * <p>
 * The region also defines a fallback entry point that can be used when
 * execution of the protected region fails. Error handlers are associated
 * with specific {@link BytecodeErrorKind bytecode error kinds} and define
 * the entry point for their corresponding recovery actions.
 * </p>
 *
 * <p>
 * This class only represents static bytecode metadata. It does not perform
 * error detection, handler resolution, state rollback, or control flow.
 * Those responsibilities belong to the virtual machine.
 * </p>
 *
 * <p>
 * Instances of this class are immutable. The handler list is defensively
 * copied during construction and cannot be modified afterwards.
 * </p>
 */
public final class AttemptRegion {

  private final int start;
  private final int end;
  private final int fallback;
  private final List<ErrorHandler> handlers;

  /**
   * Creates a new attempt region.
   *
   * @param start the first instruction position protected by this region
   * @param end the instruction position marking the end of the protected region
   * @param fallback the instruction position at which fallback execution begins
   * @param handlers the error handlers associated with this region
   *
   * @throws InvalidArgumentException if {@code start}, {@code end}, or
   * {@code fallback} is negative, or if {@code end} is less than {@code start}
   * @throws NullArgumentException if {@code handlers} is {@code null} or
   * contains a {@code null} element
   */
  public AttemptRegion(int start, int end, int fallback, List<ErrorHandler> handlers) {
    if (start < 0) throw new InvalidArgumentException("start", "start position must be non-negative");
    if (end < start) throw new InvalidArgumentException("end", "end position must be greater than or equal to start");
    if (fallback < 0) throw new InvalidArgumentException("fallback", "fallback position must be non-negative");
    if (handlers == null) throw new NullArgumentException("handlers");
    if (handlers.stream().anyMatch(Objects::isNull)) throw new NullArgumentException("handler");
    this.start = start;
    this.end = end;
    this.fallback = fallback;
    this.handlers = List.copyOf(handlers);
  }

  /**
   * Returns the first instruction position protected by this region.
   *
   * @return the start instruction position
   */
  public int start() {
    return start;
  }

  /**
   * Returns the instruction position marking the end of the protected region.
   *
   * @return the end instruction position
   */
  public int end() {
    return end;
  }

  /**
   * Returns the instruction position at which fallback execution begins.
   *
   * @return the fallback instruction position
   */
  public int fallback() {
    return fallback;
  }

  /**
   * Returns the error handlers associated with this region.
   *
   * @return an immutable list of error handlers
   */
  public List<ErrorHandler> handlers() {
    return handlers;
  }

  /**
   * Returns the number of error handlers associated with this region.
   *
   * @return the handler count
   */
  public int handlerCount() {
    return handlers.size();
  }

  /**
   * Returns the error handler located at the specified position.
   *
   * @param index the handler index
   * @return the error handler at the given index
   *
   * @throws InvalidArgumentException if the index is outside the handler list
   */
  public ErrorHandler handlerAt(int index) {
    if (index < 0 || index >= handlers.size()) throw new InvalidArgumentException("index", "handler index out of bounds: " + index);
    return handlers.get(index);
  }

  /**
   * Determines whether an instruction position belongs to this protected region.
   *
   * <p>
   * The start position is inclusive and the end position is exclusive.
   * </p>
   *
   * @param instructionPointer the instruction position to test
   * @return {@code true} if the instruction position is within this region;
   * {@code false} otherwise
   */
  public boolean contains(int instructionPointer) {
    return instructionPointer >= start && instructionPointer < end;
  }

  /**
   * Determines whether this attempt region is equal to another object.
   *
   * <p>
   * Two attempt regions are considered equal when they have the same start
   * position, end position, fallback position, and error handlers.
   * </p>
   *
   * @param object the object to compare with this region
   * @return {@code true} if the objects represent the same attempt region;
   * {@code false} otherwise
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof AttemptRegion other)) return false;
    return start == other.start && end == other.end && fallback == other.fallback && handlers.equals(other.handlers);
  }

  /**
   * Returns a hash code derived from this attempt region's contents.
   *
   * @return the hash code of this attempt region
   */
  @Override
  public int hashCode() {
    return Objects.hash(start, end, fallback, handlers);
  }

  /**
   * Returns a string representation of this attempt region.
   *
   * @return a string containing the region's start, end, fallback,
   * and error handlers
   */
  @Override
  public String toString() {
    return "AttemptRegion{" +
        "start=" + start +
        ", end=" + end +
        ", fallback=" + fallback +
        ", handlers=" + handlers +
        '}';
  }
}