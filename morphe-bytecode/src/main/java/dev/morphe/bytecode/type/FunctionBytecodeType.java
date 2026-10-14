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

import java.util.List;

/**
 * Represents the signature of a function at the bytecode level.
 *
 * <p>
 * A function bytecode type describes the parameter types accepted by a
 * function together with its declared return type. This information is
 * purely structural and does not represent executable code.
 * </p>
 */
public final class FunctionBytecodeType implements IBytecodeType {

  private final List<IBytecodeType> parameterTypes;
  private final IBytecodeType returnType;

  /**
   * Creates a function bytecode type.
   *
   * @param parameterTypes the declared parameter types
   * @param returnType the declared return type
   *
   * @throws NullArgumentException if {@code parameterTypes},
   * {@code returnType}, or any parameter type is {@code null}
   */
  public FunctionBytecodeType(List<IBytecodeType> parameterTypes, IBytecodeType returnType) {
    if (parameterTypes == null) throw new NullArgumentException("parameterTypes");
    if (returnType == null) throw new NullArgumentException("returnType");
    for (IBytecodeType parameterType : parameterTypes) {
      if (parameterType == null) throw new NullArgumentException("parameterType");
    }
    this.parameterTypes = List.copyOf(parameterTypes);
    this.returnType = returnType;
  }

  /**
   * Returns the declared parameter types.
   *
   * @return an immutable list of parameter types
   */
  public List<IBytecodeType> parameterTypes() {
    return parameterTypes;
  }

  /**
   * Returns the declared return type.
   *
   * @return the return type
   */
  public IBytecodeType returnType() {
    return returnType;
  }

  /**
   * Returns the general kind of this bytecode type.
   *
   * @return {@link BytecodeTypeKind#FUNCTION}
   */
  @Override
  public BytecodeTypeKind kind() {
    return BytecodeTypeKind.FUNCTION;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof FunctionBytecodeType other)) return false;
    return parameterTypes.equals(other.parameterTypes) && returnType.equals(other.returnType);
  }

  @Override
  public int hashCode() {
    return 31 * parameterTypes.hashCode() + returnType.hashCode();
  }

  @Override
  public String toString() {
    return parameterTypes + " -> " + returnType;
  }
}