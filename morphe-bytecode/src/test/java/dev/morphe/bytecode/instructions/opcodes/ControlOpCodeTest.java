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
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ControlOpCodeTest {

  @ParameterizedTest
  @MethodSource("operandCounts")
  void declaresExpectedOperandCount(ControlOpCode opcode, int expectedCount) {
    assertEquals(expectedCount, opcode.operandCount());
  }

  static Stream<Arguments> operandCounts() {
    return Stream.of(
        Arguments.of(ControlOpCode.HALT, 0),
        Arguments.of(ControlOpCode.JMP, 1),
        Arguments.of(ControlOpCode.JMP_IF_FALSE, 1),
        Arguments.of(ControlOpCode.JMP_IF_TRUE, 1)
    );
  }
}