package dev.morphe.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload request for executing a new Morphe source code snippet.
 *
 * @param code The source code written by the user in the playground editor.
 */
public record RunRequest(
    @NotBlank(message = "Code must not be empty")
    @Size(max = 10000, message = "Code exceeds the maximum allowed size (10000 characters)")
    String code
) {}