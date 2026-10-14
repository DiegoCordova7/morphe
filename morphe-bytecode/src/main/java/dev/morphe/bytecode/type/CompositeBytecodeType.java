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
package dev.morphe.bytecode.type;

import dev.morphe.bytecode.exception.argument.NullArgumentException;
import dev.morphe.bytecode.exception.validation.DuplicateFieldException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a composite type in the Morphe bytecode type system.
 *
 * <p>
 * A composite type is defined structurally by its named fields and their
 * corresponding bytecode-level types. The type name is descriptive metadata
 * and does not participate in structural equality.</p>
 *
 * <p>
 * Field order determines the numeric field index associated with each
 * field in the composite type.
 *
 * <p>
 * This index identifies the field within the bytecode-level type and may
 * be used by compiler or runtime components when resolving field access.
 * </p>
 *
 * <p>This class represents bytecode-level type information and does not
 * depend on the Morphe runtime type system.</p>
 */
public final class CompositeBytecodeType implements IBytecodeType {

  private final String name;
  private final List<CompositeBytecodeField> fields;
  private final Map<String, Integer> fieldIndices;

  /**
   * Creates a composite bytecode type with the specified name and fields.
   *
   * @param name the name of the composite type
   * @param fields the fields declared by the composite type
   *
   * @throws NullArgumentException if {@code name}, {@code fields}, or any
   *     field is {@code null}
   * @throws DuplicateFieldException if multiple fields have the same name
   */
  public CompositeBytecodeType(String name, List<CompositeBytecodeField> fields) {
    if (name == null) throw new NullArgumentException("name");
    if (fields == null) throw new NullArgumentException("fields");
    validateFields(fields);
    this.name = name;
    this.fields = List.copyOf(fields);
    this.fieldIndices = createFieldIndices(this.fields);
  }

  /**
   * Returns the name of this composite type.
   *
   * @return the composite type name
   */
  public String name() {
    return name;
  }

  /**
   * Returns the fields declared by this composite type.
   *
   * <p>The position of a field in this list is its bytecode field index.</p>
   *
   * @return an immutable list of fields
   */
  public List<CompositeBytecodeField> fields() {
    return fields;
  }

  /**
   * Returns the field with the specified name.
   *
   * @param name the field name
   * @return the corresponding field, or {@code null} if no field exists
   *
   * @throws NullArgumentException if {@code name} is {@code null}
   */
  public CompositeBytecodeField field(String name) {
    if (name == null) throw new NullArgumentException("name");
    Integer index = fieldIndices.get(name);
    return index == null ? null : fields.get(index);
  }

  /**
   * Returns the bytecode index of the field with the specified name.
   *
   * <p>The returned index corresponds to the field's position in
   * {@link #fields()}.</p>
   *
   * @param name the field name
   * @return the field index, or {@code -1} if no field exists
   *
   * @throws NullArgumentException if {@code name} is {@code null}
   */
  public int fieldIndex(String name) {
    if (name == null) throw new NullArgumentException("name");
    Integer index = fieldIndices.get(name);
    return index == null ? -1 : index;
  }

  /**
   * Determines whether this composite type declares a field with the
   * specified name.
   *
   * @param name the field name
   * @return {@code true} if the field exists; {@code false} otherwise
   *
   * @throws NullArgumentException if {@code name} is {@code null}
   */
  public boolean hasField(String name) {
    if (name == null) throw new NullArgumentException("name");
    return fieldIndices.containsKey(name);
  }

  /**
   * Returns the general kind of this bytecode type.
   *
   * @return {@link BytecodeTypeKind#COMPOSITE}
   */
  @Override
  public BytecodeTypeKind kind() {
    return BytecodeTypeKind.COMPOSITE;
  }

  /**
   * Validates the fields declared by this composite type.
   *
   * @param fields the fields to validate
   *
   * @throws NullArgumentException if any field is {@code null}
   * @throws DuplicateFieldException if multiple fields have the same name
   */
  private void validateFields(List<CompositeBytecodeField> fields) {
    Set<String> names = new HashSet<>();
    for (CompositeBytecodeField field : fields) {
      if (field == null) throw new NullArgumentException("field");
      if (!names.add(field.name())) throw new DuplicateFieldException(field.name());
    }
  }

  /**
   * Creates the mapping between field names and their bytecode indices.
   *
   * @param fields the fields declared by the composite type
   * @return an immutable field-name-to-index mapping
   */
  private Map<String, Integer> createFieldIndices(List<CompositeBytecodeField> fields) {
    Map<String, Integer> indices = new HashMap<>();
    for (int i = 0; i < fields.size(); i++) indices.put(fields.get(i).name(), i);
    return Map.copyOf(indices);
  }

  /**
   * Determines whether this composite type is structurally equal to another
   * composite type.
   *
   * <p>The type name and field declaration order do not participate in
   * structural equality. Two composite types are equal when they declare
   * the same field names with structurally equal field types.</p>
   *
   * @param object the object to compare with this type
   * @return {@code true} if the object represents the same structure
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof CompositeBytecodeType other)) return false;
    if (fields.size() != other.fields.size()) return false;
    for (CompositeBytecodeField field : fields) {
      CompositeBytecodeField otherField = other.field(field.name());
      if (otherField == null) return false;
      if (!field.type().equals(otherField.type())) return false;
    }
    return true;
  }

  /**
   * Returns a hash code consistent with structural equality.
   *
   * @return the structural hash code
   */
  @Override
  public int hashCode() {
    return fieldsByStructureHash();
  }

  private int fieldsByStructureHash() {
    int hash = 0;
    for (CompositeBytecodeField field : fields) {
      hash += 31 * field.name().hashCode() + field.type().hashCode();
    }
    return hash;
  }

  /**
   * Returns the name of this composite type.
   *
   * @return the composite type name
   */
  @Override
  public String toString() {
    return name;
  }

  /**
   * Represents a named field belonging to a composite bytecode type.
   *
   * @param name the field name
   * @param type the bytecode type of the field
   */
  public record CompositeBytecodeField(String name, IBytecodeType type) {

    /**
     * Creates a composite bytecode field.
     *
     * @param name the field name
     * @param type the bytecode type of the field
     *
     * @throws NullArgumentException if {@code name} or {@code type}
     *     is {@code null}
     */
    public CompositeBytecodeField {
      if (name == null) throw new NullArgumentException("name");
      if (type == null) throw new NullArgumentException("type");
    }
  }
}