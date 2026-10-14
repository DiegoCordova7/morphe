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
package dev.morphe.runtime.value.reference;

import dev.morphe.runtime.kind.ValueKind;
import dev.morphe.runtime.value.IValue;

/**
 * Represents a reference to a runtime value stored in the VM heap.
 *
 * <p>
 * A reference value does not contain the referenced runtime object itself.
 * Instead, it contains the heap address used by the virtual machine to
 * locate that object.
 * </p>
 */
public final class ReferenceValue implements IValue {

  private final int address;

  /**
   * Creates a reference to the specified heap address.
   *
   * @param address the heap address referenced by this value
   */
  public ReferenceValue(int address) {
    this.address = address;
  }

  /**
   * Returns the referenced heap address.
   *
   * @return the heap address
   */
  public int address() {
    return address;
  }

  /**
   * Returns the runtime kind of this value.
   *
   * @return {@link ValueKind#REFERENCE}
   */
  @Override
  public ValueKind kind() {
    return ValueKind.REFERENCE;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof ReferenceValue other)) return false;
    return address == other.address;
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(address);
  }

  @Override
  public String toString() {
    return "Reference(" + address + ")";
  }
}