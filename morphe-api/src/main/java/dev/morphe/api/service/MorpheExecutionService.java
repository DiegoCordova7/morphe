package dev.morphe.api.service;

import dev.morphe.api.dto.ExecutionResultDto;
import dev.morphe.api.exception.MorpheExecutionLimitException;
import dev.morphe.api.io.WebIOHandler;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.engine.Engine;
import dev.morphe.engine.EngineResult;
import dev.morphe.vm.state.VMExecutionState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import dev.morphe.metrics.MetricsSession;
import java.sql.SQLException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * Core execution service for Morphe source code.
 *
 * <p>Manages asynchronous code compilation, VM execution cycles, timeout guards,
 * and multi-step interactive inputs coordinated via {@link SessionManager}.</p>
 */
@Service
public final class MorpheExecutionService {

  /**
   * Represents an isolated execution session holding its specific compiler engine
   * and I/O buffer handler.
   */
  public record ExecutionSession(Engine engine, WebIOHandler ioHandler) {}

  private final SessionManager sessionManager;
  private final ExecutorService executorService;
  private final int timeoutSeconds;
  private final int maxOutputLines;
  private final MetricsSession metricsSession = new MetricsSession();

  public MorpheExecutionService(
      SessionManager sessionManager,
      ExecutorService morpheExecutorService,
      @Value("${morphe.execution.timeout-seconds:5}") int timeoutSeconds,
      @Value("${morphe.execution.max-output-lines:500}") int maxOutputLines) {
    this.sessionManager = sessionManager;
    this.executorService = morpheExecutorService;
    this.timeoutSeconds = timeoutSeconds;
    this.maxOutputLines = maxOutputLines;
  }

  /**
   * Compiles and initiates a new execution session for a source code snippet.
   *
   * @param sourceCode The Morphe source code string.
   * @return The initial execution result dto.
   */
  public ExecutionResultDto executeCode(String sourceCode) {
    String sessionId = UUID.randomUUID().toString();
    WebIOHandler handler = new WebIOHandler(maxOutputLines);
    Engine engine = new Engine(sourceCode, handler);
    ExecutionSession session = new ExecutionSession(engine, handler);
    sessionManager.save(sessionId, session);
    Future<ExecutionResultDto> task = executorService.submit(() -> executeInternal(sessionId, session));
    return handleTaskExecution(task, sessionId, session);
  }

  /**
   * Resumes an existing paused session by injecting user input into the VM stream.
   *
   * @param sessionId The active execution session identifier.
   * @param input     The input value provided by the user.
   * @return The updated execution result dto.
   */
  public ExecutionResultDto provideInput(String sessionId, String input) {
    ExecutionSession session = sessionManager.get(sessionId);
    if (session == null) return new ExecutionResultDto(false, null, "Execution session not found", null, null);
    Future<ExecutionResultDto> task = executorService.submit(() -> provideInputInternal(sessionId, session, input));
    return handleTaskExecution(task, sessionId, session);
  }

  private ExecutionResultDto executeInternal(String sessionId, ExecutionSession session) {
    try {
      EngineResult<BytecodeProgram> compilation = session.engine().compile();
      if (!compilation.isSuccess()) {
        sessionManager.remove(sessionId);
        return new ExecutionResultDto(false, session.ioHandler().flushOutputLog(), compilation.error().getMessage(), null, null);
      }
      try {
        metricsSession.recordBytecode(compilation.value());
      } catch (SQLException e) {
        sessionManager.remove(sessionId);
        return new ExecutionResultDto(
            false,
            session.ioHandler().flushOutputLog(),
            "Could not persist compiled bytecode to the database.",
            null,
            null
        );
      }
      EngineResult<Void> execution = session.engine().execute();
      return createExecutionResult(sessionId, session, execution);
    } catch (MorpheExecutionLimitException e) {
      sessionManager.remove(sessionId);
      return new ExecutionResultDto(false, session.ioHandler().flushOutputLog(), e.getMessage(), null, null);
    } catch (StackOverflowError e) {
      sessionManager.remove(sessionId);
      return new ExecutionResultDto(false, session.ioHandler().flushOutputLog(), "Stack overflow (infinite recursion?)", null, null);
    } catch (Throwable t) {
      sessionManager.remove(sessionId);
      return new ExecutionResultDto(false, session.ioHandler().flushOutputLog(), "Unexpected error during execution", null, null);
    }
  }

  private ExecutionResultDto provideInputInternal(String sessionId, ExecutionSession session, String input) {
    try {
      EngineResult<Void> result = session.engine().provideInput(input);
      return createExecutionResult(sessionId, session, result);
    } catch (MorpheExecutionLimitException e) {
      sessionManager.remove(sessionId);
      return new ExecutionResultDto(false, session.ioHandler().flushOutputLog(), e.getMessage(), null, null);
    } catch (StackOverflowError e) {
      sessionManager.remove(sessionId);
      return new ExecutionResultDto(false, session.ioHandler().flushOutputLog(), "Stack overflow (infinite recursion?)", null, null);
    } catch (Throwable t) {
      sessionManager.remove(sessionId);
      return new ExecutionResultDto(false, session.ioHandler().flushOutputLog(), "Unexpected error during execution", null, null);
    }
  }

  private ExecutionResultDto createExecutionResult(String sessionId, ExecutionSession session, EngineResult<Void> execution) {
    List<String> incrementalOutput = session.ioHandler().flushOutputLog();
    if (!execution.isSuccess()) {
      sessionManager.remove(sessionId);
      return new ExecutionResultDto(false, incrementalOutput, execution.error().getMessage(), null, null);
    }
    VMExecutionState state = session.engine().state();
    if (state == VMExecutionState.HALTED) {
      sessionManager.remove(sessionId);
      return new ExecutionResultDto(true, incrementalOutput, null, VMExecutionState.HALTED, null);
    }
    return new ExecutionResultDto(true, incrementalOutput, null, state, sessionId);
  }

  private ExecutionResultDto handleTaskExecution(Future<ExecutionResultDto> task, String sessionId, ExecutionSession session) {
    try {
      return task.get(timeoutSeconds, TimeUnit.SECONDS);
    } catch (TimeoutException e) {
      task.cancel(true);
      sessionManager.remove(sessionId);
      return new ExecutionResultDto(
          false, session.ioHandler().flushOutputLog(),
          "Execution timed out (" + timeoutSeconds + "s). Does your program have an infinite loop?", null, null
      );
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      sessionManager.remove(sessionId);
      return new ExecutionResultDto(false, session.ioHandler().flushOutputLog(), "Execution was interrupted", null, null);
    } catch (ExecutionException e) {
      sessionManager.remove(sessionId);
      return new ExecutionResultDto(false, session.ioHandler().flushOutputLog(), "Internal error during execution", null, null);
    }
  }
}