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
package dev.morphe.runtime.type.callable;

import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.kind.TypeKind;
import dev.morphe.runtime.type.IType;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.callable.FunctionValue;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the type of a callable function value.
 *
 * <p>
 * A function type is defined by an ordered list of parameter types and
 * a return type. Two function types are considered equal when their
 * parameter types and return types are equal.
 * </p>
 *
 * <p>
 * Function types describe the signature of callable values. They do not
 * contain the function implementation or identify a specific function
 * definition in a bytecode program.
 * </p>
 */
public final class FunctionType implements IType {

  private final List<IType> parameterTypes;
  private final IType returnType;

  /**
   * Creates a function type with the specified parameter and return types.
   *
   * @param parameterTypes the ordered parameter types
   * @param returnType the return type
   *
   * @throws NullArgumentException if {@code parameterTypes},
   *     {@code returnType}, or any parameter type is {@code null}
   */
  public FunctionType(List<IType> parameterTypes, IType returnType) {
    if (parameterTypes == null) throw new NullArgumentException("parameterTypes");
    if (returnType == null) throw new NullArgumentException("returnType");
    parameterTypes.forEach(parameterType -> {
      if (parameterType == null) throw new NullArgumentException("parameterType");
    });
    this.parameterTypes = List.copyOf(parameterTypes);
    this.returnType = returnType;
  }

  /**
   * Returns the ordered parameter types of this function type.
   *
   * @return an immutable list of parameter types
   */
  public List<IType> parameterTypes() {
    return parameterTypes;
  }

  /**
   * Returns the return type of this function type.
   *
   * @return the function return type
   */
  public IType returnType() {
    return returnType;
  }

  /**
   * Returns the name of this type.
   *
   * @return the function type name
   */
  @Override
  public String name() {
    return "Function";
  }

  /**
   * Returns the runtime kind of this type.
   *
   * @return {@link TypeKind#FUNCTION}
   */
  @Override
  public TypeKind kind() {
    return TypeKind.FUNCTION;
  }

  /**
   * Determines whether the given value is accepted by this type.
   *
   * <p>
   * Function values are identified by function IDs and their concrete
   * function definitions are resolved by the virtual machine. Therefore,
   * determining whether a specific {@link FunctionValue}
   * has this exact function type requires access to the corresponding
   * function definition and is not performed directly by this runtime type.
   * </p>
   *
   * @param value the value to validate
   * @return {@code true} if the value is a function value compatible with
   *     this type; {@code false} otherwise
   */
  @Override
  public boolean accepts(IValue value) {
    return value instanceof FunctionValue;
  }

  /**
   * Compares this function type with another object.
   *
   * <p>
   * Function types are structurally equal when they have equal parameter
   * types in the same order and equal return types.
   * </p>
   *
   * @param object the object to compare
   * @return {@code true} if both function types have equal signatures
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof FunctionType other)) return false;
    return parameterTypes.equals(other.parameterTypes) && returnType.equals(other.returnType);
  }

  /**
   * Returns the hash code of this function type.
   *
   * @return the function type hash code
   */
  @Override
  public int hashCode() {
    return 31 * parameterTypes.hashCode() + returnType.hashCode();
  }

  /**
   * Returns the string representation of this function type.
   *
   * @return the function signature
   */
  @Override
  public String toString() {
    return parameterTypes.stream()
        .map(IType::name)
        .collect(Collectors.joining(", ", "(", ") -> " + returnType.name()));
  }
}