package dev.morphe.engine.listener;

import dev.morphe.bytecode.program.BytecodeProgram;

@FunctionalInterface
public interface ICompilationListener {
  void onCompilationSuccess(BytecodeProgram program);
}