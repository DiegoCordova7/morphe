package dev.morphe.metrics;

import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.metrics.collector.BytecodeCollector;

import java.sql.SQLException;
import java.util.Objects;

public class MetricsSession {

  private final BytecodeCollector bytecodeCollector;

  public MetricsSession() {
    this.bytecodeCollector = new BytecodeCollector();
  }

  public long recordBytecode(BytecodeProgram program) throws SQLException {
    Objects.requireNonNull(program, "program");
    return bytecodeCollector.collect(program);
  }
}