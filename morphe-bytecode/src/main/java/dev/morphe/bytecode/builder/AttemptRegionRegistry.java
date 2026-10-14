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

import dev.morphe.bytecode.exception.argument.InvalidArgumentException;
import dev.morphe.bytecode.exception.argument.NullArgumentException;
import dev.morphe.bytecode.program.AttemptRegion;
import dev.morphe.bytecode.program.BytecodeErrorKind;
import dev.morphe.bytecode.program.ErrorHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages attempt regions during bytecode construction.
 *
 * <p>
 * Attempt regions may be reserved before their instruction positions are
 * known. Their labels and error handlers are resolved when the region is
 * completed.
 * </p>
 *
 * <p>
 * This registry is used internally by {@link BytecodeBuilder} and is not part
 * of the public bytecode builder API.
 * </p>
 */
final class AttemptRegionRegistry {

  private final Map<Integer, PendingAttemptRegion> pendingRegions = new HashMap<>();
  private final List<AttemptRegion> regions = new ArrayList<>();

  /**
   * Reserves an attempt region whose definition will be completed later.
   *
   * <p>
   * The supplied labels are resolved to instruction positions when the
   * region is completed.
   * </p>
   *
   * @param start the start label of the protected region
   * @param end the end label of the protected region
   * @param fallback the fallback entry point label
   * @return the reserved attempt region index
   *
   * @throws NullArgumentException if {@code start}, {@code end}, or
   * {@code fallback} is null
   */
  int reserve(Label start, Label end, Label fallback) {
    if (start == null) throw new NullArgumentException("start");
    if (end == null) throw new NullArgumentException("end");
    if (fallback == null) throw new NullArgumentException("fallback");
    int regionIndex = regions.size();
    regions.add(null);
    pendingRegions.put(regionIndex, new PendingAttemptRegion(start, end, fallback, new ArrayList<>()));
    return regionIndex;
  }

  /**
   * Adds an error handler to a previously reserved attempt region.
   *
   * @param regionIndex the reserved attempt region index
   * @param errorKind the bytecode error kind handled by the handler
   * @param entryPoint the handler entry point label
   *
   * @throws NullArgumentException if {@code errorKind} or {@code entryPoint}
   * is null
   * @throws InvalidArgumentException if the attempt region is not pending
   */
  void addErrorHandler(int regionIndex, BytecodeErrorKind errorKind, Label entryPoint) {
    if (errorKind == null) throw new NullArgumentException("errorKind");
    if (entryPoint == null) throw new NullArgumentException("entryPoint");
    PendingAttemptRegion pending = pendingRegions.get(regionIndex);
    if (pending == null) throw new InvalidArgumentException("regionIndex", "Attempt region is not pending");
    pending.handlers().add(new PendingErrorHandler(errorKind, entryPoint));
  }

  /**
   * Completes a previously reserved attempt region.
   *
   * <p>
   * All labels associated with the region must have been marked before the
   * region can be completed. Once completed, the resolved region replaces
   * the reserved entry at its original index.
   * </p>
   *
   * @param regionIndex the reserved attempt region index
   *
   * @throws InvalidArgumentException if the region is not pending, if any
   * associated label has not been marked, or if an error handler entry point
   * has not been marked
   */
  void complete(int regionIndex) {
    PendingAttemptRegion pending = pendingRegions.get(regionIndex);
    if (pending == null)
      throw new InvalidArgumentException("regionIndex", "Attempt region is not pending");
    if (!pending.start().isMarked())
      throw new InvalidArgumentException("start", "Attempt region start label was never marked");
    if (!pending.end().isMarked())
      throw new InvalidArgumentException("end", "Attempt region end label was never marked");
    if (!pending.fallback().isMarked())
      throw new InvalidArgumentException("fallback", "Attempt region fallback label was never marked");
    List<ErrorHandler> handlers = new ArrayList<>();
    for (PendingErrorHandler handler : pending.handlers()) {
      if (!handler.entryPoint().isMarked())
        throw new InvalidArgumentException("entryPoint", "Error handler entry point label was never marked");
      handlers.add(new ErrorHandler(handler.errorKind(), handler.entryPoint().position()));
    }

    AttemptRegion region = new AttemptRegion(
        pending.start().position(), pending.end().position(), pending.fallback().position(), handlers
    );
    regions.set(regionIndex, region);
    pendingRegions.remove(regionIndex);
  }

  /**
   * Returns an immutable snapshot of the registered attempt regions.
   *
   * @return the registered attempt regions in region-index order
   */
  List<AttemptRegion> regions() {
    return List.copyOf(regions);
  }

  /**
   * Determines whether unresolved attempt regions remain.
   *
   * @return {@code true} if at least one attempt region is pending completion
   */
  boolean hasPendingRegions() {
    return !pendingRegions.isEmpty();
  }

  /**
   * Represents an attempt region whose final definition is pending.
   *
   * @param start the start label of the protected region
   * @param end the end label of the protected region
   * @param fallback the fallback entry point label
   * @param handlers the pending error handlers
   */
  private record PendingAttemptRegion(Label start, Label end, Label fallback, List<PendingErrorHandler> handlers) { }

  /**
   * Represents an error handler whose entry point is pending resolution.
   *
   * @param errorKind the bytecode error kind handled by the handler
   * @param entryPoint the handler entry point label
   */
  private record PendingErrorHandler(BytecodeErrorKind errorKind, Label entryPoint) { }
}