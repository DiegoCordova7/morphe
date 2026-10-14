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
import dev.morphe.runtime.type.composite.FieldType;

/**
 * Thrown when a composite field is explicitly assigned a null value.
 *
 * <p>Field values in the Morphe runtime must provide a valid runtime
 * value accepted by their declared {@link FieldType}.</p>
 */
public final class NullFieldValueException extends ValueException {

  /**
   * Creates an exception for a null value assigned to the given field.
   *
   * @param field the field whose value is null
   */
  public NullFieldValueException(FieldType field) {
    super("Value for field '%s' cannot be null", field.name());
  }

  /**
   * Creates an exception for a null value assigned to the given field.
   *
   * @param cause the underlying cause
   * @param field the field whose value is null
   */
  public NullFieldValueException(Throwable cause, FieldType field) {
    super(cause, "Value for field '%s' cannot be null", field.name());
  }
}