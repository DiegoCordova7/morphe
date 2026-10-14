package dev.morphe.vm;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.IOpCode;
import dev.morphe.bytecode.program.*;
import dev.morphe.runtime.environment.*;
import dev.morphe.runtime.value.primitive.*;
import dev.morphe.runtime.value.primitive.numeric.*;
import dev.morphe.vm.exception.argument.InvalidInstructionPointerException;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.execution.*;
import dev.morphe.vm.executor.InstructionExecutor;
import dev.morphe.vm.io.*;
import dev.morphe.vm.memory.*;
import dev.morphe.vm.scope.*;
import dev.morphe.vm.stack.*;
import dev.morphe.vm.state.*;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Represents the Morphe virtual machine.
 *
 * <p>
 * The {@code VirtualMachine} is responsible for executing a
 * {@link BytecodeProgram} and maintaining the mutable state required during
 * program execution.
 * </p>
 *
 * <p>
 * Each {@code VirtualMachine} instance owns a single
 * {@link ExecutionContext}, which encapsulates all mutable runtime state,
 * including the operand {@link ValueStack}, execution {@link Memory},
 * {@link Heap}, and the active {@link CallStack}. The VM also maintains an instruction
 * pointer that identifies the position of the instruction currently being executed or
 * the next instruction to be executed.
 * </p>
 *
 * <p>
 * The instruction pointer is reset to {@code 0} whenever a new program begins
 * execution. Under normal sequential execution, the instruction pointer is
 * automatically advanced after each instruction. Control-flow instructions
 * may explicitly modify the instruction pointer to alter the execution flow.
 * </p>
 *
 * <p>
 * The VM also maintains a halted state. Execution stops when the VM is halted
 * or when the instruction pointer reaches the end of the program.
 * </p>
 */
public final class VirtualMachine {

  private record AttemptCheckpoint(AttemptRegion region, ExecutionContext.Snapshot snapshot) {}

  private Deque<AttemptCheckpoint> attemptCheckpoints;
  private ExecutionContext context;
  private final InstructionExecutor executor;
  private final IVMIOHandler ioHandler;
  private int instructionPointer;
  private VMExecutionState state;
  private PendingInputKind pendingInput;
  private BytecodeProgram program;

  /**
   * Creates a new {@code VirtualMachine} in its initial execution state.
   *
   * <p>
   * The virtual machine creates an execution context with its default
   * execution resources. The context owns the operand stack, memory,
   * allocator, heap, and initial root call frame.
   * </p>
   */
  public VirtualMachine(IVMIOHandler ioHandler) {
    if (ioHandler == null) throw new NullArgumentException("ioHandler");
    this.ioHandler = ioHandler;
    this.executor = new InstructionExecutor();
    resetExecutionState();
  }

  /**
   * Reinitializes the execution state.
   *
   * <p>
   * This method discards the current execution state and creates a fresh
   * execution context containing empty runtime structures together with
   * the root call frame and the global scope.
   * </p>
   *
   * <p>
   * It is invoked when the virtual machine is created and before executing
   * each new bytecode program.
   * </p>
   */
  private void resetExecutionState() {
    attemptCheckpoints = new ArrayDeque<>();
    context = new ExecutionContext();
    instructionPointer = 0;
    state = VMExecutionState.READY;
    pendingInput = null;
  }

  /**
   * Executes the given bytecode program.
   *
   * <p>
   * Before execution begins, the virtual machine resets its execution
   * context and execution state. A fresh execution context containing
   * the global scope and root call frame is created, followed by a
   * call frame for the program's entry function.
   * </p>
   *
   * <p>
   * The VM then executes instructions sequentially until it is halted or
   * the instruction pointer reaches the end of the program.
   * </p>
   *
   * <p>
   * After each instruction is executed, the VM automatically advances the
   * instruction pointer by one position only when the instruction did not
   * modify it. This allows control-flow instructions such as jumps and
   * function calls to take direct control over subsequent execution.
   * </p>
   *
   * @param program the bytecode program to execute
   *
   * @throws NullArgumentException if {@code program} is {@code null}
   */
  public void execute(BytecodeProgram program) {
    if (program == null) throw new NullArgumentException("program");
    resetExecutionState();
    this.program = program;
    FunctionDefinition entry = program.functionAt(program.entryFunction());
    CallFrame frame = new CallFrame(program.instructionCount(), new Scope(null), new ClosureEnvironment());
    context.callStack().push(frame);
    instructionPointer = entry.entryPoint();
    state = VMExecutionState.RUNNING;
    run();
  }

  /**
   * Continues executing the current bytecode program while the VM is running.
   *
   * <p>Each instruction is executed at the current instruction pointer. If
   * the instruction does not modify the instruction pointer, it is advanced
   * automatically. Execution stops when the VM is halted, enters a waiting
   * state, or reaches the end of the program.</p>
   *
   * <p>Active {@code attempt} regions are entered and exited as the
   * instruction pointer moves through their ranges. Execution exceptions are
   * delegated to the attempt error-handling mechanism.</p>
   */
  private void run() {
    while (isRunning()) {
      if (instructionPointer >= program.instructionCount()) {
        state = VMExecutionState.HALTED;
        return;
      }
      enterAttempts(program);
      Instruction<? extends IOpCode> instruction = program.instructionAt(instructionPointer);
      int previousInstructionPointer = instructionPointer;
      try {
        executor.execute(this, program, instruction);
      } catch (VMExecutionException exception) {
        handleExecutionError(exception);
      }
      if (isRunning() && instructionPointer == previousInstructionPointer) instructionPointer++;
      exitAttempts();
    }
  }

  /**
   * Creates execution checkpoints for attempt regions beginning at the
   * current instruction pointer.
   *
   * <p>Each checkpoint stores the complete execution context at the beginning
   * of its corresponding attempt region so that the state can be restored if
   * an execution error occurs within that region.</p>
   *
   * @param program the bytecode program containing the attempt regions
   */
  private void enterAttempts(BytecodeProgram program) {
    for (AttemptRegion region : program.attemptRegions()) {
      if (region.start() == instructionPointer)
        attemptCheckpoints.addLast(new AttemptCheckpoint(region, context.snapshot()));
    }
  }

  /**
   * Removes attempt checkpoints whose regions no longer contain the current
   * instruction pointer.
   *
   * <p>A checkpoint remains active while execution is inside its associated
   * attempt region. Once execution leaves the region, the checkpoint is no
   * longer required.</p>
   */
  private void exitAttempts() {
    attemptCheckpoints.removeIf(checkpoint -> !checkpoint.region().contains(instructionPointer));
  }

  /**
   * Handles an execution error raised while executing an instruction.
   *
   * <p>If an active attempt region exists, the VM restores the execution
   * context captured when entering that region and transfers control to the
   * matching error handler or to the region's fallback entry point when no
   * matching handler exists.</p>
   *
   * <p>If no active attempt region exists, the original exception is
   * propagated to the caller.</p>
   *
   * @param exception the execution error raised by the instruction executor
   *
   * @throws VMExecutionException if no active attempt region can handle the
   *     exception
   */
  private void handleExecutionError(VMExecutionException exception) {
    AttemptCheckpoint checkpoint = findActiveAttempt();
    if (checkpoint == null) throw exception;
    context.restore(checkpoint.snapshot());
    BytecodeErrorKind errorKind = errorKind(exception);
    for (dev.morphe.bytecode.program.ErrorHandler handler : checkpoint.region().handlers()) {
      if (handler.errorKind() == errorKind) {
        jumpTo(handler.entryPoint());
        return;
      }
    }
    jumpTo(checkpoint.region().fallback());
  }

  /**
   * Finds the innermost active attempt region containing the current
   * instruction pointer.
   *
   * <p>When multiple nested attempt regions contain the current instruction,
   * the region with the smallest range is selected.</p>
   *
   * @return the innermost active attempt checkpoint, or {@code null} if no
   *     active attempt contains the current instruction
   */
  private AttemptCheckpoint findActiveAttempt() {
    AttemptCheckpoint active = null;
    for (AttemptCheckpoint checkpoint : attemptCheckpoints) {
      AttemptRegion region = checkpoint.region();
      if (!region.contains(instructionPointer)) continue;
      if (active == null || region.end() - region.start() < active.region().end() - active.region().start()) {
        active = checkpoint;
      }
    }

    return active;
  }

  /**
   * Maps a virtual machine execution exception to its corresponding bytecode
   * error kind.
   *
   * @param exception the execution exception to classify
   *
   * @return the bytecode error kind associated with the exception
   *
   * @throws VMExecutionException if the exception does not have a supported
   *     bytecode error mapping
   */
  private BytecodeErrorKind errorKind(VMExecutionException exception) {
    if (exception instanceof DivisionByZeroException) return BytecodeErrorKind.DIVISION_BY_ZERO;
    if (exception instanceof ModuleByZeroException) return BytecodeErrorKind.MODULO_BY_ZERO;
    if (exception instanceof InvalidArrayIndexException) return BytecodeErrorKind.INVALID_ARRAY_INDEX;
    throw exception;
  }

  /**
   * Returns the execution context associated with this virtual machine.
   *
   * <p>
   * The context contains the operand stack and memory used during program
   * execution.
   * </p>
   *
   * @return the virtual machine execution context
   */
  public ExecutionContext context() {
    return context;
  }

  /**
   * Returns the I/O handler used by this virtual machine.
   *
   * <p>The handler provides the external input and output operations
   * required during bytecode execution. The virtual machine does not
   * depend on a concrete I/O environment.</p>
   *
   * @return the virtual machine I/O handler
   */
  public IVMIOHandler ioHandler() {
    return ioHandler;
  }

  /**
   * Returns the current instruction pointer.
   *
   * <p>
   * The instruction pointer identifies the position of the current or next
   * instruction in the executing program. When execution finishes normally,
   * it points immediately after the last executed instruction.
   * </p>
   *
   * @return the current instruction pointer
   */
  public int instructionPointer() {
    return instructionPointer;
  }

  /**
   * Changes the instruction pointer to the specified target position.
   *
   * <p>
   * This method is intended to be used by control-flow instructions to alter
   * the normal sequential execution of a bytecode program. When an instruction
   * changes the instruction pointer, the VM preserves the new value instead of
   * automatically advancing it.
   * </p>
   *
   * @param instructionPointer the target instruction position
   *
   * @throws InvalidInstructionPointerException if {@code instructionPointer}
   *     is negative
   */
  public void jumpTo(int instructionPointer) {
    if (instructionPointer < 0) throw new InvalidInstructionPointerException(instructionPointer);
    this.instructionPointer = instructionPointer;
  }

  /**
   * Halts execution of the virtual machine.
   *
   * <p>
   * The current instruction finishes execution, after which the VM stops
   * processing further instructions.
   * </p>
   */
  public void halt() {
    state = VMExecutionState.HALTED;
  }

  /**
   * Returns whether this virtual machine has been halted.
   *
   * @return {@code true} if execution has been halted;
   *     {@code false} otherwise
   */
  public boolean isHalted() {
    return state == VMExecutionState.HALTED;
  }

  private boolean isRunning() {
    return state == VMExecutionState.RUNNING;
  }

  /**
   * Returns the current execution state of the virtual machine.
   *
   * <p>The execution state indicates whether the VM is ready to execute a
   * program, currently running, waiting for input, or halted.</p>
   *
   * @return the current virtual machine execution state
   */
  public VMExecutionState state() {
    return state;
  }

  /**
   * Suspends execution while waiting for an input value.
   *
   * <p>The VM enters the {@link VMExecutionState#WAITING_FOR_INPUT} state
   * and records the expected input type. Execution can be resumed by calling
   * {@link #provideInput(String)}.</p>
   *
   * @param pendingInputKind the type of value expected from the input
   *
   * @throws NullArgumentException if {@code pendingInputKind} is {@code null}
   */
  public void pauseForInput(PendingInputKind pendingInputKind) {
    if (pendingInputKind == null) throw new NullArgumentException("pendingInputKind");
    state = VMExecutionState.WAITING_FOR_INPUT;
    pendingInput = pendingInputKind;
  }

  /**
   * Provides the input value requested by the virtual machine and resumes
   * execution.
   *
   * <p>If the VM is not currently waiting for input, this method has no effect.
   * When input is pending, the supplied text is converted according to the
   * {@link PendingInputKind} recorded by the VM and the resulting value is
   * pushed onto the operand stack.</p>
   *
   * <p>After the value is provided, the pending input state is cleared, the
   * instruction pointer is advanced past the input instruction, and execution
   * resumes.</p>
   *
   * @param input the input text provided to the virtual machine
   *
   * @throws NullArgumentException if the VM is waiting for input but
   *     {@code input} is {@code null}, or if the pending input type is missing
   * @throws NumberFormatException if the expected input type is numeric and
   *     {@code input} cannot be parsed as the corresponding number
   */
  public void provideInput(String input) {
    if (state != VMExecutionState.WAITING_FOR_INPUT) return;
    if (pendingInput == null) throw new NullArgumentException("pendingInputKind");
    if (input == null) throw new NullArgumentException("input");
    switch (pendingInput) {
      case STRING -> context.stack().push(new StringValue(input));
      case INTEGER -> context.stack().push(new IntegerValue(Integer.parseInt(input)));
      case DOUBLE -> context.stack().push(new DoubleValue(Double.parseDouble(input)));
      case BOOLEAN -> context.stack().push(new BooleanValue(Boolean.parseBoolean(input)));
    }
    pendingInput = null;
    instructionPointer++;
    state = VMExecutionState.RUNNING;
    run();
  }
}