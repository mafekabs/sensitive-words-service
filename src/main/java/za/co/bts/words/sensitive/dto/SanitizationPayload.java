package za.co.bts.words.sensitive.dto;

import jakarta.validation.constraints.NotBlank;

public record SanitizationPayload(
    @NotBlank(message = "message is required")
    String message
) {}
