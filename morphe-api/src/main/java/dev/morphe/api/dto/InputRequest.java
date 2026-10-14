package dev.morphe.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload request for providing runtime input to an active Morphe execution session.
 *
 * @param input The text or number entered by the user in the console prompt.
 */
public record InputRequest(
    @NotBlank(message = "Input must not be empty")
    @Size(max = 1000, message = "Input exceeds the maximum allowed size (1000 characters)")
    String input
) {}