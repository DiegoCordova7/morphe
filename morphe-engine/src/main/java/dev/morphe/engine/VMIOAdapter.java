package dev.morphe.engine;

import dev.morphe.vm.io.IVMIOHandler;

public final class VMIOAdapter implements IVMIOHandler {

  private final IOutputHandler outputHandler;

  public VMIOAdapter(IOutputHandler outputHandler) {
    if (outputHandler == null) throw new IllegalArgumentException("outputHandler cannot be null");
    this.outputHandler = outputHandler;
  }

  @Override
  public void print(String output) {
    outputHandler.print(output);
  }
}