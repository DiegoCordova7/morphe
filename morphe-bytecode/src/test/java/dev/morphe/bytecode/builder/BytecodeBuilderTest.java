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

import dev.morphe.bytecode.constants.IntegerConstant;
import dev.morphe.bytecode.exception.argument.InvalidArgumentException;
import dev.morphe.bytecode.exception.argument.NullArgumentException;
import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.ControlOpCode;
import dev.morphe.bytecode.instructions.opcodes.StackOpCode;
import dev.morphe.bytecode.program.*;
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.bytecode.type.PrimitiveBytecodeType;
import dev.morphe.bytecode.type.VoidBytecodeType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BytecodeBuilderTest {

  @Test
  void emitsExistingInstruction() {
    BytecodeBuilder builder = new BytecodeBuilder();
    Instruction<StackOpCode> instruction = new Instruction<>(StackOpCode.PUSH, 42);
    builder.emit(instruction);
    assertEquals(1, builder.instructionCount());
  }

  @Test
  void rejectsNullOpcode() {
    BytecodeBuilder builder = new BytecodeBuilder();
    assertThrows(NullArgumentException.class, () -> builder.emit(null));
  }

  @Test
  void rejectsNullInstruction() {
    BytecodeBuilder builder = new BytecodeBuilder();
    assertThrows(NullArgumentException.class, () -> builder.emit(null));
  }

  @Test
  void reportsCurrentPosition() {
    BytecodeBuilder builder = new BytecodeBuilder();
    assertEquals(0, builder.position());
    builder.emit(StackOpCode.POP);
    assertEquals(1, builder.position());
    builder.emit(StackOpCode.POP);
    assertEquals(2, builder.position());
  }

  @Test
  void instructionCountMatchesEmittedInstructions() {
    BytecodeBuilder builder = new BytecodeBuilder();
    builder.emit(StackOpCode.POP);
    builder.emit(StackOpCode.POP);
    builder.emit(StackOpCode.POP);
    assertEquals(3, builder.instructionCount());
  }

  @Test
  void createsUnmarkedLabel() {
    BytecodeBuilder builder = new BytecodeBuilder();
    Label label = builder.createLabel();
    assertNotNull(label);
    assertFalse(label.isMarked());
  }

  @Test
  void marksLabelAtCurrentPosition() {
    BytecodeBuilder builder = new BytecodeBuilder();
    builder.emit(StackOpCode.POP);
    Label label = builder.createLabel();
    builder.mark(label);
    assertTrue(label.isMarked());
    assertEquals(1, label.position());
  }

  @Test
  void rejectsNullLabelWhenMarking() {
    BytecodeBuilder builder = new BytecodeBuilder();
    assertThrows(NullArgumentException.class, () -> builder.mark(null));
  }

  @Test
  void rejectsAlreadyMarkedLabel() {
    BytecodeBuilder builder = new BytecodeBuilder();
    Label label = builder.createLabel();
    builder.mark(label);
    assertThrows(InvalidArgumentException.class, () -> builder.mark(label));
  }

  @Test
  void emitsLabelReference() {
    BytecodeBuilder builder = new BytecodeBuilder();
    Label target = builder.createLabel();
    builder.emit(ControlOpCode.JMP, target);
    builder.emit(StackOpCode.POP);
    builder.mark(target);
    FunctionDefinition function = createFunction("main", 0);
    builder.function(function);
    builder.entryFunction(0);
    BytecodeProgram program = builder.build();
    assertEquals(new Instruction<>(ControlOpCode.JMP, 2), program.instructionAt(0));
  }

  @Test
  void rejectsNullOpcodeForLabelReference() {
    BytecodeBuilder builder = new BytecodeBuilder();
    Label label = builder.createLabel();
    assertThrows(NullArgumentException.class, () -> builder.emit(null, label));
  }

  @Test
  void rejectsOpcodeThatDoesNotAcceptOneOperandAsLabelReference() {
    BytecodeBuilder builder = new BytecodeBuilder();
    Label label = builder.createLabel();
    assertThrows(InvalidArgumentException.class, () -> builder.emit(StackOpCode.POP, label));
  }

  @Test
  void rejectsUnmarkedReferencedLabelWhenBuilding() {
    BytecodeBuilder builder = new BytecodeBuilder();
    Label target = builder.createLabel();
    builder.emit(ControlOpCode.JMP, target);
    builder.function(createFunction("main", 0));
    builder.entryFunction(0);
    assertThrows(InvalidArgumentException.class, builder::build);
  }

  @Test
  void registersNewConstant() {
    BytecodeBuilder builder = new BytecodeBuilder();
    IntegerConstant constant = new IntegerConstant(42);
    int index = builder.constant(constant);
    assertEquals(0, index);
  }

  @Test
  void reusesEqualConstantIndex() {
    BytecodeBuilder builder = new BytecodeBuilder();
    int first = builder.constant(new IntegerConstant(42));
    int second = builder.constant(new IntegerConstant(42));
    assertEquals(first, second);
  }

  @Test
  void registersConstantsInOrder() {
    BytecodeBuilder builder = new BytecodeBuilder();
    int first = builder.constant(new IntegerConstant(10));
    int second = builder.constant(new IntegerConstant(20));
    assertEquals(0, first);
    assertEquals(1, second);
  }

  @Test
  void rejectsNullConstant() {
    BytecodeBuilder builder = new BytecodeBuilder();
    assertThrows(NullArgumentException.class, () -> builder.constant(null));
  }

  @Test
  void registersNewType() {
    BytecodeBuilder builder = new BytecodeBuilder();
    int index = builder.type(PrimitiveBytecodeType.INTEGER);
    assertEquals(0, index);
  }

  @Test
  void reusesEqualTypeIndex() {
    BytecodeBuilder builder = new BytecodeBuilder();
    int first = builder.type(PrimitiveBytecodeType.INTEGER);
    int second = builder.type(PrimitiveBytecodeType.INTEGER);
    assertEquals(first, second);
  }

  @Test
  void registersTypesInOrder() {
    BytecodeBuilder builder = new BytecodeBuilder();
    int first = builder.type(PrimitiveBytecodeType.INTEGER);
    int second = builder.type(PrimitiveBytecodeType.STRING);
    assertEquals(0, first);
    assertEquals(1, second);
  }

  @Test
  void rejectsNullType() {
    BytecodeBuilder builder = new BytecodeBuilder();
    assertThrows(NullArgumentException.class, () -> builder.type(null));
  }

  @Test
  void registersResolvedFunction() {
    BytecodeBuilder builder = new BytecodeBuilder();
    FunctionDefinition function = createFunction("main", 0);
    int index = builder.function(function);
    assertEquals(0, index);
    builder.entryFunction(index);
    BytecodeProgram program = builder.build();
    assertEquals(1, program.functionCount());
    assertEquals(function, program.functionAt(0));
  }

  @Test
  void reservesAndCompletesFunction() {
    BytecodeBuilder builder = new BytecodeBuilder();
    Label entryPoint = builder.createLabel();
    int index = builder.function("main", createFunctionType(), entryPoint);
    builder.mark(entryPoint);
    builder.completeFunction(index, List.of());
    builder.entryFunction(index);
    BytecodeProgram program = builder.build();
    assertEquals(1, program.functionCount());
    assertEquals("main", program.functionAt(0).name());
    assertEquals(0, program.functionAt(0).entryPoint());
  }

  @Test
  void rejectsBuildWithUnresolvedFunction() {
    BytecodeBuilder builder = new BytecodeBuilder();
    Label entryPoint = builder.createLabel();
    int index = builder.function("main", createFunctionType(), entryPoint);
    builder.entryFunction(index);
    assertThrows(InvalidArgumentException.class, builder::build);
  }

  @Test
  void rejectsCompletingFunctionWithUnmarkedEntryPoint() {
    BytecodeBuilder builder = new BytecodeBuilder();
    Label entryPoint = builder.createLabel();
    int index = builder.function("main", createFunctionType(), entryPoint);
    assertThrows(InvalidArgumentException.class, () -> builder.completeFunction(index, List.of()));
  }

  @Test
  void definesEntryFunction() {
    BytecodeBuilder builder = new BytecodeBuilder();
    int index = builder.function(createFunction("main", 0));
    builder.entryFunction(index);
    BytecodeProgram program = builder.build();
    assertEquals(index, program.entryFunction());
  }

  @Test
  void rejectsInvalidEntryFunctionIndex() {
    BytecodeBuilder builder = new BytecodeBuilder();
    assertThrows(InvalidArgumentException.class, () -> builder.entryFunction(0));
  }

  @Test
  void rejectsNegativeEntryFunctionIndex() {
    BytecodeBuilder builder = new BytecodeBuilder();
    assertThrows(InvalidArgumentException.class, () -> builder.entryFunction(-1));
  }

  @Test
  void rejectsDuplicateEntryFunctionDefinition() {
    BytecodeBuilder builder = new BytecodeBuilder();
    builder.function(createFunction("main", 0));
    builder.function(createFunction("other", 0));
    builder.entryFunction(0);
    assertThrows(InvalidArgumentException.class, () -> builder.entryFunction(1));
  }

  @Test
  void reservesAndCompletesAttemptRegion() {
    BytecodeBuilder builder = new BytecodeBuilder();
    Label start = builder.createLabel();
    Label end = builder.createLabel();
    Label fallback = builder.createLabel();
    Label handler = builder.createLabel();
    builder.mark(start);
    builder.emit(StackOpCode.POP);
    builder.mark(end);
    int regionIndex = builder.reserveAttempt(start, end, fallback);
    builder.addErrorHandler(regionIndex, BytecodeErrorKind.DIVISION_BY_ZERO, handler);
    builder.mark(fallback);
    builder.mark(handler);
    builder.completeAttemptRegion(regionIndex);
    builder.function(createFunction("main", 0));
    builder.entryFunction(0);
    BytecodeProgram program = builder.build();
    assertEquals(1, program.attemptRegionCount());
    AttemptRegion region = program.attemptRegionAt(0);
    assertEquals(0, region.start());
    assertEquals(1, region.end());
    assertEquals(1, region.fallback());
    assertEquals(1, region.handlerCount());
    ErrorHandler errorHandler = region.handlerAt(0);
    assertEquals(BytecodeErrorKind.DIVISION_BY_ZERO, errorHandler.errorKind());
    assertEquals(1, errorHandler.entryPoint());
  }

  @Test
  void rejectsBuildWithUnresolvedAttemptRegion() {
    BytecodeBuilder builder = new BytecodeBuilder();
    Label start = builder.createLabel();
    Label end = builder.createLabel();
    Label fallback = builder.createLabel();
    int regionIndex = builder.reserveAttempt(start, end, fallback);
    builder.function(createFunction("main", 0));
    builder.entryFunction(0);
    assertEquals(0, regionIndex);
    assertThrows(InvalidArgumentException.class, builder::build);
  }

  @Test
  void rejectsAddingHandlerToUnknownAttemptRegion() {
    BytecodeBuilder builder = new BytecodeBuilder();
    Label handler = builder.createLabel();
    assertThrows(
        InvalidArgumentException.class, () -> builder.addErrorHandler(0, BytecodeErrorKind.DIVISION_BY_ZERO, handler)
    );
  }

  @Test
  void rejectsBuildWithoutEntryFunction() {
    BytecodeBuilder builder = new BytecodeBuilder();
    builder.function(createFunction("main", 0));
    assertThrows(InvalidArgumentException.class, builder::build);
  }

  @Test
  void buildsProgramWithAllRegisteredComponents() {
    BytecodeBuilder builder = new BytecodeBuilder();
    builder.emit(StackOpCode.PUSH, 42);
    builder.emit(StackOpCode.POP);
    builder.constant(new IntegerConstant(42));
    builder.type(PrimitiveBytecodeType.INTEGER);
    FunctionDefinition function = createFunction("main", 0);
    builder.function(function);
    builder.entryFunction(0);
    BytecodeProgram program = builder.build();
    assertEquals(2, program.instructionCount());
    assertEquals(1, program.constantCount());
    assertEquals(1, program.typeCount());
    assertEquals(1, program.functionCount());
    assertEquals(0, program.attemptRegionCount());
    assertEquals(0, program.entryFunction());
    assertEquals(new Instruction<>(StackOpCode.PUSH, 42), program.instructionAt(0));
    assertEquals(new IntegerConstant(42), program.constantAt(0));
    assertEquals(PrimitiveBytecodeType.INTEGER, program.typeAt(0));
    assertEquals(function, program.functionAt(0));
  }

  private static FunctionBytecodeType createFunctionType() {
    return new FunctionBytecodeType(List.of(), VoidBytecodeType.INSTANCE);
  }

  private static FunctionDefinition createFunction(String name, int entryPoint) {
    return new FunctionDefinition(name, createFunctionType(), entryPoint, List.of());
  }
}