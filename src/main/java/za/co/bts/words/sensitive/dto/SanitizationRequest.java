package za.co.bts.words.sensitive.dto;

public record SanitizationRequest(
   EnterpriseHeaderRequest header,
   SanitizationPayload payload
) {}
