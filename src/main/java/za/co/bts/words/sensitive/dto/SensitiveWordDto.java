package za.co.bts.words.sensitive.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record SensitiveWordDto(
        @JsonProperty(required = false)
        String id,

        @NotBlank(message = "Sensitive word is required.")
        String word
) {}
