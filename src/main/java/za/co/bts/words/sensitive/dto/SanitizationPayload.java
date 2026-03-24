package za.co.bts.words.sensitive.dto;

import jakarta.validation.constraints.NotBlank;

public record SanitizationPayload(
    @NotBlank(message = "Message is required.")
    String message
) {}
