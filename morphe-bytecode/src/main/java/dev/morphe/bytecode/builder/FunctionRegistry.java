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
import dev.morphe.bytecode.program.CaptureDefinition;
import dev.morphe.bytecode.program.FunctionDefinition;
import dev.morphe.bytecode.type.FunctionBytecodeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages function definitions during bytecode construction.
 *
 * <p>
 * Functions may either be registered with an already-resolved definition or
 * reserved for completion after their entry point and captures are known.
 * Reserved functions retain their assigned index until their definition is
 * completed.
 * </p>
 *
 * <p>
 * This registry is used internally by {@link BytecodeBuilder} and is not part
 * of the public bytecode builder API.
 * </p>
 */
final class FunctionRegistry {

  private final Map<Integer, PendingFunction> pendingFunctions = new HashMap<>();
  private final List<FunctionDefinition> functions = new ArrayList<>();

  /**
   * Registers an already-resolved function definition.
   *
   * @param function the function definition to register
   * @return the function index assigned to the registered function
   *
   * @throws NullArgumentException if {@code function} is null
   */
  int register(FunctionDefinition function) {
    if (function == null) throw new NullArgumentException("function");
    int functionIndex = functions.size();
    functions.add(function);
    return functionIndex;
  }

  /**
   * Reserves a function index for a function whose definition will be
   * completed later.
   *
   * <p>
   * The supplied entry point label is resolved when the function is
   * completed. The reserved index remains stable until then.
   * </p>
   *
   * @param name the function name
   * @param type the function bytecode type
   * @param entryPoint the function entry point label
   * @return the reserved function index
   *
   * @throws NullArgumentException if {@code name}, {@code type}, or
   * {@code entryPoint} is null
   */
  int reserve(String name, FunctionBytecodeType type, Label entryPoint) {
    if (name == null) throw new NullArgumentException("name");
    if (type == null) throw new NullArgumentException("type");
    if (entryPoint == null) throw new NullArgumentException("entryPoint");
    int functionIndex = functions.size();
    functions.add(null);
    pendingFunctions.put(functionIndex, new PendingFunction(name, type, entryPoint));
    return functionIndex;
  }

  /**
   * Completes a previously reserved function definition.
   *
   * <p>
   * The entry point label must have been marked before the function can be
   * completed. Once completed, the function replaces the reserved entry at
   * its original index.
   * </p>
   *
   * @param functionIndex the reserved function index
   * @param captures the function capture definitions
   *
   * @throws NullArgumentException if {@code captures} is null
   * @throws InvalidArgumentException if the function is not pending or its
   * entry point label has not been marked
   */
  void complete(int functionIndex, List<CaptureDefinition> captures) {
    if (captures == null)
      throw new NullArgumentException("captures");
    PendingFunction pending = pendingFunctions.get(functionIndex);
    if (pending == null)
      throw new InvalidArgumentException("functionIndex", "Function is not pending");
    if (!pending.entryPoint().isMarked())
      throw new InvalidArgumentException("entryPoint", "Function entry point label was never marked");
    FunctionDefinition function = new FunctionDefinition(
        pending.name(), pending.type(), pending.entryPoint().position(), captures
    );
    functions.set(functionIndex, function);
    pendingFunctions.remove(functionIndex);
  }

  /**
   * Returns an immutable snapshot of the registered function definitions.
   *
   * @return the registered function definitions in function-index order
   */
  List<FunctionDefinition> functions() {
    return List.copyOf(functions);
  }

  /**
   * Determines whether unresolved function definitions remain.
   *
   * @return {@code true} if at least one function is pending completion
   */
  boolean hasPendingFunctions() {
    return !pendingFunctions.isEmpty();
  }

  /**
   * Determines whether a function index refers to a registered function.
   *
   * @param functionIndex the function index to validate
   * @return {@code true} if the index refers to a registered function;
   * {@code false} otherwise
   */
  boolean contains(int functionIndex) {
    return functionIndex >= 0 && functionIndex < functions.size();
  }

  /**
   * Represents a function whose final definition is pending.
   *
   * @param name the function name
   * @param type the function bytecode type
   * @param entryPoint the function entry point label
   */
  private record PendingFunction(String name, FunctionBytecodeType type, Label entryPoint) { }
}