package za.co.bts.words.sensitive.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record SensitiveWordRequestPayload(
    @NotNull @Valid
    SensitiveWordDto sensitiveWord
) {}