package za.co.bts.words.sensitive.dto;

public record SensitiveWordResponse(
    EnterpriseHeaderResponse header,
    Result result,
    SensitiveWordResponsePayload payload
) {}
