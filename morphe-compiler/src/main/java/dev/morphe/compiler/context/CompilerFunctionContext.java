package dev.morphe.compiler.context;

import dev.morphe.ast.declaration.FunctionDeclaration;
import dev.morphe.bytecode.builder.Label;
import dev.morphe.bytecode.program.CaptureDefinition;
import dev.morphe.bytecode.program.CaptureSourceKind;
import dev.morphe.semantic.symbol.Symbol;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CompilerFunctionContext {

  private final CompilerContext compiler;
  private final CompilerFunctionContext parent;

  private final Map<Symbol, Integer> slots = new HashMap<>();
  private final Map<Symbol, Integer> captures = new HashMap<>();
  private final List<CaptureDefinition> captureDefinitions = new ArrayList<>();

  private final Deque<Label> breakLabels = new ArrayDeque<>();
  private final Deque<Label> continueLabels = new ArrayDeque<>();

  private int nextSlot;

  public CompilerFunctionContext(CompilerContext compiler, CompilerFunctionContext parent) {
    if (compiler == null) throw new IllegalArgumentException("context cannot be null");
    this.compiler = compiler;
    this.parent = parent;
  }

  public CompilerContext compiler() {
    return compiler;
  }

  public CompilerFunctionContext parent() {
    return parent;
  }

  public int allocateSlot(Symbol symbol) {
    if (symbol == null) throw new IllegalArgumentException("symbol cannot be null");
    if (slots.containsKey(symbol)) throw new IllegalArgumentException("symbol already has a slot");
    int slot = nextSlot++;
    slots.put(symbol, slot);
    return slot;
  }

  public int slot(Symbol symbol) {
    if (symbol == null) throw new IllegalArgumentException("symbol cannot be null");
    Integer slot = slots.get(symbol);
    if (slot == null) throw new IllegalArgumentException("symbol has no slot");
    return slot;
  }

  public boolean hasSlot(Symbol symbol) {
    if (symbol == null) throw new IllegalArgumentException("symbol cannot be null");
    return slots.containsKey(symbol);
  }

  public int slotCount() {
    return nextSlot;
  }

  public int allocateTemporarySlot() {
    return nextSlot++;
  }

  public int capture(Symbol symbol) {
    if (symbol == null) throw new IllegalArgumentException("symbol cannot be null");
    Integer existing = captures.get(symbol);
    if (existing != null) return existing;
    CaptureDefinition definition = resolveCapture(symbol);
    int index = captureDefinitions.size();
    captureDefinitions.add(definition);
    captures.put(symbol, index);
    return index;
  }

  public List<CaptureDefinition> captures() {
    return List.copyOf(captureDefinitions);
  }

  public int captureCount() {
    return captureDefinitions.size();
  }

  private CaptureDefinition resolveCapture(Symbol symbol) {
    if (parent == null) throw new IllegalArgumentException("Symbol is not available from an enclosing function");
    if (parent.hasSlot(symbol)) return new CaptureDefinition(CaptureSourceKind.LOCAL, parent.slot(symbol));
    int parentCapture = parent.capture(symbol);
    return new CaptureDefinition(CaptureSourceKind.CAPTURE, parentCapture);
  }

  public void enterLoop(Label breakLabel, Label continueLabel) {
    if (breakLabel == null) throw new IllegalArgumentException("breakLabel cannot be null");
    if (continueLabel == null) throw new IllegalArgumentException("continueLabel cannot be null");
    breakLabels.push(breakLabel);
    continueLabels.push(continueLabel);
  }

  public void exitLoop() {
    if (breakLabels.isEmpty() || continueLabels.isEmpty()) throw new IllegalStateException("Not inside a loop");
    breakLabels.pop();
    continueLabels.pop();
  }

  public Label breakLabel() {
    if (breakLabels.isEmpty()) throw new IllegalStateException("Not inside a loop");
    return breakLabels.peek();
  }

  public Label continueLabel() {
    if (continueLabels.isEmpty()) throw new IllegalStateException("Not inside a loop");
    return continueLabels.peek();
  }
}