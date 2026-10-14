package dev.morphe.compiler.context;

import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.compiler.natives.NativeInstructionResolver;
import dev.morphe.compiler.visitors.CompilerDispatch;
import dev.morphe.semantic.symbol.Symbol;
import dev.morphe.semantic.visitors.context.SemanticContext;

import java.util.HashMap;
import java.util.Map;

public final class CompilerContext {

  private final BytecodeBuilder builder;
  private final SemanticContext semantic;
  private final NativeInstructionResolver nativeInstructions;
  private CompilerDispatch dispatch;
  private final Map<Symbol, Integer> functions;

  public CompilerContext(BytecodeBuilder builder, SemanticContext semantic, NativeInstructionResolver nativeInstructions) {
    this.builder = builder;
    this.semantic = semantic;
    this.nativeInstructions = nativeInstructions;
    this.dispatch = null;
    functions = new HashMap<>();
  }

  public BytecodeBuilder builder() {
    return builder;
  }

  public SemanticContext semantic() {
    return semantic;
  }

  public void dispatch(CompilerDispatch dispatch) {
    this.dispatch = dispatch;
  }

  public CompilerDispatch dispatch() {
    return dispatch;
  }

  public NativeInstructionResolver nativeInstructions() {
    return nativeInstructions;
  }

  public void registerFunction(Symbol symbol, int functionIndex) {
    if (symbol == null) throw new IllegalArgumentException("symbol cannot be null");
    if (functions.containsKey(symbol)) throw new IllegalArgumentException("function symbol already registered");
    functions.put(symbol, functionIndex);
  }

  public int functionIndex(Symbol symbol) {
    if (symbol == null) throw new IllegalArgumentException("symbol cannot be null");
    Integer index = functions.get(symbol);
    if (index == null)
      throw new IllegalArgumentException("function symbol is not registered");
    return index;
  }
}