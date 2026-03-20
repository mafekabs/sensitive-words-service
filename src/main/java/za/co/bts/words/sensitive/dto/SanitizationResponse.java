package za.co.bts.words.sensitive.dto;

public record SanitizationResponse(
   EnterpriseHeaderResponse header,
   Result result,
   SanitizationPayload payload
) {}
