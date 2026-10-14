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

import dev.morphe.bytecode.constants.IConstant;
import dev.morphe.bytecode.exception.argument.InvalidArgumentException;
import dev.morphe.bytecode.exception.argument.NullArgumentException;
import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.IOpCode;
import dev.morphe.bytecode.program.BytecodeErrorKind;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.bytecode.program.CaptureDefinition;
import dev.morphe.bytecode.program.FunctionDefinition;
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.bytecode.type.IBytecodeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Builds immutable Morphe bytecode programs.
 *
 * <p>
 * The builder incrementally collects instructions, constants, bytecode types,
 * functions, attempt regions, and symbolic label references. Deferred
 * definitions and label references are resolved when {@link #build()} is
 * called.
 * </p>
 *
 * <p>
 * Once built, the resulting {@link BytecodeProgram} owns its immutable
 * representation independently from this builder.
 * </p>
 */
public final class BytecodeBuilder {

  private Integer entryFunction;

  private final List<Instruction<? extends IOpCode>> instructions = new ArrayList<>();
  private final List<IConstant> constants = new ArrayList<>();
  private final List<IBytecodeType> types = new ArrayList<>();

  private final FunctionRegistry functions = new FunctionRegistry();
  private final AttemptRegionRegistry attemptRegions = new AttemptRegionRegistry();

  private final Map<Integer, Label> labelReferences = new java.util.HashMap<>();

  /**
   * Defines the function used as the program entry point.
   *
   * @param functionIndex the index of the entry function
   *
   * @throws InvalidArgumentException if the function index is invalid or
   * the entry function has already been defined
   */
  public void entryFunction(int functionIndex) {
    if (!functions.contains(functionIndex))
      throw new InvalidArgumentException("functionIndex", "Function index is invalid");
    if (entryFunction != null)
      throw new InvalidArgumentException("entryFunction", "Entry function has already been defined");
    entryFunction = functionIndex;
  }

  /**
   * Emits a new instruction using the specified opcode and operands.
   *
   * @param opcode the opcode of the instruction
   * @param operands the operands of the instruction
   *
   * @throws NullArgumentException if {@code opcode} is null
   */
  public void emit(IOpCode opcode, int... operands) {
    if (opcode == null) throw new NullArgumentException("opcode");
    emit(new Instruction<>(opcode, operands));
  }

  /**
   * Emits an existing instruction.
   *
   * @param instruction the instruction to emit
   *
   * @throws NullArgumentException if {@code instruction} is null
   */
  public void emit(Instruction<? extends IOpCode> instruction) {
    if (instruction == null) throw new NullArgumentException("instruction");
    instructions.add(instruction);
  }

  /**
   * Returns the position at which the next instruction will be emitted.
   *
   * @return the current instruction position
   */
  public int position() {
    return instructions.size();
  }

  /**
   * Returns the number of instructions emitted so far.
   *
   * @return the instruction count
   */
  public int instructionCount() {
    return instructions.size();
  }

  /**
   * Creates a new unmarked label.
   *
   * @return the created label
   */
  public Label createLabel() {
    return new Label();
  }

  /**
   * Marks the specified label at the current instruction position.
   *
   * @param label the label to mark
   *
   * @throws NullArgumentException if {@code label} is null
   * @throws InvalidArgumentException if the label has already been marked
   */
  public void mark(Label label) {
    if (label == null) throw new NullArgumentException("label");
    if (label.isMarked()) throw new InvalidArgumentException("label", "Label has already been marked");
    label.mark(position());
  }

  /**
   * Emits an instruction whose operand references a label.
   *
   * <p>
   * The label is resolved to an instruction index when {@link #build()} is
   * called.
   * </p>
   *
   * @param opcode the opcode
   * @param label the target label
   *
   * @throws NullArgumentException if {@code opcode} or {@code label} is null
   * @throws InvalidArgumentException if the opcode does not expect exactly
   * one operand
   */
  public void emit(IOpCode opcode, Label label) {
    if (opcode == null) throw new NullArgumentException("opcode");
    if (label == null) throw new NullArgumentException("label");
    if (opcode.operandCount() != 1)
      throw new InvalidArgumentException("opcode", String.format("%s does not accept a label operand", opcode));
    int instructionIndex = position();
    emit(opcode, 0);
    labelReferences.put(instructionIndex, label);
  }

  /**
   * Registers a constant in the constant pool.
   *
   * <p>
   * If an equal constant is already present in the pool, its existing index
   * is returned instead of adding a duplicate.
   * </p>
   *
   * @param constant the constant to register
   * @return the index of the constant in the constant pool
   *
   * @throws NullArgumentException if {@code constant} is null
   */
  public int constant(IConstant constant) {
    if (constant == null) throw new NullArgumentException("constant");
    int index = constants.indexOf(constant);
    if (index >= 0) return index;
    constants.add(constant);
    return constants.size() - 1;
  }

  /**
   * Registers a bytecode type in the type table.
   *
   * <p>
   * If an equal type is already present in the table, its existing index is
   * returned instead of adding a duplicate.
   * </p>
   *
   * @param type the bytecode type to register
   * @return the index of the bytecode type in the type table
   *
   * @throws NullArgumentException if {@code type} is null
   */
  public int type(IBytecodeType type) {
    if (type == null) throw new NullArgumentException("type");
    int index = types.indexOf(type);
    if (index >= 0) return index;
    types.add(type);
    return types.size() - 1;
  }

  /**
   * Registers an already-resolved function definition.
   *
   * @param function the function definition to register
   * @return the function index assigned to the function
   *
   * @throws NullArgumentException if {@code function} is null
   */
  public int function(FunctionDefinition function) {
    return functions.register(function);
  }

  /**
   * Reserves a function index for a function whose definition will be
   * completed later.
   *
   * @param name the function name
   * @param type the function bytecode type
   * @param entryPoint the function entry point label
   * @return the reserved function index
   *
   * @throws NullArgumentException if {@code name}, {@code type}, or
   * {@code entryPoint} is null
   */
  public int function(String name, FunctionBytecodeType type, Label entryPoint) {
    return functions.reserve(name, type, entryPoint);
  }

  /**
   * Completes a previously reserved function definition.
   *
   * @param functionIndex the reserved function index
   * @param captures the function capture definitions
   *
   * @throws NullArgumentException if {@code captures} is null
   * @throws InvalidArgumentException if the function is not pending or its
   * entry point has not been marked
   */
  public void completeFunction(int functionIndex, List<CaptureDefinition> captures) {
    functions.complete(functionIndex, captures);
  }

  /**
   * Reserves an attempt region whose definition will be completed later.
   *
   * @param start the start label of the protected region
   * @param end the end label of the protected region
   * @param fallback the fallback entry point label
   * @return the reserved attempt region index
   *
   * @throws NullArgumentException if {@code start}, {@code end}, or
   * {@code fallback} is null
   */
  public int reserveAttempt(Label start, Label end, Label fallback) {
    return attemptRegions.reserve(start, end, fallback);
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
  public void addErrorHandler(int regionIndex, BytecodeErrorKind errorKind, Label entryPoint) {
    attemptRegions.addErrorHandler(regionIndex, errorKind, entryPoint);
  }

  /**
   * Completes a previously reserved attempt region.
   *
   * @param regionIndex the reserved attempt region index
   *
   * @throws InvalidArgumentException if the attempt region is not pending
   * or any associated label has not been marked
   */
  public void completeAttemptRegion(int regionIndex) {
    attemptRegions.complete(regionIndex);
  }

  /**
   * Builds the final bytecode program.
   *
   * <p>
   * All label references, pending functions, and pending attempt regions
   * must be resolved before the immutable {@link BytecodeProgram} is created.
   * </p>
   *
   * @return the resolved bytecode program
   *
   * @throws InvalidArgumentException if the program contains unresolved
   * functions or attempt regions, if a referenced label remains unresolved,
   * or if no entry function has been defined
   */
  public BytecodeProgram build() {
    if (functions.hasPendingFunctions())
      throw new InvalidArgumentException("functions", "There are unresolved functions");
    if (attemptRegions.hasPendingRegions())
      throw new InvalidArgumentException("attemptRegions", "There are unresolved attempt regions");
    if (entryFunction == null)
      throw new InvalidArgumentException("entryFunction", "No entry function has been defined");
    List<Instruction<? extends IOpCode>> resolved = new ArrayList<>(instructions);
    resolveLabels(resolved);
    return new BytecodeProgram(
        resolved, constants, types, functions.functions(), attemptRegions.regions(), entryFunction
    );
  }

  private void resolveLabels(List<Instruction<? extends IOpCode>> instructions) {
    for (Map.Entry<Integer, Label> entry : labelReferences.entrySet()) {
      int instructionIndex = entry.getKey();
      Label label = entry.getValue();
      if (!label.isMarked()) throw new InvalidArgumentException("label", "Label was referenced but never marked");
      Instruction<? extends IOpCode> instruction = instructions.get(instructionIndex);
      instructions.set(instructionIndex, instruction.withOperand(0, label.position()));
    }
  }
}