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
package dev.morphe.runtime.value;

import dev.morphe.runtime.kind.ValueKind;

/**
 * Represents a runtime value in Morphe.
 *
 * <p>Every value exposes its runtime kind through {@link #kind()}.</p>
 */
public interface IValue {

  /**
   * Returns the runtime kind of this value.
   *
   * @return the value kind
   */
  ValueKind kind();
}