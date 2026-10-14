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

class IntegerConstantTest {

  @Test
  void preservesValue() {
    assertEquals(42, new IntegerConstant(42).value());
  }

  @Test
  void preservesNegativeValue() {
    assertEquals(-42, new IntegerConstant(-42).value());
  }

  @Test
  void preservesIntegerBounds() {
    assertEquals(Integer.MIN_VALUE, new IntegerConstant(Integer.MIN_VALUE).value());
    assertEquals(Integer.MAX_VALUE, new IntegerConstant(Integer.MAX_VALUE).value());
  }

  @Test
  void considersEqualConstantsEqual() {
    assertEquals(new IntegerConstant(42), new IntegerConstant(42));
  }

  @Test
  void considersDifferentValuesDifferent() {
    assertNotEquals(
        new IntegerConstant(42),
        new IntegerConstant(43)
    );
  }

  @Test
  void considersSameInstanceEqualToItself() {
    IntegerConstant constant = new IntegerConstant(42);
    assertEquals(constant, constant);
  }

  @Test
  void rejectsNullInEquality() {
    IntegerConstant constant = new IntegerConstant(42);
    assertNotEquals(constant, null);
  }

  @Test
  void rejectsDifferentTypeInEquality() {
    IntegerConstant constant = new IntegerConstant(42);
    assertNotEquals(constant, "42");
  }

  @Test
  void equalConstantsHaveEqualHashCode() {
    assertEquals(new IntegerConstant(42).hashCode(), new IntegerConstant(42).hashCode());
  }

  @Test
  void returnsExpectedStringRepresentation() {
    assertEquals("42", new IntegerConstant(42).toString());
    assertEquals("-42", new IntegerConstant(-42).toString());
  }
}