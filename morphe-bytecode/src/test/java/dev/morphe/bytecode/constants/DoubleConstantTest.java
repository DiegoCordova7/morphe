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
package dev.morphe.bytecode.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleConstantTest {

  @Test
  void preservesValue() {
    assertEquals(3.14, new DoubleConstant(3.14).value());
  }

  @Test
  void preservesNegativeValue() {
    assertEquals(-3.14, new DoubleConstant(-3.14).value());
  }

  @Test
  void considersEqualValuesEqual() {
    assertEquals(new DoubleConstant(3.14), new DoubleConstant(3.14));
  }

  @Test
  void considersDifferentValuesDifferent() {
    assertNotEquals(new DoubleConstant(3.14), new DoubleConstant(2.71));
  }

  @Test
  void considersSameInstanceEqualToItself() {
    DoubleConstant constant = new DoubleConstant(3.14);
    assertEquals(constant, constant);
  }

  @Test
  void rejectsNullInEquality() {
    DoubleConstant constant = new DoubleConstant(3.14);
    assertNotEquals(constant, null);
  }

  @Test
  void rejectsDifferentTypeInEquality() {
    DoubleConstant constant = new DoubleConstant(3.14);
    assertNotEquals(constant, "3.14");
  }

  @Test
  void equalConstantsHaveEqualHashCode() {
    assertEquals(new DoubleConstant(3.14).hashCode(), new DoubleConstant(3.14).hashCode());
  }

  @Test
  void returnsExpectedStringRepresentation() {
    assertEquals("3.14", new DoubleConstant(3.14).toString());
    assertEquals("-3.14", new DoubleConstant(-3.14).toString());
  }
}