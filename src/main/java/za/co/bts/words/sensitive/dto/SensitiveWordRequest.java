package za.co.bts.words.sensitive.dto;

public record SensitiveWordRequest(
    EnterpriseHeaderRequest header,
    SensitiveWordRequestPayload payload
) {}
