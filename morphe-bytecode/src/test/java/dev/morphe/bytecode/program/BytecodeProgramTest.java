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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.morphe.bytecode.constants.IConstant;
import dev.morphe.bytecode.constants.IntegerConstant;
import dev.morphe.bytecode.exception.argument.InvalidIndexException;
import dev.morphe.bytecode.exception.argument.NullArgumentException;
import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.StackOpCode;
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.bytecode.type.IBytecodeType;
import dev.morphe.bytecode.type.PrimitiveBytecodeType;
import dev.morphe.bytecode.type.VoidBytecodeType;
import java.util.List;
import org.junit.jupiter.api.Test;

class BytecodeProgramTest {

  private static final Instruction<StackOpCode> INSTRUCTION = new Instruction<>(StackOpCode.PUSH, 0);
  private static final IConstant CONSTANT = new IntegerConstant(42);
  private static final IBytecodeType TYPE = PrimitiveBytecodeType.INTEGER;

  private static FunctionDefinition function() {
    return new FunctionDefinition("main", new FunctionBytecodeType(List.of(), VoidBytecodeType.INSTANCE), 0);
  }

  private static AttemptRegion attemptRegion() {
    return new AttemptRegion(0, 1, 1, List.of());
  }

  private static BytecodeProgram program() {
    return new BytecodeProgram(
        List.of(INSTRUCTION),
        List.of(CONSTANT),
        List.of(TYPE),
        List.of(function()),
        List.of(attemptRegion()),
        0
    );
  }

  @Test
  void rejectsNullInstructions() {
    assertThrows(
        NullArgumentException.class,
        () -> new BytecodeProgram(
            null,
            List.of(),
            List.of(),
            List.of(function()),
            List.of(),
            0
        )
    );
  }

  @Test
  void rejectsNullConstants() {
    assertThrows(
        NullArgumentException.class,
        () -> new BytecodeProgram(
            List.of(),
            null,
            List.of(),
            List.of(function()),
            List.of(),
            0
        )
    );
  }

  @Test
  void rejectsNullTypes() {
    assertThrows(
        NullArgumentException.class,
        () -> new BytecodeProgram(
            List.of(),
            List.of(),
            null,
            List.of(function()),
            List.of(),
            0
        )
    );
  }

  @Test
  void rejectsNullFunctions() {
    assertThrows(
        NullArgumentException.class,
        () -> new BytecodeProgram(
            List.of(),
            List.of(),
            List.of(),
            null,
            List.of(),
            0
        )
    );
  }

  @Test
  void rejectsNullAttemptRegions() {
    assertThrows(
        NullArgumentException.class,
        () -> new BytecodeProgram(
            List.of(),
            List.of(),
            List.of(),
            List.of(function()),
            null,
            0
        )
    );
  }

  @Test
  void rejectsNegativeEntryFunction() {
    assertThrows(
        InvalidIndexException.class,
        () -> new BytecodeProgram(
            List.of(),
            List.of(),
            List.of(),
            List.of(function()),
            List.of(),
            -1
        )
    );
  }

  @Test
  void rejectsEntryFunctionOutsideFunctionTable() {
    assertThrows(
        InvalidIndexException.class,
        () -> new BytecodeProgram(
            List.of(),
            List.of(),
            List.of(),
            List.of(function()),
            List.of(),
            1
        )
    );
  }

  @Test
  void returnsEntryFunction() {
    assertEquals(0, program().entryFunction());
  }

  @Test
  void returnsInstructions() {
    BytecodeProgram program = program();

    assertEquals(List.of(INSTRUCTION), program.instructions());
  }

  @Test
  void returnsInstructionAtIndex() {
    BytecodeProgram program = program();

    assertSame(INSTRUCTION, program.instructionAt(0));
  }

  @Test
  void rejectsNegativeInstructionIndex() {
    assertThrows(
        InvalidIndexException.class,
        () -> program().instructionAt(-1)
    );
  }

  @Test
  void rejectsInstructionIndexOutsideTable() {
    assertThrows(
        InvalidIndexException.class,
        () -> program().instructionAt(1)
    );
  }

  @Test
  void returnsInstructionCount() {
    assertEquals(1, program().instructionCount());
  }

  @Test
  void reportsNonEmptyInstructionSequence() {
    assertFalse(program().isEmpty());
  }

  @Test
  void reportsEmptyInstructionSequence() {
    BytecodeProgram program =
        new BytecodeProgram(
            List.of(),
            List.of(),
            List.of(),
            List.of(function()),
            List.of(),
            0
        );

    assertTrue(program.isEmpty());
  }

  @Test
  void returnsConstants() {
    assertEquals(List.of(CONSTANT), program().constants());
  }

  @Test
  void returnsConstantAtIndex() {
    assertSame(CONSTANT, program().constantAt(0));
  }

  @Test
  void rejectsNegativeConstantIndex() {
    assertThrows(
        InvalidIndexException.class,
        () -> program().constantAt(-1)
    );
  }

  @Test
  void rejectsConstantIndexOutsidePool() {
    assertThrows(
        InvalidIndexException.class,
        () -> program().constantAt(1)
    );
  }

  @Test
  void returnsConstantCount() {
    assertEquals(1, program().constantCount());
  }

  @Test
  void returnsTypes() {
    assertEquals(List.of(TYPE), program().types());
  }

  @Test
  void returnsTypeAtIndex() {
    assertSame(TYPE, program().typeAt(0));
  }

  @Test
  void rejectsNegativeTypeIndex() {
    assertThrows(
        InvalidIndexException.class,
        () -> program().typeAt(-1)
    );
  }

  @Test
  void rejectsTypeIndexOutsideTable() {
    assertThrows(
        InvalidIndexException.class,
        () -> program().typeAt(1)
    );
  }

  @Test
  void returnsTypeCount() {
    assertEquals(1, program().typeCount());
  }

  @Test
  void returnsFunctions() {
    FunctionDefinition function = program().functionAt(0);

    assertEquals(List.of(function), program().functions());
  }

  @Test
  void returnsFunctionAtIndex() {
    FunctionDefinition function = program().functionAt(0);

    assertEquals(function, program().functionAt(0));
  }

  @Test
  void rejectsNegativeFunctionIndex() {
    assertThrows(
        InvalidIndexException.class,
        () -> program().functionAt(-1)
    );
  }

  @Test
  void rejectsFunctionIndexOutsideTable() {
    assertThrows(
        InvalidIndexException.class,
        () -> program().functionAt(1)
    );
  }

  @Test
  void returnsFunctionCount() {
    assertEquals(1, program().functionCount());
  }

  @Test
  void returnsAttemptRegions() {
    AttemptRegion region = program().attemptRegionAt(0);

    assertEquals(List.of(region), program().attemptRegions());
  }

  @Test
  void returnsAttemptRegionAtIndex() {
    AttemptRegion region = program().attemptRegions().get(0);

    assertEquals(region, program().attemptRegionAt(0));
  }

  @Test
  void rejectsNegativeAttemptRegionIndex() {
    assertThrows(
        InvalidIndexException.class,
        () -> program().attemptRegionAt(-1)
    );
  }

  @Test
  void rejectsAttemptRegionIndexOutsideTable() {
    assertThrows(
        InvalidIndexException.class,
        () -> program().attemptRegionAt(1)
    );
  }

  @Test
  void returnsAttemptRegionCount() {
    assertEquals(1, program().attemptRegionCount());
  }

  @Test
  void instructionListIsImmutable() {
    assertThrows(
        UnsupportedOperationException.class,
        () -> program().instructions().clear()
    );
  }

  @Test
  void constantPoolIsImmutable() {
    assertThrows(
        UnsupportedOperationException.class,
        () -> program().constants().clear()
    );
  }

  @Test
  void typeTableIsImmutable() {
    assertThrows(
        UnsupportedOperationException.class,
        () -> program().types().clear()
    );
  }

  @Test
  void functionTableIsImmutable() {
    assertThrows(
        UnsupportedOperationException.class,
        () -> program().functions().clear()
    );
  }

  @Test
  void attemptRegionTableIsImmutable() {
    assertThrows(
        UnsupportedOperationException.class,
        () -> program().attemptRegions().clear()
    );
  }
}