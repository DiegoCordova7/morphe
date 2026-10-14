package dev.morphe.runtime.exception.value;

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
import dev.morphe.runtime.type.collection.ArrayType;

/**
 * Thrown when an array contains a null element value.
 *
 * <p>Array elements in the Morphe runtime must provide a valid
 * runtime value compatible with the array's declared element type.</p>
 */
public final class NullElementValueException extends ValueException {

  /**
   * Creates an exception for a null element in the given array type.
   *
   * @param type the array type containing the null element
   */
  public NullElementValueException(ArrayType type) {
    super("Element value cannot be null for array type '%s'", type.name());
  }

  /**
   * Creates an exception for a null element in the given array type
   * with an underlying cause.
   *
   * @param cause the underlying cause
   * @param type the array type containing the null element
   */
  public NullElementValueException(Throwable cause, ArrayType type) {
    super(cause, "Element value cannot be null for array type '%s'", type.name());
  }
}