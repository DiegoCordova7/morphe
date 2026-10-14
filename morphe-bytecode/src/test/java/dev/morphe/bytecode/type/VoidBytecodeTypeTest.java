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
package dev.morphe.bytecode.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class VoidBytecodeTypeTest {

  @Test
  void exposesSingletonInstance() {
    assertSame(VoidBytecodeType.INSTANCE, VoidBytecodeType.INSTANCE);
  }

  @Test
  void hasVoidKind() {
    assertEquals(BytecodeTypeKind.VOID, VoidBytecodeType.INSTANCE.kind());
  }
}