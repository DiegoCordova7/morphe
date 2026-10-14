package dev.morphe.vm.executor;

import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.*;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.internal.instruction.UnsupportedOpcodeException;

import java.util.HashMap;
import java.util.Map;

/**
 * Dispatches bytecode instructions to their corresponding instruction executor.
 *
 * <p>Instructions in the Morphe VM are grouped by opcode family (such as
 * stack, arithmetic, logical, or control-flow instructions). Each family is
 * handled by a dedicated {@link IExecutor} implementation responsible for
 * executing every opcode belonging to that category.</p>
 *
 * <p>This dispatcher determines the opcode family of an instruction and
 * delegates its execution to the appropriate executor.</p>
 */
public final class InstructionExecutor {

  /**
   * Maps opcode families to their corresponding executors.
   */
  private final Map<Class<? extends IOpCode>, IExecutor<? extends IOpCode>> executors = new HashMap<>();

  /**
   * Creates a dispatcher with the default instruction executors.
   *
   * <p>Each supported opcode family is associated with the executor
   * responsible for handling its instructions.</p>
   */
  public InstructionExecutor() {
    executors.put(StackOpCode.class, new StackExecutor());
    executors.put(ArithmeticOpCode.class, new ArithmeticExecutor());
    executors.put(ComparisonOpCode.class, new ComparisonExecutor());
    executors.put(LogicalOpCode.class, new LogicalExecutor());
    executors.put(ControlOpCode.class, new ControlExecutor());
    executors.put(ScopeOpCode.class, new ScopeExecutor());
    executors.put(CallOpCode.class, new CallExecutor());
    executors.put(CompositeOpCode.class, new CompositeExecutor());
    executors.put(ArrayOpCode.class, new ArrayExecutor());
    executors.put(IOOpCode.class, new IOExecutor());
  }

  /**
   * Returns the executor registered for the given opcode family.
   *
   * @param type the opcode family type
   * @param <T> the opcode type handled by the executor
   * @return the registered executor, or {@code null} if no executor is
   *     registered for the given type
   */
  @SuppressWarnings("unchecked")
  private <T extends IOpCode> IExecutor<T> executor(Class<? extends IOpCode> type) {
    return (IExecutor<T>) executors.get(type);
  }

  /**
   * Executes a single bytecode instruction.
   *
   * <p>The instruction is dispatched according to the runtime type of its
   * opcode family. If no executor is registered for that family, execution
   * fails.</p>
   *
   * @param vm the virtual machine executing the program
   * @param program the program being executed
   * @param instruction the instruction to execute
   * @param <T> the opcode type of the instruction
   * @throws NullArgumentException if any argument is {@code null}
   * @throws UnsupportedOpcodeException if no executor is registered for
   *     the instruction opcode family
   */
  public <T extends IOpCode> void execute(VirtualMachine vm, BytecodeProgram program,
                                          Instruction<T> instruction) {
    if (vm == null) throw new NullArgumentException("vm");
    if (program == null) throw new NullArgumentException("program");
    if (instruction == null) throw new NullArgumentException("instruction");
    IExecutor<T> executor = executor(instruction.opcode().getClass());
    if (executor == null) throw new UnsupportedOpcodeException(instruction.opcode());
    executor.execute(vm, program, instruction);
  }
}