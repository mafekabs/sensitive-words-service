package za.co.bts.words.sensitive.dto;

import java.util.List;

public record SensitiveWordResponsePayload(
    List<SensitiveWordDto> sensitiveWords
) {}
