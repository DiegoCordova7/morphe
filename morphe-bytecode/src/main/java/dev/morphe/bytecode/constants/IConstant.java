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
package dev.morphe.bytecode.constants;

/**
 * Represents an immutable literal value stored in a Morphe bytecode
 * constant pool.
 *
 * <p>
 * Constants are referenced by bytecode instructions through their
 * index in the constant pool. They are independent from runtime values;
 * the virtual machine is responsible for converting them into the
 * corresponding runtime representation during execution.
 * </p>
 */
public sealed interface IConstant permits
    IntegerConstant, DoubleConstant,
    StringConstant, BooleanConstant {}