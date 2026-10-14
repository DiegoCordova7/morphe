package dev.morphe.vm.operations;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.ControlOpCode;
import dev.morphe.bytecode.instructions.opcodes.IOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.bytecode.program.FunctionDefinition;
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.bytecode.type.PrimitiveBytecodeType;
import dev.morphe.runtime.environment.ClosureEnvironment;
import dev.morphe.runtime.value.callable.FunctionValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.exception.internal.call.RootFrameReturnException;
import dev.morphe.vm.scope.CallFrame;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CallOperationsTest {

  private VirtualMachine vm() {
    return new VirtualMachine(output -> {});
  }

  private FunctionBytecodeType functionType(int parameterCount) {
    return new FunctionBytecodeType(
        Collections.nCopies(parameterCount, PrimitiveBytecodeType.INTEGER), PrimitiveBytecodeType.INTEGER
    );
  }

  private FunctionDefinition function(String name, int entryPoint, int parameterCount) {
    return new FunctionDefinition(name, functionType(parameterCount), entryPoint);
  }

  private BytecodeProgram program(List<Instruction<? extends IOpCode>> instructions, List<FunctionDefinition> functions) {
    return new BytecodeProgram(instructions, List.of(), List.of(), functions, List.of(), 0);
  }

  @Test
  void shouldCallFunctionByIndex() {
    VirtualMachine vm = vm();
    BytecodeProgram program = program(
        List.of(new Instruction<>(ControlOpCode.HALT)),
        List.of(
            function("main", 0, 0),
            function("foo", 0, 0)
        )
    );
    vm.jumpTo(10);
    CallOperations.call(vm, program, 1, 0);
    assertEquals(0, vm.instructionPointer());
    assertEquals(2, vm.context().callStack().size());
  }

  @Test
  void shouldCreateCallFrameWithReturnAddress() {
    VirtualMachine vm = vm();
    BytecodeProgram program = program(
        List.of(new Instruction<>(ControlOpCode.HALT)),
        List.of(
            function("main", 0, 0),
            function("foo", 0, 0)
        )
    );
    vm.jumpTo(5);
    CallOperations.call(vm, program, 1, 0);
    CallFrame frame = vm.context().currentFrame();
    assertEquals(6, frame.returnAddress());
  }

  @Test
  void shouldRejectIncorrectArgumentCount() {
    VirtualMachine vm = vm();
    BytecodeProgram program = program(
        List.of(new Instruction<>(ControlOpCode.HALT)),
        List.of(
            function("main", 0, 0),
            function("foo", 0, 2)
        )
    );
    assertThrows(IllegalArgumentException.class, () -> CallOperations.call(vm, program, 1, 1));
  }

  @Test
  void shouldBindArgumentsInParameterSlots() {
    VirtualMachine vm = vm();
    BytecodeProgram program = program(
        List.of(new Instruction<>(ControlOpCode.HALT)),
        List.of(
            function("main", 0, 0),
            function("foo", 0, 2)
        )
    );
    vm.context().stack().push(new IntegerValue(10));
    vm.context().stack().push(new IntegerValue(20));
    CallOperations.call(vm, program, 1, 2);
    CallFrame frame = vm.context().currentFrame();
    ScopeOperations.load(vm.context().stack(), vm.context().memory(), frame.scope(), 0);
    assertEquals(new IntegerValue(10), vm.context().stack().pop());
    ScopeOperations.load(vm.context().stack(), vm.context().memory(), frame.scope(), 1);
    assertEquals(new IntegerValue(20), vm.context().stack().pop());
  }

  @Test
  void shouldCallFunctionValue() {
    VirtualMachine vm = vm();
    ClosureEnvironment environment = new ClosureEnvironment();
    FunctionValue function = new FunctionValue(1, environment);
    BytecodeProgram program = program(
        List.of(new Instruction<>(ControlOpCode.HALT)),
        List.of(
            function("main", 0, 0),
            function("foo", 0, 0)
        )
    );
    vm.context().stack().push(function);
    CallOperations.callValue(vm, program);
    assertEquals(2, vm.context().callStack().size());
    assertSame(environment, vm.context().currentFrame().environment());
  }

  @Test
  void shouldCreateClosureAndPushFunctionValue() {
    VirtualMachine vm = vm();
    BytecodeProgram program = program(
        List.of(new Instruction<>(ControlOpCode.HALT)),
        List.of(
            function("main", 0, 0),
            function("foo", 0, 0)
        )
    );
    CallOperations.createClosure(vm, program, 1);
    assertEquals(1, vm.context().stack().size());
    FunctionValue function = (FunctionValue) vm.context().stack().pop();
    assertEquals(1, function.functionId());
    assertNotNull(function.environment());
  }

  @Test
  void shouldReturnFromFunction() {
    VirtualMachine vm = vm();
    BytecodeProgram program = program(
        List.of(new Instruction<>(ControlOpCode.HALT)),
        List.of(
            function("main", 0, 0),
            function("foo", 0, 0)
        )
    );
    CallOperations.call(vm, program, 1, 0);
    vm.jumpTo(7);
    CallOperations.ret(vm);
    assertEquals(1, vm.context().callStack().size());
    assertEquals(1, vm.instructionPointer());
  }

  @Test
  void shouldRejectReturningFromRootFrame() {
    VirtualMachine vm = vm();
    assertThrows(RootFrameReturnException.class, () -> CallOperations.ret(vm));
  }
}