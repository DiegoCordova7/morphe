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
package dev.morphe.runtime.type.collection;

import dev.morphe.runtime.type.IType;

/**
 * Represents a collection type in the Morphe runtime.
 *
 * <p>
 * Collection types describe runtime types whose values represent
 * collections of other runtime values.
 * </p>
 *
 * <p>
 * Implementations define the specific collection structure and the
 * type constraints applied to their elements.
 * </p>
 */
public interface ICollectionType extends IType { }