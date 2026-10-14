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
package dev.morphe.runtime.type.composite;

import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.exception.field.DuplicateFieldException;
import dev.morphe.runtime.kind.TypeKind;
import dev.morphe.runtime.type.IType;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.composite.CompositeValue;
import dev.morphe.runtime.value.primitive.NullValue;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a composite type defined by a collection of named fields.
 *
 * <p>Composite types use structural typing. Two composite types are
 * considered equal when they declare the same fields with the same
 * corresponding types, regardless of their names.</p>
 *
 * <p>The type name is retained for diagnostics, debugging, and language-level
 * identification, but it does not participate in structural equality.</p>
 */
public final class CompositeType implements IType {

  private final String name;
  private final List<FieldType> fields;
  private final Map<String, FieldType> fieldsByName;

  /**
   * Creates a composite type with the specified name and fields.
   *
   * @param name the name of the composite type
   * @param fields the fields declared by the composite type
   * @throws NullArgumentException if {@code name}, {@code fields}, or any
   * field is {@code null}
   * @throws DuplicateFieldException if multiple fields have the same name
   */
  public CompositeType(String name, List<FieldType> fields) {
    if (name == null) throw new NullArgumentException("name");
    if (fields == null) throw new NullArgumentException("fields");
    validateFields(name, fields);
    this.name = name;
    this.fields = List.copyOf(fields);
    this.fieldsByName = this.fields.stream().collect(Collectors.toUnmodifiableMap(FieldType::name, field -> field));
  }

  /**
   * Returns the name of this composite type.
   *
   * @return the type name
   */
  @Override
  public String name() {
    return name;
  }

  /**
   * Returns the runtime kind of this type.
   *
   * @return {@link TypeKind#COMPOSITE}
   */
  @Override
  public TypeKind kind() {
    return TypeKind.COMPOSITE;
  }

  /**
   * Returns the fields declared by this composite type.
   *
   * @return an immutable list of field types
   */
  public List<FieldType> fields() {
    return fields;
  }

  /**
   * Returns the field with the specified name.
   *
   * @param name the field name
   * @return the corresponding field type, or {@code null} if no field exists
   * @throws NullArgumentException if {@code name} is {@code null}
   */
  public FieldType field(String name) {
    if (name == null) throw new NullArgumentException("name");
    return fieldsByName.get(name);
  }

  /**
   * Determines whether this composite type declares a field with the
   * specified name.
   *
   * @param name the field name
   * @return {@code true} if the field exists; {@code false} otherwise
   * @throws NullArgumentException if {@code name} is {@code null}
   */
  public boolean hasField(String name) {
    if (name == null) throw new NullArgumentException("name");
    return fieldsByName.containsKey(name);
  }

  /**
   * Determines whether this composite type has the same structure as
   * another composite type.
   *
   * <p>The type name is intentionally ignored. Field order is also ignored;
   * fields are matched by name and their corresponding types must match.</p>
   *
   * @param other the composite type to compare with
   * @return {@code true} if both types have the same structure
   */
  private boolean structurallyEquals(CompositeType other) {
    if (other == null) return false;
    if (fieldsByName.size() != other.fieldsByName.size()) return false;
    for (FieldType field : fields) {
      FieldType otherField = other.fieldsByName.get(field.name());
      if (otherField == null) return false;
      if (!field.type().equals(otherField.type())) return false;
    }
    return true;
  }

  /**
   * Validates the fields declared by this composite type.
   *
   * @param typeName the name of the composite type
   * @param fields the fields to validate
   * @throws NullArgumentException if any field is {@code null}
   * @throws DuplicateFieldException if multiple fields have the same name
   */
  private void validateFields(String typeName, List<FieldType> fields) {
    fields.forEach(field -> {
      if (field == null) throw new NullArgumentException("field");
    });
    Set<String> names = new HashSet<>();
    fields.forEach(field -> {
      if (!names.add(field.name())) throw new DuplicateFieldException(field.name(), typeName);
    });
  }

  /**
   * Determines whether the given value is accepted by this composite type.
   *
   * <p>A composite value is accepted when its type has the same structure
   * as this type.</p>
   *
   * @param value the value to validate
   * @return {@code true} if the value is accepted; {@code false} otherwise
   */
  @Override
  public boolean accepts(IValue value) {
    if (value instanceof NullValue) return true;
    if (!(value instanceof CompositeValue compositeValue)) return false;
    return structurallyEquals(compositeValue.type());
  }

  /**
   * Determines whether this composite type is structurally equal to another
   * object.
   *
   * @param object the object to compare with this type
   * @return {@code true} if the object represents the same structure
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof CompositeType other)) return false;
    return structurallyEquals(other);
  }

  /**
   * Returns a hash code based on the structural definition of this type.
   *
   * @return the structural hash code
   */
  @Override
  public int hashCode() {
    return fieldsByName.hashCode();
  }
}