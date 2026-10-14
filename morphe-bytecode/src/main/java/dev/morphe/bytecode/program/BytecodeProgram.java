package dev.morphe.bytecode.program;

import dev.morphe.bytecode.constants.IConstant;
import dev.morphe.bytecode.exception.argument.InvalidIndexException;
import dev.morphe.bytecode.exception.argument.NullArgumentException;
import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.IOpCode;
import dev.morphe.bytecode.type.IBytecodeType;

import java.util.List;

/**
 * Represents an immutable Morphe bytecode program.
 *
 * <p>
 * A bytecode program consists of an ordered sequence of
 * {@link Instruction instructions}, a constant pool, a type table,
 * a function table, and a table of attempt regions.
 * </p>
 *
 * <p>
 * The constant pool stores constant values referenced by instructions,
 * the type table stores bytecode-level type information, and the function
 * table stores the functions defined by the program.
 * </p>
 *
 * <p>
 * Bytecode types are represented independently from the Morphe runtime
 * type system. They describe type information encoded in the compiled
 * program without depending on runtime type representations.
 * </p>
 *
 * <p>
 * Function definitions describe the signature and entry point of each
 * function contained in the program. Function identifiers used by
 * {@code CALL} instructions correspond to the position of a function
 * definition within the function table.
 * </p>
 *
 * <p>
 * Instructions are stored in a single global instruction sequence and are
 * executed sequentially unless modified by control-flow operations such as
 * jumps or function calls.
 * </p>
 *
 * <p>
 * Attempt regions describe protected instruction ranges and the handlers
 * used to recover from recoverable execution errors.
 * </p>
 *
 * <p>
 * Instances of this class are immutable. The instruction list,
 * constant pool, type table, function table, and attempt region table
 * are defensively copied during construction and cannot be modified
 * afterwards.
 * </p>
 */
public final class BytecodeProgram {

  private final List<Instruction<? extends IOpCode>> instructions;
  private final List<IConstant> constants;
  private final List<IBytecodeType> types;
  private final List<FunctionDefinition> functions;
  private final List<AttemptRegion> attemptRegions;
  private final int entryFunction;

  /**
   * Creates a new immutable bytecode program.
   *
   * @param instructions the instruction sequence that composes the program
   * @param constants the constant pool referenced by instructions
   * @param types the bytecode-level type table referenced by instructions
   * @param functions the function definitions contained in the program
   * @param attemptRegions the attempt regions contained in the program
   * @param entryFunction the index of the function used as the program entry point
   * @throws NullArgumentException if {@code instructions},
   * {@code constants}, {@code types}, {@code functions}, or
   * {@code attemptRegions} is {@code null}
   * @throws InvalidIndexException if {@code entryFunction} does not refer
   * to a function in the function table
   */
  public BytecodeProgram(List<? extends Instruction<? extends IOpCode>> instructions,
                         List<IConstant> constants, List<IBytecodeType> types,
                         List<FunctionDefinition> functions, List<AttemptRegion> attemptRegions,
                         int entryFunction) {
    if (instructions == null) throw new NullArgumentException("instructions");
    if (constants == null) throw new NullArgumentException("constants");
    if (types == null) throw new NullArgumentException("types");
    if (functions == null) throw new NullArgumentException("functions");
    if (attemptRegions == null) throw new NullArgumentException("attemptRegions");
    if (entryFunction < 0 || entryFunction >= functions.size())
      throw new InvalidIndexException("function table", entryFunction, functions.size());
    this.instructions = List.copyOf(instructions);
    this.constants = List.copyOf(constants);
    this.types = List.copyOf(types);
    this.functions = List.copyOf(functions);
    this.attemptRegions = List.copyOf(attemptRegions);
    this.entryFunction = entryFunction;
  }

  /**
   * Returns the function used as the program entry point.
   *
   * @return the entry function index
   */
  public int entryFunction() {
    return entryFunction;
  }

  /**
   * Returns the instructions that compose this program.
   *
   * @return an immutable list of instructions
   */
  public List<Instruction<? extends IOpCode>> instructions() {
    return instructions;
  }

  /**
   * Returns the instruction located at the specified position.
   *
   * @param index the instruction index
   * @return the instruction at the given index
   * @throws InvalidIndexException if the index is invalid
   */
  public Instruction<? extends IOpCode> instructionAt(int index) {
    validateIndex("instruction table", index, instructions.size());
    return instructions.get(index);
  }

  /**
   * Returns the number of instructions contained in this program.
   *
   * @return the instruction count
   */
  public int instructionCount() {
    return instructions.size();
  }

  /**
   * Returns whether this program contains no instructions.
   *
   * @return {@code true} if the program is empty;
   * {@code false} otherwise
   */
  public boolean isEmpty() {
    return instructions.isEmpty();
  }

  /**
   * Returns the constant pool associated with this program.
   *
   * @return an immutable list of constants
   */
  public List<IConstant> constants() {
    return constants;
  }

  /**
   * Returns the constant located at the specified position.
   *
   * @param index the constant index
   * @return the constant at the given index
   * @throws InvalidIndexException if the index is invalid
   */
  public IConstant constantAt(int index) {
    validateIndex("constant pool", index, constants.size());
    return constants.get(index);
  }

  /**
   * Returns the number of constants stored in the constant pool.
   *
   * @return the constant count
   */
  public int constantCount() {
    return constants.size();
  }

  /**
   * Returns the bytecode-level type table associated with this program.
   *
   * @return an immutable list of bytecode types
   */
  public List<IBytecodeType> types() {
    return types;
  }

  /**
   * Returns the bytecode type located at the specified position.
   *
   * @param index the type index
   * @return the bytecode type at the given index
   * @throws InvalidIndexException if the index is invalid
   */
  public IBytecodeType typeAt(int index) {
    validateIndex("type table", index, types.size());
    return types.get(index);
  }

  /**
   * Returns the number of types stored in the type table.
   *
   * @return the type count
   */
  public int typeCount() {
    return types.size();
  }

  /**
   * Returns the function definitions contained in this program.
   *
   * @return an immutable list of function definitions
   */
  public List<FunctionDefinition> functions() {
    return functions;
  }

  /**
   * Returns the function definition located at the specified position.
   *
   * @param index the function index
   * @return the function definition at the given index
   * @throws InvalidIndexException if the index is invalid
   */
  public FunctionDefinition functionAt(int index) {
    validateIndex("function table", index, functions.size());
    return functions.get(index);
  }

  /**
   * Returns the number of function definitions contained in this program.
   *
   * @return the function count
   */
  public int functionCount() {
    return functions.size();
  }

  /**
   * Returns the attempt regions contained in this program.
   *
   * @return an immutable list of attempt regions
   */
  public List<AttemptRegion> attemptRegions() {
    return attemptRegions;
  }

  /**
   * Returns the attempt region located at the specified position.
   *
   * @param index the attempt region index
   * @return the attempt region at the given index
   * @throws InvalidIndexException if the index is invalid
   */
  public AttemptRegion attemptRegionAt(int index) {
    validateIndex("attempt region table", index, attemptRegions.size());
    return attemptRegions.get(index);
  }

  /**
   * Returns the number of attempt regions contained in this program.
   *
   * @return the attempt region count
   */
  public int attemptRegionCount() {
    return attemptRegions.size();
  }

  private static void validateIndex(String table, int index, int size) {
    if (index < 0 || index >= size) throw new InvalidIndexException(table, index, size);
  }
}