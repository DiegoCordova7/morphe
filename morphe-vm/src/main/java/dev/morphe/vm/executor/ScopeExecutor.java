package dev.morphe.vm.executor;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.ScopeOpCode;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.vm.ExecutionContext;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.operations.ScopeOperations;

/**
 * Executes scope-related bytecode instructions.
 *
 * <p>
 * This executor maps {@link ScopeOpCode scope opcodes} to their
 * corresponding scope operations. The actual scope management,
 * variable resolution, memory interaction, and closure capture
 * interaction are delegated to {@link ScopeOperations}.
 * </p>
 *
 * <p>
 * Supported operations include:
 * </p>
 *
 * <ul>
 *   <li>{@code DECLARE} – declares a variable slot in the current scope.</li>
 *   <li>{@code LOAD} – loads the value associated with a variable slot onto the operand stack.</li>
 *   <li>{@code STORE} – stores the top stack value into the variable slot.</li>
 *   <li>{@code LOAD_CAPTURE} – loads a captured value onto the operand stack.</li>
 *   <li>{@code STORE_CAPTURE} – stores the top stack value into a captured variable.</li>
 *   <li>{@code ENTER_SCOPE} – creates and enters a new nested runtime scope.</li>
 *   <li>{@code EXIT_SCOPE} – exits the current runtime scope.</li>
 * </ul>
 */
public final class ScopeExecutor implements IExecutor<ScopeOpCode> {

  /**
   * Executes a single scope instruction.
   *
   * @param vm the virtual machine executing the instruction
   * @param program the program containing the instruction
   * @param instruction the instruction to execute
   */
  @Override
  public void execute(VirtualMachine vm, BytecodeProgram program, Instruction<ScopeOpCode> instruction) {
    switch (instruction.opcode()) {
      case DECLARE -> declare(vm, instruction);
      case LOAD -> load(vm, instruction);
      case STORE -> store(vm, instruction);
      case LOAD_CAPTURE -> loadCapture(vm, instruction);
      case STORE_CAPTURE -> storeCapture(vm, instruction);
      case ENTER_SCOPE -> enterScope(vm);
      case EXIT_SCOPE -> exitScope(vm);
    }
  }

  private void declare(VirtualMachine vm, Instruction<ScopeOpCode> instruction) {
    ExecutionContext context = vm.context();
    ScopeOperations.declare(context.allocator(), context.scope(), instruction.operand(0));
  }

  private void load(VirtualMachine vm, Instruction<ScopeOpCode> instruction) {
    ExecutionContext context = vm.context();
    ScopeOperations.load(context.stack(), context.memory(), context.scope(), instruction.operand(0));
  }

  private void store(VirtualMachine vm, Instruction<ScopeOpCode> instruction) {
    ExecutionContext context = vm.context();
    ScopeOperations.store(context.stack(), context.memory(), context.scope(), instruction.operand(0));
  }

  private void loadCapture(VirtualMachine vm, Instruction<ScopeOpCode> instruction) {
    ScopeOperations.loadCapture(vm.context().stack(), vm.context().currentFrame().environment(), instruction.operand(0));
  }

  private void storeCapture(VirtualMachine vm, Instruction<ScopeOpCode> instruction) {
    ScopeOperations.storeCapture(vm.context().stack(), vm.context().currentFrame().environment(), instruction.operand(0));
  }

  private void enterScope(VirtualMachine vm) {
    ScopeOperations.enterScope(vm.context());
  }

  private void exitScope(VirtualMachine vm) {
    ScopeOperations.exitScope(vm.context());
  }

}