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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

class BytecodeErrorKindTest {

  @Test
  void containsExpectedKinds() {
    assertArrayEquals(
        new BytecodeErrorKind[] {
            BytecodeErrorKind.DIVISION_BY_ZERO,
            BytecodeErrorKind.MODULO_BY_ZERO,
            BytecodeErrorKind.INVALID_ARRAY_INDEX
        },
        BytecodeErrorKind.values()
    );
  }
}