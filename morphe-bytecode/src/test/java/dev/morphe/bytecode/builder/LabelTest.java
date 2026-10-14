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
package dev.morphe.bytecode.builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LabelTest {

  @Test
  void createsUnmarkedLabel() {
    Label label = new Label();
    assertFalse(label.isMarked());
    assertNull(label.position());
  }

  @Test
  void marksLabelAtPosition() {
    Label label = new Label();
    label.mark(10);
    assertTrue(label.isMarked());
    assertEquals(10, label.position());
  }

  @Test
  void marksLabelAtZero() {
    Label label = new Label();
    label.mark(0);
    assertTrue(label.isMarked());
    assertEquals(0, label.position());
  }

  @Test
  void updatesMarkedPosition() {
    Label label = new Label();
    label.mark(10);
    label.mark(20);
    assertTrue(label.isMarked());
    assertEquals(20, label.position());
  }
}