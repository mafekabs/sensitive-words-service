package za.co.bts.words.sensitive.dto;

public record EnterpriseHeaderRequest(
    String senderId,
    String transactionId,
    String messageId,
    String timestamp
) {}
