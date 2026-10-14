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
package dev.morphe.bytecode.instructions;

import dev.morphe.bytecode.exception.argument.InvalidArgumentException;
import dev.morphe.bytecode.exception.argument.InvalidIndexException;
import dev.morphe.bytecode.exception.argument.NullArgumentException;
import dev.morphe.bytecode.instructions.opcodes.StackOpCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InstructionTest {

  @Test
  void createsInstructionWithoutOperands() {
    Instruction<StackOpCode> instruction = new Instruction<>(StackOpCode.POP);
    assertEquals(StackOpCode.POP, instruction.opcode());
    assertArrayEquals(new int[0], instruction.operands());
  }

  @Test
  void createsInstructionWithOperands() {
    Instruction<StackOpCode> instruction = new Instruction<>(StackOpCode.PUSH, 42);
    assertEquals(StackOpCode.PUSH, instruction.opcode());
    assertArrayEquals(new int[]{42}, instruction.operands());
  }

  @Test
  void rejectsNullOpcode() {
    assertThrows(NullArgumentException.class, () -> new Instruction<>(null));
  }

  @Test
  void rejectsNullOperands() {
    assertThrows(NullArgumentException.class, () -> new Instruction<>(StackOpCode.PUSH, (int[]) null));
  }

  @Test
  void rejectsIncorrectOperandCount() {
    assertThrows(InvalidArgumentException.class, () -> new Instruction<>(StackOpCode.POP, 42));
  }

  @Test
  void rejectsMissingRequiredOperands() {
    assertThrows(InvalidArgumentException.class, () -> new Instruction<>(StackOpCode.PUSH));
  }

  @Test
  void preservesOperandArrayFromConstructor() {
    int[] operands = {42};
    Instruction<StackOpCode> instruction = new Instruction<>(StackOpCode.PUSH, operands);
    operands[0] = 99;
    assertArrayEquals(new int[]{42}, instruction.operands());
  }

  @Test
  void protectsOperandArrayWhenAccessed() {
    Instruction<StackOpCode> instruction = new Instruction<>(StackOpCode.PUSH, 42);
    int[] operands = instruction.operands();
    operands[0] = 99;
    assertEquals(42, instruction.operand(0));
  }

  @Test
  void returnsOperandAtValidIndex() {
    Instruction<StackOpCode> instruction = new Instruction<>(StackOpCode.PUSH, 42);
    assertEquals(42, instruction.operand(0));
  }

  @Test
  void rejectsNegativeOperandIndex() {
    Instruction<StackOpCode> instruction = new Instruction<>(StackOpCode.PUSH, 42);
    assertThrows(InvalidIndexException.class, () -> instruction.operand(-1));
  }

  @Test
  void rejectsOperandIndexBeyondBounds() {
    Instruction<StackOpCode> instruction = new Instruction<>(StackOpCode.PUSH, 42);
    assertThrows(InvalidIndexException.class, () -> instruction.operand(1));
  }

  @Test
  void createsInstructionWithUpdatedOperand() {
    Instruction<StackOpCode> original = new Instruction<>(StackOpCode.PUSH, 42);
    Instruction<StackOpCode> updated = original.withOperand(0, 99);
    assertEquals(42, original.operand(0));
    assertEquals(99, updated.operand(0));
  }

  @Test
  void preservesOpcodeWhenUpdatingOperand() {
    Instruction<StackOpCode> original = new Instruction<>(StackOpCode.PUSH, 42);
    Instruction<StackOpCode> updated = original.withOperand(0, 99);
    assertEquals(StackOpCode.PUSH, updated.opcode());
  }

  @Test
  void rejectsInvalidIndexWhenUpdatingOperand() {
    Instruction<StackOpCode> instruction = new Instruction<>(StackOpCode.PUSH, 42);
    assertThrows(InvalidIndexException.class, () -> instruction.withOperand(1, 99));
  }

  @Test
  void considersEqualInstructionsEqual() {
    Instruction<StackOpCode> first = new Instruction<>(StackOpCode.PUSH, 42);
    Instruction<StackOpCode> second = new Instruction<>(StackOpCode.PUSH, 42);
    assertEquals(first, second);
  }

  @Test
  void considersInstructionsWithDifferentOperandsDifferent() {
    Instruction<StackOpCode> first = new Instruction<>(StackOpCode.PUSH, 42);
    Instruction<StackOpCode> second = new Instruction<>(StackOpCode.PUSH, 99);
    assertNotEquals(first, second);
  }

  @Test
  void considersInstructionsWithDifferentOpcodesDifferent() {
    Instruction<StackOpCode> first = new Instruction<>(StackOpCode.POP);
    Instruction<StackOpCode> second = new Instruction<>(StackOpCode.DUP);
    assertNotEquals(first, second);
  }

  @Test
  void considersSameInstructionEqualToItself() {
    Instruction<StackOpCode> instruction = new Instruction<>(StackOpCode.PUSH, 42);
    assertEquals(instruction, instruction);
  }

  @Test
  void rejectsNullInEquality() {
    Instruction<StackOpCode> instruction = new Instruction<>(StackOpCode.PUSH, 42);
    assertNotEquals(instruction, null);
  }

  @Test
  void rejectsDifferentTypeInEquality() {
    Instruction<StackOpCode> instruction = new Instruction<>(StackOpCode.PUSH, 42);
    assertNotEquals(instruction, "PUSH");
  }

  @Test
  void equalInstructionsHaveEqualHashCode() {
    Instruction<StackOpCode> first = new Instruction<>(StackOpCode.PUSH, 42);
    Instruction<StackOpCode> second = new Instruction<>(StackOpCode.PUSH, 42);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void includesOpcodeAndOperandsInToString() {
    Instruction<StackOpCode> instruction = new Instruction<>(StackOpCode.PUSH, 42);
    String representation = instruction.toString();
    assertTrue(representation.contains("PUSH"));
    assertTrue(representation.contains("42"));
  }
}