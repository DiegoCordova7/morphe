package dev.morphe.compiler.context;

import dev.morphe.bytecode.builder.Label;
import dev.morphe.semantic.symbol.Symbol;

public final class CompilerPatternContext {

  private final CompilerFunctionContext function;
  private final int subjectSlot;
  private final Label matched;
  private final Label failed;

  public CompilerPatternContext(CompilerFunctionContext function, int subjectSlot, Label matched, Label failed) {
    if (function == null) throw new IllegalArgumentException("function cannot be null");
    if (subjectSlot < 0) throw new IllegalArgumentException("subjectSlot cannot be negative");
    if (matched == null) throw new IllegalArgumentException("matched cannot be null");
    if (failed == null) throw new IllegalArgumentException("failed cannot be null");
    this.function = function;
    this.subjectSlot = subjectSlot;
    this.matched = matched;
    this.failed = failed;
  }

  public CompilerFunctionContext function() {
    return function;
  }

  public int subjectSlot() {
    return subjectSlot;
  }

  public Label matched() {
    return matched;
  }

  public Label failed() {
    return failed;
  }

  public int allocateSlot(Symbol symbol) {
    return function.allocateSlot(symbol);
  }

  public int slot(Symbol symbol) {
    return function.slot(symbol);
  }

  public int capture(Symbol symbol) {
    return function.capture(symbol);
  }
}