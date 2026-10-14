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
package dev.morphe.runtime.value.callable;

import dev.morphe.runtime.value.IValue;

/**
 * Represents a callable runtime value in Morphe.
 *
 * <p>
 * Callable values are runtime values that can participate in function
 * invocation. They extend {@link IValue} so they can be represented,
 * stored, and manipulated through the general runtime value abstraction.
 * </p>
 *
 * <p>
 * This interface acts as a marker for the callable category of runtime
 * values. Concrete callable implementations define the information required
 * by the virtual machine to resolve and invoke the represented callable.
 * </p>
 */
public interface ICallableValue extends IValue { }