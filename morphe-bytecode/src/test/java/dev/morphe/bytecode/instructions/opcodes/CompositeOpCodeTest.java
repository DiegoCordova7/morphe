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
package dev.morphe.bytecode.instructions.opcodes;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompositeOpCodeTest {

  @ParameterizedTest
  @EnumSource(CompositeOpCode.class)
  void verifiesOperandCounts(CompositeOpCode opcode) {
    int expectedOperands = switch (opcode) {
      case COMPOSITE_NEW, GET_FIELD, SET_FIELD -> 1;
      case STRUCT_MATCH -> 0;
    };
    assertEquals(expectedOperands, opcode.operandCount());
  }
}