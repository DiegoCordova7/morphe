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

class ScopeOpCodeTest {

  @ParameterizedTest
  @MethodSource("operandCounts")
  void declaresExpectedOperandCount(ScopeOpCode opcode, int expectedCount) {
    assertEquals(expectedCount, opcode.operandCount());
  }

  static Stream<Arguments> operandCounts() {
    return Stream.of(
        Arguments.of(ScopeOpCode.DECLARE, 1),
        Arguments.of(ScopeOpCode.STORE, 1),
        Arguments.of(ScopeOpCode.LOAD, 1),
        Arguments.of(ScopeOpCode.LOAD_CAPTURE, 1),
        Arguments.of(ScopeOpCode.STORE_CAPTURE, 1),
        Arguments.of(ScopeOpCode.ENTER_SCOPE, 0),
        Arguments.of(ScopeOpCode.EXIT_SCOPE, 0)
    );
  }
}