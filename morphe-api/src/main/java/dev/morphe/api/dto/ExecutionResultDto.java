package dev.morphe.api.dto;

import dev.morphe.vm.state.VMExecutionState;
import java.util.List;

/**
 * Data Transfer Object representing the result of a Morphe code execution step
 * or session update sent back to the playground frontend.
 *
 * @param success Indicates whether the execution step completed without errors.
 * @param output List of incremental console output lines generated during execution.
 * @param error Error message if the execution failed or threw an exception.
 * @param state Current execution state of the Virtual Machine (e.g., HALTED, WAITING_FOR_INPUT).
 * @param sessionId Unique session identifier used for interactive multi-step inputs.
 */
public record ExecutionResultDto(
    boolean success,
    List<String> output,
    String error,
    VMExecutionState state,
    String sessionId
) {}