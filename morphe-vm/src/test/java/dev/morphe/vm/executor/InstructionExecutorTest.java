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
package dev.morphe.vm.executor;

import dev.morphe.bytecode.constants.IntegerConstant;
import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.*;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.bytecode.program.FunctionDefinition;
import dev.morphe.bytecode.type.ArrayBytecodeType;
import dev.morphe.bytecode.type.CompositeBytecodeType;
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.bytecode.type.PrimitiveBytecodeType;
import dev.morphe.runtime.environment.ClosureEnvironment;
import dev.morphe.runtime.type.composite.CompositeType;
import dev.morphe.runtime.value.composite.CompositeValue;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.runtime.value.reference.ReferenceValue;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.scope.CallFrame;
import dev.morphe.vm.scope.Scope;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InstructionExecutorTest {

  private Instruction<ControlOpCode> haltInstruction() {
    return new Instruction<>(ControlOpCode.HALT);
  }

  private FunctionBytecodeType functionType() {
    return new FunctionBytecodeType(List.of(), PrimitiveBytecodeType.INTEGER);
  }

  private FunctionDefinition entryFunction() {
    return new FunctionDefinition("main", functionType(), 0);
  }

  private BytecodeProgram program(List<Instruction<? extends IOpCode>> instructions) {
    return new BytecodeProgram(
        instructions,
        List.of(new IntegerConstant(42)),
        List.of(
            new ArrayBytecodeType(PrimitiveBytecodeType.INTEGER),
            new CompositeBytecodeType("Test", List.of())
        ),
        List.of(entryFunction()),
        List.of(),
        0
    );
  }

  private BytecodeProgram emptyProgram() {
    return program(List.of());
  }

  @Test
  void shouldRejectNullVirtualMachine() {
    InstructionExecutor executor = new InstructionExecutor();
    assertThrows(NullArgumentException.class, () -> executor.execute(null, emptyProgram(), haltInstruction()));
  }

  @Test
  void shouldDispatchStackOpcode() {
    InstructionExecutor executor = new InstructionExecutor();
    VirtualMachine vm = new VirtualMachine(output -> {});
    BytecodeProgram program = program(
        List.of(new Instruction<>(StackOpCode.PUSH, 0))
    );
    executor.execute(vm, program, program.instructionAt(0));
    assertEquals(new IntegerValue(42), vm.context().stack().peek());
  }

  @Test
  void shouldDispatchControlOpcode() {
    InstructionExecutor executor = new InstructionExecutor();
    VirtualMachine vm = new VirtualMachine(output -> {});
    BytecodeProgram program = program(
        List.of(new Instruction<>(ControlOpCode.JMP, 5))
    );
    executor.execute(vm, program, program.instructionAt(0));
    assertEquals(5, vm.instructionPointer());
  }

  @Test
  void shouldDispatchArithmeticOpcode() {
    InstructionExecutor executor = new InstructionExecutor();
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(10));
    vm.context().stack().push(new IntegerValue(20));
    BytecodeProgram program = program(
        List.of(new Instruction<>(ArithmeticOpCode.ADD))
    );
    executor.execute(vm, program, program.instructionAt(0));
    assertEquals(new IntegerValue(30), vm.context().stack().peek());
  }

  private record DispatchCase(Instruction<? extends IOpCode> instruction, Consumer<VirtualMachine> prepare) {}

  @ParameterizedTest
  @MethodSource("supportedInstructions")
  void shouldDispatchEverySupportedOpcodeFamily(DispatchCase testCase) {
    InstructionExecutor executor = new InstructionExecutor();
    VirtualMachine vm = new VirtualMachine(output -> {});
    testCase.prepare().accept(vm);
    BytecodeProgram program = program(List.of(testCase.instruction()));
    executor.execute(vm, program, testCase.instruction());
  }

  private static Stream<DispatchCase> supportedInstructions() {
    return Stream.of(
        new DispatchCase(
            new Instruction<>(StackOpCode.PUSH, 0),
            vm -> {}
        ),
        new DispatchCase(
            new Instruction<>(ArithmeticOpCode.ADD),
            vm -> {
              vm.context().stack().push(new IntegerValue(10));
              vm.context().stack().push(new IntegerValue(20));
            }
        ),
        new DispatchCase(
            new Instruction<>(ComparisonOpCode.EQ),
            vm -> {
              vm.context().stack().push(new IntegerValue(10));
              vm.context().stack().push(new IntegerValue(10));
            }
        ),
        new DispatchCase(
            new Instruction<>(LogicalOpCode.NOT),
            vm -> vm.context().stack().push(new BooleanValue(true))
        ),
        new DispatchCase(
            new Instruction<>(ControlOpCode.HALT),
            vm -> {}
        ),
        new DispatchCase(
            new Instruction<>(ScopeOpCode.ENTER_SCOPE),
            vm -> {}
        ),
        new DispatchCase(
            new Instruction<>(CallOpCode.RETURN),
            vm -> {
              Scope scope = new dev.morphe.vm.scope.Scope(null);
              ClosureEnvironment environment = new ClosureEnvironment();
              vm.context().callStack().push(new CallFrame(0, scope, environment));
            }
        ),
        new DispatchCase(
            new Instruction<>(CompositeOpCode.COMPOSITE_NEW, 1),
            vm -> {}
        ),
        new DispatchCase(
            new Instruction<>(CompositeOpCode.STRUCT_MATCH),
            vm -> {
              CompositeType runtimeType = new CompositeType("Test", List.of());
              CompositeValue composite = new CompositeValue(runtimeType, Map.of());
              int addr1 = vm.context().heap().allocate(composite);
              int addr2 = vm.context().heap().allocate(composite);
              vm.context().stack().push(new ReferenceValue(addr1));
              vm.context().stack().push(new ReferenceValue(addr2));
            }
        ),
        new DispatchCase(
            new Instruction<>(ArrayOpCode.ARRAY_NEW, 0),
            vm -> vm.context().stack().push(new IntegerValue(0))
        ),
        new DispatchCase(
            new Instruction<>(IOOpCode.PRINT),
            vm -> vm.context().stack().push(new IntegerValue(42))
        )
    );
  }
}