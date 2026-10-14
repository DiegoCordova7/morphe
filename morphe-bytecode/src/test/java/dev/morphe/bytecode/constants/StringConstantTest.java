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

import dev.morphe.bytecode.exception.argument.NullArgumentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringConstantTest {

  @Test
  void preservesValue() {
    assertEquals("hello", new StringConstant("hello").value());
  }

  @Test
  void acceptsEmptyString() {
    assertEquals("", new StringConstant("").value());
  }

  @Test
  void rejectsNullValue() {
    assertThrows(NullArgumentException.class, () -> new StringConstant(null));
  }

  @Test
  void considersEqualConstantsEqual() {
    assertEquals(new StringConstant("hello"), new StringConstant("hello"));
  }

  @Test
  void considersDifferentValuesDifferent() {
    assertNotEquals(new StringConstant("hello"), new StringConstant("world"));
  }

  @Test
  void considersSameInstanceEqualToItself() {
    StringConstant constant = new StringConstant("hello");
    assertEquals(constant, constant);
  }

  @Test
  void rejectsNullInEquality() {
    StringConstant constant = new StringConstant("hello");
    assertNotEquals(constant, null);
  }

  @Test
  void rejectsDifferentTypeInEquality() {
    StringConstant constant = new StringConstant("hello");
    assertNotEquals(constant, 42);
  }

  @Test
  void equalConstantsHaveEqualHashCode() {
    assertEquals(new StringConstant("hello").hashCode(), new StringConstant("hello").hashCode());
  }

  @Test
  void returnsExpectedStringRepresentation() {
    assertEquals("hello", new StringConstant("hello").toString());
  }
}