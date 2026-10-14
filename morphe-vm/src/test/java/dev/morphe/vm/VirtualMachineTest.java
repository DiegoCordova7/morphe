package dev.morphe.vm;

import dev.morphe.bytecode.constants.IConstant;
import dev.morphe.bytecode.constants.IntegerConstant;
import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.*;
import dev.morphe.bytecode.program.*;
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.bytecode.type.PrimitiveBytecodeType;
import dev.morphe.runtime.value.primitive.*;
import dev.morphe.runtime.value.primitive.numeric.*;
import dev.morphe.vm.exception.argument.InvalidInstructionPointerException;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.execution.DivisionByZeroException;
import dev.morphe.vm.io.*;
import dev.morphe.vm.state.VMExecutionState;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VirtualMachineTest {

  private BytecodeProgram divisionByZeroAttemptProgram() {
    return new BytecodeProgram(
        List.of(
            new Instruction<>(StackOpCode.PUSH, 0),
            new Instruction<>(StackOpCode.PUSH, 1),
            new Instruction<>(ArithmeticOpCode.DIV),
            new Instruction<>(ControlOpCode.HALT),
            new Instruction<>(ControlOpCode.HALT)
        ),
        List.of(
            new IntegerConstant(10),
            new IntegerConstant(0)
        ),
        List.of(),
        List.of(entryFunction()),
        List.of(new AttemptRegion(0, 3, 4, List.of(new ErrorHandler(BytecodeErrorKind.DIVISION_BY_ZERO, 3)))),
        0
    );
  }

  private BytecodeProgram divisionByZeroRollbackProgram() {
    return new BytecodeProgram(
        List.of(
            new Instruction<>(StackOpCode.PUSH, 0),
            new Instruction<>(StackOpCode.PUSH, 1),
            new Instruction<>(ArithmeticOpCode.DIV),
            new Instruction<>(StackOpCode.PUSH, 2),
            new Instruction<>(ControlOpCode.HALT)
        ),
        List.of(
            new IntegerConstant(10),
            new IntegerConstant(0),
            new IntegerConstant(42)
        ),
        List.of(),
        List.of(entryFunction()),
        List.of(
            new AttemptRegion(0, 3, 4, List.of(new ErrorHandler(BytecodeErrorKind.DIVISION_BY_ZERO, 3)))
        ),
        0
    );
  }

  private BytecodeProgram divisionByZeroFallbackProgram() {
    return new BytecodeProgram(
        List.of(
            new Instruction<>(StackOpCode.PUSH, 0),
            new Instruction<>(StackOpCode.PUSH, 1),
            new Instruction<>(ArithmeticOpCode.DIV),
            new Instruction<>(ControlOpCode.HALT),
            new Instruction<>(StackOpCode.PUSH, 2),
            new Instruction<>(ControlOpCode.HALT)
        ),
        List.of(
            new IntegerConstant(10),
            new IntegerConstant(0),
            new IntegerConstant(99)
        ),
        List.of(),
        List.of(entryFunction()),
        List.of(
            new AttemptRegion(0, 3, 4, List.of(new ErrorHandler(BytecodeErrorKind.MODULO_BY_ZERO, 3)))
        ),
        0
    );
  }

  private BytecodeProgram divisionByZeroProgram() {
    return program(
        List.of(
            new Instruction<>(StackOpCode.PUSH, 0),
            new Instruction<>(StackOpCode.PUSH, 1),
            new Instruction<>(ArithmeticOpCode.DIV)
        ),
        List.of(
            new IntegerConstant(10),
            new IntegerConstant(0)
        )
    );
  }

  private BytecodeProgram nestedAttemptProgram() {
    return new BytecodeProgram(
        List.of(
            new Instruction<>(StackOpCode.PUSH, 0),
            new Instruction<>(StackOpCode.PUSH, 1),
            new Instruction<>(ArithmeticOpCode.DIV),
            new Instruction<>(StackOpCode.PUSH, 2),
            new Instruction<>(ControlOpCode.HALT),
            new Instruction<>(StackOpCode.PUSH, 3),
            new Instruction<>(ControlOpCode.HALT)
        ),
        List.of(
            new IntegerConstant(10),
            new IntegerConstant(0),
            new IntegerConstant(99),
            new IntegerConstant(42)
        ),
        List.of(),
        List.of(entryFunction()),
        List.of(
            new AttemptRegion(0, 5, 5, List.of(new ErrorHandler(BytecodeErrorKind.DIVISION_BY_ZERO, 5))),
            new AttemptRegion(0, 3, 3, List.of(new ErrorHandler(BytecodeErrorKind.DIVISION_BY_ZERO, 3)))
        ),
        0
    );
  }

  private BytecodeProgram pushProgram() {
    return program(
        List.of(
            new Instruction<>(StackOpCode.PUSH, 0),
            new Instruction<>(ControlOpCode.HALT)
        ),
        List.of(new IntegerConstant(42))
    );
  }

  private IOOpCode inputOpcode(PendingInputKind inputKind) {
    return switch (inputKind) {
      case STRING -> IOOpCode.INPUT_STRING;
      case INTEGER -> IOOpCode.INPUT_INTEGER;
      case DOUBLE -> IOOpCode.INPUT_DOUBLE;
      case BOOLEAN -> IOOpCode.INPUT_BOOLEAN;
    };
  }

  private BytecodeProgram inputProgram(PendingInputKind inputKind) {
    return program(
        List.of(
            new Instruction<>(inputOpcode(inputKind)),
            new Instruction<>(ControlOpCode.HALT)
        )
    );
  }

  private FunctionBytecodeType functionType() {
    return new FunctionBytecodeType(List.of(), PrimitiveBytecodeType.INTEGER);
  }

  private FunctionDefinition entryFunction() {
    return new FunctionDefinition("main", functionType(), 0);
  }

  private BytecodeProgram program(List<Instruction<? extends IOpCode>> instructions) {
    return program(instructions, List.of());
  }

  private BytecodeProgram program(List<Instruction<? extends IOpCode>> instructions,
                                  List<IConstant> constants) {
    return new BytecodeProgram(
        instructions, constants, List.of(), List.of(entryFunction()), List.of(), 0
    );
  }

  private BytecodeProgram emptyProgram() {
    return program(List.of());
  }

  private BytecodeProgram haltProgram() {
    return program(
        List.of(new Instruction<>(ControlOpCode.HALT))
    );
  }

  private BytecodeProgram jumpProgram() {
    return program(
        List.of(
            new Instruction<>(ControlOpCode.JMP, 2),
            new Instruction<>(ControlOpCode.HALT),
            new Instruction<>(ControlOpCode.HALT)
        )
    );
  }

  private BytecodeProgram pushPopProgram() {
    return program(
        List.of(
            new Instruction<>(StackOpCode.PUSH, 0),
            new Instruction<>(StackOpCode.POP),
            new Instruction<>(ControlOpCode.HALT)
        ),
        List.of(new IntegerConstant(42))
    );
  }

  private BytecodeProgram inputStringProgram() {
    return program(
        List.of(
            new Instruction<>(IOOpCode.INPUT_STRING),
            new Instruction<>(ControlOpCode.HALT)
        )
    );
  }

  @Test
  void shouldRejectNullIOHandler() {
    assertThrows(NullArgumentException.class, () -> new VirtualMachine(null));
  }

  @Test
  void shouldInitializeInReadyState() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    assertEquals(VMExecutionState.READY, vm.state());
    assertFalse(vm.isHalted());
  }

  @Test
  void shouldInitializeInstructionPointerToZero() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    assertEquals(0, vm.instructionPointer());
  }

  @Test
  void shouldExposeIOHandler() {
    IVMIOHandler handler = output -> {};
    VirtualMachine vm = new VirtualMachine(handler);
    assertSame(handler, vm.ioHandler());
  }

  @Test
  void shouldInitializeExecutionContext() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    assertNotNull(vm.context());
  }

  @Test
  void shouldInitializeWithRootCallFrame() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    assertTrue(vm.context().hasFrame());
    assertNotNull(vm.context().currentFrame());
    assertNotNull(vm.context().scope());
  }

  @Test
  void shouldJumpToInstruction() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.jumpTo(10);
    assertEquals(10, vm.instructionPointer());
  }

  @Test
  void shouldRejectNegativeInstructionPointer() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    assertThrows(InvalidInstructionPointerException.class, () -> vm.jumpTo(-1));
  }

  @Test
  void shouldHaltExecution() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.halt();
    assertEquals(VMExecutionState.HALTED, vm.state());
    assertTrue(vm.isHalted());
  }

  @Test
  void shouldPauseForInput() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.pauseForInput(PendingInputKind.STRING);
    assertEquals(VMExecutionState.WAITING_FOR_INPUT, vm.state());
    assertFalse(vm.isHalted());
  }

  @Test
  void shouldRejectNullPendingInputKind() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    assertThrows(NullArgumentException.class, () -> vm.pauseForInput(null));
  }

  @Test
  void shouldIgnoreInputWhenNotWaiting() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.provideInput("Diego");
    assertEquals(VMExecutionState.READY, vm.state());
    assertEquals(0, vm.instructionPointer());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void shouldRejectNullProgram() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    assertThrows(NullArgumentException.class, () -> vm.execute(null));
  }

  @Test
  void shouldExecuteProgram() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.execute(emptyProgram());
    assertEquals(VMExecutionState.HALTED, vm.state());
  }

  @Test
  void shouldHaltWhenProgramHasNoInstructions() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.execute(emptyProgram());
    assertTrue(vm.isHalted());
  }

  @Test
  void shouldResetInstructionPointerBeforeExecution() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.jumpTo(10);
    vm.execute(emptyProgram());
    assertEquals(0, vm.instructionPointer());
  }

  @Test
  void shouldResetExecutionContextBeforeExecution() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.context().stack().push(new IntegerValue(42));
    vm.execute(emptyProgram());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void shouldCreateEntryCallFrame() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.execute(emptyProgram());
    assertTrue(vm.context().hasFrame());
    assertNotNull(vm.context().currentFrame());
  }

  @Test
  void shouldHaltWhenExecutingHaltInstruction() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.execute(haltProgram());
    assertEquals(VMExecutionState.HALTED, vm.state());
    assertTrue(vm.isHalted());
    assertEquals(0, vm.instructionPointer());
  }

  @Test
  void shouldFollowJumpInstruction() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.execute(jumpProgram());
    assertEquals(VMExecutionState.HALTED, vm.state());
    assertEquals(2, vm.instructionPointer());
  }

  @Test
  void shouldAdvanceInstructionPointerAfterInstruction() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.execute(pushPopProgram());
    assertEquals(VMExecutionState.HALTED, vm.state());
    assertEquals(2, vm.instructionPointer());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void shouldWaitForInput() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.execute(inputStringProgram());
    assertEquals(VMExecutionState.WAITING_FOR_INPUT, vm.state());
    assertEquals(0, vm.instructionPointer());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void shouldResumeAfterProvidingStringInput() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.execute(inputStringProgram());
    vm.provideInput("Diego");
    assertEquals(VMExecutionState.HALTED, vm.state());
    assertEquals(1, vm.instructionPointer());
    assertEquals(new StringValue("Diego"), vm.context().stack().peek());
  }

  @Test
  void shouldResumeAfterProvidingIntegerInput() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.execute(inputProgram(PendingInputKind.INTEGER));
    vm.provideInput("42");
    assertEquals(VMExecutionState.HALTED, vm.state());
    assertEquals(1, vm.instructionPointer());
    assertEquals(new IntegerValue(42), vm.context().stack().peek());
  }

  @Test
  void shouldResumeAfterProvidingDoubleInput() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.execute(inputProgram(PendingInputKind.DOUBLE));
    vm.provideInput("42.5");
    assertEquals(VMExecutionState.HALTED, vm.state());
    assertEquals(1, vm.instructionPointer());
    assertEquals(new DoubleValue(42.5), vm.context().stack().peek());
  }

  @Test
  void shouldResumeAfterProvidingBooleanInput() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.execute(inputProgram(PendingInputKind.BOOLEAN));
    vm.provideInput("true");
    assertEquals(VMExecutionState.HALTED, vm.state());
    assertEquals(1, vm.instructionPointer());
    assertEquals(new BooleanValue(true), vm.context().stack().peek());
  }

  @Test
  void shouldRemainWaitingAfterInvalidIntegerInput() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.execute(inputProgram(PendingInputKind.INTEGER));
    assertThrows(NumberFormatException.class, () -> vm.provideInput("invalid"));
    vm.provideInput("42");
    assertEquals(VMExecutionState.HALTED, vm.state());
    assertEquals(new IntegerValue(42), vm.context().stack().peek());
  }

  @Test
  void shouldResetStateWhenExecutingAnotherProgram() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.execute(pushPopProgram());
    assertEquals(VMExecutionState.HALTED, vm.state());
    assertTrue(vm.context().stack().isEmpty());
    vm.execute(haltProgram());
    assertEquals(VMExecutionState.HALTED, vm.state());
    assertEquals(0, vm.instructionPointer());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void shouldClearPreviousExecutionContext() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.execute(pushProgram());
    assertEquals(new IntegerValue(42), vm.context().stack().peek());
    vm.execute(emptyProgram());
    assertEquals(VMExecutionState.HALTED, vm.state());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void shouldStartNewExecutionFromReadyState() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.execute(inputProgram(PendingInputKind.STRING));
    assertEquals(VMExecutionState.WAITING_FOR_INPUT, vm.state());
    vm.provideInput("Diego");
    assertEquals(VMExecutionState.HALTED, vm.state());
    vm.execute(inputProgram(PendingInputKind.INTEGER));
    assertEquals(VMExecutionState.WAITING_FOR_INPUT, vm.state());
    assertEquals(0, vm.instructionPointer());
    assertTrue(vm.context().stack().isEmpty());
  }

  @Test
  void shouldHandleDivisionByZeroWithMatchingAttemptHandler() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.execute(divisionByZeroAttemptProgram());
    assertEquals(VMExecutionState.HALTED, vm.state());
    assertEquals(3, vm.instructionPointer());
  }

  @Test
  void shouldRestoreExecutionContextBeforeHandlingAttemptError() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.execute(divisionByZeroRollbackProgram());
    assertEquals(VMExecutionState.HALTED, vm.state());
    assertEquals(4, vm.instructionPointer());
    assertEquals(new IntegerValue(42), vm.context().stack().peek());
    assertEquals(1, vm.context().stack().size());
  }

  @Test
  void shouldUseAttemptFallbackWhenNoHandlerMatches() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.execute(divisionByZeroFallbackProgram());
    assertEquals(VMExecutionState.HALTED, vm.state());
    assertEquals(5, vm.instructionPointer());
    assertEquals(new IntegerValue(99), vm.context().stack().peek());
  }

  @Test
  void shouldPropagateExecutionErrorOutsideAttempt() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    assertThrows(DivisionByZeroException.class, () -> vm.execute(divisionByZeroProgram()));
  }

  @Test
  void shouldUseInnermostAttemptRegion() {
    VirtualMachine vm = new VirtualMachine(output -> {});
    vm.execute(nestedAttemptProgram());
    assertEquals(VMExecutionState.HALTED, vm.state());
    assertEquals(4, vm.instructionPointer());
    assertEquals(new IntegerValue(99), vm.context().stack().peek());
  }
}