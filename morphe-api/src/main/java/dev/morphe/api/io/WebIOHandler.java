package dev.morphe.api.io;

import dev.morphe.api.exception.MorpheExecutionLimitException;
import dev.morphe.engine.IOutputHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles input/output streams for Morphe virtual machine executions triggered
 * via the web playground interface. Captures printed lines up to a defined limit
 * to prevent memory exhaustion from excessive output or infinite printing loops.
 */
public final class WebIOHandler implements IOutputHandler {

  private final List<String> outputLog = new ArrayList<>();
  private final int maxOutputLines;

  /**
   * Constructs a new web I/O handler with a specified output line capacity.
   *
   * @param maxOutputLines The maximum number of lines allowed before triggering an execution limit exception.
   */
  public WebIOHandler(int maxOutputLines) {
    this.maxOutputLines = maxOutputLines;
  }

  /**
   * Appends text to the internal output log buffer.
   *
   * <p>Enforces the output line constraint. If the buffer size reaches the maximum limit,
   * an exception is thrown to abort runaway executions.</p>
   *
   * @param text The string to be printed to the console.
   * @throws MorpheExecutionLimitException if the output limit is exceeded.
   */
  @Override
  public void print(String text) {
    if (outputLog.size() >= maxOutputLines) {
      throw new MorpheExecutionLimitException(
          "Output limit reached (" + maxOutputLines + " lines). Does your program have an infinite loop?"
      );
    }
    outputLog.add(text);
  }

  /**
   * Retrieves accumulated output lines and clears the internal buffer
   * to support incremental rendering in the web console.
   *
   * @return An immutable copy of the flushed output logs.
   */
  public synchronized List<String> flushOutputLog() {
    List<String> flushed = List.copyOf(outputLog);
    outputLog.clear();
    return flushed;
  }
}