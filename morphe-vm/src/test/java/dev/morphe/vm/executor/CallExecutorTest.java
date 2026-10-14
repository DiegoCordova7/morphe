package dev.morphe.vm.executor;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.CallOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.bytecode.program.FunctionDefinition;
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.bytecode.type.IBytecodeType;
import dev.morphe.bytecode.type.PrimitiveBytecodeType;
import dev.morphe.runtime.environment.ClosureEnvironment;
import dev.morphe.runtime.value.callable.FunctionValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.scope.CallFrame;
import dev.morphe.vm.scope.Scope;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CallExecutorTest {


  private static void execute(VirtualMachine vm, BytecodeProgram program, CallOpCode opcode, int... operands) {
    new CallExecutor().execute(vm, program, new Instruction<>(opcode, operands));
  }

  private static BytecodeProgram program(FunctionDefinition... functions) {
    return new BytecodeProgram(List.of(), List.of(), List.of(), List.of(functions), List.of(), 0);
  }

  private static FunctionDefinition function(int parameterCount, int entryPoint) {
    List<IBytecodeType> parameters = Collections.nCopies(parameterCount, PrimitiveBytecodeType.INTEGER);
    return new FunctionDefinition(
        "function", new FunctionBytecodeType(parameters, PrimitiveBytecodeType.INTEGER), entryPoint
    );
  }

  @Test
  void callShouldCreateFrameAndJumpToFunction() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.jumpTo(5);
    vm.context().stack().push(new IntegerValue(42));
    execute(vm, program(function(1, 10)), CallOpCode.CALL, 0, 1);
    CallFrame frame = vm.context().currentFrame();
    assertEquals(10, vm.instructionPointer());
    assertEquals(2, vm.context().callStack().size());
    assertEquals(6, frame.returnAddress());
    assertEquals(new IntegerValue(42), vm.context().memory().load(frame.scope().load(0)));
  }

  @Test
  void callValueShouldCreateFrameUsingFunctionEnvironment() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.jumpTo(5);
    ClosureEnvironment environment = new ClosureEnvironment();
    FunctionValue function = new FunctionValue(0, environment);
    vm.context().stack().push(function);
    execute(vm, program(function(0, 10)), CallOpCode.CALL_VALUE);
    assertEquals(10, vm.instructionPointer());
    assertEquals(2, vm.context().callStack().size());
    assertSame(environment, vm.context().currentFrame().environment());
  }

  @Test
  void createClosureShouldPushFunctionValue() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    execute(vm, program(function(0, 10)), CallOpCode.CREATE_CLOSURE, 0);
    FunctionValue function = (FunctionValue) vm.context().stack().pop();
    assertEquals(0, function.functionId());
    assertTrue(function.environment().size() == 0);
  }

  @Test
  void returnShouldPopCurrentFrameAndJumpToReturnAddress() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.jumpTo(5);
    vm.context().callStack().push(new CallFrame(42, new Scope(null), new ClosureEnvironment()));
    execute(vm, program(function(0, 10)), CallOpCode.RETURN);
    assertEquals(42, vm.instructionPointer());
    assertEquals(1, vm.context().callStack().size());
  }
}