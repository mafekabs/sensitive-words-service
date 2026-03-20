package za.co.bts.words.sensitive.dto;

public record EnterpriseHeaderResponse(
    String senderId,
    String transactionId,
    String messageId,
    String relatesToId,
    String timestamp
) {}
