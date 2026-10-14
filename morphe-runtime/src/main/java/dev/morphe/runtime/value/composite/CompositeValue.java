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
package dev.morphe.runtime.value.composite;

import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.exception.field.MissingFieldException;
import dev.morphe.runtime.exception.field.UnknownFieldException;
import dev.morphe.runtime.exception.type.TypeMismatchException;
import dev.morphe.runtime.exception.value.NullFieldValueException;
import dev.morphe.runtime.kind.ValueKind;
import dev.morphe.runtime.type.collection.ArrayType;
import dev.morphe.runtime.type.composite.*;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.reference.ReferenceValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a composite runtime value in Morphe.
 *
 * <p>A composite value consists of named fields whose structure and
 * expected types are defined by an associated {@link CompositeType}.
 * Each field stores an {@link IValue} representing its current runtime
 * value.</p>
 *
 * <p>Composite values follow structural semantics rather than nominal
 * type identity. A composite may therefore contain additional fields
 * beyond those required by another composite value.</p>
 *
 * <p>Values representing arrays and nested composites may be stored as
 * {@link ReferenceValue} instances that refer to objects located in the
 * VM heap.</p>
 */
public final class CompositeValue implements ICompositeValue {

  private final CompositeType type;
  private final Map<String, IValue> values;

  /**
   * Creates a composite value with the specified type and field values.
   *
   * <p>The supplied values are validated against the composite type.
   * All fields declared by the type must be present, no unknown fields
   * may be provided, and each field value must satisfy its declared
   * field type.</p>
   *
   * @param type the composite type defining the structure of this value
   * @param values the initial values indexed by field name
   * @throws NullArgumentException if {@code type} or {@code values} is
   *         {@code null}
   * @throws UnknownFieldException if {@code values} contains a field
   *         not declared by the composite type
   * @throws MissingFieldException if a field declared by the composite
   *         type is missing
   * @throws NullFieldValueException if a field contains Java
   *         {@code null}
   * @throws TypeMismatchException if a field value does not satisfy
   *         its declared field type
   */
  public CompositeValue(CompositeType type, Map<String, IValue> values) {
    if (type == null) throw new NullArgumentException("type");
    if (values == null) throw new NullArgumentException("values");
    this.type = type;
    validate(values);
    this.values = new HashMap<>(values);
  }

  /**
   * Returns the composite type associated with this value.
   *
   * @return the composite type
   */
  public CompositeType type() {
    return type;
  }

  /**
   * Returns the value of the specified field.
   *
   * @param fieldName the name of the field
   * @return the runtime value stored in the field
   * @throws NullArgumentException if {@code fieldName} is {@code null}
   * @throws UnknownFieldException if the field is not declared by the
   *         composite type
   */
  public IValue get(String fieldName) {
    if (fieldName == null) throw new NullArgumentException("fieldName");
    if (!type.hasField(fieldName)) throw new UnknownFieldException(fieldName, type.name());
    return values.get(fieldName);
  }

  /**
   * Updates the value of the specified field.
   *
   * <p>The new value must satisfy the type declared for the field.
   * Array and composite fields may contain a {@link ReferenceValue}
   * referring to the corresponding object in the VM heap.</p>
   *
   * @param fieldName the name of the field to update
   * @param value the new runtime value
   * @throws NullArgumentException if {@code fieldName} is {@code null}
   * @throws UnknownFieldException if the field is not declared by the
   *         composite type
   * @throws NullFieldValueException if {@code value} is Java
   *         {@code null}
   * @throws TypeMismatchException if the value does not satisfy the
   *         declared field type
   */
  public void set(String fieldName, IValue value) {
    if (fieldName == null) throw new NullArgumentException("fieldName");
    if (value == null) throw new NullFieldValueException(type.field(fieldName));
    FieldType field = type.field(fieldName);
    if (field == null) throw new UnknownFieldException(fieldName, type.name());
    if (value instanceof ReferenceValue &&
        (field.type() instanceof ArrayType || field.type() instanceof CompositeType)
    ) {
      values.put(fieldName, value);
      return;
    }
    if (!field.type().accepts(value)) throw new TypeMismatchException(field.type(), value.kind().name());
    values.put(fieldName, value);
  }

  /**
   * Returns an unmodifiable view of the fields stored in this value.
   *
   * @return an unmodifiable map containing the field values
   */
  public Map<String, IValue> values() {
    return Collections.unmodifiableMap(values);
  }

  /**
   * Compares this composite value with another composite value for exact structural equality.
   * Both must have the exact same fields and equal values.
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof CompositeValue other)) return false;
    if (this.values.size() != other.values.size()) return false;
    for (Map.Entry<String, IValue> entry : this.values.entrySet()) {
      String key = entry.getKey();
      if (!other.values.containsKey(key)) return false;
      if (!entry.getValue().equals(other.values.get(key))) return false;
    }
    return true;
  }

  /**
   * Checks if this composite value structurally matches/satisfies another
   * composite value.
   *
   * <p>This value must contain every field required by the other value
   * and each corresponding field value must be equal.</p>
   */
  public boolean matchesShape(CompositeValue other) {
    if (other == null) return false;
    for (FieldType field : other.type().fields()) {
      if (!values.containsKey(field.name())) return false;
      IValue thisValue = values.get(field.name());
      IValue otherValue = other.values.get(field.name());
      if (!thisValue.equals(otherValue)) return false;
    }
    return true;
  }

  /**
   * Returns the hash code of this composite value.
   *
   * <p>Composite values currently use a constant hash code because
   * structural comparison is directed and composite values are mutable.
   * This avoids deriving the hash code from mutable field contents.</p>
   *
   * @return a constant hash code
   */
  @Override
  public int hashCode() {
    return 0;
  }

  /**
   * Validates the supplied field values against the composite type.
   *
   * @param values the field values to validate
   * @throws UnknownFieldException if an unknown field is present
   * @throws MissingFieldException if a required field is missing
   * @throws NullFieldValueException if a field contains Java
   *         {@code null}
   * @throws TypeMismatchException if a field value does not satisfy
   *         its declared field type
   */
  private void validate(Map<String, IValue> values) {
    validateUnknownFields(values);
    validateRequiredFields(values);
  }

  /**
   * Validates that all supplied fields are declared by the composite type.
   *
   * @param values the field values to validate
   * @throws UnknownFieldException if an unknown field is present
   */
  private void validateUnknownFields(Map<String, IValue> values) {
    values.keySet().forEach(fieldName -> {
      if (!type.hasField(fieldName)) throw new UnknownFieldException(fieldName, type.name());
    });
  }

  /**
   * Validates that all declared fields are present and contain values
   * compatible with their declared types.
   *
   * @param values the field values to validate
   * @throws MissingFieldException if a required field is missing
   * @throws NullFieldValueException if a field contains Java
   *         {@code null}
   * @throws TypeMismatchException if a field value does not satisfy
   *         its declared field type
   */
  private void validateRequiredFields(Map<String, IValue> values) {
    type.fields().forEach(field -> {
      if (!values.containsKey(field.name())) throw new MissingFieldException(field.name(), type.name());
      IValue value = values.get(field.name());
      if (value == null) throw new NullFieldValueException(field);
      if (value instanceof ReferenceValue && (field.type() instanceof ArrayType
          || field.type() instanceof CompositeType)) return;
      if (!field.type().accepts(value)) throw new TypeMismatchException(field.type(), value.kind().name());
    });
  }

  /**
   * Returns the runtime kind of this value.
   *
   * @return {@link ValueKind#COMPOSITE}
   */
  @Override
  public ValueKind kind() {
    return ValueKind.COMPOSITE;
  }

  /**
   * Returns a string representation containing the fields and their
   * current values.
   *
   * @return the composite value representation
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("{");
    boolean first = true;
    for (FieldType field : type.fields()) {
      if (!first) builder.append(", ");
      first = false;
      builder
          .append(field.name())
          .append(" = ")
          .append(values.get(field.name()));
    }
    return builder.append("}").toString();
  }
}