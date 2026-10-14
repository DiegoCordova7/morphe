package dev.morphe.api.controller;

import dev.morphe.api.dto.ExecutionResultDto;
import dev.morphe.api.dto.InputRequest;
import dev.morphe.api.dto.RunRequest;
import dev.morphe.api.service.MorpheExecutionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller managing code execution lifecycle and interactive console inputs
 * for the Morphe playground.
 */
@RestController
@RequestMapping("/api/morphe")
public class MorpheController {

  private final MorpheExecutionService executionService;

  public MorpheController(MorpheExecutionService executionService) {
    this.executionService = executionService;
  }

  /**
   * Compiles and executes a new Morphe code snippet, initializing an interactive session if needed.
   *
   * @param request The container holding the raw source code.
   * @return The execution result including output logs, vm state, and a session identifier.
   */
  @PostMapping("/run")
  public ExecutionResultDto runCode(@Valid @RequestBody RunRequest request) {
    return executionService.executeCode(request.code());
  }

  /**
   * Resumes a paused execution session by providing the required user input.
   *
   * @param sessionId The unique identifier of the active execution session.
   * @param request   The container holding the input string.
   * @return The updated execution result after processing the input.
   */
  @PostMapping("/run/{sessionId}/input")
  public ExecutionResultDto provideInput(
      @PathVariable String sessionId,
      @Valid @RequestBody InputRequest request) {
    return executionService.provideInput(sessionId, request.input());
  }
}