package dev.morphe.metrics.collector;

import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.metrics.persistence.CompilationRepository;

import java.sql.SQLException;
import java.util.Objects;

public class BytecodeCollector {

  private final CompilationRepository repository;

  public BytecodeCollector() {
    this.repository = new CompilationRepository();
  }

  public long collect(BytecodeProgram program) throws SQLException {
    Objects.requireNonNull(program, "program");
    return repository.save(program);
  }
}