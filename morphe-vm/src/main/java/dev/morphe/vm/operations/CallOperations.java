package dev.morphe.vm.operations;

import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.bytecode.program.CaptureDefinition;
import dev.morphe.bytecode.program.FunctionDefinition;
import dev.morphe.runtime.environment.ClosureEnvironment;
import dev.morphe.runtime.value.callable.FunctionValue;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.exception.internal.call.RootFrameReturnException;
import dev.morphe.vm.scope.CallFrame;
import dev.morphe.vm.scope.Scope;

/**
 * Provides operations related to function invocation, closure creation,
 * and function return.
 *
 * <p>
 * These operations implement the runtime semantics of call-related
 * bytecode instructions. They are responsible for creating and destroying
 * {@link CallFrame call frames}, managing function-local scopes,
 * creating closure environments, and updating the instruction pointer.
 * </p>
 *
 * <p>
 * Function definitions are resolved through the
 * {@link BytecodeProgram} function table.
 * </p>
 */
public final class CallOperations {

  /**
   * Prevents instantiation of this utility class.
   */
  private CallOperations() {}

  /**
   * Invokes a function referenced by a function identifier.
   *
   * <p>
   * The function definition is resolved from the program function table.
   * A new call frame is created with an empty closure environment because
   * the function is invoked directly and does not carry a captured
   * environment.
   * </p>
   *
   * <p>
   * Arguments are consumed from the operand stack in reverse order and
   * bound to the function's parameter slots.
   * </p>
   *
   * @param vm the virtual machine performing the invocation
   * @param program the program containing the function definitions
   * @param functionIndex the index of the function to invoke
   * @param argumentCount the number of arguments supplied by the caller
   *
   * @throws IllegalArgumentException if the argument count does not match
   *     the function parameter count
   */
  public static void call(VirtualMachine vm, BytecodeProgram program, int functionIndex, int argumentCount) {
    FunctionDefinition definition = program.functionAt(functionIndex);
    int parameterCount = definition.type().parameterTypes().size();
    if (argumentCount != parameterCount)
      throw new IllegalArgumentException("Expected " + parameterCount + " arguments but received " + argumentCount);
    int returnAddress = vm.instructionPointer() + 1;
    CallFrame frame = new CallFrame(returnAddress, new Scope(null), new ClosureEnvironment());
    vm.context().callStack().push(frame);
    for (int slot = argumentCount - 1; slot >= 0; slot--) {
      ScopeOperations.declare(vm.context().allocator(), frame.scope(), slot);
      ScopeOperations.store(vm.context().stack(), vm.context().memory(), frame.scope(), slot);
    }
    vm.jumpTo(definition.entryPoint());
  }

  /**
   * Invokes a function value from the operand stack.
   *
   * <p>
   * The function value is removed from the operand stack. Its function
   * identifier and captured environment are used to create the new call frame.
   * </p>
   *
   * @param vm the virtual machine performing the invocation
   * @param program the program containing the function definition
   */
  public static void callValue(VirtualMachine vm, BytecodeProgram program) {
    FunctionValue function = (FunctionValue) vm.context().stack().pop();
    call(vm, program, function.functionId(), function);
  }

  /**
   * Invokes a function value using its captured environment.
   *
   * <p>
   * The captured environment is assigned directly to the new call frame,
   * preserving access to the variables captured by the function.
   * </p>
   *
   * @param vm the virtual machine performing the invocation
   * @param program the program containing the function definition
   * @param functionIndex the index of the function to invoke
   * @param function the function value being invoked
   */
  private static void call(VirtualMachine vm, BytecodeProgram program, int functionIndex, FunctionValue function) {
    FunctionDefinition definition = program.functionAt(functionIndex);
    int returnAddress = vm.instructionPointer() + 1;
    CallFrame frame = new CallFrame(returnAddress, new Scope(null), function.environment());
    vm.context().callStack().push(frame);
    vm.jumpTo(definition.entryPoint());
  }

  /**
   * Creates a closure for the specified function.
   *
   * <p>
   * The function's capture definitions are resolved against the current
   * call frame. Captured variables are stored by reference in the resulting
   * closure environment.
   * </p>
   *
   * @param vm the virtual machine creating the closure
   * @param program the program containing the function definition
   * @param functionIndex the index of the function
   */
  public static void createClosure(VirtualMachine vm, BytecodeProgram program, int functionIndex) {
    FunctionDefinition definition = program.functionAt(functionIndex);
    ClosureEnvironment environment = new ClosureEnvironment();
    for (CaptureDefinition capture : definition.captures()) {
      switch (capture.sourceKind()) {
        case LOCAL -> environment.capture(vm.context().scope().resolveCell(vm.context().memory(), capture.index()));
        case CAPTURE -> environment.capture(vm.context().currentFrame().environment().get(capture.index()));
      }
    }
    vm.context().stack().push(new FunctionValue(functionIndex, environment));
  }

  /**
   * Returns from the current function.
   *
   * <p>
   * The current {@link CallFrame} is removed from the call stack and
   * execution resumes at its stored return address.
   * </p>
   *
   * <p>
   * Returning from the root call frame is not permitted because it does not
   * represent a function invocation and therefore has no caller to resume.
   * </p>
   *
   * @param vm the virtual machine performing the return
   *
   * @throws RootFrameReturnException if the current call frame is the root
   *     frame of the virtual machine
   */
  public static void ret(VirtualMachine vm) {
    if (vm.context().callStack().size() == 1) throw new RootFrameReturnException();
    CallFrame frame = vm.context().callStack().pop();
    vm.jumpTo(frame.returnAddress());
  }

}